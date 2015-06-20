package com.svn.utils.mirror.app.util;

import com.svn.utils.mirror.app.remote.Configuration;
import org.junit.Test;

import javax.naming.ConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Created by mar on 20.06.15.
 */
public class ConfigurationTest {

    @Test
    public void generatedAndCheckConfigurationFile() throws FileNotFoundException {
        Configuration configuration = new Configuration();
        configuration.addRepoName("Test");
        configuration.addSVNServerPath("/home/mar/exampleRepository");
        configuration.addHookConfig(
                        "#!/bin/sh" +
                        "svnsync --non-interactive sync " +
                        "file://","post-commit"
        );
        try {
            File file = configuration.getConfig();
            String content = new Scanner(file).useDelimiter("\\Z").next();
            System.out.println(content);
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }


    }
}
