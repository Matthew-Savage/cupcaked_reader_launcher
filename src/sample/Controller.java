package sample;

public class Controller {

    public void initialize() {
        if (ReaderVersion.checkForUpdate()) {
            ReaderUpdater.downloadNewVersion("main.jar");
            ReaderUpdater.downloadNewVersion("changelog.txt");
        }
    }

    private static void loltest() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", "MangaReaderPinkFinal.jar");
            processBuilder.redirectErrorStream(true);
            processBuilder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
