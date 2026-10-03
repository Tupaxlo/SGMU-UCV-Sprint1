package modelo;

public class Usuario {
    private String cedula;
    private String nombre;
    private String correo;
    private String clave;
    private String rol; // Puede ser "Pasajero" o "Admin"

    public Usuario(String cedula, String nombre, String correo, String clave, String rol) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.correo = correo;
        this.clave = clave;
        this.rol = rol;
    }

    // Getters
    public String getCedula() { return cedula; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public String getClave() { return clave; }
    public String getRol() { return rol; }
}