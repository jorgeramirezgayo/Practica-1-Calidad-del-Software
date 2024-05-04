require 'calabash-android/calabash_steps'

# Scenario by default
When(/^I press "([^"]*)"$/) do |button_name|
end

Given(/^I see "([^"]*)"$/) do |text|
end

Then(/^I see "([^"]*)"$/) do |text|
end

# Scenario Dark Mode
Given(/^the user is in the application settings$/) do
    touch("Ajustes")
  end
  
  When(/^the user taps on the "(.*?)" option$/) do |option|
    touch("ModoOscuro")
  end
  
  Then(/^the application's color scheme changes to dark$/) do
    wait_for_element_exists("* id:'ModoOscuro'")
    assert_element_exists("* id:'ModoOscuro'")
  end
  