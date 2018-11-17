package org.gemini4j.testapp.reporter;

public class LooksDifferentEvent implements RecordedEvent {
    private final String screenshotName;

    public LooksDifferentEvent(final String screenshotName) {
        this.screenshotName = screenshotName;
    }

    @Override
    public void accept(final EventVisitor visitor) {
        visitor.visitLooksDifferentEvent(this);
    }

    public String getScreenshotName() {
        return screenshotName;
    }

    @Override
    public String toString() {
        return "LooksDifferentEvent{" +
                "screenshotName='" + screenshotName + '\'' +
                '}';
    }
}
