package org.gemini4j.cucumber.reporter;

public class LooksSameEvent implements RecordedEvent {
    private final String screenshotName;

    public LooksSameEvent(final String screenshotName) {
        this.screenshotName = screenshotName;
    }

    @Override
    public boolean accept(final EventVisitor visitor) {
        return visitor.visitLooksSameEvent(this);
    }

    public String getScreenshotName() {
        return screenshotName;
    }
}
