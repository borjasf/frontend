package usuarios.rabbitmq;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import usuarios.puertos.ManejadorEventos;

public class ConsumidorEventos {

	private static final String DEFAULT_RABBITMQ_URI = "amqps://oklxofze:TjosECA3Q1TT7Y4XbVEnj7XBnXym52qD@rat.rmq2.cloudamqp.com/oklxofze";
	private static final String EXCHANGE_NAME = "bus";
	private static final String QUEUE_NAME = "usuarios";
	private static final String BINDING_KEY = "bus.compraventas.#";
	private static final String COMPRAVENTA_CREADA_KEY = "bus.compraventas.compraventa-creada";

	private final ManejadorEventos manejador = new ManejadorEventos();
	private final Gson gson = new Gson();
	private Connection connection;
	private Channel channel;

	public void start() {
		try {
			ConnectionFactory factory = new ConnectionFactory();
			factory.setUri(getRabbitMqUri());

			connection = factory.newConnection();
			channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);
			channel.queueDeclare(QUEUE_NAME, true, false, false, null);
			channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, BINDING_KEY);

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			    long deliveryTag = delivery.getEnvelope().getDeliveryTag();
			    String routingKey = delivery.getEnvelope().getRoutingKey();

			    try {
			        if (COMPRAVENTA_CREADA_KEY.equals(routingKey)) {
			            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
			            
			            // Usamos el Gson que ya tienes para convertir el JSON a un Map temporal
			            // Esto es más flexible ya que no dependes de una clase intermedia fija
			            java.util.Map<String, Object> evento = gson.fromJson(message, java.util.Map.class);

			            String idComprador = (String) evento.get("idComprador");
			            String idVendedor = (String) evento.get("idVendedor");

			            // Llamamos al manejador con los IDs
			            manejador.compraventaCreada(idComprador, idVendedor);
			        }

			        channel.basicAck(deliveryTag, false);
			    } catch (Exception e) {
			        System.err.println("Error procesando evento en Usuarios: " + e.getMessage());
			        channel.basicNack(deliveryTag, false, true);
			    }
			};

			channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {
			});
		} catch (Exception e) {
			throw new RuntimeException("No se ha podido iniciar el consumidor RabbitMQ de usuarios", e);
		}
	}

	public void end() {
		try {
			if (channel != null) {
				channel.close();
			}
			if (connection != null) {
				connection.close();
			}
		} catch (IOException | TimeoutException e) {
			throw new RuntimeException("No se ha podido detener el consumidor RabbitMQ de usuarios", e);
		}
	}

	private String getRabbitMqUri() {
		String rabbitMqUri = System.getenv("RABBITMQ_URI");
		return rabbitMqUri == null || rabbitMqUri.trim().isEmpty() ? DEFAULT_RABBITMQ_URI : rabbitMqUri;
	}
}