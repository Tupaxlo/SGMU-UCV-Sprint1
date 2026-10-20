package modelo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class Itinerario {
    private static final List<Itinerario> ITINERARIOS_REGISTRADOS = new ArrayList<>();

    private String id;
    private String ruta;
    private String fecha;
    private String hora;
    private Vehiculo vehiculoAsignado;
    private String choferAsignado;

    public Itinerario(String id, String ruta, String fecha, String hora, Vehiculo vehiculoAsignado) {
        this(id, ruta, fecha, hora, vehiculoAsignado, "Sin asignar");
    }

    public Itinerario(String id, String ruta, String fecha, String hora, Vehiculo vehiculoAsignado, String choferAsignado) {
        this.id = id;
        this.ruta = ruta;
        this.fecha = fecha;
        this.hora = hora;
        this.vehiculoAsignado = vehiculoAsignado;
        this.choferAsignado = choferAsignado;
    }

    public static List<Itinerario> getItinerariosRegistrados() {
        return ITINERARIOS_REGISTRADOS;
    }

    public static void registrar(Itinerario itinerario) {
        if (itinerario == null) {
            return;
        }
        if (!existeId(itinerario.getId(), ITINERARIOS_REGISTRADOS)) {
            ITINERARIOS_REGISTRADOS.add(itinerario);
        }
    }

    public static void eliminar(String id) {
        if (id == null) {
            return;
        }
        ITINERARIOS_REGISTRADOS.removeIf(i -> i.getId().equalsIgnoreCase(id.trim()));
    }

    public static boolean esRutaValida(String ruta) {
        return ruta != null && !ruta.trim().isEmpty() && ruta.trim().length() >= 3;
    }

    public static boolean esFechaValida(String fecha) {
        if (fecha == null || fecha.trim().isEmpty()) {
            return false;
        }
        try {
            LocalDate.parse(fecha.trim());
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean esHoraValida(String hora) {
        if (hora == null || hora.trim().isEmpty()) {
            return false;
        }
        try {
            LocalTime.parse(hora.trim());
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static boolean existeId(String id, java.util.List<Itinerario> lista) {
        if (id == null) {
            return false;
        }
        for (Itinerario itinerario : lista) {
            if (itinerario.getId().equalsIgnoreCase(id.trim())) {
                return true;
            }
        }
        return false;
    }

    public String getId() { return id; }
    public String getRuta() { return ruta; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public Vehiculo getVehiculoAsignado() { return vehiculoAsignado; }
    public String getChoferAsignado() { return choferAsignado; }
    public void setChoferAsignado(String choferAsignado) { this.choferAsignado = choferAsignado; }
}