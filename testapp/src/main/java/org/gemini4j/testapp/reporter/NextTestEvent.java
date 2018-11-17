package org.gemini4j.testapp.reporter;

public class NextTestEvent implements RecordedEvent {
    private final String testName;

    public NextTestEvent(final String testName) {
        this.testName = testName;
    }

    @Override
    public void accept(final EventVisitor visitor) {
        visitor.visitNextTestEvent(this);
    }

    public String getTestName() {
        return testName;
    }

    @Override
    public String toString() {
        return "NextTestEvent{" +
                "testName='" + testName + '\'' +
                '}';
    }
}
