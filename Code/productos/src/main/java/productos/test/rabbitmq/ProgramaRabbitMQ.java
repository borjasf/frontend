package productos.test.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

import productos.ProductosApplication;

public class ProgramaRabbitMQ {

	public static void main(String[] args) {
		
		ConfigurableApplicationContext contexto = SpringApplication.run(ProductosApplication.class, args);

		PublicadorEventos publicador = contexto.getBean(PublicadorEventos.class);
		
		publicador.emitirEvento("hola");
		
		EjemploInfo info = new EjemploInfo("test");
		
		publicador.emitirEvento(info);
		
		contexto.close();		
	}
}
