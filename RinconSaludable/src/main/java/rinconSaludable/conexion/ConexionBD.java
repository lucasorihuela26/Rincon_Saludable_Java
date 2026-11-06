package rinconSaludable.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import rinconSaludable.BaseDatosException;

public final class ConexionBD {
    // Se guarda en el directorio de trabajo configurado en Eclipse.
    private static final String URL = "jdbc:sqlite:rincon_saludable.db";

    private ConexionBD() { }

    public static Connection obtenerConexion() throws BaseDatosException {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            throw new BaseDatosException("No se pudo conectar a SQLite: " + e.getMessage());
        }
    }

    public static void inicializarBaseDatos() throws BaseDatosException {
        String sql = """
                CREATE TABLE IF NOT EXISTS productos (
                    id TEXT PRIMARY KEY,
                    nombre TEXT NOT NULL,
                    tipo TEXT NOT NULL CHECK (tipo IN ('ENVASADO', 'GRANEL')),
                    precio_base REAL NOT NULL CHECK (precio_base >= 0),
                    stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),
                    gramos REAL,
                    CHECK ((tipo = 'ENVASADO' AND gramos IS NULL)
                        OR (tipo = 'GRANEL' AND gramos IS NOT NULL AND gramos > 0))
                )
                """;
        try (Connection con = obtenerConexion(); Statement st = con.createStatement()) {
            st.executeUpdate(sql);
        } catch (SQLException e) {
            throw new BaseDatosException("No se pudo crear/verificar la tabla productos: " + e.getMessage());
        }
    }
}
