package servicio;

import modelo.Monedero;
import java.math.BigDecimal;

public class ServicioPago {

    public BigDecimal calcularMontoConDescuento(BigDecimal tarifaBase, String rolUsuario) {
        String rol = rolUsuario.toUpperCase();
        
        switch (rol) {
            case "ESTUDIANTE":
                // 50% de descuento
                return tarifaBase.multiply(new BigDecimal("0.50")); 
            case "TERCERA EDAD":
            case "PROFESOR":
                // 100% de descuento (Pasaje exonerado)
                return BigDecimal.ZERO; 
            default:
                // REGULAR paga completo
                return tarifaBase; 
        }
    }

    public String procesarPagoCobro(Monedero monedero, BigDecimal tarifaBase, String rolUsuario) {
        BigDecimal montoFinal = calcularMontoConDescuento(tarifaBase, rolUsuario);

        if (montoFinal.compareTo(BigDecimal.ZERO) == 0) {
            return "Pasaje exonerado por rol (" + rolUsuario + "). No se descontó saldo.";
        }

        try {
            monedero.descontar(montoFinal);
            return "Pago procesado con éxito (Rol: " + rolUsuario + "). Se debitó: Bs. " + montoFinal.toPlainString();
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}