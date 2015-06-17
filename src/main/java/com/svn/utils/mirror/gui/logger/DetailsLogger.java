package com.svn.utils.mirror.gui.logger;

import com.svn.utils.mirror.app.Revision;

import javax.swing.*;
import java.awt.*;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-12.
 */
public class DetailsLogger extends AbstractTextPaneLogger {
    public DetailsLogger(JTextPane logContainer) {
        super(logContainer);
    }

    public void logRevisionDetails(Revision revision) {
        logContainer.setText("");
        appendToPane("\nMESSAGE:", Color.BLACK);
        appendToPane(revision.getMessage(), Color.GREEN);
        appendToPane("\nAUTHOR:", Color.BLACK);
        appendToPane(revision.getAuthor(), Color.GREEN);
        appendToPane("\nDATE:", Color.BLACK);
        appendToPane(revision.getDate().toString(), Color.BLUE);
        appendToPane("\nCHANGES:", Color.BLACK);
        appendToPane(revision.getChanges(), Color.MAGENTA);
    }
}
