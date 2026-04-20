package compraventas.adaptadores;

import compraventas.modelo.externo.ProductoExterno;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitProductos {
	// Llamada normal al microservicio de Productos
	@GET("productos/{id}")
	Call<ProductoExterno> getProducto(@Path("id") String id);
}