package vista;

import modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VistaLogin extends JFrame {
    private final List<Usuario> usuariosRegistrados;

    public VistaLogin() {
        usuariosRegistrados = new ArrayList<>();

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
        String[] roles = {"Pasajero", "Administrador"};
        JComboBox<String> selectorRol = new JComboBox<>(roles);
        panelRegistro.add(etiquetaRol);
        panelRegistro.add(selectorRol);

        JButton botonRegistrar = new JButton("Registrarse");
        panelRegistro.add(new JLabel(""));
        panelRegistro.add(botonRegistrar);

        botonRegistrar.addActionListener(evento -> {
            String cedula = campoCedula.getText().trim();
            String nombre = campoNombre.getText().trim();
            String correo = campoCorreo.getText().trim();
            String clave = new String(campoClave.getPassword()).trim();
            String rol = (String) selectorRol.getSelectedItem();

            if (cedula.isEmpty() || nombre.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe completar todos los campos antes de registrarse.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!correoValido(correo)) {
                JOptionPane.showMessageDialog(this, "El correo electrónico no tiene un formato válido.", "Error", JOptionPane.ERROR_MESSAGE);
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

            usuariosRegistrados.add(new Usuario(cedula, nombre, correo, clave, rol));
            campoCedula.setText("");
            campoNombre.setText("");
            campoCorreo.setText("");
            campoClave.setText("");
            selectorRol.setSelectedIndex(0);

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

            Usuario usuarioAutenticado = buscarUsuario(correo, clave, rol);

            if (usuarioAutenticado == null) {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas o rol no válido para este usuario.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            campoCorreo.setText("");
            campoClave.setText("");
            selectorRol.setSelectedIndex(0);

            JOptionPane.showMessageDialog(this, "Bienvenido, " + usuarioAutenticado.getNombre() + ". Ha iniciado sesión como " + usuarioAutenticado.getRol() + ".");
        });

        return panelInicioSesion;
    }

    private boolean correoValido(String correo) {
        return correo.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
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
