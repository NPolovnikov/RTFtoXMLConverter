package com.techinfocom.utils;

import java.util.Stack;

/**
 * Created by volkov_kv on 27.05.2016.
 */
public class GroupState {
    private Stack<TextFormat> stack;

    public GroupState() {
        this.stack = new Stack<>();
    }

    public void addLevel() {
        stack.push(new TextFormat());
    }

    public void removeLevel() {
        stack.pop();
    }

    public void processCommand(RtfCommand rtfCommand) {
        stack.peek().processCommand(rtfCommand);
    }

    public Integer getDepth() {
        return stack.size();
    }

    public TextFormat getCurrent() {
        TextFormat currentTextFormat = new TextFormat();
        for (TextFormat tf : stack) {
            currentTextFormat.getFontFormat().addAll(tf.getFontFormat());
            currentTextFormat.getParagraphFormat().addAll(tf.paragraphFormat);
        }
        return currentTextFormat;
    }

    public String printCurrentLevel() {
        TextFormat currentTextFormat = getCurrent();
        return currentTextFormat.toString();
    }
}
