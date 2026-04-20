package usuarios.eventos;

import java.time.LocalDateTime;

public abstract class Evento {
	
	private String timeStamp;
	private String id;
	private String tipoEvento;
	
	public Evento() {
		
	}
	
	public Evento(String id, String tipoEvento) {
		this.timeStamp = LocalDateTime.now().toString();
		this.id = id;
		this.tipoEvento = tipoEvento;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTipoEvento() {
		return tipoEvento;
	}

	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	
	
}
