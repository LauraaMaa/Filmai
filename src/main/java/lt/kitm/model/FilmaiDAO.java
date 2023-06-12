package lt.kitm.model;

import lt.kitm.dto.FilmaiDTO;

import java.sql.*;
import java.util.ArrayList;

public class FilmaiDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/filmai";
    private static final String[] prisijungimas = new String[]{"root", ""};

    public static ArrayList<FilmaiDTO> visiFilmai() throws SQLException {
        ArrayList<FilmaiDTO> filmai = new ArrayList<>();
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT * FROM filmai JOIN kategorijos ON kategorijos_id = kategorijos.id");
        while (resultSet.next()) {
            filmai.add(new FilmaiDTO(
                    resultSet.getInt(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getInt(5),
                    resultSet.getString(6)
                    ));
        }
        statement.close();
        connection.close();
        return filmai;
    }

    public static void delete(int id) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM filmai WHERE id = ?");
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static void create(Filmai filmai) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO filmai (reitingas, pavadinimas, aprasas, kategorijos_id) VALUES (?, ?, ?, ?)");
        preparedStatement.setDouble(1, filmai.getReitingas());
        preparedStatement.setString(2, filmai.getPavadinimas());
        preparedStatement.setString(3, filmai.getAprasas());
        preparedStatement.setInt(4, filmai.getKategorijosId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static void update(Filmai filmai) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE filmai SET reitingas = ?, pavadinimas = ?, santrauka = ?, kategorijos_id = ? WHERE id = ?");
        preparedStatement.setDouble(1, filmai.getReitingas());
        preparedStatement.setString(2, filmai.getPavadinimas());
        preparedStatement.setString(3, filmai.getAprasas());
        preparedStatement.setInt(4, filmai.getKategorijosId());
        preparedStatement.setInt(5, filmai.getId());
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static void pamegtiFilmai(int vartotojoId, int filmaiId) throws SQLException {
        //TODO: neleisti pamegti daugiau nei viena kartą
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO pamegti_filmai VALUES(?, ?)");
        preparedStatement.setInt(1, vartotojoId);
        preparedStatement.setInt(2, filmaiId);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static ArrayList<FilmaiDTO> gautiPamegtusFilmus(int vartotojoId) throws SQLException {
        ArrayList<FilmaiDTO> filmai = new ArrayList<>();
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM filmai JOIN kategorijos ON kategorijos_id = kategorijos.id JOIN pamegti_filmai ON filmai.id = pamegti_filmai.filmai_id WHERE pamegti_filmai.vartotojo_id = ?");
        preparedStatement.setInt(1, vartotojoId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            filmai.add(new FilmaiDTO(
                    resultSet.getInt(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getInt(5),
                    resultSet.getString(6)
            ));
        }
        preparedStatement.close();
        connection.close();
        return filmai;
    }

    public static void rezervuotiFilma(int vartotojoId, int filmaiId) throws SQLException {
        //TODO: neleisti rezervuoti daugiau nei viena kartą
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO rezervacijos VALUES(?, ?)");
        preparedStatement.setInt(1, vartotojoId);
        preparedStatement.setInt(2, filmaiId);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static ArrayList<FilmaiDTO> gautiRezervuotusFilmus(int vartotojoId) throws SQLException {
        ArrayList<FilmaiDTO> filmai = new ArrayList<>();
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM filmai JOIN kategorijos ON kategorijos_id = kategorijos.id JOIN rezervacijos ON filmai.id = rezervacijos.filmai_id WHERE rezervacijos.vartotojo_id = ?");
        preparedStatement.setInt(1, vartotojoId);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            filmai.add(new FilmaiDTO(
                    resultSet.getInt(1),
                    resultSet.getInt(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getInt(5),
                    resultSet.getString(6)
            ));
        }
        preparedStatement.close();
        connection.close();
        return filmai;
    }

    public static boolean filmasRezervuotas(int filmaiId) throws SQLException {
        boolean filmasUzimtas = false;
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM rezervacijos WHERE filmai_id = ?");
        preparedStatement.setInt(1, filmaiId);
        ResultSet resultSet = preparedStatement.executeQuery();
        filmasUzimtas = resultSet.isBeforeFirst();
        preparedStatement.close();
        connection.close();
        return filmasUzimtas;
    }

    public static void nebemegtiFilmo(int vartotojoId, int filmaiId) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM pamegti_filmai WHERE vartotojo_id = ? AND filmai_id = ?");
        preparedStatement.setInt(1, vartotojoId);
        preparedStatement.setInt(2, filmaiId);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static void atsauktiRezervacija(int vartotojoId, int filmaiId) throws SQLException {
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM rezervacijos WHERE vartotojo_id = ? AND filmai_id = ?");
        preparedStatement.setInt(1, vartotojoId);
        preparedStatement.setInt(2, filmaiId);
        preparedStatement.executeUpdate();
        preparedStatement.close();
        connection.close();
    }

    public static boolean arVartotojasPamegesFilma(int vartotojoId, int filmaiId) throws SQLException {
        boolean filmasVartotojoPamegtas = false;
        Connection connection = DriverManager.getConnection(URL, prisijungimas[0], prisijungimas[1]);
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM pamegti_filmai WHERE vartotojo_id = ? AND filmai_id = ?");
        preparedStatement.setInt(1, vartotojoId);
        preparedStatement.setInt(2, filmaiId);
        ResultSet resultSet = preparedStatement.executeQuery();
        filmasVartotojoPamegtas = resultSet.isBeforeFirst();
        preparedStatement.close();
        connection.close();
        return filmasVartotojoPamegtas;
    }
}
