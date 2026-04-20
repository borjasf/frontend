/* Middleware de autenticación.
   Lee la cookie jwt httpOnly de la request y verifica que existe y es válido.
   Si no hay token o es inválido redirige a /login.
   Si es válido añade los datos del usuario (id, nombre, rol) a res.locals para que las vistas los usen. */
const jwt = require('jsonwebtoken');

function authMiddleware(req, res, next) {
    const token = req.cookies.jwt;
    if (!token) return res.redirect('/login');
    const decoded = jwt.decode(token);
    if (!decoded) return res.redirect('/login');
    res.locals.usuario = { id: decoded.sub, rol: decoded.rol };
    next();
}

// Middleware opcional para rutas públicas que quieran mostrar info de usuario si está logueado, pero no requieren auth.
function optionalAuth(req, res, next) {
    const token = req.cookies.jwt;
    if (token) {
        const decoded = jwt.decode(token);
        if (decoded) {
            res.locals.usuario = { id: decoded.sub, rol: decoded.rol };
        }
    }
    next();
}

module.exports = authMiddleware;
module.exports.optionalAuth = optionalAuth;