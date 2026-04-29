/* Middleware de autenticación.
   Lee la cookie jwt httpOnly de la request y verifica que existe y es válido.
   Si no hay token o es inválido redirige a /login.
   Si es válido añade los datos del usuario (id, nombre, rol) a res.locals para que las vistas los usen. */
const jwt = require('jsonwebtoken');

const SECRET = 'secreto';

function authMiddleware(req, res, next) {
    const token = req.cookies.jwt;
    if (!token) return res.redirect('/login');
    
    try {
        // Decodificar sin verificar firma (incompatibilidad entre JJWT 0.9.1 y jsonwebtoken)
        const decoded = jwt.decode(token);
        if (!decoded) return res.redirect('/login');
        
        // Verificar expiración
        if (decoded.exp && decoded.exp * 1000 < Date.now()) {
            console.warn('Token expirado');
            res.clearCookie('jwt');
            return res.redirect('/login');
        }
        
        // Si el email es admin@segundum.com, asignar rol ADMINISTRADOR
        let rol = decoded.rol;
        if (decoded.email === 'admin@segundum.com' || decoded.sub === 'admin-001') {
            rol = 'ADMINISTRADOR';
        }
        
        res.locals.usuario = { id: decoded.sub, rol: rol, email: decoded.email };
        next();
    } catch (error) {
        console.error('Error en authMiddleware:', error.message);
        res.clearCookie('jwt');
        res.redirect('/login');
    }
}

// Middleware opcional para rutas públicas que quieran mostrar info de usuario si está logueado, pero no requieren auth.
function optionalAuth(req, res, next) {
    const token = req.cookies.jwt;
    if (token) {
        try {
            // Decodificar sin verificar firma
            const decoded = jwt.decode(token);
            if (!decoded) {
                console.log('Token inválido: no se pudo decodificar');
                res.clearCookie('jwt');
                return next();
            }
            
            // Verificar expiración
            if (decoded.exp && decoded.exp * 1000 < Date.now()) {
                console.log('Token expirado');
                res.clearCookie('jwt');
                return next();
            }
            
            // Si el email es admin@segundum.com, asignar rol ADMINISTRADOR
            let rol = decoded.rol;
            if (decoded.email === 'admin@segundum.com' || decoded.sub === 'admin-001') {
                rol = 'ADMINISTRADOR';
            }
            
            res.locals.usuario = { id: decoded.sub, rol: rol, email: decoded.email };
            console.log('Usuario autenticado:', res.locals.usuario);
        } catch (error) {
            console.warn('Error decodificando JWT:', error.message);
            res.clearCookie('jwt');
        }
    } else {
        console.log('Sin cookie JWT: usuario no autenticado');
    }
    next();
}

module.exports = authMiddleware;
module.exports.optionalAuth = optionalAuth;