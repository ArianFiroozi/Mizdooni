package mizdooni.model;
import org.junit.jupiter.api.Assertions;
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

    @ParameterizedTest
    @ValueSource(doubles={2.5, 3, 2.9, 3.3})
    void getStarCount_OverallRatingLessThanFive_ReturnsRoundedInt(double overallRating) {
        Rating rating=new Rating();
        rating.overall=overallRating;
        Assertions.assertEquals(3, rating.getStarCount());
    }
}
