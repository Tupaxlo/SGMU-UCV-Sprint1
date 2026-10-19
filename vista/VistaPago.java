package vista;

import modelo.Monedero;
import servicio.ServicioPago;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class VistaPago extends JFrame {

    private Monedero monedero;
    private final VistaPrincipal vistaPadre;
    private ServicioPago servicioPago;

    private JLabel lblSaldo;
    private JTextField txtTarifaBase;
    private JComboBox<String> cmbRoles;

    public VistaPago(Monedero monederoCompartido, VistaPrincipal padre) {
        this.monedero = monederoCompartido;
        this.vistaPadre = padre;
        this.servicioPago = new ServicioPago();

        setTitle("Cobro de Itinerario");
        setSize(480, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);

        construirInterfaz();
    }

    private void construirInterfaz() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        lblSaldo = new JLabel("Bs. " + monedero.getSaldo().toPlainString());
        txtTarifaBase = new JTextField("10.00"); // Tarifa por defecto de 10 Bs
        
        String[] roles = {"Regular", "Estudiante", "Profesor", "Tercera Edad"};
        cmbRoles = new JComboBox<>(roles);

        panel.add(new JLabel("Saldo actual en el Monedero:"));
        panel.add(lblSaldo);

        panel.add(new JLabel("Tarifa Base del Viaje (Bs.):"));
        panel.add(txtTarifaBase);

        panel.add(new JLabel("Rol del Usuario:"));
        panel.add(cmbRoles);

        JButton btnPagar = new JButton("Cobrar Pasaje");
        btnPagar.addActionListener(e -> realizarPago());

        panel.add(new JLabel("")); // Espacio en blanco
        panel.add(btnPagar);

        add(panel);
    }

    private void realizarPago() {
        try {
            BigDecimal tarifa = new BigDecimal(txtTarifaBase.getText().trim().replace(",", "."));
            String rol = (String) cmbRoles.getSelectedItem();

            String resultado = servicioPago.procesarPagoCobro(monedero, tarifa, rol);

            lblSaldo.setText("Bs. " + monedero.getSaldo().toPlainString());
            JOptionPane.showMessageDialog(this, resultado, "Resumen de Cobro", JOptionPane.INFORMATION_MESSAGE);

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La tarifa debe ser numérica.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Saldo Insuficiente", JOptionPane.WARNING_MESSAGE);
        }
    }
}