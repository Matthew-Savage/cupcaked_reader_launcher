package sample;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Arrays {

    private static Statement statement;
    private static ResultSet resultSet;

    public static void initializeArrays() {
        Database.openConnection(Val.DB_NAME_MANGA.get());
        CategoryMangaLists.notCollectedMangaList.addAll(buildArray(Val.DB_TABLE_AVAILABLE.get()));
        CategoryMangaLists.rejectedMangaList.addAll(buildArray(Val.DB_TABLE_NOT_INTERESTED.get()));
        CategoryMangaLists.completedMangaList.addAll(buildArray(Val.DB_TABLE_COMPLETED.get()));
        CategoryMangaLists.collectedMangaList.addAll(buildArray(Val.DB_TABLE_READING.get()));
        CategoryMangaLists.bookmark.addAll(buildArray(Val.DB_TABLE_BOOKMARK.get()));
        Database.closeConnection();
        Database.openConnection(Val.DB_NAME_DOWNLOADING.get());
        CategoryMangaLists.downloading.addAll(buildArray(Val.DB_TABLE_DOWNLOAD.get()));
        Database.closeConnection();
    }

    private static ArrayList<Manga> buildArray(String tableName) {
        return resultSetToArrayList(tableName);
    }

    private static ArrayList<Manga> resultSetToArrayList(String tableName) {
        ArrayList<Manga> list = new ArrayList<>();
        resultSet = fetchResultSet(tableName);
        try {
            while (resultSet.next()) {
                list.add(new Manga(
                        resultSet.getInt("title_id"),
                        resultSet.getString("title"),
                        resultSet.getString("authors"),
                        resultSet.getString("status"),
                        resultSet.getString("summary"),
                        resultSet.getString("web_address"),
                        resultSet.getString("genre_tags"),
                        resultSet.getInt("total_chapters"),
                        resultSet.getInt("current_page"),
                        resultSet.getInt("last_chapter_read"),
                        resultSet.getInt("last_chapter_downloaded"),
                        resultSet.getInt("new_chapters"),
                        resultSet.getInt("favorite")));
            }
            resultSet.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
        return list;
    }

    private static ResultSet fetchResultSet(String tableName) {
        try {
            statement = Database.connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM " + tableName);
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
        return resultSet;
    }
}
