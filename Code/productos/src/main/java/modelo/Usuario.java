package modelo;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name="Usuario") 
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id; // El login/ID original
    
    private String email;
    private String nombre;
    private String apellidos;

    
    public Usuario() {}

    // Constructor para la inicialización rápida (Nota 2)
    public Usuario(String id, String email, String nombre, String apellidos) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    @Override
    public String toString() {
        return "Usuario_Replica [id=" + id + ", nombre=" + nombre + "]";
    }
}