package com.techinfocom.nvis.agendartftoxml.report;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by volkov_kv on 16.06.2016.
 */
public class ConversionReport {
    private List<AbstractReportMessage> messages;

    public ConversionReport() {
        this.messages = new ArrayList<>();
    }

    public void collectMessage(AbstractReportMessage message) {
        messages.add(message);
    }

    public List<AbstractReportMessage> getMessages() {
        return messages;
    }

    public String printReport(String... types) {
        List<AbstractReportMessage.ReportMessageType> typeList = Stream.of(types).
                map(AbstractReportMessage.ReportMessageType::valueOf)
                .collect(Collectors.toList());
        return messages.stream().filter(m -> typeList.contains(m.getReportMessageType())).map(m -> m.toString())
                .collect(Collectors.joining("\r\n"));
    }

    public boolean hasMessage(String type) {
        AbstractReportMessage.ReportMessageType type1 = AbstractReportMessage.ReportMessageType.valueOf(type);
        return messages.stream().anyMatch(m -> m.getReportMessageType() == type1);
    }
}
