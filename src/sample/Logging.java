package sample;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Logging {

    public static void logError(String error) {
        try {
            File errorFile = new File(Val.DIR_ROOT.get() + File.separator + "err.log");
            FileWriter fileWriter = new FileWriter(errorFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            if (!errorFile.exists()) {
                errorFile.createNewFile();
            }
            bufferedWriter.write(System.currentTimeMillis() + " - " + error);
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
            Logging.logError(e.toString());
        }
    }
}