package com.example.easynotes

import io.cucumber.java.en.Given
import io.cucumber.java.en.When
import io.cucumber.java.en.Then
import org.junit.Assert.assertEquals

class StepDefinitions {
    private var num1: Int = 0
    private var num2: Int = 0
    private var result: Int = 0

    @Given("I have two numbers {int} and {int}")
    fun iHaveTwoNumbers(num1: Int, num2: Int) {
        this.num1 = num1
        this.num2 = num2
    }

    @When("I add them together")
    fun iAddThemTogether() {
        result = num1 + num2
    }

    @Then("the result should be {int}")
    fun theResultShouldBe(expectedResult: Int) {
        assertEquals(expectedResult, result)
    }
}
