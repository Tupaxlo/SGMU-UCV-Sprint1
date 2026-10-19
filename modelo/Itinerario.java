package modelo;

public class Itinerario {
    private String id;
    private String ruta;
    private String fecha;
    private String hora;
    private Vehiculo vehiculoAsignado;

    public Itinerario(String id, String ruta, String fecha, String hora, Vehiculo vehiculoAsignado) {
        this.id = id;
        this.ruta = ruta;
        this.fecha = fecha;
        this.hora = hora;
        this.vehiculoAsignado = vehiculoAsignado;
    }

    // Getters
    public String getId() { return id; }
    public String getRuta() { return ruta; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public Vehiculo getVehiculoAsignado() { return vehiculoAsignado; }
}