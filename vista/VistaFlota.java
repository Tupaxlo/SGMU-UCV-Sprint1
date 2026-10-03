package vista;

import modelo.Vehiculo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VistaFlota extends JFrame {
    private List<Vehiculo> flota;
    private DefaultTableModel modeloTabla;

    public VistaFlota() {
        flota = new ArrayList<>();
        
        // Configuración básica de la ventana
        setTitle("Backoffice - Gestión de Flota SGMU-UCV");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null); // Centrar en pantalla

        // Panel superior: Formulario de Registro
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Vehículo"));

        panelFormulario.add(new JLabel("Placa:"));
        JTextField txtPlaca = new JTextField();
        panelFormulario.add(txtPlaca);

        panelFormulario.add(new JLabel("Modelo (Ej. Encava, Toyota):"));
        JTextField txtModelo = new JTextField();
        panelFormulario.add(txtModelo);

        panelFormulario.add(new JLabel("Capacidad de Pasajeros:"));
        JTextField txtCapacidad = new JTextField();
        panelFormulario.add(txtCapacidad);

        panelFormulario.add(new JLabel("Estado Operativo:"));
        String[] estados = {"Activo", "En Mantenimiento", "Fuera de Servicio"};
        JComboBox<String> cbEstado = new JComboBox<>(estados);
        panelFormulario.add(cbEstado);

        JButton btnRegistrar = new JButton("Registrar Vehículo");
        panelFormulario.add(new JLabel("")); // Espacio vacío para alinear el botón
        panelFormulario.add(btnRegistrar);

        add(panelFormulario, BorderLayout.NORTH);

        // Panel central: Tabla para mostrar los vehículos
        String[] columnas = {"Placa", "Modelo", "Capacidad", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tablaFlota = new JTable(modeloTabla);
        add(new JScrollPane(tablaFlota), BorderLayout.CENTER);

        // Lógica del botón "Registrar Vehículo"
        btnRegistrar.addActionListener(e -> {
            String placa = txtPlaca.getText().trim();
            String modelo = txtModelo.getText().trim();
            String capStr = txtCapacidad.getText().trim();
            String estado = (String) cbEstado.getSelectedItem();

            // Validación de campos vacíos
            if (placa.isEmpty() || modelo.isEmpty() || capStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, llene todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int capacidad = Integer.parseInt(capStr);
                
                // Crear el objeto Vehiculo y guardarlo en la lista
                Vehiculo nuevoVehiculo = new Vehiculo(placa, modelo, capacidad, estado);
                flota.add(nuevoVehiculo);
                
                // Agregar el vehículo a la tabla visual
                modeloTabla.addRow(new Object[]{placa, modelo, capacidad, estado});
                
                // Limpiar el formulario
                txtPlaca.setText("");
                txtModelo.setText("");
                txtCapacidad.setText("");
                
                JOptionPane.showMessageDialog(this, "¡Vehículo registrado exitosamente en la flota!");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La capacidad debe ser un número entero válido.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // Método main para probar esta pantalla de forma independiente
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VistaFlota().setVisible(true);
        });
    }
}