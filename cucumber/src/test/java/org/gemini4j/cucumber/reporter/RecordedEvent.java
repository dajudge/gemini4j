package org.gemini4j.cucumber.reporter;

public interface RecordedEvent {
    boolean accept(EventVisitor visitor);
}
