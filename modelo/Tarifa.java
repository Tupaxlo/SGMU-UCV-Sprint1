package modelo;

public class Tarifa {
    private String tipoUsuario;
    private String tipoRuta;
    private double monto;

    public Tarifa(String tipoUsuario, String tipoRuta, double monto) {
        this.tipoUsuario = tipoUsuario;
        this.tipoRuta = tipoRuta;
        this.monto = monto;
    }

    public String getTipoUsuario() { return tipoUsuario; }
    public String getTipoRuta() { return tipoRuta; }
    public double getMonto() { return monto; }

    public void setTipoUsuario(String tipoUsuario) { this.tipoUsuario = tipoUsuario; }
    public void setTipoRuta(String tipoRuta) { this.tipoRuta = tipoRuta; }
    public void setMonto(double monto) { this.monto = monto; }
}
