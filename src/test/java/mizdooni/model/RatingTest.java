package mizdooni.model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RatingTest {
    @Test
    void OverAllStarCountIsBelowFive() {
        Rating rating=new Rating();
        rating.overall=10.5;
        Assertions.assertTrue(rating.getStarCount()<=5);
    }
    // TODO: no negative ratings
}
