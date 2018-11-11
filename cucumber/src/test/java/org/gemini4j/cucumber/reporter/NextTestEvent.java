package org.gemini4j.cucumber.reporter;

public class NextTestEvent implements RecordedEvent {
    private final String testName;

    public NextTestEvent(final String testName) {
        this.testName = testName;
    }

    @Override
    public boolean accept(final EventVisitor visitor) {
        return visitor.visitNextTestEvent(this);
    }

    public String getTestName() {
        return testName;
    }
}
