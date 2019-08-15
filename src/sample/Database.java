package sample;

import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.sql.*;

public class Database {

    private static Connection connection;

    public static int readVersion() {
        try {
            openConnection();
            Statement statement = connection.createStatement();
            return parseResultSet(statement.executeQuery("SELECT " + Val.COLUMN.get() + " FROM " + Val.TABLE.get()));
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        } finally {
            closeConnection();
        }
        return -1;
    }

    public static void writeVersion(int version) {
        try {
            openConnection();
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM " + Val.TABLE.get());
            statement.execute("INSERT INTO " + Val.TABLE.get() + " VALUES ('" + version + "')");
        } catch (Exception e) {

        } finally {
            closeConnection();
        }
    }

    private static int parseResultSet(ResultSet resultSet) throws Exception{
        return resultSet.getInt(Val.COLUMN.get());
    }

    private static void openConnection() {
        try {
            connection = DriverManager.getConnection(Val.JDBC_PREFIX.get() + Val.DIR_ROOT.get() + File.separator + Val.DIR_DB.get() + File.separator + Val.DB_NAME_SETTINGS.get());
        } catch (SQLException e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
    }

    private static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
    }

}
