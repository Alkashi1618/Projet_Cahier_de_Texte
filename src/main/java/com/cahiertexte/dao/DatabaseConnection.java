package com.cahiertexte.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // Configuration MySQL (phpMyAdmin)
    private static final String HOST = "localhost"; // ou ton IP si c’est distant
    private static final int PORT = 3306;
    private static final String DATABASE = "Cahier_de_Texte";
    private static final String USERNAME = "root"; // ou ton utilisateur phpMyAdmin
    private static final String PASSWORD = ""; // ton mot de passe, si défini

    // URL JDBC MySQL
    private static final String DB_URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE + "?useSSL=false&serverTimezone=UTC";

    // Driver JDBC pour MySQL
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    // Instance unique (Singleton)
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            // Charger le driver MySQL
            Class.forName(DRIVER);
            System.out.println("✓ Driver MySQL chargé avec succès");

            // Établir la connexion
            this.connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            System.out.println("✓ Connexion à MySQL établie avec succès");
            System.out.println("  Hôte : " + HOST);
            System.out.println("  Base de données : " + DATABASE);

        } catch (ClassNotFoundException e) {
            System.err.println("✗ Erreur : Driver MySQL non trouvé");
            e.printStackTrace();
            throw new RuntimeException("Driver MySQL non trouvé", e);

        } catch (SQLException e) {
            System.err.println("✗ Erreur de connexion à MySQL");
            System.err.println("  URL: " + DB_URL);
            System.err.println("  User: " + USERNAME);
            System.err.println("  Message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Impossible de se connecter à MySQL", e);
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                System.out.println("⚠ Reconnexion à MySQL...");
                connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                System.out.println("✓ Reconnexion réussie");
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la reconnexion");
            e.printStackTrace();
            throw new RuntimeException("Erreur de connexion", e);
        }
        return connection;
    }

    public boolean testConnection() {
        try {
            Connection conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("✗ Test de connexion échoué");
            e.printStackTrace();
            return false;
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Connexion fermée avec succès");
            }
        } catch (SQLException e) {
            System.err.println("✗ Erreur lors de la fermeture");
            e.printStackTrace();
        }
    }

    public String getConnectionInfo() {
        StringBuilder info = new StringBuilder();
        info.append("=== INFORMATIONS DE CONNEXION MYSQL ===\n");
        info.append("Hôte: ").append(HOST).append("\n");
        info.append("Base de données: ").append(DATABASE).append("\n");
        info.append("Utilisateur: ").append(USERNAME).append("\n");
        info.append("Driver: ").append(DRIVER).append("\n");

        try {
            if (connection != null) {
                info.append("État: ").append(connection.isClosed() ? "FERMÉE" : "OUVERTE").append("\n");
                info.append("Catalogue: ").append(connection.getCatalog()).append("\n");
            } else {
                info.append("État: NON INITIALISÉE\n");
            }
        } catch (SQLException e) {
            info.append("État: ERREUR - ").append(e.getMessage()).append("\n");
        }

        return info.toString();
    }

    public static void main(String[] args) {
        System.out.println("=== TEST DE CONNEXION MYSQL ===\n");

        try {
            DatabaseConnection dbConn = DatabaseConnection.getInstance();

            if (dbConn.testConnection()) {
                System.out.println("\n✓✓✓ CONNEXION RÉUSSIE ✓✓✓\n");
                System.out.println(dbConn.getConnectionInfo());

                Connection conn = dbConn.getConnection();
                var stmt = conn.createStatement();
                var rs = stmt.executeQuery("SELECT COUNT(*) as total FROM users");

                if (rs.next()) {
                    System.out.println("\n=== TEST DE REQUÊTE ===");
                    System.out.println("Nombre d'utilisateurs : " + rs.getInt("total"));
                }

                rs.close();
                stmt.close();

            } else {
                System.err.println("\n✗✗✗ CONNEXION ÉCHOUÉE ✗✗✗\n");
            }

        } catch (Exception e) {
            System.err.println("\n✗✗✗ ERREUR LORS DU TEST ✗✗✗");
            e.printStackTrace();
        }
    }
}
