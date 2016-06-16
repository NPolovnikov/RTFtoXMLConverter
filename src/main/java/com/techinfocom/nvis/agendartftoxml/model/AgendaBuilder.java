package com.techinfocom.nvis.agendartftoxml.model;

import com.techinfocom.nvis.agendartftoxml.model.agenda.Agenda;
import com.techinfocom.nvis.agendartftoxml.model.agenda.Group;
import com.techinfocom.nvis.agendartftoxml.model.agenda.ObjectFactory;
import com.techinfocom.nvis.agendartftoxml.model.agenda.AgendaItem;
import com.techinfocom.nvis.agendartftoxml.report.ConversionReport;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class AgendaBuilder {

    private AgendaItem currentItem;
    private Group currentGroup;
    private Group.Speakers.Speaker currentSpeaker;
    private final ObjectFactory objectFactory;
    private final Agenda agenda;
    private int rowCount = 0;
    private final ConversionReport conversionReport;

    public AgendaBuilder() {
        objectFactory = new ObjectFactory();
        agenda = objectFactory.createAgenda();
        conversionReport = new ConversionReport();
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public AgendaItem newAgendaItem() {
        rowCount++;
        if (currentItem == null) {
            currentItem = objectFactory.createAgendaItem();
            currentItem.setId(UUID.randomUUID().toString());
        } else {
            throw new RuntimeException("currentItem already exists"); // TODO: 08.06.2016 заменить исключение
        }
        return currentItem;
    }

    public Group newGroup() {
        if (currentGroup == null) {
            currentGroup = objectFactory.createGroup();
        } else {
            throw new RuntimeException("CurrentGroup already exists");
        }
        return currentGroup;
    }

    public Group.Speakers.Speaker newSpeaker() {
        if (currentSpeaker == null) {
            currentSpeaker = objectFactory.createGroupSpeakersSpeaker();
        } else {
            throw new RuntimeException("CurrentSpeaker already exists");
        }
        return currentSpeaker;
    }


    public void mergeAgendaItem() {
        if (currentItem != null) {
            agenda.getItemOrBlock().add(currentItem);
            currentItem = null;
        } else throw new RuntimeException("can't merge null currentItem");
    }

    public void mergeGroup() {
        if (currentGroup != null) {
            if (currentItem.getSpeakerGroups() == null) {
                currentItem.setSpeakerGroups(objectFactory.createSpeakers());
            }
            currentItem.getSpeakerGroups().getGroup().add(currentGroup);
            trim(currentGroup);
            currentGroup = null;//чтобы null poiter сгенерировался, если криво начнем контекст отслеживать.
        } else {
            throw new RuntimeException("can't merge null currentGroup");
        }
    }

    public void mergeSpeaker() {
        if (currentSpeaker != null) {
            if (currentGroup.getSpeakers() == null) {
                currentGroup.setSpeakers(objectFactory.createGroupSpeakers());
            }
            trim(currentSpeaker);
            currentGroup.getSpeakers().getSpeaker().add(currentSpeaker);
            currentSpeaker = null;//чтобы null poiter сгенерировался, если криво начнем контекст отслеживать.
        } else throw new RuntimeException("can't merge null currentSpeaker");
    }

    public void dropAgendaItem() {
        currentItem = null;
    }

    public AgendaItem getAgendaItem() {
        return currentItem;
    }

    public Group getGroup() {
        return currentGroup;
    }

    public Group.Speakers.Speaker getSpeaker() {
        return currentSpeaker;
    }

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public int getRowCount() {
        return rowCount;
    }

    public ConversionReport getConversionReport() {
        return conversionReport;
    }

    /**
     * преобразует двойные пробелы, абзацы
     *
     * @param srcStr
     * @return
     */
    public String conformString(String srcStr) {
        String conformed = null;
        if (srcStr != null) {
            conformed = srcStr.replaceAll("  ", " ");
            List<String> pars = Arrays.asList(conformed.split("(\\r\\n)|(\\n)"));
            //раскладывает на абзацы, удаляет начальные конечные пробелы, удаляет пустые абзацы, складывает обратно через перевод каретки.
            conformed = pars.stream()
                    .map(String::trim)
                    .filter(p -> !p.isEmpty())
                    .collect(Collectors.joining("\r\n"));
        }
        return conformed;
    }

    private void trim(Group.Speakers.Speaker speaker) {
        speaker.setName(conformString(speaker.getName()));
        speaker.setPost(conformString(speaker.getPost()));
    }


    private void trim(Group group) {
        group.setGroupName(conformString(group.getGroupName()));
    }


    /**
     * извлекает номер документа, согласно описанию:
     * Для поиска регистрационного номера документа выделяется в тексте первого абзаца ячейки таблицы выделяется
     * строка, начинающаяся символом №, за которым идет один иле несколько пробелов, потом одна или несколько
     * десятичных цифр (не более 10), потом символ «-» (минус), потом одна или две цифры, потом один или несколько
     * пробелов. В случае, если в первом абзаце пункта порядка работы встретилось несколько подстрок, удовлетворяющих
     * описанному шаблону, регистрационный номер выделяется из первой встреченной подстроки. В качестве
     * регистрационного номера выделяется подстрока, начинающаяся с цифры и заканчивающаяся последней
     * цифрой в найденном шаблоне;
     *
     * @return
     */
    public void docNumberExtractAndSave() {
        String text = currentItem.getText(); // TODO: 13.06.2016  до первого переовода строки выделить
        if (text != null) {
            Pattern p = Pattern.compile("^.*№ ?(\\d{1,10}-\\d{1,2}) +.*$", Pattern.MULTILINE);
            Matcher m = p.matcher(text);
            if (m.find()) {
                String docNumber = m.group(1);
                currentItem.setRn(docNumber);
            }
        }
    }

}

