package com.techinfocom.nvis.agendartftoxml.model.validation;

import com.techinfocom.nvis.agendartftoxml.ConfigHandler;
import com.techinfocom.nvis.agendartftoxml.model.agenda.AgendaItem;
import com.techinfocom.nvis.agendartftoxml.model.agenda.Group;
import com.techinfocom.nvis.agendartftoxml.report.ConversionReport;
import com.techinfocom.nvis.agendartftoxml.report.WarningMessage;
import com.typesafe.config.Config;

import java.time.Instant;
import java.util.Iterator;
import java.util.List;

/**
 * Created by volkov_kv on 16.06.2016.
 */
public class AgendaValidator {
    private static final String IN_PUNKT = "В пункте ";
    private final Config config;

    public AgendaValidator() {
        config = ConfigHandler.getInstance().getValidationRules();
    }

    /**
     * Валидирует все простые свойства AgendaItem. Не валидирует иерархию speakerGroup, которая должна быть
     * провалидирована ранее при слиянии с AgendaItem
     *
     * @param agendaItem
     * @param conversionReport
     */
    public void validate(final AgendaItem agendaItem, final ConversionReport conversionReport) {
        if (agendaItem == null) {
            return;
        }
        Config itemValidationRules = config.getConfig("agendaItem");

        if (agendaItem.getNumber() != null) {
            final ValidateResponse<String> nvr = validate(agendaItem.getNumber(), itemValidationRules.getConfig("number"));
            agendaItem.setNumber(nvr.getValue());
            if (nvr.getMessage() != null) {
                final String strMessage = nvr.getMessage().getMessage();
                nvr.getMessage().setMessage(IN_PUNKT + agendaItem.getNumber() + ", в номере пункта порядка работы " + strMessage);
                conversionReport.collectMessage(nvr.getMessage());
            }
        }
        if (agendaItem.getInfo() != null) {
            final ValidateResponse<String> ivr = validate(agendaItem.getInfo(), itemValidationRules.getConfig("info"));
            agendaItem.setInfo(ivr.getValue());
            if (ivr.getMessage() != null) {
                final String strMessage = ivr.getMessage().getMessage();
                ivr.getMessage().setMessage(IN_PUNKT + agendaItem.getNumber() + ", в доп информации " + strMessage);
                conversionReport.collectMessage(ivr.getMessage());
            }
        }
        if (agendaItem.getAddon() != null) {
            final ValidateResponse<String> avr = validate(agendaItem.getAddon(), itemValidationRules.getConfig("addon"));
            agendaItem.setAddon(avr.getValue());
            if (avr.getMessage() != null) {
                final String strMessage = avr.getMessage().getMessage();
                avr.getMessage().setMessage(IN_PUNKT + agendaItem.getNumber() + ", в дополнении " + strMessage);
                conversionReport.collectMessage(avr.getMessage());
            }
        }
        if (agendaItem.getRn() != null) {
            final ValidateResponse<String> rvr = validate(agendaItem.getRn(), itemValidationRules.getConfig("rn"));
            agendaItem.setRn(rvr.getValue());
            if (rvr.getMessage() != null) {
                final String strMessage = rvr.getMessage().getMessage();
                rvr.getMessage().setMessage(IN_PUNKT + agendaItem.getNumber() + ", в номере документа " + strMessage);
                conversionReport.collectMessage(rvr.getMessage());
            }
        }
        if (agendaItem.getText() != null) {
            final ValidateResponse<String> tvr = validate(agendaItem.getText(), itemValidationRules.getConfig("text"));
            agendaItem.setText(tvr.getValue());
            if (tvr.getMessage() != null) {
                final String strMessage = tvr.getMessage().getMessage();
                tvr.getMessage().setMessage(IN_PUNKT + agendaItem.getNumber() + ", в тексте пункта порядка работы " + strMessage);
                conversionReport.collectMessage(tvr.getMessage());
            }
        }
        if (agendaItem.getNotes() != null && !agendaItem.getNotes().getNote().isEmpty()) {
            final List<String> noteList = agendaItem.getNotes().getNote();
            final Config validationConfig = itemValidationRules.getConfig("notes.note");
            for (int i = 0; i < noteList.size(); i++) {
                final String s = noteList.get(i);
                final ValidateResponse<String> nvr = validate(s, validationConfig);
                noteList.set(i, nvr.getValue());
                if (nvr.getMessage() != null) {
                    final String strMessage = nvr.getMessage().getMessage();
                    nvr.getMessage().setMessage(IN_PUNKT + agendaItem.getNumber() + ", в примечании " + strMessage);
                    conversionReport.collectMessage(nvr.getMessage());
                }
            }
        }

        //кол-во докладчиков
        if (agendaItem.getSpeakerGroups() != null && !agendaItem.getSpeakerGroups().getGroup().isEmpty()) {
            final int maxSpeakerCount = itemValidationRules.getInt("speakerGroups.maxTotalSpeakerCount");
            int speakerCount = 0; //считаем кол-во докладчиков во всех группах, поэтому инициализация вне первого цикла.

            for (Iterator<Group> i = agendaItem.getSpeakerGroups().getGroup().iterator(); i.hasNext(); ) {
                final Group group = i.next();
                if (group.getSpeakers() != null && !group.getSpeakers().getSpeaker().isEmpty()) {

                    for (Iterator<Group.Speakers.Speaker> ii = group.getSpeakers().getSpeaker().iterator(); ii.hasNext(); ) {
                        final Group.Speakers.Speaker speaker = ii.next();
                        speakerCount++;
                        if (speakerCount > maxSpeakerCount) {
                            String number;
                            if (agendaItem.getNumber() == null || agendaItem.getNumber().isEmpty()) {
                                number = "(не указан)";
                            } else {
                                number = agendaItem.getNumber();
                            }
                            final WarningMessage warningMessage = new WarningMessage(IN_PUNKT +
                                    number + "кол-во докладчиков превосходит максимально разрешенное - " +
                                    maxSpeakerCount + ". Докладчики проигнорированы.", speaker.getPost());
                            conversionReport.collectMessage(warningMessage);
                            ii.remove();
                        }
                    }
                }
                //если группа осталась пустой, удалим и ее
                if (group.getSpeakers().getSpeaker().isEmpty()) {
                    i.remove();
                }
            }
        }

        //кол-во note
        if (agendaItem.getNotes() != null && !agendaItem.getNotes().getNote().isEmpty()) {
            final int maxNoteCount = itemValidationRules.getInt("notes.maxNoteCount");
            int noteCount = 0;
            for (Iterator<String> i = agendaItem.getNotes().getNote().iterator(); i.hasNext(); ) {
                String note = i.next();
                noteCount++;
                if (noteCount > maxNoteCount) {
                    String number;
                    if (agendaItem.getNumber() == null || agendaItem.getNumber().isEmpty()) {
                        number = "(не указан)";
                    } else {
                        number = agendaItem.getNumber();
                    }
                    final WarningMessage warningMessage = new WarningMessage(IN_PUNKT +
                            number + "кол-во примечаний превосходит максимально разрешенное - " +
                            maxNoteCount + ". примечание проигнорировано.", note);
                    conversionReport.collectMessage(warningMessage);
                    i.remove();
                }
            }
        }

    }

