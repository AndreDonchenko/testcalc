package ua.goit.andre.testcalc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created by andre on 17.05.16.
 */
public class HistoryDB {

    private static Connection connection;
    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryDB.class);

    public static String getHistory() {
        StringBuilder rowHistory = new StringBuilder();
        StringBuilder fullHistory = new StringBuilder();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM history");
            while (resultSet.next()) {
                rowHistory.append("<tr><td>");
                rowHistory.append(resultSet.getString("argument"));
                rowHistory.append("</td>");
                rowHistory.append("<td>");
                rowHistory.append(resultSet.getString("result"));
                rowHistory.append("</td></tr>");
            }
            fullHistory.append(rowHistory);
        } catch (SQLException e) {
            LOGGER.error("Cannot read history from database.");
        }
        return fullHistory.toString();
    }

    public static void addHistory(String argument, String result) {
        try {
            PreparedStatement statement=connection.prepareStatement("INSERT INTO history VALUES (?,?)");
            statement.setString(1, argument);
            statement.setString(2, result);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Cannot write history to database.");
        }
    }
    public static void initDatabase() {
        final String jdbcDriverName = "org.h2.Driver";
        try{
            LOGGER.info("Loading JDBC driver: " + jdbcDriverName);
            Class.forName(jdbcDriverName);
            LOGGER.info("Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            LOGGER.error("Cannot find driver: " + jdbcDriverName);
        }
        try {
            connection = DriverManager.getConnection("jdbc:h2:~/testcalc", "sa", "");
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS history (argument VARCHAR, result VARCHAR);");
        } catch (SQLException e) {
            LOGGER.error("Cannot connect to database.");
        }
    }

}
