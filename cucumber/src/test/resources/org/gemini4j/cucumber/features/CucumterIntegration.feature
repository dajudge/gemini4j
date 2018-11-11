Feature: Integrating with Cucumber JVM

  Scenario: Snapping a couple of screenshots
    # First we run a couple of browser interactions
    Given I open app1.html
    When I click on the Input nav item
    When I click on the Loading nav item
    # Then we verify that the interactions resulted in the correct reportings
    Then The test 'Snapping a couple of screenshots' should have started
    Then I should hurz
