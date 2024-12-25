package mizdooni;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"classpath:features/UserAddReservation.feature", "classpath:features/RestaurantGetAverageRating.feature"},
        glue = "mizdooni.StepDefs",
        publish = true
)
public class CucumberRunner {
}
