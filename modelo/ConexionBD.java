package modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConexionBD {
    private static final String URL = "jdbc:sqlite:./sgmu.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("No se encontró el driver SQLite.", e);
        }
    }

    public static Connection abrirConexion() throws SQLException {
        Connection conexion = DriverManager.getConnection(URL);
        crearTablasSiNoExisten(conexion);
        return conexion;
    }

    private static void crearTablasSiNoExisten(Connection conexion) throws SQLException {
        try (Statement stmt = conexion.createStatement()) {
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS usuarios (" +
                "cedula TEXT PRIMARY KEY, " +
                "nombre TEXT NOT NULL, " +
                "correo TEXT NOT NULL UNIQUE, " +
                "clave TEXT NOT NULL, " +
                "rol TEXT NOT NULL)"
            );

            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS tarifas (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tipoUsuario TEXT NOT NULL, " +
                "tipoRuta TEXT NOT NULL, " +
                "monto REAL NOT NULL)"
            );

            insertarUsuarioAdminBase(stmt);
            insertarTarifasBase(stmt);
        }
    }

    private static void insertarUsuarioAdminBase(Statement stmt) throws SQLException {
        java.sql.ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM usuarios WHERE correo = 'admin@gmail.com'");
        if (rs.next() && rs.getInt(1) == 0) {
            stmt.executeUpdate("INSERT INTO usuarios (cedula, nombre, correo, clave, rol) VALUES ('10000001', 'Administrador Principal', 'admin@gmail.com', 'admin123', 'Administrador')");
        }
        rs.close();
    }

    private static void insertarTarifasBase(Statement stmt) throws SQLException {
        String consulta = "SELECT COUNT(*) FROM tarifas";
        java.sql.ResultSet rs = stmt.executeQuery(consulta);
        if (rs.next() && rs.getInt(1) == 0) {
            stmt.executeUpdate("INSERT INTO tarifas (tipoUsuario, tipoRuta, monto) VALUES ('Comunidad Universitaria', 'Extraurbana', 2.50)");
            stmt.executeUpdate("INSERT INTO tarifas (tipoUsuario, tipoRuta, monto) VALUES ('Público General', 'Extraurbana', 5.00)");
        }
        rs.close();
    }
}
