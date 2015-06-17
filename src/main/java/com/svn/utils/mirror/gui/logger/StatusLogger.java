package com.svn.utils.mirror.gui.logger;

import javax.swing.*;
import java.awt.*;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-12.
 */
public class StatusLogger extends AbstractTextPaneLogger {
    public StatusLogger(JTextPane logContainer) {
        super(logContainer);
    }

    public void logError(String message) {
        appendToPane(message + "\n", Color.RED);
    }

    public void logSuccess(String message) {
        appendToPane(message + "\n", Color.GREEN);

    }
}
