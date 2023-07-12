Feature: Create multiple new tasks

  Scenario Outline: The user can add new task
    Given Click add new Task
    And Enter "<TaskName>" and "<TaskDesc>"
    When Click Save
    Then Task "<TaskName>" Added Successfully

    Examples:
      | TaskName        | TaskDesc       |
      | Cucumber Task 1 | Task 1 Details |
      | Cucumber Task 2 | Task 2 Details |