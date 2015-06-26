package com.svn.utils.mirror.app.remote;

import javax.naming.ConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mar on 19.06.15.
 */
public class Configuration {
    private static final String FINAL_CONFIGURATION_FILE =
            "#!/bin/bash\n" +
                    "\n" +
                    "SVN_SERVER_PATH=@SVN_SERVER_PATH\n" +
                    "HOOK_DIR=\"$SVN_SERVER_PATH/hook\"\n" +
                    "HOOK_NAMES=@HOOK_NAME\n" +
                    "HOOK_CONFIGURATIONS=@HOOK_CONF\n" +
                    "\n" +
                    "svnadmin create @REPO_NAME\n" +
                    "cd HOOK_DIR\n" +
                    "\n" +
                    "\n";

    private static final String FILE_NAME = "conf.sh";
    private static final String REPO_NAME = "@REPO_NAME";
    private static final String SVN_SERVER_PATH = "@SVN_SERVER_PATH";
    private static final String HOOK_NAME = "@HOOK_NAME";
    private static final String HOOK_CONF = "@HOOK_CONF";
    private Map<String, String> properties;
    private Map<String, String> hookConfiguration;

    public Configuration() {
        properties = new HashMap<>();
        hookConfiguration = new HashMap<>();
    }

    public Configuration addRepoName(String name) {
        if (properties.containsKey(REPO_NAME))
            properties.replace(REPO_NAME, name);
        else
            properties.put(REPO_NAME, name);
        return this;
    }

    public Configuration addSVNServerPath(String path) {
        if (properties.containsKey(SVN_SERVER_PATH))
            properties.replace(SVN_SERVER_PATH, path);
        else
            properties.put(SVN_SERVER_PATH, path);
        return this;
    }

    public Configuration addHookConfig(String hookBody, String name) {
        hookConfiguration.put(name, hookBody);
        return this;
    }

    public void clearHooks() {
        hookConfiguration.clear();
    }

    public void clearProperties() {
        properties.clear();
    }


    public File build() throws ConfigurationException {

        File configurationFile = new File(FILE_NAME);

        String configurationBody = FINAL_CONFIGURATION_FILE;

        for (Map.Entry<String, String> entry : properties.entrySet()) {
            configurationBody = configurationBody.replace(entry.getKey(), entry.getValue());
        }

        String names = "";
        String confs = "";

        for (Map.Entry<String, String> entry : hookConfiguration.entrySet()) {
            names += entry.getKey() + ";";
            confs += entry.getValue() + ";";
        }

        configurationBody = configurationBody.replace(HOOK_NAME, names);
        configurationBody = configurationBody.replace(HOOK_CONF, confs);

        try (PrintStream out = new PrintStream(new FileOutputStream(FILE_NAME))) {
            out.print(configurationBody);
        } catch (FileNotFoundException e) {
            throw new ConfigurationException("Cannot find file " + FILE_NAME);
        }

        return configurationFile;
    }

}
