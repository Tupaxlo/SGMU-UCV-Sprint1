package modelo;

import java.math.BigDecimal;

public class Monedero {

    private String idUsuario;
    private BigDecimal saldo;

    public Monedero(String idUsuario) {
        this.idUsuario = idUsuario;
        this.saldo = BigDecimal.ZERO;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void recargar(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                "El monto debe ser mayor que cero."
            );
        }

        saldo = saldo.add(monto);
    }

    public void descontar(BigDecimal monto) {
        if (monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El monto a descontar debe ser mayor que cero.");
        }
        if (saldo.compareTo(monto) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente en el monedero.");
        }
        saldo = saldo.subtract(monto);
    }
}