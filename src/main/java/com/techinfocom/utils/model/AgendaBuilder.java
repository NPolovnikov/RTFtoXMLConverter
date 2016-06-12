package com.techinfocom.utils.model;

import com.techinfocom.utils.GroupState;
import com.techinfocom.utils.RtfCommand;
import com.techinfocom.utils.model.agenda.Agenda;
import com.techinfocom.utils.model.agenda.AgendaItem;
import com.techinfocom.utils.model.agenda.Group;
import com.techinfocom.utils.model.agenda.ObjectFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class AgendaBuilder {

    private AgendaItem currentItem;
    private Group currentGroup;
    private Group.Speakers.Speaker currentSpeaker;

    public AgendaBuilder() {
        objectFactory = new ObjectFactory();
    }

    private ObjectFactory objectFactory;
    private Agenda agenda;


    public void processString(String string) {
        System.err.println("Билдеру дали строку=" + string);
    }


    public void createAgenda() {
        if (agenda == null) {
            agenda = objectFactory.createAgenda();
        } else {
            throw new RuntimeException("agenda already exists"); // TODO: 08.06.2016 заменить исключение
        }
    }

    public AgendaItem newAgendaItem() {
        if (currentItem == null) {
            currentItem = objectFactory.createAgendaItem();
        } else {
            throw new RuntimeException("currentItem already exists"); // TODO: 08.06.2016 заменить исключение
        }
        return currentItem;
    }

    public AgendaItem getCurrentItem() {
        return currentItem;
    }

    public void mergeItem() {
        agenda.getItemOrBlock().add(currentItem);
        currentItem = null;
    }


    /**
     * преобразует двойные пробелы, абзацы
     *
     * @param srcStr
     * @return
     */
    public String conformString(String srcStr) {
        String conformed = srcStr.replaceAll("  ", " ");
        List<String> pars = Arrays.asList(conformed.split("\\r\\n"));
        //раскладывает на абзацы, удаляет начальные конечные пробелы, удаляет пустые абзацы, складывает обратно через перевод каретки.
        conformed = pars.stream()
                .map(String::trim)
                .filter(p -> !p.isEmpty())
                .collect(Collectors.joining("\r\n"));
        return conformed;
    }

    public Group newCurrentGroup() {
        if (currentGroup == null) {
            currentGroup = objectFactory.createGroup();
            currentGroup.setGroupName("");
        } else {
            throw new RuntimeException("CurrentGroup already exists");
        }
        return currentGroup;
    }

    public Group getCurrentGroup() {
        return currentGroup;
    }

    public void mergeCurrentGroup() {
        if (currentItem.getSpeakerGroups() == null) {
            currentItem.setSpeakerGroups(objectFactory.createSpeakers());
        }
        currentItem.getSpeakerGroups().getGroup().add(currentGroup);
        currentGroup = null;//чтобы null poiter сгенерировался, если криво начнем контекст отслеживать.
    }

    public Group.Speakers.Speaker newCurrentSpeaker() {
        if (currentSpeaker == null) {
            currentSpeaker = objectFactory.createGroupSpeakersSpeaker();
            currentSpeaker.setName("");
            currentSpeaker.setPost("");
        } else {
            throw new RuntimeException("CurrentSpeaker already exists");
        }
        return currentSpeaker;
    }

    public Group.Speakers.Speaker getCurrentSpeaker() {
        return currentSpeaker;
    }

    public void mergeCurrentSpeaker() {
        if (currentGroup.getSpeakers() == null) {
            currentGroup.setSpeakers(objectFactory.createGroupSpeakers());
        }
        currentGroup.getSpeakers().getSpeaker().add(currentSpeaker);
        currentSpeaker = null;//чтобы null poiter сгенерировался, если криво начнем контекст отслеживать.
    }


    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    // TODO: 07.06.2016 удалить currentItem, если строка таблицы оказалась битой?

}

