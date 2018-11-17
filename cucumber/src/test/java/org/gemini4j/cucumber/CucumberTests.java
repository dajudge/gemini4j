package org.gemini4j.cucumber;

import com.palantir.docker.compose.DockerComposeRule;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.gemini4j.testapp.reporter.RecordingReporter;
import org.junit.ClassRule;
import org.junit.runner.RunWith;

import static org.gemini4j.testapp.util.TestAppUtil.testAppEnvironment;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"org.gemini4j.cucumber.Gemini4jPlugin"})
public class CucumberTests {
    @ClassRule
    public static DockerComposeRule DOCKER = testAppEnvironment();

    public static final RecordingReporter REPORTER = new RecordingReporter();
}
