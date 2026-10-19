package vista;

import modelo.Tarifa;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VistaTarifa extends JFrame {
    private final List<Tarifa> tarifas;
    private final DefaultTableModel modeloTabla;

    public VistaTarifa() {
        tarifas = new ArrayList<>();
        tarifas.add(new Tarifa("Comunidad Universitaria", "Extraurbana", 2.50));
        tarifas.add(new Tarifa("Público General", "Extraurbana", 5.00));

        setTitle("Configuración de Tarifas - SGMU-UCV");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Configurar tarifa para ruta extraurbana"));

        panelFormulario.add(new JLabel("Tipo de usuario:"));
        String[] tiposUsuario = {"Comunidad Universitaria", "Público General"};
        JComboBox<String> comboTipoUsuario = new JComboBox<>(tiposUsuario);
        panelFormulario.add(comboTipoUsuario);

        panelFormulario.add(new JLabel("Tipo de ruta:"));
        String[] tiposRuta = {"Extraurbana"};
        JComboBox<String> comboTipoRuta = new JComboBox<>(tiposRuta);
        panelFormulario.add(comboTipoRuta);

        panelFormulario.add(new JLabel("Monto (USD):"));
        JTextField txtMonto = new JTextField();
        panelFormulario.add(txtMonto);

        JButton btnGuardar = new JButton("Guardar tarifa");
        panelFormulario.add(new JLabel(""));
        panelFormulario.add(btnGuardar);

        add(panelFormulario, BorderLayout.NORTH);

        String[] columnas = {"Tipo de usuario", "Tipo de ruta", "Monto"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        cargarTarifasIniciales();

        JTable tablaTarifas = new JTable(modeloTabla);
        add(new JScrollPane(tablaTarifas), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> {
            String tipoUsuario = (String) comboTipoUsuario.getSelectedItem();
            String tipoRuta = (String) comboTipoRuta.getSelectedItem();
            String montoTexto = txtMonto.getText().trim();

            if (montoTexto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un monto para la tarifa.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                double monto = Double.parseDouble(montoTexto);

                if (monto <= 0) {
                    JOptionPane.showMessageDialog(this, "El monto debe ser mayor que cero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Tarifa tarifaExistente = buscarTarifa(tipoUsuario, tipoRuta);
                if (tarifaExistente != null) {
                    tarifaExistente.setMonto(monto);
                } else {
                    tarifas.add(new Tarifa(tipoUsuario, tipoRuta, monto));
                }

                actualizarTabla();
                txtMonto.setText("");
                JOptionPane.showMessageDialog(this, "Tarifa guardada correctamente.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "El monto debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void cargarTarifasIniciales() {
        for (Tarifa tarifa : tarifas) {
            modeloTabla.addRow(new Object[]{tarifa.getTipoUsuario(), tarifa.getTipoRuta(), tarifa.getMonto()});
        }
    }

    private void actualizarTabla() {
        modeloTabla.setRowCount(0);
        for (Tarifa tarifa : tarifas) {
            modeloTabla.addRow(new Object[]{tarifa.getTipoUsuario(), tarifa.getTipoRuta(), tarifa.getMonto()});
        }
    }

    private Tarifa buscarTarifa(String tipoUsuario, String tipoRuta) {
        for (Tarifa tarifa : tarifas) {
            if (tarifa.getTipoUsuario().equalsIgnoreCase(tipoUsuario)
                    && tarifa.getTipoRuta().equalsIgnoreCase(tipoRuta)) {
                return tarifa;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VistaTarifa().setVisible(true);
        });
    }
}
