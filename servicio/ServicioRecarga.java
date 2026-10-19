package servicio;

import modelo.Monedero;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ServicioRecarga {

    private final Set<String> referenciasProcesadas = new HashSet<>();

    public String recargar(
            Monedero monedero,
            BigDecimal monto,
            String telefono,
            String banco,
            String referencia) {

        if (monedero == null) {
            throw new IllegalArgumentException(
                "No se ha seleccionado un monedero."
            );
        }

        if (monto == null ||
                monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "El monto debe ser mayor que cero."
            );
        }

        if (telefono == null ||
                !telefono.matches("[0-9]{10,11}")) {
            throw new IllegalArgumentException(
                "Introduce un teléfono de 10 u 11 dígitos."
            );
        }

        if (banco == null ||
                !banco.matches("[0-9]{4}")) {
            throw new IllegalArgumentException(
                "El código bancario debe tener 4 dígitos."
            );
        }

        if (referencia == null ||
                !referencia.matches("[0-9]{6,20}")) {
            throw new IllegalArgumentException(
                "La referencia debe tener entre 6 y 20 dígitos."
            );
        }

        if (referenciasProcesadas.contains(referencia)) {
            throw new IllegalArgumentException(
                "Esta referencia ya fue utilizada."
            );
        }

        // Pago Móvil simulado: no consulta un banco real.
        monedero.recargar(monto);
        referenciasProcesadas.add(referencia);

        return "Recarga simulada realizada correctamente.";
    }
}