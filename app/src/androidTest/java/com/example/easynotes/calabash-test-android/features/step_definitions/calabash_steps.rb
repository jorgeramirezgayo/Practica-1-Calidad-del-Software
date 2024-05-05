require 'calabash-android/calabash_steps'

Given(/^I am in the "([^"]*)" view$/) do |view_name|
  # Your code to navigate to the "Settings" view
  # For example:
  # wait_for_element_exists("* marked:'#{view_name}'")
end

When(/^I press the "([^"]*)" radio button$/) do |radio_button_name|
  # Your code to tap the specified radio button
  # For example:
  # touch("* marked:'#{radio_button_name}'")
end

Then(/^I see the mode go dark$/) do
  # Your code to verify if the dark mode is enabled
  # For example:
  # wait_for_element_exists("* marked:'dark_mode_enabled'")
end

Given(/^I am in the "([^"]*)" view$/) do |view_name|
  # Your code to navigate to the "Edit Note" view
  # For example:
  # wait_for_element_exists("* marked:'#{view_name}'")
end

When(/^I press the "([^"]*)" radio button$/) do |radio_button_name|
  # Your code to tap the specified radio button
  # For example:
  # touch("* marked:'#{radio_button_name}'")
end

When(/^I press the "([^"]*)" button$/) do |button_name|
  # Your code to tap the specified button
  # For example:
  # touch("* marked:'#{button_name}'")
end

Then(/^I see note change colour$/) do
  # Your code to verify if the note's color has changed
  # For example:
  # wait_for_element_exists("* marked:'note_color_changed'")
end

Given(/^I am in the "([^"]*)" view$/) do |view_name|
  # Your code to navigate to the "Edit Note" view
  # For example:
  # wait_for_element_exists("* marked:'#{view_name}'")
end

When(/^I press the "([^"]*)" radio button$/) do |radio_button_name|
  # Your code to tap the specified radio button
  # For example:
  # touch("* marked:'#{radio_button_name}'")
end

When(/^I press the "([^"]*)" button$/) do |button_name|
  # Your code to tap the specified button
  # For example:
  # touch("* marked:'#{button_name}'")
end

Then(/^I see the text of my note change colour$/) do
  # Your code to verify if the text color of the note has changed
  # For example:
  # wait_for_element_exists("* marked:'note_text_color_changed'")
end