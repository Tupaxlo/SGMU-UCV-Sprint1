package test;

import modelo.Usuario;
import org.junit.jupiter.api.Test;
import vista.VistaLogin;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @Test
    public void testRegistrarUsuarioYAutenticarComoPasajero() throws Exception {
        VistaLogin vistaLogin = new VistaLogin();

        Field campoUsuariosRegistrados = VistaLogin.class.getDeclaredField("usuariosRegistrados");
        campoUsuariosRegistrados.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Usuario> usuariosRegistrados = (List<Usuario>) campoUsuariosRegistrados.get(vistaLogin);
        usuariosRegistrados.clear();

        Usuario usuarioPasajero = new Usuario("12345678", "Ana López", "ana@correo.com", "clave123", "Pasajero");
        usuariosRegistrados.add(usuarioPasajero);

        Method metodoCorreoValido = VistaLogin.class.getDeclaredMethod("correoValido", String.class);
        metodoCorreoValido.setAccessible(true);
        assertTrue((boolean) metodoCorreoValido.invoke(vistaLogin, "ana@correo.com"));

        Method metodoBuscarUsuario = VistaLogin.class.getDeclaredMethod("buscarUsuario", String.class, String.class, String.class);
        metodoBuscarUsuario.setAccessible(true);

        Usuario usuarioAutenticado = (Usuario) metodoBuscarUsuario.invoke(vistaLogin, "ana@correo.com", "clave123", "Pasajero");

        assertNotNull(usuarioAutenticado, "Debe encontrar al usuario registrado con sus credenciales.");
        assertEquals("Ana López", usuarioAutenticado.getNombre(), "El nombre del usuario autenticado debe coincidir.");
        assertEquals("Pasajero", usuarioAutenticado.getRol(), "El rol del usuario debe coincidir con el que inició sesión.");
    }

    @Test
    public void testValidarCorreoYRechazarCredencialesIncorrectas() throws Exception {
        VistaLogin vistaLogin = new VistaLogin();

        Method metodoCorreoValido = VistaLogin.class.getDeclaredMethod("correoValido", String.class);
        metodoCorreoValido.setAccessible(true);

        assertTrue((boolean) metodoCorreoValido.invoke(vistaLogin, "admin@empresa.com"));
        assertFalse((boolean) metodoCorreoValido.invoke(vistaLogin, "correo-invalido"));

        Method metodoBuscarUsuario = VistaLogin.class.getDeclaredMethod("buscarUsuario", String.class, String.class, String.class);
        metodoBuscarUsuario.setAccessible(true);

        Usuario usuarioInvalido = (Usuario) metodoBuscarUsuario.invoke(vistaLogin, "admin@empresa.com", "claveIncorrecta", "Administrador");
        assertNull(usuarioInvalido, "No debe autenticar un usuario con credenciales incorrectas.");
    }
}
