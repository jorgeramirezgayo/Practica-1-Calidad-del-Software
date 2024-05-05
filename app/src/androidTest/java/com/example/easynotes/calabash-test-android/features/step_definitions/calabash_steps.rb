require 'calabash-android/calabash_steps'

Given(/^I am on the "([a-zA-Z-]+)" Screen$/) do |screen_name|
  wait_for_element_exists("* id:'LayoutAjusteClaro'")
end

When(/^I touch the "([a-zA-Z]+)" button$/) do |button_name|
  touch("* id:'rbModoOscuro'")
end

Then(/^I should see the "([a-zA-Z-]*)" Screen$/) do |next_screen_name|
  wait_for_element_exists("* id:'LayoutAjusteClaro'")
end

And(/^I take a screenshot$/) do
  screenshot_and_save("app/res/screenshots/darkMode.png")
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