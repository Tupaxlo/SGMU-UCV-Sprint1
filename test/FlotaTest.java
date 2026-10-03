import modelo.Vehiculo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class FlotaTest {

    @Test
    public void testRegistrarVehiculoEnFlota() {
        // Preparación (Given)
        List<Vehiculo> flota = new ArrayList<>();
        Vehiculo nuevoBus = new Vehiculo("UCV-001", "Encava", 32, "Activo");

        // Ejecución (When)
        flota.add(nuevoBus);

        // Verificación (Then)
        assertEquals(1, flota.size(), "La flota debería tener 1 vehículo registrado");
        assertEquals("UCV-001", flota.get(0).getPlaca(), "La placa del vehículo debe coincidir");
    }

    @Test
    public void testCambiarEstadoVehiculo() {
        // Preparación (Given)
        Vehiculo bus = new Vehiculo("UCV-002", "Marcopolo", 40, "Activo");

        // Ejecución (When)
        bus.setEstado("En Mantenimiento");

        // Verificación (Then)
        assertEquals("En Mantenimiento", bus.getEstado(), "El estado del vehículo debió cambiar a En Mantenimiento");
    }
}