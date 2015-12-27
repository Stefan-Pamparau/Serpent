package core;

import java.io.IOException;

public class Terminal implements Runnable {

    private String osName = "";
    private String osVersion = "";
    private String osArch = "";
    private String startCommand = "";

    public Terminal() {
        osName = System.getProperty("os.name").toLowerCase();
        osVersion = System.getProperty("os.version");
        osArch = System.getProperty("os.arch");

        if (osName.contains("win")) {
            startCommand = "cmd /c start cmd.exe";
        } else if (osName.contains("linux")) {
            startCommand = "/usr/bin/xterm";
        }
    }

    @Override
    public void run() {
        try {
            Process p = Runtime.getRuntime().exec(startCommand);
            p.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Failed to start terminal\n");
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public String toString() {
        return "OS: " + osName.toUpperCase() + ", " + osArch +
                "\nVersion: " + osVersion;
    }
}
