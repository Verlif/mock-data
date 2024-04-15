package idea.verlif.test;

import com.alibaba.fastjson2.JSONObject;
import idea.verlif.mock.data.MockDataCreator;
import idea.verlif.mock.data.pool.SimplePool;
import idea.verlif.mock.data.pool.VirtualDataPool;
import idea.verlif.reflection.domain.ClassGrc;
import idea.verlif.test.entity.Person;
import idea.verlif.test.entity.Pet;
import org.junit.Test;

public class DataTest {

    @Test
    public void mainTest() {
        MockDataCreator creator = new MockDataCreator();
        creator.dataPool(new VirtualDataPool().withTemplate());
        Person person = new Person();
        for (int i = 0; i < 100; i++) {
            creator.mock(person);
        }
    }

    @Test
    public void diyTest() {
        MockDataCreator creator = new MockDataCreator();
        VirtualDataPool dataPool = new VirtualDataPool().withTemplate();
        dataPool.add("nickname", new SimplePool() {
            @Override
            public Object fetch(ClassGrc classGrc, String key) {
                if (classGrc.getTarget() == Person.class) {
                    return "personNickname";
                } else if (classGrc.getTarget() == Pet.class) {
                    return "petNickname";
                }
                return null;
            }
        });
        creator.dataPool(dataPool);
        Pet pet = new Pet();
        for (int i = 0; i < 100; i++) {
            creator.mock(pet);
        }
    }
}