package org.gemini4j.cucumber.reporter;

public class ScreenshotNotKnownEvent implements RecordedEvent {
    private final String screenshotName;

    public ScreenshotNotKnownEvent(final String screenshotName) {
        this.screenshotName = screenshotName;
    }

    @Override
    public boolean accept(final EventVisitor visitor) {
        return visitor.visitScreenshotNotKnownEvent(this);
    }

    public String getScreenshotName() {
        return screenshotName;
    }
}
