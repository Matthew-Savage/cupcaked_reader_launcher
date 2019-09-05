package sample;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class RemoteImage {

    public static void saveLocally(String webAddress, int imageNumber) {
        try {
            InputStream website = new URL(webAddress.replace("http:", "https:")).openStream();
            Path localPath = Paths.get(Val.DIR_ROOT.get() + File.separator + Val.DIR_THUMBS.get());
            Files.createDirectories(localPath);
            Files.copy(website, Paths.get(Val.DIR_ROOT.get() + File.separator + Val.DIR_THUMBS.get() + File.separator + imageNumber + ".jpg"), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
    }
}
