package compraventas.puertos;

import compraventas.eventos.EventoCompraventaCreada;

public interface PublicadorEventos {
    void emitirEventoCompraventaCreada(EventoCompraventaCreada evento);
}