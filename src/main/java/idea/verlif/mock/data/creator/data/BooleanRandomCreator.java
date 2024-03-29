package idea.verlif.mock.data.creator.data;

import idea.verlif.mock.data.MockDataCreator;
import idea.verlif.mock.data.creator.DataCreator;
import idea.verlif.mock.data.domain.MockSrc;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BooleanRandomCreator implements DataCreator<Boolean> {

    private final Random random;

    public BooleanRandomCreator() {
        random = new Random();
    }

    @Override
    public Boolean mock(MockSrc src, MockDataCreator.Creator creator) {
        return random.nextBoolean();
    }

    @Override
    public List<Class<?>> types() {
        List<Class<?>> list = new ArrayList<>();
        list.add(Boolean.class);
        list.add(boolean.class);
        return list;
    }
}
