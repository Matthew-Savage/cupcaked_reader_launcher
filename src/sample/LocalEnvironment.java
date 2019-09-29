package sample;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class LocalEnvironment {

    public static void verify() throws Exception {
        placeholderImage();
    }

    private static void placeholderImage() throws Exception {
        File file = new File("placeholder.png");
        Path placeholder= Paths.get(Val.DIR_ROOT.get() + File.separator + file);
        if (Files.notExists(placeholder)) {
            InputStream readerFile = new URL("https://" + Val.REMOTE_URL.get() + "/reader/" + file).openStream();
            Files.copy(readerFile, Paths.get(Val.DIR_ROOT.get() + File.separator + file));
        }
    }

    private static void databases(String databaseName) {

    }

    private static void thumbs(){

    }
}