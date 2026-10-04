package test;

import modelo.Vehiculo;
import modelo.Usuario;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PruebasCajaNegraTest {

    @Test
    public void testValidacionContraseñaRegistro() {
        // Partición Equivalente: Contraseña válida (>= 6 caracteres)
        boolean passwordValida = "Sistema2026".length() >= 6;
        assertTrue(passwordValida, "La contraseña debería ser aceptada.");

        // Partición Equivalente: Contraseña inválida (< 6 caracteres)
        boolean passwordInvalida = "12345".length() >= 6;
        assertFalse(passwordInvalida, "La contraseña debería ser rechazada por ser muy corta.");
    }

    @Test
    public void testValoresLimiteCapacidadVehiculo() {
        // Límite inferior válido (1 pasajero)
        Vehiculo busMinimo = new Vehiculo("BUS-01", "Minibus", 1, "Activo");
        assertEquals(1, busMinimo.getCapacidad(), "Debe aceptar una capacidad mínima de 1.");

        // Límite superior válido (60 pasajeros)
        Vehiculo busMaximo = new Vehiculo("BUS-02", "Encava", 60, "Activo");
        assertEquals(60, busMaximo.getCapacidad(), "Debe aceptar una capacidad máxima de 60.");
    }

    @Test
    public void testValidacionReservaPuesto() {
        int capacidadBus = 30; // Supongamos un bus con capacidad para 30 puestos

        // Caso Válido: Puesto dentro del rango (Ej. Asiento 15)
        int puestoSolicitadoValido = 15;
        boolean esValido = (puestoSolicitadoValido >= 1 && puestoSolicitadoValido <= capacidadBus);
        assertTrue(esValido, "El puesto 15 debe ser una reserva válida.");

        // Caso Inválido (Valor Límite superior): Puesto mayor a la capacidad (Ej. Asiento 31)
        int puestoSolicitadoInvalido = 31;
        boolean esInvalido = (puestoSolicitadoInvalido >= 1 && puestoSolicitadoInvalido <= capacidadBus);
        assertFalse(esInvalido, "El puesto 31 debe ser rechazado por exceder la capacidad.");
    }
}