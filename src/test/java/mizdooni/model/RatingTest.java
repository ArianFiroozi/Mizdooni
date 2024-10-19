package mizdooni.model;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class RatingTest {
    @ParameterizedTest
    @ValueSource(doubles={10,5.12,5,1234})
    void getStarCount_OverallRatingMoreThanFive_ReturnsFive(double overallRating) {
        Rating rating=new Rating();
        rating.overall=overallRating;
        Assertions.assertEquals(5, rating.getStarCount());
    }
}
