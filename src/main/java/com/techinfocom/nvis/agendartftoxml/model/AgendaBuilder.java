package com.techinfocom.nvis.agendartftoxml.model;

import com.techinfocom.nvis.agendartftoxml.ConfigHandler;
import com.techinfocom.nvis.agendartftoxml.model.agenda.*;
import com.techinfocom.nvis.agendartftoxml.model.validation.AgendaValidator;
import com.techinfocom.nvis.agendartftoxml.report.ConversionReport;
import com.techinfocom.nvis.agendartftoxml.report.WarningMessage;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by volkov_kv on 07.06.2016.
 */
public class AgendaBuilder {

    private final Pattern rnPattern = Pattern.compile("№ ?(\\d{1,10}-\\d{1,2}) +");

    private AgendaItem currentItem;
    private Group currentGroup;
    private Group.Speakers.Speaker currentSpeaker;
    private final ObjectFactory objectFactory;
    private final Agenda agenda;
    private int parsedRowCount = 0;
    private int acceptedRowCount = 0;
    private final ConversionReport conversionReport;
    private final AgendaValidator agendaValidator;

    public AgendaBuilder() {
        objectFactory = new ObjectFactory();
        agenda = objectFactory.createAgenda();
        conversionReport = new ConversionReport();
        agendaValidator = new AgendaValidator();
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public AgendaItem newAgendaItem() {
        parsedRowCount++;
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
            if (ConfigHandler.getInstance().getValidationRules().hasPath("maxItemCount")) {
                int count = agenda.getItemOrBlock().size();
                int maxItemCount = ConfigHandler.getInstance().getValidationRules().getInt("maxItemCount");

                if (count > maxItemCount - 1 ) {//потому, что слияние еще впереди, надо на 1 уменьшить
                    String number;
                    if (currentItem.getNumber() == null && currentItem.getNumber().isEmpty()){
                        number = "(не указан)";
                    } else {
                        number = currentItem.getNumber();
                    }
                    WarningMessage warningMessage = new WarningMessage("Кол-во пунктов превышает максимально " +
                            "допустимое - " + maxItemCount + ". Пункт " + number + " проигнорирован");
                    conversionReport.collectMessage(warningMessage);
                    currentItem = null;
                    return;
                }
            }

            agendaValidator.validate(currentItem, conversionReport);
            agenda.getItemOrBlock().add(currentItem);
            currentItem = null;
        } else throw new RuntimeException("can't merge null currentItem");
    }

    public void mergeGroup() {
        if (currentGroup != null) {
            if (currentItem.getSpeakerGroups() == null) {
                currentItem.setSpeakerGroups(objectFactory.createSpeakers());
            }
            trim(currentGroup);
            agendaValidator.validate(currentGroup, conversionReport);
            currentItem.getSpeakerGroups().getGroup().add(currentGroup);
            currentGroup = null;//чтобы null pointer сгенерировался, если криво начнем контекст отслеживать.
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
            agendaValidator.validate(currentSpeaker, conversionReport);
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

    public int getParsedRowCount() {
        return parsedRowCount;
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


    public boolean meetingDateExtractAndSave(String string) {
        List<Pattern> patterns = new ArrayList<>();
        patterns.add(Pattern.compile("(\\d{1,2}) {1,4}([^ \\d]{3,10}) {1,4}(20\\d{2})", Pattern.MULTILINE)); //формат 10 мая 2016 года
        patterns.add(Pattern.compile("(\\d{1,2})\\.(\\d{2})\\.(20\\d{2})", Pattern.MULTILINE)); //формат 10.05.2016
        LocalDate date = null;
        Matcher m;
        try {
            m = patterns.get(0).matcher(string);
            if (date == null && m.find()) {
                int day = Integer.valueOf(m.group(1));
                String month = m.group(2);
                int year = Integer.valueOf(m.group(3));
                Month mo = getMonth(month);
                if (mo != null) {
                    date = LocalDate.of(year, mo, day);
                }
            }

            m = patterns.get(1).matcher(string);
            if (date == null && m.find()) {
                int day = Integer.valueOf(m.group(1));
                int mo = Integer.valueOf(m.group(2));
                int year = Integer.valueOf(m.group(3));
                date = LocalDate.of(year, mo, day);
            }

            if (date != null) {
                GregorianCalendar gcal = GregorianCalendar.from(date.atStartOfDay(ZoneId.systemDefault()));
                XMLGregorianCalendar xcal = null;
                xcal = DatatypeFactory.newInstance().newXMLGregorianCalendar(gcal);
                agenda.setMeetingDate(xcal);
                return true;
            } else return false;
        } catch (Exception e) {
            return false;
        }

    }

    private Month getMonth(String month) {
        switch (month.toLowerCase()) {
            case "января":
                return Month.JANUARY;
            case "февраля":
                return Month.FEBRUARY;
            case "марта":
                return Month.MARCH;
            case "апреля":
                return Month.APRIL;
            case "мая":
                return Month.MAY;
            case "июня":
                return Month.JUNE;
            case "июля":
                return Month.JULY;
            case "августа":
                return Month.AUGUST;
            case "сентября":
                return Month.SEPTEMBER;
            case "октября":
                return Month.OCTOBER;
            case "ноября":
                return Month.NOVEMBER;
            case "декабря":
                return Month.DECEMBER;
            default:
                return null;
        }
    }

    public void splitTextToItem() {
        String text = currentItem.getText();
        if (text == null) {
            return;
        }
        List<String> pars = Arrays.asList(text.split("(\\n)|(\\r\\n)"));
        String first = pars.get(0);

        Matcher m = rnPattern.matcher(first);
        //Если в первом абзаце найден номер
        if (m.find()) {
            String rn = m.group(1);
            String addon = first.substring(0, m.start()).trim(); //до номера
            String textNew = first.substring(m.end(), first.length()); //после номера

            currentItem.setAddon(addon);
            currentItem.setRn(rn);
            currentItem.setText(textNew);
        } else {
            currentItem.setText(first);
        }
        NotesList notesList = new NotesList();
        for (int i = 1; i < pars.size(); i++) {
            notesList.getNote().add(pars.get(i));
        }
        if (notesList.getNote().size() > 0) {
            currentItem.setNotes(notesList);
        }
    }

}

