Feature: Integrating with Cucumber JVM

  Scenario: Snapping a couple of screenshots
    # Take a couple of shots
    Given I open app1.html
    When I click on the Input nav item
    When I click on the Loading nav item
    # Verify they were recorded correctly
    Then the test 'Snapping a couple of screenshots' should have started
    Then the shot 'I open app1.html' should have differed
    Then the shot 'I click on the Input nav item' should have looked the same
    Then the shot 'I click on the Loading nav item' should have been missing
