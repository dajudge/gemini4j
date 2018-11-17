package org.gemini4j.testapp.reporter;

public class ScreenshotNotKnownEvent implements RecordedEvent {
    private final String screenshotName;

    public ScreenshotNotKnownEvent(final String screenshotName) {
        this.screenshotName = screenshotName;
    }

    @Override
    public void accept(final EventVisitor visitor) {
        visitor.visitScreenshotNotKnownEvent(this);
    }

    public String getScreenshotName() {
        return screenshotName;
    }

    @Override
    public String toString() {
        return "ScreenshotNotKnownEvent{" +
                "screenshotName='" + screenshotName + '\'' +
                '}';
    }
}
