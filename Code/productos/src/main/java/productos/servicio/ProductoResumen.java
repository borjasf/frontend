package productos.servicio;

import java.time.LocalDateTime;
import modelo.EstadoProducto;

public class ProductoResumen {

    private String id;
    private String titulo;
    private double precio;
    private EstadoProducto estado;
    private LocalDateTime fechaPublicacion;
    private boolean envioDisponible;
    private String categoriaNombre; // Solo el nombre, sin cargar la entidad

    public ProductoResumen() {
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getPrecio() {
        return precio;
    }
    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public EstadoProducto getEstado() {
        return estado;
    }
    public void setEstado(EstadoProducto estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }
    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public boolean isEnvioDisponible() {
        return envioDisponible;
    }
    public void setEnvioDisponible(boolean envioDisponible) {
        this.envioDisponible = envioDisponible;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }
    public void setCategoriaNombre(String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }

    @Override
    public String toString() {
        return "ProductoResumen [id=" + id + ", titulo=" + titulo + ", precio=" + precio +
               ", estado=" + estado + ", fechaPublicacion=" + fechaPublicacion +
               ", envioDisponible=" + envioDisponible + ", categoriaNombre=" + categoriaNombre + "]";
    }
}