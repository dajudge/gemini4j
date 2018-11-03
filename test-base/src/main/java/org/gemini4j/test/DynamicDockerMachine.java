package org.gemini4j.test;

import com.palantir.docker.compose.DockerComposeRule;
import com.palantir.docker.compose.connection.DockerMachine;

public class DynamicDockerMachine {
    public static DockerComposeRule.Builder dynamicMachine(final DockerComposeRule.Builder builder) {
        System.out.println("DOCKER_HOST: " + System.getenv("DOCKER_HOST"));
        if (null != System.getenv("DOCKER_HOST")) {
            return builder.machine(DockerMachine.remoteMachine()
                    .host(System.getenv("DOCKER_HOST"))
                    .build()
            );
        }
        return builder;
    }
}
