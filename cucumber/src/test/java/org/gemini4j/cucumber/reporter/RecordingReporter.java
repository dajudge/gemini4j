package org.gemini4j.cucumber.reporter;

import org.gemini4j.reporter.Reporter;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.fail;

public class RecordingReporter implements Reporter {
    private final List<RecordedEvent> recordedEvents = new ArrayList<>();

    @Override
    public void nextTest(final String testName) {
        recordedEvents.add(new NextTestEvent(testName));
    }

    @Override
    public void screenshotNotKnown(final String screenshotName, final BufferedImage takenImage) {
        recordedEvents.add(new ScreenshotNotKnownEvent(screenshotName));
    }

    @Override
    public void looksSame(final String screenshotName, final BufferedImage takenImage) {
        recordedEvents.add(new LooksSameEvent(screenshotName));
    }

    @Override
    public void looksDifferent(
            final String screenshotName,
            final BufferedImage takenImage,
            final BufferedImage referenceImage,
            final BufferedImage diff
    ) {
        recordedEvents.add(new LooksDifferentEvent(screenshotName));
    }

    @Override
    public void shutdown() {
    }

    public void assertVisitor(final EventVisitor visitor) {
        final RecordedEvent event = recordedEvents.remove(0);
        if (!event.accept(visitor)) {
            fail("Unexpected event recorded: " + event);
        }
    }
}
