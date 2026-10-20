package vista;

import modelo.Itinerario;
import modelo.Vehiculo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VistaItinerario extends JFrame {
    private final List<Itinerario> listaItinerarios;
    private final DefaultTableModel modeloTabla;

    private final JTextField txtId;
    private final JTextField txtRuta;
    private final JTextField txtFecha;
    private final JTextField txtHora;
    private final JTextField txtPlacaVehiculo;
    private final JTextField txtChofer;

    public VistaItinerario() {
        listaItinerarios = new ArrayList<>();

        setTitle("Gestión de Itinerarios - SGMU-UCV");
        setSize(800, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(0, 2, 5, 5));
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

        panelFormulario.add(new JLabel(" Hora (Ej. 08:00):"));
        txtHora = new JTextField();
        panelFormulario.add(txtHora);

        panelFormulario.add(new JLabel(" Placa Vehículo Asignado:"));
        txtPlacaVehiculo = new JTextField();
        panelFormulario.add(txtPlacaVehiculo);

        panelFormulario.add(new JLabel(" Chofer asignado:"));
        txtChofer = new JTextField();
        panelFormulario.add(txtChofer);

        JButton btnRegistrar = new JButton("Registrar Itinerario");
        panelFormulario.add(new JLabel());
        panelFormulario.add(btnRegistrar);

        add(panelFormulario, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(new String[]{"ID", "Ruta", "Fecha", "Hora", "Vehículo (Placa)", "Chofer"}, 0);
        JTable tablaItinerarios = new JTable(modeloTabla);
        add(new JScrollPane(tablaItinerarios), BorderLayout.CENTER);

        btnRegistrar.addActionListener(e -> registrarItinerario());
    }

    private void registrarItinerario() {
        String id = txtId.getText().trim();
        String ruta = txtRuta.getText().trim();
        String fecha = txtFecha.getText().trim();
        String hora = txtHora.getText().trim();
        String placaVehiculo = txtPlacaVehiculo.getText().trim();
        String chofer = txtChofer.getText().trim();

        if (id.isEmpty() || ruta.isEmpty() || fecha.isEmpty() || hora.isEmpty() || placaVehiculo.isEmpty() || chofer.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Itinerario.esRutaValida(ruta)) {
            JOptionPane.showMessageDialog(this, "La ruta debe contener al menos 3 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Itinerario.esFechaValida(fecha)) {
            JOptionPane.showMessageDialog(this, "La fecha debe tener formato yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Itinerario.esHoraValida(hora)) {
            JOptionPane.showMessageDialog(this, "La hora debe tener formato HH:mm.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!Vehiculo.existePlaca(placaVehiculo)) {
            JOptionPane.showMessageDialog(this, "La placa del vehículo no existe en la flota registrada.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (Itinerario.existeId(id, listaItinerarios)) {
            JOptionPane.showMessageDialog(this, "El ID del itinerario ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Vehiculo vehiculoTemp = null;
        for (Vehiculo vehiculo : Vehiculo.getFlotaRegistrada()) {
            if (vehiculo.getPlaca().equalsIgnoreCase(placaVehiculo)) {
                vehiculoTemp = vehiculo;
                break;
            }
        }

        Itinerario nuevoItinerario = new Itinerario(id, ruta, fecha, hora, vehiculoTemp, chofer);
        listaItinerarios.add(nuevoItinerario);

        modeloTabla.addRow(new Object[]{
            nuevoItinerario.getId(),
            nuevoItinerario.getRuta(),
            nuevoItinerario.getFecha(),
            nuevoItinerario.getHora(),
            nuevoItinerario.getVehiculoAsignado().getPlaca(),
            nuevoItinerario.getChoferAsignado()
        });

        txtId.setText("");
        txtRuta.setText("");
        txtFecha.setText("");
        txtHora.setText("");
        txtPlacaVehiculo.setText("");
        txtChofer.setText("");

        JOptionPane.showMessageDialog(this, "Itinerario registrado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VistaItinerario().setVisible(true);
        });
    }
}