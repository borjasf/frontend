package productos.rabbitmq;

import java.nio.charset.StandardCharsets;
import java.util.Map;


import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;


import productos.puertos.ManejadorEventos;
import productos.test.rabbitmq.RabbitMQConfig;

@Component
public class ConsumidorEventos {

	@Autowired
	private ManejadorEventos manejadorEventos;
	
	@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
	public void handleEvent(Map<String, Object> mensaje,
	        @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) {
	    
	    // 1. Si el evento viene de Usuarios
	    if ("bus.usuarios.usuario-creado".equals(routingKey)) {
	        this.manejadorEventos.usuarioCreado(
	                (String) mensaje.get("id"),
                    (String) mensaje.get("email"),
                    (String) mensaje.get("nombre"),
                    (String) mensaje.get("apellidos"));
        }
        
        // 2. Si el evento viene de Compraventas (Tarea 7)
        else if ("bus.compraventas.compraventa-creada".equals(routingKey)) {
            // Extraemos el "idProducto" del JSON que enviaste desde tu microservicio
            String idProducto = (String) mensaje.get("idProducto");
            this.manejadorEventos.compraventaCreada(idProducto);
        }
    }
}