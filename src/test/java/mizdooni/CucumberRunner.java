package mizdooni;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/test.feature", // Simplified path using classpath
        glue = "mizdooni.StepDefs", // Ensure the step definitions are in the right package
//        plugin = {"pretty", "html:target/cucumber-html-report"},
//        tags = "@OnlyOneTime",
        publish = true // Optional: enable if you're generating reports
)
public class CucumberRunner {
}
