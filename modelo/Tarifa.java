package modelo;

import java.util.ArrayList;
import java.util.List;

public class Tarifa {
    private static final List<Tarifa> TARIFAS_REGISTRADAS = new ArrayList<>();

    private String tipoUsuario;
    private String tipoRuta;
    private double monto;

    public Tarifa(String tipoUsuario, String tipoRuta, double monto) {
        this.tipoUsuario = tipoUsuario;
        this.tipoRuta = tipoRuta;
        this.monto = monto;
    }

    public static List<Tarifa> getTarifasRegistradas() {
        return TARIFAS_REGISTRADAS;
    }

    public static void registrar(Tarifa tarifa) {
        if (tarifa == null) {
            return;
        }
        for (Tarifa existente : TARIFAS_REGISTRADAS) {
            if (existente.getTipoUsuario().equalsIgnoreCase(tarifa.getTipoUsuario())
                    && existente.getTipoRuta().equalsIgnoreCase(tarifa.getTipoRuta())) {
                existente.setMonto(tarifa.getMonto());
                return;
            }
        }
        TARIFAS_REGISTRADAS.add(tarifa);
    }

    public static void cargarBaseInicial() {
        if (TARIFAS_REGISTRADAS.isEmpty()) {
            TARIFAS_REGISTRADAS.add(new Tarifa("Comunidad Universitaria", "Extraurbana", 2.50));
            TARIFAS_REGISTRADAS.add(new Tarifa("Público General", "Extraurbana", 5.00));
        }
    }

    public String getTipoUsuario() { return tipoUsuario; }
    public String getTipoRuta() { return tipoRuta; }
    public double getMonto() { return monto; }

    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    public void setTipoRuta(String tipoRuta) { this.tipoRuta = tipoRuta; }
    public void setMonto(double monto) { this.monto = monto; }
}
