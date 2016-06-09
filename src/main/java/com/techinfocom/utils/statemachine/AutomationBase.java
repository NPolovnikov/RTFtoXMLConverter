package com.techinfocom.utils.statemachine;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * Created by volkov_kv on 24.05.2016.
 * базовый класс для всех автоматов. Он предоставляет возможность
 * наследнику регистрировать переходы, используя метод addEdge. Дополнительно класс
 * AutomatonBase реализует интерфейс уведомления о событии
 */
public abstract class AutomationBase<AI> implements EventSink {
    protected AI state;
    private final Map<AI, Map<Event, AI>> edges =
            new HashMap<AI, Map<Event, AI>>();

    protected void addEdge(AI source, Event event, AI target) {
        Map<Event, AI> row = edges.get(source);
        if (row == null) {
            row = new IdentityHashMap<Event, AI>();
            edges.put(source, row);
        }
        row.put(event, target);
    }

    public void castEvent(Event event) {
        String report = state.getClass().getCanonicalName() + "-->";
        try {
            state = edges.get(state).get(event);
            report += state.getClass().getCanonicalName();
        } catch (NullPointerException e) {
            throw new IllegalStateException("Edge is not defined");
        }
        System.err.println(report);
    }

}
