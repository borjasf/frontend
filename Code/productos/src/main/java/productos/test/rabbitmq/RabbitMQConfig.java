package productos.test.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String EXCHANGE_NAME = "bus";
	public static final String QUEUE_NAME = "productos";
	public static final String ROUTING_KEY = "bus.usuarios.#";
	public static final String BINDING_KEY = "bus.usuarios.prueba"; 

	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}

	@Bean
	public Queue queue() {
		return new Queue(QUEUE_NAME, true); 
	}

	@Bean
	public Binding binding(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
	}
	
	// NUEVO BINDING TAREA 7: (Escucha a Compraventas)
	@Bean
	public Binding bindingCompraventas(Queue queue, TopicExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with("bus.compraventas.#");
	}

	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}