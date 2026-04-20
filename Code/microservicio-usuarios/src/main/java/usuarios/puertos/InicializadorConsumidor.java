package usuarios.puertos;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import usuarios.rabbitmq.ConsumidorEventos;

@WebListener
public class InicializadorConsumidor implements ServletContextListener {
	
	private ConsumidorEventos consumidorEventos;
	
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		consumidorEventos = new ConsumidorEventos();
		consumidorEventos.start();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		if(consumidorEventos != null) {
			consumidorEventos.end();
		}
	}

}
