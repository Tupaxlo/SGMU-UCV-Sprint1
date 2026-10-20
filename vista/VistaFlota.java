package vista;

import modelo.Vehiculo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VistaFlota extends JFrame {
    private final List<Vehiculo> flota;
    private final DefaultTableModel modeloTabla;

    public VistaFlota() {
        flota = new ArrayList<>();
        
        setTitle("Backoffice - Gestión de Flota SGMU-UCV");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Vehículo"));

        JTextField txtPlaca = new JTextField();
        JTextField txtModelo = new JTextField();
        JTextField txtCapacidad = new JTextField();
        JComboBox<String> cbEstado = new JComboBox<>(new String[]{"Activo", "En Mantenimiento", "Fuera de Servicio"});

        panelFormulario.add(new JLabel("Placa:"));
        panelFormulario.add(txtPlaca);

        panelFormulario.add(new JLabel("Modelo (Ej. Encava, Toyota):"));
        panelFormulario.add(txtModelo);

        panelFormulario.add(new JLabel("Capacidad de Pasajeros:"));
        panelFormulario.add(txtCapacidad);

        panelFormulario.add(new JLabel("Estado Operativo:"));
        panelFormulario.add(cbEstado);

        JButton btnRegistrar = new JButton("Registrar Vehículo");
        JButton btnEliminar = new JButton("Eliminar Vehículo");
        panelFormulario.add(new JLabel(""));
        panelFormulario.add(btnRegistrar);
        panelFormulario.add(new JLabel(""));
        panelFormulario.add(btnEliminar);

        add(panelFormulario, BorderLayout.NORTH);

        String[] columnas = {"Placa", "Modelo", "Capacidad", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tablaFlota = new JTable(modeloTabla);
        add(new JScrollPane(tablaFlota), BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> {
            String placa = txtPlaca.getText().trim();
            String modelo = txtModelo.getText().trim();
            String capStr = txtCapacidad.getText().trim();
            String estado = (String) cbEstado.getSelectedItem();

            if (placa.isEmpty() || modelo.isEmpty() || capStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, llene todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!placa.matches("^[A-Z0-9-]+$")) {
                JOptionPane.showMessageDialog(this, "La placa solo puede contener letras mayúsculas, números y guion.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Vehiculo.placaUnica(placa)) {
                JOptionPane.showMessageDialog(this, "La placa ya existe en la flota.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int capacidad = Integer.parseInt(capStr);
                if (capacidad <= 0) {
                    JOptionPane.showMessageDialog(this, "La capacidad debe ser mayor que cero.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Vehiculo nuevoVehiculo = new Vehiculo(placa, modelo, capacidad, estado);
                Vehiculo.registrar(nuevoVehiculo);
                flota.add(nuevoVehiculo);
                modeloTabla.addRow(new Object[]{placa, modelo, capacidad, estado});

                txtPlaca.setText("");
                txtModelo.setText("");
                txtCapacidad.setText("");
                cbEstado.setSelectedIndex(0);

                JOptionPane.showMessageDialog(this, "¡Vehículo registrado exitosamente en la flota!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La capacidad debe ser un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnEliminar.addActionListener(e -> {
            int filaSeleccionada = tablaFlota.getSelectedRow();
            if (filaSeleccionada < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione una fila para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String placa = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
            Vehiculo.eliminar(placa);
            flota.removeIf(v -> v.getPlaca().equalsIgnoreCase(placa));
            modeloTabla.removeRow(filaSeleccionada);
            JOptionPane.showMessageDialog(this, "Vehículo eliminado correctamente.");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VistaFlota().setVisible(true);
        });
    }
}