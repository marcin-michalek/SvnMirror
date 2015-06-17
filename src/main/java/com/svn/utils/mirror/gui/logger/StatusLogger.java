package com.svn.utils.mirror.gui.logger;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;

/**
 * SvnMirror
 * Created by Marcin on 2015-06-12.
 */
public class StatusLogger {
    private JTextPane logContainer;

    private void appendToPane(JTextPane jTextPane, String message, Color color) {
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        AttributeSet attributeSet = styleContext.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
        attributeSet = styleContext.addAttribute(attributeSet, StyleConstants.FontFamily, "Lucida Console");
        attributeSet = styleContext.addAttribute(attributeSet, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);
        int len = jTextPane.getDocument().getLength();
        jTextPane.setCaretPosition(len);
        jTextPane.setCharacterAttributes(attributeSet, false);
        jTextPane.replaceSelection(message);
    }

    public StatusLogger(JTextPane logContainer) {
        this.logContainer = logContainer;
    }

    public void logError(String message) {
        appendToPane(logContainer, message+"\n", Color.RED);
    }

    public void logSuccess(String message) {
        appendToPane(logContainer, message+"\n", Color.GREEN);

    }
}
