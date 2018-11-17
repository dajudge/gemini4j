package org.gemini4j.testapp.reporter;

public interface RecordedEvent {
    void accept(EventVisitor visitor);
}
