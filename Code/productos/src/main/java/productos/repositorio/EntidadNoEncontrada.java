package productos.repositorio;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * Excepción notificada si no existe una entidad con el identificador
 * proporcionado en el repositorio.
 */

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntidadNoEncontrada extends Exception {

		
	public EntidadNoEncontrada(String msg) {
		super(msg);		
	}
	
		
}
