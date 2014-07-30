Feature: Page Navigation features

Scenario: Basic page scenario
  When I go to the home page
  Then I am at the home page

Scenario: Instant redirect
  When I go via the instant redirect page
  Then I am at the second page

Scenario: Delayed redirect
  When I go to the delayed redirect page
  Then I am at the delayed redirect page
  When I pause for 2 seconds
  Then I am at the second page

Scenario: Direct URL
  When I go to 'http://localhost:8080/integration-test/form.jsp'
  Then I am at the form page

Scenario: Direct URI
  When I go to 'form.jsp'
  Then I am at the form page

Scenario: URL Test
  When I go to the form page
  Then I am at 'http://localhost:8080/integration-test/form.jsp'
  And I am at 'form.jsp'