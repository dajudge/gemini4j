package org.gemini4j.testapp.reporter;

public class LooksSameEvent implements RecordedEvent {
    private final String screenshotName;

    public LooksSameEvent(final String screenshotName) {
        this.screenshotName = screenshotName;
    }

    @Override
    public void accept(final EventVisitor visitor) {
        visitor.visitLooksSameEvent(this);
    }

    public String getScreenshotName() {
        return screenshotName;
    }

    @Override
    public String toString() {
        return "LooksSameEvent{" +
                "screenshotName='" + screenshotName + '\'' +
                '}';
    }
}