    public void validate(final Group group, final ConversionReport conversionReport) {
        if (group == null) {
            return;
        }
        Config groupValidationRules = config.getConfig("agendaItem.speakerGroups.group");

        if (group.getGroupName() != null) {
            final ValidateResponse<String> gvr = validate(group.getGroupName(), groupValidationRules.getConfig("groupName"));
            group.setGroupName(gvr.getValue());
            if (gvr.getMessage() != null) {
                conversionReport.collectMessage(gvr.getMessage());
            }
        }

    }

    public void validate(final Group.Speakers.Speaker speaker, final ConversionReport conversionReport) {
        if (speaker == null) {
            return;
        }
        final Config speakerValidationRule = this.config.getConfig("agendaItem.speakerGroups.group.speakers.speaker");

        if (speaker.getPost() != null) {
            final ValidateResponse<String> pvr = validate(speaker.getPost(), speakerValidationRule.getConfig("post"));
            speaker.setPost(pvr.getValue());
            if (pvr.getMessage() != null) {
                conversionReport.collectMessage(pvr.getMessage());
            }
        }
        if (speaker.getName() != null) {
            final ValidateResponse<String> nvr = validate(speaker.getName(), speakerValidationRule.getConfig("name"));
            speaker.setName(nvr.getValue());
            if (nvr.getMessage() != null) {
                conversionReport.collectMessage(nvr.getMessage());
            }
        }
    }

    /**
     * Валидирует строки по параметру maxLength
     *
     * @param s
     * @param config
     * @return
     */
    public ValidateResponse<String> validate(final String s, final Config config) {
        final ValidateResponse<String> validateResponse = new ValidateResponse<>();
        if (config == null || config.isEmpty() || !config.hasPath("maxLength") || s == null) {
            //нечего валидировать, отдадим обратно, мало ли там что-то есть.
            validateResponse.setValue(s);
            return validateResponse;
        }
        try {
            final int maxLen = config.getInt("maxLength");
            if (s.length() > maxLen) {
                final WarningMessage warningMessage = new WarningMessage("длина строки превышает максимальную - " + maxLen, s);
                validateResponse.setMessage(warningMessage);
                validateResponse.setValue(s.substring(0, maxLen));
            } else {
                validateResponse.setValue(s);
            }
        } catch (Exception e) {
            validateResponse.setValue(s);
        }
        return validateResponse;
    }

    public ValidateResponse<Instant> validate(final Instant dt, final Config config) {
        return null;
    }

}
