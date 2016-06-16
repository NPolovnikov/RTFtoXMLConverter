package com.techinfocom.nvis.agendartftoxml;

import com.techinfocom.nvis.agendartftoxml.model.RtfCommand;
import com.techinfocom.nvis.agendartftoxml.model.TextFormat;

import java.util.Stack;

/**
 * Отслеживает текущее форматирование каждого символа, обрабатывает вложенности заданные в RTF
 * фигурными скобками. Не знает ничего о destination
 */
public class GroupState {
    private Stack<TextFormat> stack;

    public GroupState() {
        this.stack = new Stack<>();
    }

    public void addLevel() {
        if (!stack.empty()) {
            stack.push(new TextFormat(stack.peek())); //текущий формат войдет в формат верхнего уровня
        } else {
            stack.push(new TextFormat());
        }
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
        TextFormat textFormat = new TextFormat();
        TextFormat current = stack.peek();
        textFormat.getFontFormat().addAll(current.getFontFormat());
        textFormat.getParagraphFormat().addAll(current.getParagraphFormat());
        return textFormat;
    }

    public String printCurrentLevel() {
        if (stack.isEmpty()){
            return new TextFormat().toString();
        } else {
            return getCurrent().toString();
        }
    }
}
