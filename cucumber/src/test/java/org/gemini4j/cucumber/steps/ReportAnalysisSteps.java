package org.gemini4j.cucumber.steps;

import cucumber.api.java.en.Then;
import org.gemini4j.cucumber.DontSnap;
import org.gemini4j.testapp.reporter.*;

import static org.gemini4j.cucumber.CucumberTests.REPORTER;
import static org.junit.Assert.assertEquals;

@DontSnap
public class ReportAnalysisSteps {
    @Then("^the test '(.*)' should have started$")
    public void testStarted(final String testName) {
        REPORTER.assertVisitor(new EventVisitor() {
            @Override
            public void visitNextTestEvent(final NextTestEvent e) {
                assertEquals(testName, e.getTestName());
            }
        });
    }

    @Then("^the shot '(.*)' should have differed$")
    public void lookedDifferent(final String shotName) {
        REPORTER.assertVisitor(new EventVisitor() {
            @Override
            public void visitLooksDifferentEvent(final LooksDifferentEvent e) {
                assertEquals(shotName, e.getScreenshotName());
            }
        });
    }

    @Then("^the shot '(.*)' should have looked the same$")
    public void lookedSame(String shotName) {
        REPORTER.assertVisitor(new EventVisitor() {
            @Override
            public void visitLooksSameEvent(final LooksSameEvent e) {
                assertEquals(shotName, e.getScreenshotName());
            }
        });
    }

    @Then("^the shot '(.*)' should have been missing$")
    public void wasMissing(String shotName) {
        REPORTER.assertVisitor(new EventVisitor() {
            @Override
            public void visitScreenshotNotKnownEvent(final ScreenshotNotKnownEvent e) {
                assertEquals(shotName, e.getScreenshotName());
            }
        });
    }
}
