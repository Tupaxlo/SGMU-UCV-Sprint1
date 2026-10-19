package vista;

import modelo.ConexionBD;
import modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VistaLogin extends JFrame {
    private final List<Usuario> usuariosRegistrados;
    private Usuario usuarioSesionActual;
    private JComboBox<String> selectorRolRegistro;

    public VistaLogin() {
        usuariosRegistrados = new ArrayList<>();
        cargarUsuariosDesdeBD();
        actualizarRolesRegistro();

        setTitle("Login de pasajeros y administradores");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panelPrincipal = new JPanel(new BorderLayout(15, 15));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTabbedPane pestanias = new JTabbedPane();
        pestanias.addTab("Registro", crearPanelRegistro());
        pestanias.addTab("Inicio de sesión", crearPanelInicioSesion());

        panelPrincipal.add(pestanias, BorderLayout.CENTER);
        add(panelPrincipal);
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
            JOptionPane.showMessageDialog(this, "No se pudo cargar la base de datos de usuarios.", "Error", JOptionPane.ERROR_MESSAGE);
        }
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
            JOptionPane.showMessageDialog(this, "No se pudo guardar el usuario en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel crearPanelRegistro() {
        JPanel panelRegistro = new JPanel(new GridLayout(0, 2, 10, 10));
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registro de usuario"));

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
        selectorRolRegistro = new JComboBox<>(obtenerRolesDisponibles());
        panelRegistro.add(etiquetaRol);
        panelRegistro.add(selectorRolRegistro);

        JButton botonRegistrar = new JButton("Registrarse");
        panelRegistro.add(new JLabel(""));
        panelRegistro.add(botonRegistrar);

        botonRegistrar.addActionListener(evento -> {
            String cedula = campoCedula.getText().trim();
            String nombre = campoNombre.getText().trim();
            String correo = campoCorreo.getText().trim();
            String clave = new String(campoClave.getPassword()).trim();
            String rol = (String) selectorRolRegistro.getSelectedItem();

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

            if ("Administrador".equalsIgnoreCase(rol) && (usuarioSesionActual == null || !"Administrador".equalsIgnoreCase(usuarioSesionActual.getRol()))) {
                JOptionPane.showMessageDialog(this, "Solo un administrador autenticado puede registrar otro administrador.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (usuarioExiste(correo, cedula)) {
                JOptionPane.showMessageDialog(this, "Ya existe un usuario con la misma cédula o correo registrado.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            usuariosRegistrados.add(new Usuario(cedula, nombre, correo, clave, rol));
            guardarUsuarioEnBD(new Usuario(cedula, nombre, correo, clave, rol));
            campoCedula.setText("");
            campoNombre.setText("");
            campoCorreo.setText("");
            campoClave.setText("");
            selectorRolRegistro.setSelectedIndex(0);

            JOptionPane.showMessageDialog(this, "Usuario registrado correctamente.");
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

            if (!correoValido(correo)) {
                JOptionPane.showMessageDialog(this, "El correo debe terminar en @gmail.com o @hotmail.com.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Usuario usuarioAutenticado = buscarUsuario(correo, clave, rol);

            if (usuarioAutenticado == null) {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas o rol no válido para este usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            usuarioSesionActual = usuarioAutenticado;
            actualizarRolesRegistro();

            campoCorreo.setText("");
            campoClave.setText("");
            selectorRol.setSelectedIndex(0);

            JOptionPane.showMessageDialog(this, "Bienvenido, " + usuarioAutenticado.getNombre() + ". Ha iniciado sesión como " + usuarioAutenticado.getRol() + ".");
        });

        return panelInicioSesion;
    }

    private String[] obtenerRolesDisponibles() {
        if (usuarioSesionActual != null && "Administrador".equalsIgnoreCase(usuarioSesionActual.getRol())) {
            return new String[]{"Pasajero", "Administrador"};
        }
        return new String[]{"Pasajero"};
    }

    private void actualizarRolesRegistro() {
        if (selectorRolRegistro == null) {
            return;
        }

        selectorRolRegistro.setModel(new DefaultComboBoxModel<>(obtenerRolesDisponibles()));
        selectorRolRegistro.setSelectedIndex(0);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VistaLogin ventanaLogin = new VistaLogin();
            ventanaLogin.setVisible(true);
        });
    }
}
