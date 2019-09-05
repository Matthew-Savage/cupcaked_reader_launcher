package sample;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class ReaderVersion {

    public static void checkForUpdate() throws Exception{
        if (compareLocalToRemote()) {
            Controller.paneTop.setValue("top_update_2");
            Controller.paneMid.setValue("mid_update_1");
            ReaderUpdater.downloadNewVersion("main.jar");
            ReaderUpdater.downloadNewVersion("changelog.txt");
        }
    }

    private static boolean compareLocalToRemote() {
        int localVersion = fetchLocalVersion();
        int remoteVersion;
        if (remoteLibraryAvailable()) {
            remoteVersion = fetchRemoteVersion();
        } else {
            return false;
        }
        return remoteVersion > localVersion;
    }

    private static boolean remoteLibraryAvailable() {
        try {
            return InetAddress.getByName(Val.REMOTE_URL.get()).isReachable(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static int fetchLocalVersion() {
        return Database.readVersion();
    }

    private static int fetchRemoteVersion() {
        try {
            InputStream versionFile = new URL("https://" + Val.REMOTE_URL.get() + "/reader/version.txt").openStream();
            Scanner scanner = new Scanner(versionFile, StandardCharsets.UTF_8);
            return scanner.nextInt();
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
        return -1;
    }
}
