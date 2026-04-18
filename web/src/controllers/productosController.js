/* Controlador de productos.
   - listado: obtiene productos paginados/filtrados de la API y renderiza listado.hbs con los datos (React toma el relevo en cliente)
   - detalle: obtiene un producto por id, incrementa visualizaciones vía API y renderiza detalle.hbs
   - mostrarCrear / crear: renderiza y procesa el formulario de nuevo producto
   - mostrarEditar / editar: renderiza y procesa el formulario de edición (precio/descripción)
   - comprar: llama a compraventasService para registrar la compra */
