package com.svn.utils.mirror.app.remote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

/**
 * Created by mar on 19.06.15.
 */
public class Configuration {
    private static final String FINAL_CONFIGURATION_FILE =
            "";

    private static final String FILE_NAME = "conf.sh";
    //todo edit and generate configuration bash file!
    public File getConfig() {
        String text = "Hello wordl";
        try (PrintStream out = new PrintStream(new FileOutputStream("filename.txt"))) {
            out.print(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new File("filename.txt");
    }
}
