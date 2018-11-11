package org.gemini4j.cucumber.steps;

import cucumber.api.java.en.Then;
import org.gemini4j.cucumber.reporter.EventVisitor;
import org.gemini4j.cucumber.reporter.NextTestEvent;

import static org.gemini4j.cucumber.CucumberTests.REPORTER;
import static org.junit.Assert.fail;

public class ReportAnalysisSteps {
    @Then("I should hurz")
    public void hurz() {
        fail();
    }

    @Then("The test '(.*)' should have started")
    public void testStarted(final String testName) {
        REPORTER.assertVisitor(new EventVisitor() {
            @Override
            public boolean visitNextTestEvent(final NextTestEvent e) {
                return testName.equals(e.getTestName());
            }
        });
    }
}
