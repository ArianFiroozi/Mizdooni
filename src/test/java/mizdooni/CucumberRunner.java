package mizdooni;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features/UserAddReservation.feature",
        glue = "mizdooni.StepDefs",
//        plugin = {"pretty", "html:target/cucumber-html-report"},
//        tags = "@OnlyOneTime",
        publish = true
)
public class CucumberRunner {
}
