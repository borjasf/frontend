package segundum.pasarela.dto;

public class UsuarioRegistroDTO {
    private String nombre;
    private String apellidos;
    private String email;
    private String clave;
    private String fechaNacimiento;
    private String telefono;
    private boolean admin;

    public UsuarioRegistroDTO() {}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getClave() { return clave; }
    public void setClave(String clave) { this.clave = clave; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public boolean isAdmin() { return admin; }
    public void setAdmin(boolean admin) { this.admin = admin; }
}
