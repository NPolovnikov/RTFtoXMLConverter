package com.techinfocom.nvis.agendartftoxml.model.validation;

import com.techinfocom.nvis.agendartftoxml.ConfigHandler;
import com.techinfocom.nvis.agendartftoxml.model.agenda.AgendaItem;
import com.techinfocom.nvis.agendartftoxml.model.agenda.Group;
import com.techinfocom.nvis.agendartftoxml.report.ConversionReport;
import com.techinfocom.nvis.agendartftoxml.report.WarningMessage;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import org.slf4j.Logger;

import java.time.Instant;
import java.util.function.Consumer;

/**
 * Created by volkov_kv on 16.06.2016.
 */
public class AgendaValidator {
    private static final Logger LOGGER = com.techinfocom.nvis.agendartftoxml.Logger.LOGGER;
    private final Config config;

    public AgendaValidator() {
        config = ConfigHandler.getInstance().getValidationRules();
    }

    public void validate(AgendaItem agendaItem, ConversionReport conversionReport) {
        if (agendaItem == null){
            return;
        }
        Config itemValidationRules = this.config.getConfig("agendaItem");

        if (agendaItem.getNumber() != null) {
            ValidateResponse<String> nvr = validate(agendaItem.getNumber(), itemValidationRules.getConfig("number"));
            agendaItem.setNumber(nvr.getValue());
            if (nvr.getMessage() != null) {
                conversionReport.collectMessage(nvr.getMessage());
            }
        }
        if (agendaItem.getInfo() != null) {
            ValidateResponse<String> ivr = validate(agendaItem.getInfo(), itemValidationRules.getConfig("info"));
            agendaItem.setInfo(ivr.getValue());
            if (ivr.getMessage() != null) {
                conversionReport.collectMessage(ivr.getMessage());
            }
        }
        if (agendaItem.getAddon() != null) {
            ValidateResponse<String> avr = validate(agendaItem.getAddon(), itemValidationRules.getConfig("addon"));
            agendaItem.setAddon(avr.getValue());
            if (avr.getMessage() != null) {
                conversionReport.collectMessage(avr.getMessage());
            }
        }
        if (agendaItem.getRn() != null) {
            ValidateResponse<String> rvr = validate(agendaItem.getRn(), itemValidationRules.getConfig("rn"));
            agendaItem.setRn(rvr.getValue());
            if (rvr.getMessage() != null) {
                conversionReport.collectMessage(rvr.getMessage());
            }
        }
        if (agendaItem.getText() != null) {
            ValidateResponse<String> tvr = validate(agendaItem.getText(), itemValidationRules.getConfig("text"));
            agendaItem.setText(tvr.getValue());
            if (tvr.getMessage() != null) {
                conversionReport.collectMessage(tvr.getMessage());
            }
        }
    }

    public void validate(Group group, ConversionReport conversionReport) {
        if (group == null) {
            return;
        }
        Config groupValidationRules = this.config.getConfig("group");

        if (group.getGroupName() != null) {
            ValidateResponse<String> gvr = validate(group.getGroupName(), groupValidationRules.getConfig("groupName"));
            group.setGroupName(gvr.getValue());
            if (gvr.getMessage() != null) {
                conversionReport.collectMessage(gvr.getMessage());
            }
        }
    }

    public void validate(Group.Speakers.Speaker speaker, ConversionReport conversionReport) {
        if (speaker == null) {
            return;
        }
        Config groupValidationRules = this.config.getConfig("speaker");

        if (speaker.getPost() != null) {
            ValidateResponse<String> pvr = validate(speaker.getPost(), groupValidationRules.getConfig("post"));
            speaker.setPost(pvr.getValue());
            if (pvr.getMessage() != null) {
                conversionReport.collectMessage(pvr.getMessage());
            }
        }
        if (speaker.getName() != null) {
            ValidateResponse<String> nvr = validate(speaker.getName(), groupValidationRules.getConfig("name"));
            speaker.setName(nvr.getValue());
            if (nvr.getMessage() != null) {
                conversionReport.collectMessage(nvr.getMessage());
            }
        }

    }


    public ValidateResponse<String> validate(String s, Config config) {
        ValidateResponse<String> validateResponse = new ValidateResponse<>();
        if (config == null || config.isEmpty() || s == null) {
            validateResponse.setValue(s);
            return validateResponse;
        }

        try {
            int maxLen = config.getInt("maxLength");
            if (s.length() > maxLen) {
                WarningMessage warningMessage = new WarningMessage("Длина строки превышает максимальную, равную " + String.valueOf(maxLen), s);
                validateResponse.setMessage(warningMessage);
                validateResponse.setValue(s.substring(0, maxLen));
            } else {
                validateResponse.setValue(s);
            }
        } catch (ConfigException.Missing e) {
            LOGGER.error("Валидация строки {}. не обнаружена параметр maxLength в конфиге валидатора. {}", s, e);
        }

        return validateResponse;
    }

    public ValidateResponse<Instant> validate(Instant dt, Config config) {
        return null;
    }

}
