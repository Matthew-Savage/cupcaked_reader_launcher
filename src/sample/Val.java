package sample;

public enum Val {

    REMOTE_URL("www.matthew-savage.net"),
//    DIR_ROOT (GetApplicationPath.getPath()),  //live dir
    DIR_ROOT("C:\\Users\\Apple Laptop\\Documents\\reader"),  //development dir
//    DIR_ROOT  ("C:\\Users\\Apple\\Cupcaked Manga Reader"),  //development dir
    DIR_DB("data"),
    DIR_THUMBS("thumbs"),
    DB_TABLE_AVAILABLE("available_manga"),
    DB_TABLE_COMPLETED("completed"),
    DB_TABLE_READING("currently_reading"),
    DB_TABLE_HISTORY("history"),
    DB_TABLE_DOWNLOAD("downloading"),
    DB_TABLE_NOT_INTERESTED("not_interested"),
    DB_TABLE_UNDECIDED("undecided"),
    DB_TABLE_BOOKMARK("bookmark"),
    DB_NAME_MANGA("main.db"),
    DB_NAME_DOWNLOADING("downloads.db"),
    URL_ROOT("https://manganelo.com/"),
    VER("version_number"),
    COLUMN("version"),
    JDBC_PREFIX("jdbc:sqlite:"),
    DB_NAME_SETTINGS("usr.db");

    private final String value;

    Val(final String valueString) {
        this.value = valueString;
    }

    public final String get() {
        return value;
    }

}
