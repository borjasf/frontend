const express = require('express');
const path = require('path');
const hbs = require('hbs');
const cookieParser = require('cookie-parser');

const app = express();

// Motor de vistas HBS
app.set('view engine', 'hbs');
app.set('views', path.join(__dirname, 'views'));
app.set('view options', { layout: 'layouts/main' }); 
hbs.registerPartials(path.join(__dirname, 'views/partials'));
hbs.localsAsTemplateData(app); // hace que res.locals esté disponible en todas las plantillas HBS

// Middleware
app.use(express.urlencoded({ extended: false })); // leer formularios POST
app.use(express.json());
app.use(express.static(path.join(__dirname, 'public'))); // archivos estáticos (CSS, JS, img)
app.use(cookieParser()); // para leer cookies (JWT)
const { optionalAuth } = require('./src/middleware/authMiddleware');
app.use(optionalAuth); // inyecta res.locals.usuario en todas las rutas si hay JWT válido
// Rutas
const routes = require('./routes/index');
app.use('/', routes);

app.locals.currentYear = new Date().getFullYear(); // variable global para el año actual en el footer

module.exports = app;
