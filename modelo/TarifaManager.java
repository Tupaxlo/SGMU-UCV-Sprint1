package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TarifaManager {
    public static double obtenerTarifa(String tipoUsuario, String tipoRuta) {
        String sql = "SELECT monto FROM tarifas WHERE tipoUsuario = ? AND tipoRuta = ?";
        try (Connection conexion = ConexionBD.abrirConexion();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {
            stmt.setString(1, tipoUsuario);
            stmt.setString(2, tipoRuta);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("monto");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
