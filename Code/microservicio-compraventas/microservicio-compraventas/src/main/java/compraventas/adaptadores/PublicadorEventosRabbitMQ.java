package compraventas.adaptadores;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import compraventas.eventos.EventoCompraventaCreada;
import compraventas.puertos.PublicadorEventos;
import compraventas.rabbitmq.RabbitMQConfig;

@Component
public class PublicadorEventosRabbitMQ implements PublicadorEventos {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public PublicadorEventosRabbitMQ(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void emitirEventoCompraventaCreada(EventoCompraventaCreada evento) {
        
        rabbitTemplate.convertAndSend( //inyecta el JSON directamente en el exchange con la clave de enrutamiento
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY_COMPRAVENTA_CREADA,
                evento
        );
        System.out.println(" [x] Evento emitido al bus: " + RabbitMQConfig.ROUTING_KEY_COMPRAVENTA_CREADA);
    }
}