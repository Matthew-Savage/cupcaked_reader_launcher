package sample;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ReaderUpdater {

    public static void downloadNewVersion(String file) throws Exception {
        InputStream readerFile = new URL("https://" + Val.REMOTE_URL.get() + "/reader/" + file).openStream();
        Files.copy(readerFile, Paths.get(Val.DIR_ROOT.get() + File.separator + file), StandardCopyOption.REPLACE_EXISTING);
    }
}
