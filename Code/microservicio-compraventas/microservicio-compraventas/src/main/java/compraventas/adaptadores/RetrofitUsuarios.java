package compraventas.adaptadores;

import compraventas.modelo.externo.UsuarioExterno;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitUsuarios {
	
	@GET("api/usuarios/{id}/nombre")
	Call<UsuarioExterno> getNombreUsuario(@Path("id") String id);
	
}