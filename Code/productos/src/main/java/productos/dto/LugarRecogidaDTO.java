package productos.dto;

public class LugarRecogidaDTO {
    private String descripcion;
    private double longitud;
    private double latitud;

    //POJO
    public LugarRecogidaDTO() {
    }
    
    public LugarRecogidaDTO(String descripcion, double longitud, double latitud) {
        this.descripcion = descripcion;
        this.longitud = longitud;
        this.latitud = latitud;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getLongitud() {
        return longitud;
    }

    public double getLatitud() {
        return latitud;
    }
}