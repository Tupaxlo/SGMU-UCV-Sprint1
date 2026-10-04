package vista;

import modelo.Itinerario;
import modelo.Vehiculo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VistaItinerario extends JFrame {
    private List<Itinerario> listaItinerarios;
    private DefaultTableModel modeloTabla;
    
    private JTextField txtId;
    private JTextField txtRuta;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JTextField txtPlacaVehiculo;

    public VistaItinerario() {
        listaItinerarios = new ArrayList<>();

        setTitle("Gestión de Itinerarios - SGMU-UCV");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Panel Superior (Formulario)
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar Nuevo Itinerario"));

        panelFormulario.add(new JLabel(" ID Itinerario:"));
        txtId = new JTextField();
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel(" Ruta:"));
        txtRuta = new JTextField();
        panelFormulario.add(txtRuta);

        panelFormulario.add(new JLabel(" Fecha (Ej. 2026-10-05):"));
        txtFecha = new JTextField();
        panelFormulario.add(txtFecha);

        panelFormulario.add(new JLabel(" Hora (Ej. 08:00 AM):"));
        txtHora = new JTextField();
        panelFormulario.add(txtHora);

        panelFormulario.add(new JLabel(" Placa Vehículo Asignado:"));
        txtPlacaVehiculo = new JTextField();
        panelFormulario.add(txtPlacaVehiculo);

        JButton btnRegistrar = new JButton("Registrar Itinerario");
        panelFormulario.add(new JLabel()); // Espacio vacío
        panelFormulario.add(btnRegistrar);

        add(panelFormulario, BorderLayout.NORTH);

        // Panel Central (Tabla)
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Ruta", "Fecha", "Hora", "Vehículo (Placa)"}, 0);
        JTable tablaItinerarios = new JTable(modeloTabla);
        add(new JScrollPane(tablaItinerarios), BorderLayout.CENTER);

        // Acción del botón
        btnRegistrar.addActionListener(e -> registrarItinerario());
    }

    private void registrarItinerario() {
        String id = txtId.getText().trim();
        String ruta = txtRuta.getText().trim();
        String fecha = txtFecha.getText().trim();
        String hora = txtHora.getText().trim();
        String placaVehiculo = txtPlacaVehiculo.getText().trim();

        if (id.isEmpty() || ruta.isEmpty() || fecha.isEmpty() || hora.isEmpty() || placaVehiculo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Creamos un vehículo temporal con la placa indicada para asociarlo al itinerario
        Vehiculo vehiculoTemp = new Vehiculo(placaVehiculo, "Modelo Genérico", 30, "Activo");

        // Usamos el constructor real de tu clase Itinerario
        Itinerario nuevoItinerario = new Itinerario(id, ruta, fecha, hora, vehiculoTemp);
        listaItinerarios.add(nuevoItinerario);

        // Agregar a la tabla visual
        modeloTabla.addRow(new Object[]{
            nuevoItinerario.getId(), 
            nuevoItinerario.getRuta(), 
            nuevoItinerario.getFecha(), 
            nuevoItinerario.getHora(), 
            nuevoItinerario.getVehiculoAsignado().getPlaca()
        });

        // Limpiar campos
        txtId.setText("");
        txtRuta.setText("");
        txtFecha.setText("");
        txtHora.setText("");
        txtPlacaVehiculo.setText("");

        JOptionPane.showMessageDialog(this, "Itinerario registrado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VistaItinerario().setVisible(true);
        });
    }
}