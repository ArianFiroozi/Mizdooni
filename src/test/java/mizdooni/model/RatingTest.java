package mizdooni.model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RatingTest {
    @Test
    void getStarCount_OverallRatingMoreThanFive_ReturnsFive() {
        Rating rating=new Rating();
        rating.overall=10.5;
        Assertions.assertEquals(5, rating.getStarCount());
    }
}
