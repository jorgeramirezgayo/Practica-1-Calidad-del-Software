package com.example.easynotes

import io.cucumber.android.runner.CucumberAndroidJUnitRunner
import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@CucumberOptions(
    plugin = ["pretty"],
    features = ["src/test/java/com/example/easynotes/cucumberTest/features"],
    glue = ["com.example.easynotes"]
)
@RunWith(
    Cucumber::class
)
class CucumberUnitTestRunner : CucumberAndroidJUnitRunner()