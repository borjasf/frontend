/* Rutas de productos:
   GET  /             → listado público con filtros y paginación (monta componentes React)
   GET  /productos/:id        → detalle de un producto
   GET  /productos/nuevo      → formulario crear producto (requiere auth)
   POST /productos            → crea nuevo producto vía API (requiere auth)
   GET  /productos/:id/editar → formulario editar producto (requiere auth + ser vendedor)
   POST /productos/:id        → actualiza precio/descripción vía API (requiere auth)
   POST /productos/:id/comprar → solicita compra del producto (requiere auth) */
const express = require('express');
const router = express.Router();
const authMiddleware = require('../src/middleware/authMiddleware');

// Importamos el Controlador desde la carpeta 'src'
const productosController = require('../src/controllers/productosController');

// Middleware para bloquear admins del acceso a productos
const noAdminMiddleware = (req, res, next) => {
    if (res.locals.usuario && res.locals.usuario.rol === 'ADMINISTRADOR') {
        return res.status(403).render('error', { 
            title: 'Acceso denegado', 
            message: 'Los administradores no pueden acceder a esta sección.',
            layout: 'layouts/main'
        });
    }
    next();
};

// rutas estáticas (Fijas, siempre van primero)
router.get('/productos', productosController.listado);

// Mostrar formulario de crear (Hacemos que coincida con tu crear.hbs)
router.get('/productos/crear', authMiddleware, noAdminMiddleware, productosController.mostrarCrear);

// Procesar la creación cuando se envía el formulario (vía POST) - con multer para imagen
router.post('/productos/crear', authMiddleware, noAdminMiddleware, (req, res, next) => {
    const multer = require('multer');
    const path = require('path');
    
    const storage = multer.diskStorage({
        destination: (req, file, cb) => {
            cb(null, path.join(__dirname, '../public/uploads/'));
        },
        filename: (req, file, cb) => {
            const ext = path.extname(file.originalname);
            const name = path.basename(file.originalname, ext).replace(/\s+/g, '_');
            cb(null, `${Date.now()}_${name}${ext}`);
        }
    });
    
    const fileFilter = (req, file, cb) => {
        const allowedMimes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
        if (allowedMimes.includes(file.mimetype)) {
            cb(null, true);
        } else {
            cb(null, false);
        }
    };
    
    const upload = multer({ storage, fileFilter, limits: { fileSize: 5 * 1024 * 1024 } });
    upload.single('imagen')(req, res, (err) => {
        if (err) {
            console.error('Error en upload:', err);
        }
        next();
    });
}, productosController.crear);


// Rutas dinámicas (Con parámetros como :id, siempre al final)
router.get('/productos/:id/cardVentas', productosController.verCard);
router.get('/productos/:id/cardCompras', productosController.verCardCompras);
router.get('/productos/:id', productosController.detalle);
router.get('/productos/:id/editar', authMiddleware, noAdminMiddleware, productosController.mostrarEditar);
router.post('/productos/:id/editar', authMiddleware, noAdminMiddleware, productosController.editar);
router.post('/productos/:id/comprar', authMiddleware, noAdminMiddleware, productosController.comprar);
router.post('/productos/:id/eliminar', authMiddleware, noAdminMiddleware, productosController.eliminar);

module.exports = router;