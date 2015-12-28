package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import core.Gap;

public class FilesUtilities {
    private static final Integer DEFAULT_INITIAL_GAP_SIZE = 128;

    public static Gap readFromFile(File file) {
        Gap gapBuffer = null;
        BufferedReader br = null;
        try {
            gapBuffer = new Gap(DEFAULT_INITIAL_GAP_SIZE);
            br = new BufferedReader(new FileReader(file));
            int c;
            while ((c = br.read()) != -1) {
                gapBuffer.insert((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return gapBuffer;
    }

    public static void writeToFile(Path filePath, Gap gapBuffer) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(filePath.toFile()));
            for (int i = 0; i < gapBuffer.getTextLength(); i++) {
                bw.write(gapBuffer.getCharacter(i));
            }
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
