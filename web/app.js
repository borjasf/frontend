const express = require('express');
const path = require('path');
const hbs = require('hbs');
const cookieParser = require('cookie-parser');
const multer = require('multer');

const app = express();

// Configurar multer para guardar imágenes
const storage = multer.diskStorage({
    destination: (req, file, cb) => {
        cb(null, path.join(__dirname, 'public/uploads/'));
    },
    filename: (req, file, cb) => {
        // Nombre único: timestamp + nombre original
        const ext = path.extname(file.originalname);
        const name = path.basename(file.originalname, ext).replace(/\s+/g, '_');
        cb(null, `${Date.now()}_${name}${ext}`);
    }
});

// Filtro para solo imágenes
const fileFilter = (req, file, cb) => {
    const allowedMimes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
    if (allowedMimes.includes(file.mimetype)) {
        cb(null, true);
    } else {
        cb(new Error('Solo se permiten imágenes (JPEG, PNG, GIF, WebP)'), false);
    }
};

const upload = multer({
    storage: storage,
    fileFilter: fileFilter,
    limits: { fileSize: 5 * 1024 * 1024 } // 5MB máximo
});

// Motor de vistas HBS
app.set('view engine', 'hbs');
app.set('views', path.join(__dirname, 'views'));
app.set('view options', { layout: 'layouts/main' }); 
hbs.registerPartials(path.join(__dirname, 'views/partials'));
hbs.localsAsTemplateData(app); // hace que res.locals esté disponible en todas las plantillas HBS

// Registrar helper 'eq' para comparaciones en Handlebars
hbs.registerHelper('eq', function(a, b) {
    return a === b;
});

// Middleware
app.use(express.urlencoded({ extended: false })); // leer formularios POST
app.use(express.json());
app.use(express.static(path.join(__dirname, 'public'))); // archivos estáticos (CSS, JS, img)
app.use(cookieParser()); // para leer cookies (JWT)
const { optionalAuth } = require('./src/middleware/authMiddleware');
app.use(optionalAuth); // inyecta res.locals.usuario en todas las rutas si hay JWT válido

// Exportar upload para usarlo en las rutas
app.locals.upload = upload;

// Rutas
const routes = require('./routes/index');
app.use('/', routes);

app.locals.currentYear = new Date().getFullYear(); // variable global para el año actual en el footer

module.exports = app;
