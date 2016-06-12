package com.techinfocom.utils;

import com.rtfparserkit.rtf.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.rtfparserkit.rtf.Command.*;

/**
 * Created by volkov_kv on 08.06.2016.
 */
public class TextFormat {


    public TextFormat() {
        paragraphFormat = new ArrayList<>();
        fontFormat = new ArrayList<>();
    }

    public TextFormat(TextFormat textFormat) {
        paragraphFormat = new ArrayList<>();
        fontFormat = new ArrayList<>();
        paragraphFormat.addAll(textFormat.getParagraphFormat());
        fontFormat.addAll(textFormat.getFontFormat());
    }

    private final List<RtfCommand> paragraphFormat;
    private final List<RtfCommand> fontFormat;

    // все виды подчеркнутого текста
    private Command[] allUnderlinesA = {ul
            , ulc
            , uld
            , uldash
            , uldashd
            , uldashdd
            , uldb
            , ulhwave
            , ulldash
            , ulth
            , ulthd
            , ulthdash
            , ulthdashd
            , ulthdashdd
            , ulthldash
            , ululdbwave
            , ulw
            , ulwave};

    public void processCommand(RtfCommand rtfCommand) {
        switch (rtfCommand.getCommand()) {
            //Paragraph Formatting Properties
            /**
             * Paragraph is part of a table.
             */
            case intbl:
                paragraphFormat.add(rtfCommand);
                break;
            /**
             * Resets to default paragraph properties.
             * If the \pard control word is not present, the current paragraph inherits all
             * paragraph properties from the previous paragraph.
             */
            case pard:
                paragraphFormat.clear();// TODO: 08.06.2016 reset to default. Непонятно, что является Default
                break;

            case sectd:
                // TODO: 08.06.2016 сбросить какое-то форматирование
                break;
            /**
             * Reset font (character) formatting properties to a default value defined by the
             * application (for example, bold, underline and italic are disabled; font size is
             * reset to 12 point). The associated font (character) formatting properties
             * (described in the section Associated Character Properties of this Specification)
             * are also reset.
             */
            case plain:
                fontFormat.clear();
                break;
            //Font (Character) Formatting Properties
            case b: //toggle
            case i: //toggle
                if (rtfCommand.isHasParameter()) {
                    fontFormat.removeIf(c -> c.getCommand() == (rtfCommand.getCommand()));
                } else {
                    fontFormat.add(rtfCommand);
                }
                break;
            /**
             * Continuous underline. ul0 turns off all underlining.
             */
            case ul: //toggle
                if (rtfCommand.isHasParameter()) {
                    fontFormat.removeIf(c -> Arrays.asList(allUnderlinesA).contains(c.getCommand()));
                } else {
                    fontFormat.add(rtfCommand);
                }
                break;
            /**
             * Stops all underlining.
             */
            case ulnone:
                fontFormat.removeIf(c -> Arrays.asList(allUnderlinesA).contains(c.getCommand()));
                break;
            /**
             * Foreground color (default is 0). N specifies the color as an index of the color table.
             */
            case cf:
                //System.err.println("Цвет текста нашли");
                break;
            case cs:
                //System.err.println("Стили текста нашли");
                break;
        }

    }

    public boolean paragraphContain(Command command){
        return paragraphFormat.stream().anyMatch(c -> c.getCommand() == command);
    }

    public boolean fontContain(Command command){
        return fontFormat.stream().anyMatch(c -> c.getCommand() == command);
    }

    public List<RtfCommand> getParagraphFormat() {
        return paragraphFormat;
    }

    public List<RtfCommand> getFontFormat() {
        return fontFormat;
    }

    @Override
    public String toString() {
        String commands = "";
        commands += "FontFormat = ";
        for (RtfCommand c : fontFormat) {
            commands += c.getCommand().getCommandName() + "-" + c.getParameter() + ";";
        }
        commands += "ParFormat = ";
        for (RtfCommand c : paragraphFormat) {
            commands += c.getCommand().getCommandName() + "-" + c.getParameter() + ";";
        }
        return commands;
    }
}
