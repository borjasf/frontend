const express = require('express');
const path = require('path');
const hbs = require('hbs');

const app = express();

// Motor de vistas HBS
app.set('view engine', 'hbs');
app.set('views', path.join(__dirname, 'views'));
hbs.registerPartials(path.join(__dirname, 'views/partials'));

// Middleware
app.use(express.urlencoded({ extended: false })); // leer formularios POST
app.use(express.json());
app.use(express.static(path.join(__dirname, 'public'))); // archivos estáticos (CSS, JS, img)

// Rutas
const routes = require('./routes/index');
app.use('/', routes);

app.locals.currentYear = new Date().getFullYear(); // variable global para el año actual en el footer

module.exports = app;
