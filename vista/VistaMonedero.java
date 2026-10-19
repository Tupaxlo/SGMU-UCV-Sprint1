package vista;

import modelo.Monedero;
import servicio.ServicioRecarga;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class VistaMonedero extends JFrame {

    private final Monedero monedero;
    private final ServicioRecarga servicio;

    private JTextField txtMonto;
    private JTextField txtTelefono;
    private JTextField txtBanco;
    private JTextField txtReferencia;
    private JLabel lblSaldo;

    public VistaMonedero() {
        monedero = new Monedero("USUARIO-001");
        servicio = new ServicioRecarga();

        setTitle("SGTU-UCV - Monedero Virtual");
        setSize(480, 440);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        construirInterfaz();
    }

    private void construirInterfaz() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(
            20, 20, 20, 20
        ));

        lblSaldo = new JLabel("Saldo: Bs. 0.00");
        txtMonto = new JTextField();
        txtTelefono = new JTextField();
        txtBanco = new JTextField();
        txtReferencia = new JTextField();

        panel.add(new JLabel("Monedero:"));
        panel.add(new JLabel(monedero.getIdUsuario()));

        panel.add(new JLabel("Saldo disponible:"));
        panel.add(lblSaldo);

        panel.add(new JLabel("Monto a recargar (Bs.):"));
        panel.add(txtMonto);

        panel.add(new JLabel("Teléfono Pago Móvil:"));
        panel.add(txtTelefono);

        panel.add(new JLabel("Código del banco:"));
        panel.add(txtBanco);

        panel.add(new JLabel("Referencia:"));
        panel.add(txtReferencia);

        JButton btnRecargar = new JButton("Realizar recarga");
        btnRecargar.addActionListener(e -> realizarRecarga());

        panel.add(new JLabel(""));
        panel.add(btnRecargar);

        add(panel);
    }

    private void realizarRecarga() {
        try {
            String montoTexto = txtMonto.getText()
                    .trim().replace(",", ".");

            BigDecimal monto = new BigDecimal(montoTexto);

            String resultado = servicio.recargar(
                monedero,
                monto,
                txtTelefono.getText().trim(),
                txtBanco.getText().trim(),
                txtReferencia.getText().trim()
            );

            lblSaldo.setText(
                "Saldo: Bs. " + monedero.getSaldo().toPlainString()
            );

            JOptionPane.showMessageDialog(
                this,
                resultado + "\nNuevo saldo: Bs. "
                    + monedero.getSaldo().toPlainString(),
                "Recarga exitosa",
                JOptionPane.INFORMATION_MESSAGE
            );

            txtMonto.setText("");
            txtTelefono.setText("");
            txtBanco.setText("");
            txtReferencia.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this,
                "Introduce un monto numérico válido.",
                "Error",
                JOptionPane.ERROR_MESSAGE
            );

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "No se pudo realizar la recarga",
                JOptionPane.WARNING_MESSAGE
            );
        }
    }
}