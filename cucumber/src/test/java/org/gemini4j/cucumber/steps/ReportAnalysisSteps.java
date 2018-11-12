package org.gemini4j.cucumber.steps;

import cucumber.api.java.en.Then;
import org.gemini4j.cucumber.DontSnap;
import org.gemini4j.cucumber.reporter.*;

import static org.gemini4j.cucumber.CucumberTests.REPORTER;
import static org.junit.Assert.fail;

@DontSnap
public class ReportAnalysisSteps {
    @Then("^the test '(.*)' should have started$")
    public void testStarted(final String testName) {
        REPORTER.assertVisitor(new EventVisitor() {
            @Override
            public boolean visitNextTestEvent(final NextTestEvent e) {
                return testName.equals(e.getTestName());
            }
        });
    }

    @Then("^the shot '(.*)' should have differed$")
    public void lookedDifferent(final String shotName) {
        REPORTER.assertVisitor(new EventVisitor() {
            @Override
            public boolean visitLooksDifferentEvent(final LooksDifferentEvent e) {
                return shotName.equals(e.getScreenshotName());
            }
        });
    }

    @Then("^the shot '(.*)' should have looked the same$")
    public void lookedSame(String shotName) {
        REPORTER.assertVisitor(new EventVisitor() {
            @Override
            public boolean visitLooksSameEvent(final LooksSameEvent e) {
                return shotName.equals(e.getScreenshotName());
            }
        });
    }

    @Then("^the shot '(.*)' should have been missing$")
    public void wasMissing(String shotName) {
        REPORTER.assertVisitor(new EventVisitor() {
            @Override
            public boolean visitScreenshotNotKnownEvent(final ScreenshotNotKnownEvent e) {
                return shotName.equals(e.getScreenshotName());
            }
        });
    }
}
