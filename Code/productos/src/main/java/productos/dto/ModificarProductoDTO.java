package productos.dto;

public class ModificarProductoDTO{ 

    private Double precio;
    private String descripcion;

    //POJO 
    public ModificarProductoDTO() {
    }

    public ModificarProductoDTO(Double precio, String descripcion) {
        this.precio = precio;
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }


}