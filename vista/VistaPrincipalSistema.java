package vista;

import modelo.ConexionBD;
import modelo.Monedero;
import modelo.Usuario;
import modelo.Vehiculo;
import modelo.Itinerario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VistaPrincipalSistema extends JFrame {
    private final List<Usuario> usuariosRegistrados;
    private Usuario usuarioActual;
    private final JPanel contenido;
    private final CardLayout cardLayout;

    public VistaPrincipalSistema() {
        usuariosRegistrados = new ArrayList<>();
        cargarUsuariosDesdeBD();

        setTitle("SGMU-UCV - Sistema de Transporte");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        contenido = new JPanel(cardLayout);
        setContentPane(contenido);

        mostrarPantallaLogin();
    }

    private void cargarUsuariosDesdeBD() {
        try (Connection conexion = ConexionBD.abrirConexion();
             PreparedStatement stmt = conexion.prepareStatement("SELECT cedula, nombre, correo, clave, rol FROM usuarios");
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuariosRegistrados.add(new Usuario(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("correo"),
                        rs.getString("clave"),
                        rs.getString("rol")
                ));
            }
        } catch (SQLException e) {
            System.out.println("No se pudo cargar la base de datos de usuarios: " + e.getMessage());
        }
    }

    private void mostrarPantallaLogin() {
        JPanel panelLogin = new JPanel(new BorderLayout());
        panelLogin.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTabbedPane pestanias = new JTabbedPane();
        pestanias.addTab("Registro", crearPanelRegistroPublico());
        pestanias.addTab("Inicio de sesión", crearPanelInicioSesion());

        panelLogin.add(pestanias, BorderLayout.CENTER);

        contenido.removeAll();
        contenido.add(panelLogin, "login");
        cardLayout.show(contenido, "login");
        contenido.revalidate();
        contenido.repaint();
    }

    private JPanel crearPanelRegistroPublico() {
        JPanel panelRegistro = new JPanel(new GridLayout(0, 2, 10, 10));
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de pasajero"));

        JLabel etiquetaCedula = new JLabel("Cédula:");
        JTextField campoCedula = new JTextField();
        panelRegistro.add(etiquetaCedula);
        panelRegistro.add(campoCedula);

        JLabel etiquetaNombre = new JLabel("Nombre completo:");
        JTextField campoNombre = new JTextField();
        panelRegistro.add(etiquetaNombre);
        panelRegistro.add(campoNombre);

        JLabel etiquetaCorreo = new JLabel("Correo electrónico:");
        JTextField campoCorreo = new JTextField();
        panelRegistro.add(etiquetaCorreo);
        panelRegistro.add(campoCorreo);

        JLabel etiquetaClave = new JLabel("Contraseña:");
        JPasswordField campoClave = new JPasswordField();
        panelRegistro.add(etiquetaClave);
        panelRegistro.add(campoClave);

        JLabel etiquetaRol = new JLabel("Rol:");
        JTextField campoRol = new JTextField("Pasajero");
        campoRol.setEditable(false);
        panelRegistro.add(etiquetaRol);
        panelRegistro.add(campoRol);

        JButton botonRegistrar = new JButton("Registrarse");
        panelRegistro.add(new JLabel(""));
        panelRegistro.add(botonRegistrar);

        botonRegistrar.addActionListener(evento -> {
            String cedula = campoCedula.getText().trim();
            String nombre = campoNombre.getText().trim();
            String correo = campoCorreo.getText().trim();
            String clave = new String(campoClave.getPassword()).trim();
            String rol = "Pasajero";

            if (cedula.isEmpty() || nombre.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe completar todos los campos antes de registrarse.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!correoValido(correo)) {
                JOptionPane.showMessageDialog(this, "El correo debe terminar en @gmail.com o @hotmail.com.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (clave.length() < 6) {
                JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 6 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (usuarioExiste(correo, cedula)) {
                JOptionPane.showMessageDialog(this, "Ya existe un usuario con la misma cédula o correo registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Usuario nuevoUsuario = new Usuario(cedula, nombre, correo, clave, rol);
            usuariosRegistrados.add(nuevoUsuario);
            guardarUsuarioEnBD(nuevoUsuario);

            campoCedula.setText("");
            campoNombre.setText("");
            campoCorreo.setText("");
            campoClave.setText("");

            JOptionPane.showMessageDialog(this, "Registro de pasajero realizado correctamente.");
        });

        return panelRegistro;
    }

    private JPanel crearPanelRegistroAdmin() {
        JPanel panelRegistro = new JPanel(new GridLayout(0, 2, 10, 10));
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de administrador"));

        JLabel etiquetaCedula = new JLabel("Cédula:");
        JTextField campoCedula = new JTextField();
        panelRegistro.add(etiquetaCedula);
        panelRegistro.add(campoCedula);

        JLabel etiquetaNombre = new JLabel("Nombre completo:");
        JTextField campoNombre = new JTextField();
        panelRegistro.add(etiquetaNombre);
        panelRegistro.add(campoNombre);

        JLabel etiquetaCorreo = new JLabel("Correo electrónico:");
        JTextField campoCorreo = new JTextField();
        panelRegistro.add(etiquetaCorreo);
        panelRegistro.add(campoCorreo);

        JLabel etiquetaClave = new JLabel("Contraseña:");
        JPasswordField campoClave = new JPasswordField();
        panelRegistro.add(etiquetaClave);
        panelRegistro.add(campoClave);

        JLabel etiquetaRol = new JLabel("Rol:");
        JTextField campoRol = new JTextField("Administrador");
        campoRol.setEditable(false);
        panelRegistro.add(etiquetaRol);
        panelRegistro.add(campoRol);

        JButton botonRegistrar = new JButton("Registrar administrador");
        panelRegistro.add(new JLabel(""));
        panelRegistro.add(botonRegistrar);

        botonRegistrar.addActionListener(evento -> {
            if (usuarioActual == null || !"Administrador".equalsIgnoreCase(usuarioActual.getRol())) {
                JOptionPane.showMessageDialog(this, "Solo un administrador autenticado puede crear otro administrador.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String cedula = campoCedula.getText().trim();
            String nombre = campoNombre.getText().trim();
            String correo = campoCorreo.getText().trim();
            String clave = new String(campoClave.getPassword()).trim();

            if (cedula.isEmpty() || nombre.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe completar todos los campos antes de registrarse.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!correoValido(correo)) {
                JOptionPane.showMessageDialog(this, "El correo debe terminar en @gmail.com o @hotmail.com.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (clave.length() < 6) {
                JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 6 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (usuarioExiste(correo, cedula)) {
                JOptionPane.showMessageDialog(this, "Ya existe un usuario con la misma cédula o correo registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Usuario nuevoUsuario = new Usuario(cedula, nombre, correo, clave, "Administrador");
            usuariosRegistrados.add(nuevoUsuario);
            guardarUsuarioEnBD(nuevoUsuario);

            campoCedula.setText("");
            campoNombre.setText("");
            campoCorreo.setText("");
            campoClave.setText("");

            JOptionPane.showMessageDialog(this, "Administrador registrado correctamente.");
        });

        return panelRegistro;
    }

    private JPanel crearPanelInicioSesion() {
        JPanel panelInicioSesion = new JPanel(new GridLayout(0, 2, 10, 10));
        panelInicioSesion.setBorder(BorderFactory.createTitledBorder("Inicio de sesión"));

        JLabel etiquetaCorreo = new JLabel("Correo electrónico:");
        JTextField campoCorreo = new JTextField();
        panelInicioSesion.add(etiquetaCorreo);
        panelInicioSesion.add(campoCorreo);

        JLabel etiquetaClave = new JLabel("Contraseña:");
        JPasswordField campoClave = new JPasswordField();
        panelInicioSesion.add(etiquetaClave);
        panelInicioSesion.add(campoClave);

        JLabel etiquetaRol = new JLabel("Ingresar como:");
        String[] roles = {"Pasajero", "Administrador"};
        JComboBox<String> selectorRol = new JComboBox<>(roles);
        panelInicioSesion.add(etiquetaRol);
        panelInicioSesion.add(selectorRol);

        JButton botonIniciarSesion = new JButton("Iniciar sesión");
        panelInicioSesion.add(new JLabel(""));
        panelInicioSesion.add(botonIniciarSesion);

        botonIniciarSesion.addActionListener(evento -> {
            String correo = campoCorreo.getText().trim();
            String clave = new String(campoClave.getPassword()).trim();
            String rol = (String) selectorRol.getSelectedItem();

            if (correo.isEmpty() || clave.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar el correo y la contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Usuario usuarioAutenticado = buscarUsuario(correo, clave, rol);
            if (usuarioAutenticado == null) {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas o rol no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            usuarioActual = usuarioAutenticado;
            campoCorreo.setText("");
            campoClave.setText("");
            selectorRol.setSelectedIndex(0);

            JOptionPane.showMessageDialog(this, "Bienvenido, " + usuarioActual.getNombre() + ".");

            if ("Administrador".equalsIgnoreCase(usuarioActual.getRol())) {
                mostrarPanelAdministrador();
            } else {
                mostrarPanelPasajero();
            }
        });

        return panelInicioSesion;
    }

    private void mostrarPanelAdministrador() {
        JPanel panelBase = new JPanel(new BorderLayout());
        JToolBar barra = new JToolBar();
        barra.setFloatable(false);

        JButton btnTarifas = new JButton("Tarifas");
        JButton btnFlota = new JButton("Flota");
        JButton btnItinerarios = new JButton("Itinerarios");
        JButton btnAdmin = new JButton("Registrar admin");
        JButton btnCerrar = new JButton("Cerrar sesión");

        final JPanel areaTrabajo = new JPanel(new BorderLayout());
        panelBase.add(areaTrabajo, BorderLayout.CENTER);

        btnTarifas.addActionListener(e -> mostrarSeccionAdmin(areaTrabajo, "tarifas"));
        btnFlota.addActionListener(e -> mostrarSeccionAdmin(areaTrabajo, "flota"));
        btnItinerarios.addActionListener(e -> mostrarSeccionAdmin(areaTrabajo, "itinerarios"));
        btnAdmin.addActionListener(e -> mostrarSeccionAdmin(areaTrabajo, "registroAdmin"));
        btnCerrar.addActionListener(e -> {
            usuarioActual = null;
            mostrarPantallaLogin();
        });

        barra.add(btnTarifas);
        barra.add(btnFlota);
        barra.add(btnItinerarios);
        barra.add(btnAdmin);
        barra.add(Box.createHorizontalGlue());
        barra.add(btnCerrar);

        panelBase.add(barra, BorderLayout.NORTH);

        contenido.removeAll();
        contenido.add(panelBase, "admin");
        cardLayout.show(contenido, "admin");

        mostrarSeccionAdmin(areaTrabajo, "tarifas");
    }

    private void mostrarSeccionAdmin(JPanel areaTrabajo, String seccion) {
        areaTrabajo.removeAll();

        switch (seccion) {
            case "tarifas":
                VistaTarifa tarifa = new VistaTarifa();
                JPanel panelTarifas = new JPanel(new BorderLayout());
                panelTarifas.add(tarifa.getPanelTarifa(), BorderLayout.CENTER);
                areaTrabajo.add(panelTarifas, BorderLayout.CENTER);
                break;
            case "flota":
                areaTrabajo.add(crearPanelFlota(), BorderLayout.CENTER);
                break;
            case "itinerarios":
                areaTrabajo.add(crearPanelItinerario(), BorderLayout.CENTER);
                break;
            case "registroAdmin":
                areaTrabajo.add(crearPanelRegistroAdmin(), BorderLayout.CENTER);
                break;
            default:
                JPanel panelDefault = new JPanel(new BorderLayout());
                panelDefault.add(new JLabel("Sección no disponible", SwingConstants.CENTER), BorderLayout.CENTER);
                areaTrabajo.add(panelDefault, BorderLayout.CENTER);
        }

        areaTrabajo.revalidate();
        areaTrabajo.repaint();
    }

    private JPanel crearPanelFlota() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formulario = new JPanel(new GridLayout(0, 2, 10, 10));
        formulario.setBorder(BorderFactory.createTitledBorder("Registrar vehículo"));

        JTextField placa = new JTextField();
        JTextField modelo = new JTextField();
        JTextField capacidad = new JTextField();
        JComboBox<String> estado = new JComboBox<>(new String[]{"Activo", "En Mantenimiento", "Fuera de Servicio"});

        formulario.add(new JLabel("Placa:"));
        formulario.add(placa);
        formulario.add(new JLabel("Modelo:"));
        formulario.add(modelo);
        formulario.add(new JLabel("Capacidad:"));
        formulario.add(capacidad);
        formulario.add(new JLabel("Estado:"));
        formulario.add(estado);

        JButton guardar = new JButton("Registrar vehículo");
        JButton eliminar = new JButton("Eliminar vehículo");
        formulario.add(guardar);
        formulario.add(eliminar);

        String[] columnas = {"Placa", "Modelo", "Capacidad", "Estado"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(26);

        for (Vehiculo vehiculo : Vehiculo.getFlotaRegistrada()) {
            modeloTabla.addRow(new Object[]{vehiculo.getPlaca(), vehiculo.getModelo(), vehiculo.getCapacidad(), vehiculo.getEstado()});
        }

        guardar.addActionListener(e -> {
            if (placa.getText().trim().isEmpty() || modelo.getText().trim().isEmpty() || capacidad.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Complete todos los campos del vehículo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int capacidadNum = Integer.parseInt(capacidad.getText().trim());
                if (capacidadNum <= 0) {
                    JOptionPane.showMessageDialog(this, "La capacidad debe ser mayor que 0.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (!Vehiculo.placaUnica(placa.getText().trim())) {
                    JOptionPane.showMessageDialog(this, "La placa ya existe en la flota.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Vehiculo nuevoVehiculo = new Vehiculo(placa.getText().trim(), modelo.getText().trim(), capacidadNum, (String) estado.getSelectedItem());
                Vehiculo.registrar(nuevoVehiculo);
                modeloTabla.addRow(new Object[]{nuevoVehiculo.getPlaca(), nuevoVehiculo.getModelo(), nuevoVehiculo.getCapacidad(), nuevoVehiculo.getEstado()});
                placa.setText("");
                modelo.setText("");
                capacidad.setText("");
                estado.setSelectedIndex(0);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "La capacidad debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        eliminar.addActionListener(e -> {
            int filaSeleccionada = tabla.getSelectedRow();
            if (filaSeleccionada < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un vehículo para eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String placaEliminar = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
            Vehiculo.eliminar(placaEliminar);
            modeloTabla.removeRow(filaSeleccionada);
            JOptionPane.showMessageDialog(this, "Vehículo eliminado correctamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
        });

        panel.add(formulario, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelItinerario() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel formulario = new JPanel(new GridLayout(0, 2, 10, 10));
        formulario.setBorder(BorderFactory.createTitledBorder("Registrar itinerario"));

        JTextField id = new JTextField();
        JTextField ruta = new JTextField();
        JTextField fecha = new JTextField();
        JTextField hora = new JTextField();
        JTextField placa = new JTextField();
        JTextField chofer = new JTextField();

        formulario.add(new JLabel("ID:"));
        formulario.add(id);
        formulario.add(new JLabel("Ruta:"));
        formulario.add(ruta);
        formulario.add(new JLabel("Fecha:"));
        formulario.add(fecha);
        formulario.add(new JLabel("Hora:"));
        formulario.add(hora);
        formulario.add(new JLabel("Placa vehículo:"));
        formulario.add(placa);
        formulario.add(new JLabel("Chofer asignado:"));
        formulario.add(chofer);

        JButton guardar = new JButton("Registrar itinerario");
        formulario.add(new JLabel(""));
        formulario.add(guardar);

        String[] columnas = {"ID", "Ruta", "Fecha", "Hora", "Vehículo", "Chofer"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
        JTable tabla = new JTable(modeloTabla);
        tabla.setRowHeight(26);

        for (Itinerario itinerario : Itinerario.getItinerariosRegistrados()) {
            modeloTabla.addRow(new Object[]{
                itinerario.getId(),
                itinerario.getRuta(),
                itinerario.getFecha(),
                itinerario.getHora(),
                itinerario.getVehiculoAsignado() != null ? itinerario.getVehiculoAsignado().getPlaca() : "",
                itinerario.getChoferAsignado()
            });
        }

        guardar.addActionListener(e -> {
            if (id.getText().trim().isEmpty() || ruta.getText().trim().isEmpty() || fecha.getText().trim().isEmpty() || hora.getText().trim().isEmpty() || placa.getText().trim().isEmpty() || chofer.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe completar todos los campos del itinerario, incluido el chofer.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Itinerario.esRutaValida(ruta.getText())) {
                JOptionPane.showMessageDialog(this, "La ruta debe tener al menos 3 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Itinerario.esFechaValida(fecha.getText())) {
                JOptionPane.showMessageDialog(this, "La fecha debe tener formato yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Itinerario.esHoraValida(hora.getText())) {
                JOptionPane.showMessageDialog(this, "La hora debe tener formato HH:mm.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Vehiculo.existePlaca(placa.getText().trim())) {
                JOptionPane.showMessageDialog(this, "La placa del vehículo no existe en la flota registrada.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (Itinerario.existeId(id.getText(), Itinerario.getItinerariosRegistrados())) {
                JOptionPane.showMessageDialog(this, "El ID del itinerario ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Vehiculo vehiculoAsignado = null;
            for (Vehiculo vehiculo : Vehiculo.getFlotaRegistrada()) {
                if (vehiculo.getPlaca().equalsIgnoreCase(placa.getText().trim())) {
                    vehiculoAsignado = vehiculo;
                    break;
                }
            }

            Itinerario nuevoItinerario = new Itinerario(id.getText().trim(), ruta.getText().trim(), fecha.getText().trim(), hora.getText().trim(), vehiculoAsignado, chofer.getText().trim());
            Itinerario.registrar(nuevoItinerario);
            modeloTabla.addRow(new Object[]{
                nuevoItinerario.getId(),
                nuevoItinerario.getRuta(),
                nuevoItinerario.getFecha(),
                nuevoItinerario.getHora(),
                nuevoItinerario.getVehiculoAsignado().getPlaca(),
                nuevoItinerario.getChoferAsignado()
            });

            id.setText("");
            ruta.setText("");
            fecha.setText("");
            hora.setText("");
            placa.setText("");
            chofer.setText("");
        });

        panel.add(formulario, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    private void mostrarPanelPasajero() {
        JPanel panelBase = new JPanel(new BorderLayout());
        JToolBar barra = new JToolBar();
        barra.setFloatable(false);

        final JPanel areaTrabajo = new JPanel(new BorderLayout());
        panelBase.add(areaTrabajo, BorderLayout.CENTER);

        JButton btnMonedero = new JButton("Monedero");
        JButton btnPago = new JButton("Pago");
        JButton btnCerrar = new JButton("Cerrar sesión");

        btnMonedero.addActionListener(e -> mostrarSeccionPasajero(areaTrabajo, "monedero"));
        btnPago.addActionListener(e -> mostrarSeccionPasajero(areaTrabajo, "pago"));
        btnCerrar.addActionListener(e -> {
            usuarioActual = null;
            mostrarPantallaLogin();
        });

        barra.add(btnMonedero);
        barra.add(btnPago);
        barra.add(Box.createHorizontalGlue());
        barra.add(btnCerrar);

        panelBase.add(barra, BorderLayout.NORTH);

        contenido.removeAll();
        contenido.add(panelBase, "pasajero");
        cardLayout.show(contenido, "pasajero");

        mostrarSeccionPasajero(areaTrabajo, "monedero");
    }

    private void mostrarSeccionPasajero(JPanel areaTrabajo, String seccion) {
        areaTrabajo.removeAll();

        switch (seccion) {
            case "monedero":
                VistaMonedero monedero = new VistaMonedero(new Monedero("USUARIO-001"), null);
                JPanel panelMonedero = new JPanel(new BorderLayout());
                panelMonedero.add(monedero.getContentPane(), BorderLayout.CENTER);
                areaTrabajo.add(panelMonedero, BorderLayout.CENTER);
                break;
            case "pago":
                VistaPago pago = new VistaPago(new Monedero("USUARIO-001"), null);
                JPanel panelPago = new JPanel(new BorderLayout());
                panelPago.add(pago.getContentPane(), BorderLayout.CENTER);
                areaTrabajo.add(panelPago, BorderLayout.CENTER);
                break;
            default:
                JPanel panelDefault = new JPanel(new BorderLayout());
                panelDefault.add(new JLabel("Sección no disponible", SwingConstants.CENTER), BorderLayout.CENTER);
                areaTrabajo.add(panelDefault, BorderLayout.CENTER);
        }

        areaTrabajo.revalidate();
        areaTrabajo.repaint();
    }

    private void guardarUsuarioEnBD(Usuario usuario) {
        try (Connection conexion = ConexionBD.abrirConexion();
             PreparedStatement stmt = conexion.prepareStatement(
                     "INSERT INTO usuarios (cedula, nombre, correo, clave, rol) VALUES (?, ?, ?, ?, ?)")) {
            stmt.setString(1, usuario.getCedula());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getCorreo());
            stmt.setString(4, usuario.getClave());
            stmt.setString(5, usuario.getRol());
            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el usuario en la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean correoValido(String correo) {
        return correo.matches("(?i)^[A-Za-z0-9._%+-]+@(gmail\\.com|hotmail\\.com)$");
    }

    private boolean usuarioExiste(String correo, String cedula) {
        for (Usuario usuarioRegistrado : usuariosRegistrados) {
            if (usuarioRegistrado.getCorreo().equalsIgnoreCase(correo) || usuarioRegistrado.getCedula().equalsIgnoreCase(cedula)) {
                return true;
            }
        }
        return false;
    }

    private Usuario buscarUsuario(String correo, String clave, String rol) {
        for (Usuario usuarioRegistrado : usuariosRegistrados) {
            if (usuarioRegistrado.getCorreo().equalsIgnoreCase(correo)
                    && usuarioRegistrado.getClave().equals(clave)
                    && usuarioRegistrado.getRol().equalsIgnoreCase(rol)) {
                return usuarioRegistrado;
            }
        }
        return null;
    }
}
