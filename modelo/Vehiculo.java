package modelo;

import java.util.ArrayList;
import java.util.List;

public class Vehiculo {
    private static final List<Vehiculo> FLOTA_REGISTRADA = new ArrayList<>();

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

    public static List<Vehiculo> getFlotaRegistrada() {
        return FLOTA_REGISTRADA;
    }

    public static boolean existePlaca(String placa) {
        if (placa == null) {
            return false;
        }
        for (Vehiculo vehiculo : FLOTA_REGISTRADA) {
            if (vehiculo.getPlaca().equalsIgnoreCase(placa.trim())) {
                return true;
            }
        }
        return false;
    }

    public static void registrar(Vehiculo vehiculo) {
        if (vehiculo == null) {
            return;
        }
        if (!existePlaca(vehiculo.getPlaca())) {
            FLOTA_REGISTRADA.add(vehiculo);
        }
    }

    public static void eliminar(String placa) {
        if (placa == null) {
            return;
        }
        FLOTA_REGISTRADA.removeIf(v -> v.getPlaca().equalsIgnoreCase(placa.trim()));
    }

    public static boolean placaUnica(String placa) {
        return !existePlaca(placa);
    }

    public String getPlaca() { return placa; }
    public String getModelo() { return modelo; }
    public int getCapacidad() { return capacidad; }
    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }
}