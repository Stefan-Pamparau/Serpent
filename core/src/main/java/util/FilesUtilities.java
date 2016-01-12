package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilesUtilities {
    public static String readFromFile(File file) {
        byte[] bytes = null;
        try {
            bytes = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(bytes, Charset.defaultCharset());
    }

    public static void writeToFile(String text, Path filePath) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(filePath.toFile()));
            bw.write(text);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
