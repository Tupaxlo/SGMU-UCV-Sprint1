package test;

import modelo.Itinerario;
import modelo.Vehiculo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class ItinerarioTest {

    @Test
    public void testRegistrarItinerarioEnLista() {
        // Preparación (Given)
        List<Itinerario> listaItinerarios = new ArrayList<>();
        Vehiculo vehiculoPrueba = new Vehiculo("ABC-123", "Encava", 32, "Activo");
        Itinerario itinerarioPrueba = new Itinerario("IT-001", "Ruta Plaza Rectorado - CUC", "2026-10-05", "08:00 AM", vehiculoPrueba);

        // Ejecución (When)
        listaItinerarios.add(itinerarioPrueba);

        // Verificación (Then)
        assertEquals(1, listaItinerarios.size(), "La lista debería contener exactamente 1 itinerario.");
        assertEquals("IT-001", listaItinerarios.get(0).getId(), "El ID del itinerario debe coincidir.");
        assertEquals("Ruta Plaza Rectorado - CUC", listaItinerarios.get(0).getRuta(), "La ruta debe coincidir.");
        assertNotNull(listaItinerarios.get(0).getVehiculoAsignado(), "El vehículo asignado no debe ser nulo.");
    }
}