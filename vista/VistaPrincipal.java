package vista;

import modelo.Monedero;
import javax.swing.*;
import java.awt.*;

public class VistaPrincipal extends JFrame {

    private Monedero monederoCompartido;
    private JLabel lblSaldoGlobal;

    public VistaPrincipal() {
        // Creamos el monedero central con 0 Bs iniciales
        monederoCompartido = new Monedero("USUARIO-SGMU-01");

        setTitle("SGMU-UCV - Panel Central");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Esta ventana sí cierra el programa
        setLocationRelativeTo(null);

        construirInterfaz();
    }

    private void construirInterfaz() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblSaldoGlobal = new JLabel("Saldo Global: Bs. 0.00", SwingConstants.CENTER);
        lblSaldoGlobal.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnAbrirRecarga = new JButton("1. Ir a Recarga (Pago Móvil)");
        btnAbrirRecarga.addActionListener(e -> {
            VistaMonedero vr = new VistaMonedero(monederoCompartido, this);
            vr.setVisible(true);
        });

        JButton btnAbrirPago = new JButton("2. Ir a Pagar Itinerario");
        btnAbrirPago.addActionListener(e -> {
            VistaPago vp = new VistaPago(monederoCompartido, this);
            vp.setVisible(true);
        });

        JButton btnActualizar = new JButton("↻ Actualizar Pantalla");
        btnActualizar.addActionListener(e -> actualizarSaldoPantalla());

        panel.add(lblSaldoGlobal);
        panel.add(btnAbrirRecarga);
        panel.add(btnAbrirPago);
        panel.add(btnActualizar);

        add(panel);
    }

    // Método para que las otras ventanas avisen que el saldo cambió
    public void actualizarSaldoPantalla() {
        lblSaldoGlobal.setText("Saldo Global: Bs. " + monederoCompartido.getSaldo().toPlainString());
    }
}