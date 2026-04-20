package rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class ConfiguracionRest extends Application {
    // No hace falta poner nada aquí dentro. 
    // Solo con la etiqueta de arriba, Jetty encenderá la API.
}