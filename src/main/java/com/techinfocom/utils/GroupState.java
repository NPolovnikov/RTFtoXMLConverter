package com.techinfocom.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by volkov_kv on 27.05.2016.
 */
public class GroupState {
    private Stack<List<RtfCommand>> stack;

    public GroupState() {
        this.stack = new Stack<>();
    }

    public void addLevel() {
        stack.push(new ArrayList<>());
    }

    public void removeLevel() {
        stack.pop();
    }

    public void addToCurentLevel(RtfCommand rtfCommand) {
        stack.peek().add(rtfCommand);
    }

    public Integer getDepth(){
        return stack.size();
    }

    public List<RtfCommand> getCurrentLevel() {
        List<RtfCommand> commands = new ArrayList<>();
        for (List<RtfCommand> cl : stack) {
            commands.addAll(cl);
        }
        return commands;
    }

    public String printCurrentLevel() {
        List<RtfCommand> rtfCommandList = getCurrentLevel();
        String commands = "";
        for (RtfCommand c : rtfCommandList) {
            commands += c.getCommand() + "/";
        }
        return commands;
    }

    public void removeFromCurrentLevel(String commandName) {
        stack.peek().removeIf(rtfCommand -> rtfCommand.getCommand().getCommandName().equals(commandName));
    }
}
