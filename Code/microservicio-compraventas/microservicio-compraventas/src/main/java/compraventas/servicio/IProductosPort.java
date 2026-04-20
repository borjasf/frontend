package compraventas.servicio;
import compraventas.modelo.externo.ProductoExterno;

public interface IProductosPort {
	ProductoExterno obtenerProducto(String idProducto);
}