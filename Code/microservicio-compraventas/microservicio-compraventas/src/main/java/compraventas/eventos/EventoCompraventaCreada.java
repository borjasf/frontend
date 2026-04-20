package compraventas.eventos;

import java.time.LocalDateTime;

public class EventoCompraventaCreada {

    private String id;
    private String tipoEvento;
    private LocalDateTime fechaHora;
    
    private String idProducto;
    private String idComprador;
    private String idVendedor;

    public EventoCompraventaCreada() {
    }

    public EventoCompraventaCreada(String id, String idProducto, String idComprador, String idVendedor) {
        this.id = id;
        this.tipoEvento = "compraventa-creada";
        this.fechaHora = LocalDateTime.now();
        this.idProducto = idProducto;
        this.idComprador = idComprador;
        this.idVendedor = idVendedor;
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

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}

	public String getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(String idProducto) {
		this.idProducto = idProducto;
	}

	public String getIdComprador() {
		return idComprador;
	}

	public void setIdComprador(String idComprador) {
		this.idComprador = idComprador;
	}

	public String getIdVendedor() {
		return idVendedor;
	}

	public void setIdVendedor(String idVendedor) {
		this.idVendedor = idVendedor;
	}

    
}