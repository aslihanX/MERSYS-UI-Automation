@US014
@profileFeature
@Photo
Feature: Profile Settings and Picture Upload Functionality

  As a student,
  I should be able to upload and change my profile picture.
  This way, I can use the campus app in a more interactive way.

  Background:
    Given User is logged in as a student

  @TC01401
  @smoke
  Scenario: Successful profile picture upload and size verification
    Given the user navigates to "Profile" > "Settings"
    When the user clicks on the profile picture placeholder
    And the user clicks the "Upload" button to select a picture
    And the user selects a profile picture from path "/src/test/resources/testData/profilePhoto.jpg"
    Then the user should see the size of the uploaded picture
    When the user clicks the "Upload" button to confirm upload
    And the user see "Save" button on the page and click it
    Then the "Profile successfully updated" message should be displayed
