package compraventas.adaptadores;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

// Importamos el traductor a JSON
import com.fasterxml.jackson.databind.ObjectMapper; 

import compraventas.eventos.EventoCompraventaCreada;
import compraventas.puertos.PublicadorEventos;
import compraventas.rabbitmq.RabbitMQConfig;

@Component
public class PublicadorEventosRabbitMQ implements PublicadorEventos {

    private RabbitTemplate rabbitTemplate;
    private ObjectMapper traductorJson; // Nuestro traductor

    @Autowired
    public PublicadorEventosRabbitMQ(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.traductorJson = new ObjectMapper(); // Lo iniciamos
    }

    @Override
    public void emitirEventoCompraventaCreada(EventoCompraventaCreada evento) {
        try {
            // 1. Traducimos el evento de Java a un texto JSON limpio
            String mensajeJson = traductorJson.writeValueAsString(evento);
            
            // 2. Enviamos el texto JSON al bus de eventos
            rabbitTemplate.convertAndSend( 
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.ROUTING_KEY_COMPRAVENTA_CREADA,
                    mensajeJson 
            );
            System.out.println(" [x] Evento emitido al bus en JSON: " + mensajeJson);
            
        } catch (Exception e) {
            System.err.println(" [!] Error al traducir el evento a JSON: " + e.getMessage());
        }
    }
}