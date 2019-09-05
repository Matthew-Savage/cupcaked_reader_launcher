package sample;

import javax.swing.plaf.nimbus.State;
import java.io.File;
import java.sql.*;

public class Database {

    private static Connection connection;

    public static int readVersion() {
        try {
            openConnection(Val.DB_NAME_SETTINGS.get());
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
            openConnection(Val.DB_NAME_SETTINGS.get());
            Statement statement = connection.createStatement();
            statement.execute("DELETE FROM " + Val.TABLE.get());
            statement.execute("INSERT INTO " + Val.TABLE.get() + " VALUES ('" + version + "')");
        } catch (Exception e) {

        } finally {
            closeConnection();
        }
    }

    public static void addMangaEntry(String tableName, int mangaId, String title, String authors, String status, String summary, String webAddress, String genreTags, int totalChapters, int currentPage, int lastChapterRead, int lastChapterDownloaded, int newChaptersBoolean, int favoriteBoolean) {
        try (Statement sqlStatement = connection.createStatement()) {
            sqlStatement.execute("INSERT INTO " + tableName + " (title_id, title, authors, status, summary, web_address, genre_tags, total_chapters, current_page, last_chapter_read, last_chapter_downloaded, new_chapters, favorite) VALUES " +
                    "('" + mangaId + "', '" + title + "', '" + authors + "', '" + status + "', '" + summary + "', '" + webAddress + "', '" + genreTags + "', '" + totalChapters + "', '" + currentPage + "', '" + lastChapterRead + "', '" + lastChapterDownloaded + "', '" + newChaptersBoolean + "', '" + favoriteBoolean + "')");
        } catch (SQLException e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
    }

    private static int parseResultSet(ResultSet resultSet) throws Exception{
        return resultSet.getInt(Val.COLUMN.get());
    }

    public static void openConnection(String dbFileName) {
        try {
            connection = DriverManager.getConnection(Val.JDBC_PREFIX.get() + Val.DIR_ROOT.get() + File.separator + Val.DIR_DB.get() + File.separator + dbFileName);
        } catch (SQLException e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
    }

}
