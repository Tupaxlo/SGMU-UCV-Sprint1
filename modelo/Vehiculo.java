package modelo;

public class Vehiculo {
    private String placa;
    private String modelo;
    private int capacidad;
    private String estado; // "Activo", "En Mantenimiento"

    public Vehiculo(String placa, String modelo, int capacidad, String estado) {
        this.placa = placa;
        this.modelo = modelo;
        this.capacidad = capacidad;
        this.estado = estado;
    }

    // Getters
    public String getPlaca() { return placa; }
    public String getModelo() { return modelo; }
    public int getCapacidad() { return capacidad; }
    public String getEstado() { return estado; }
    
    // Setters (Para la prueba unitaria)
    public void setEstado(String estado) { this.estado = estado; }
}