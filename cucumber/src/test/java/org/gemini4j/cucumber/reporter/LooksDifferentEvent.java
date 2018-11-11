package org.gemini4j.cucumber.reporter;

public class LooksDifferentEvent implements RecordedEvent {
    private final String screenshotName;

    public LooksDifferentEvent(final String screenshotName) {
        this.screenshotName = screenshotName;
    }

    @Override
    public boolean accept(final EventVisitor visitor) {
        return visitor.visitLooksDifferentEvent(this);
    }

    public String getScreenshotName() {
        return screenshotName;
    }
}
