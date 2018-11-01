package org.gemini4j.cucumber;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.DockerMachine;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.ClassRule;
import org.junit.runner.RunWith;

import static com.palantir.docker.compose.connection.waiting.HealthChecks.toHaveAllPortsOpen;
import static com.palantir.docker.compose.connection.waiting.HealthChecks.toRespondOverHttp;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"org.gemini4j.cucumber.Gemini4jPlugin"})
public class CucumberTests {
    @ClassRule
    public static DockerComposeRule DOCKER = dynamicMachine(DockerComposeRule.builder())
            .pullOnStartup(true)
            .file("src/test/docker/docker-compose.yml")
            .saveLogsTo("build/test-docker-logs")
            .waitingForService("selenium-hub", toHaveAllPortsOpen())
            .waitingForService("nginx", toRespondOverHttp(80, p -> p.inFormat("http://$HOST:$EXTERNAL_PORT")))
            .build();

    private static DockerComposeRule.Builder dynamicMachine(final DockerComposeRule.Builder builder) {
        if (null != System.getenv("DOCKER_HOST")) {
            return builder.machine(DockerMachine.remoteMachine().build());
        }
        return builder;
    }

}
