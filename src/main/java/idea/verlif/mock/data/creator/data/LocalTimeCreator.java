package idea.verlif.mock.data.creator.data;

import idea.verlif.mock.data.MockDataCreator;
import idea.verlif.mock.data.creator.DataCreator;
import idea.verlif.mock.data.domain.MockSrc;

import java.time.LocalTime;
import java.util.Random;

/**
 * @author Verlif
 */
public class LocalTimeCreator implements DataCreator<LocalTime> {

    private final Random random;

    public LocalTimeCreator() {
        random = new Random();
    }

    @Override
    public LocalTime mock(MockSrc src, MockDataCreator.Creator creator) {
        return LocalTime.of(random.nextInt(24), random.nextInt(60));
    }
}
