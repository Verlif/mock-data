package idea.verlif.mock.data;

import idea.verlif.mock.data.config.MockDataConfig;
import idea.verlif.mock.data.creator.DataCreator;
import idea.verlif.mock.data.creator.data.*;
import idea.verlif.mock.data.domain.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import stopwatch.Stopwatch;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Verlif
 */
public class BaseTest {

    @Test
    public void fieldTest() throws NoSuchMethodException {
        System.out.println(BaseTest.class.getDeclaredMethod("fieldTest").equals(BaseTest.class.getDeclaredMethod("fieldTest")));
    }

    @Test
    public void baseUseTest() throws IllegalAccessException {
        // 创建数据构造器
        MockDataCreator creator = new MockDataCreator();
        // 获取构造器的当前配置
        MockDataConfig config = creator.getConfig()
                // 添加需要级联构造的类
                .cascadeCreateKey(Student.class)
                .cascadeCreateKey(Person.class)
                .cascadeCreateKey(Person.PersonInner.class)
                .cascadeCreateKey(A.class)
                .cascadeCreateKey(B.class)
                .cascadeCreateKey(Pet.class)
                .cascadeCreateKey(Dog.class)
                // 将构造深度设置为1
                .creatingDepth(1);
        // 构造测试
        for (int i = 0; i < 10; i++) {
            System.out.println(creator.mock(Student.class));
        }

        System.out.println("------>>> 使用字典生成name属性");
        System.out.println("------>>> 限制id属性生成范围");
        System.out.println("------>>> 限制age属性生成范围");
        System.out.println("------>>> 限制score属性生成范围");
        System.out.println("------>>> 自定义secondChild属性");
        config
                // 使用字典生成name属性
                .fieldCreator(Student::getName, new DictDataCreator<>(new String[]{
                        "小明", "小红", "小王", "小赵", "小李", "小周", "小强"
                }))
                // 限制id属性生成范围
                .fieldCreator(Student::getId, new LongRandomCreator(0L, 9999L))
                // 限制age属性生成范围
                .fieldCreator(Student::getAge, new IntegerRandomCreator(0, 200))
                // 限制score属性生成范围
                .fieldCreator(Student::getScore, new DoubleRandomCreator(0, 100D))
                // 自定义secondChild属性
                .fieldCreator(Student::getSecondChild, new DataCreator<Student>() {

                    private final Random random = new Random();

                    @Override
                    public Student mock(Field field, MockDataCreator.Creator creator) {
                        if (random.nextBoolean()) {
                            return new Student("这是自定义的构造");
                        } else {
                            return null;
                        }
                    }
                })
                .fieldCreator(new DataCreator<Person>() {
                    @Override
                    public Person mock(Field field, MockDataCreator.Creator creator) {
                        return new Person("Person");
                    }
                })
                .fieldCreator(Dog.class, new DataCreator<Dog>() {
                    @Override
                    public Dog mock(Field field, MockDataCreator.Creator creator) {
                        return new Dog("Dog");
                    }
                })
                .fieldCreator(Pet.class, new DataCreator<Pet>() {
                    @Override
                    public Pet mock(Field field, MockDataCreator.Creator creator) {
                        return new Pet("Pet");
                    }
                });
        System.out.println("------>>> 强制新建对象");
        config.setForceNew(true);
//        System.out.println("------>>> 关闭private属性构建");
//        config.allowedModifiers(Modifier.PRIVATE);
//        System.out.println("------>>> 开启public属性构建");
//        config.blockedModifiers(Modifier.PUBLIC);
        Student[][] personArray = creator.mock(new Student[2][5]);
        for (int i = 0; i < 10; i++) {
            System.out.println(creator.mock(Student.class));
        }

    }

    @Test
    public void configTest() throws IllegalAccessException {
        MockDataCreator creator = new MockDataCreator();
        creator.useBaseData();
        creator.useExtendData();
        MockDataConfig config = creator.getConfig();
        config.addCascadeCreateKey(Person.class);
        config.addFieldCreator(Person::getId, new DataCreator<Long>() {
            @Override
            public Long mock(Field field, MockDataCreator.Creator creator) {
                return 1L;
            }
        });
        config.setCreatingDepth(0);
        for (int i = 0; i < 5; i++) {
            System.out.println(creator.mock(Person.class));
        }
        System.out.println("------>>> 使用自定义配置");
        MockDataConfig mockDataConfig = new MockDataConfig();
        mockDataConfig.addCascadeCreateKey(Person.class);
        config.addCascadeCreateKey(Person.class);
        for (int i = 0; i < 5; i++) {
            System.out.println(creator.mock(Person.class, mockDataConfig));
        }
        System.out.println("------>>> 使用原始配置");
        for (int i = 0; i < 5; i++) {
            System.out.println(creator.mock(Person.class));
        }
    }

    @Test
    public void mockTest() throws IllegalAccessException {
        MockDataCreator creator = new MockDataCreator();
        creator.useBaseData();
        creator.addDefaultCreator(new LongRandomCreator(-100, 200));
        MockDataConfig config = new MockDataConfig();
        config.addCascadeCreateKey(A.class);
        config.addCascadeCreateKey(B.class);
        creator.setConfig(config);
        config.addCascadeCreateKey(Person.class);
        config.addIgnoredField(Person::getAList);
//        config.addFieldCreator(IListExtend::getList, (DataCreator<List<?>>) (field, creator1) -> {
//            System.out.println("Hello");
//            return new ArrayList<>();
//        });
        config.setCreatingDepth(2);
        config.addFieldCreator(Person::getId, new LongRandomCreator(100, 300));
        Person[][] mock = creator.mock(Person[][].class);
        System.out.println(Arrays.toString(mock));
        Person[][] mock2 = creator.mock(new Person[2][5]);
        System.out.println(Arrays.toString(mock2));
        IList<String> list = creator.mock(new IListExtend());
        System.out.println(Arrays.toString(list.getList().toArray()));
    }

    @Test
    public void randomTest() throws IllegalAccessException {
        MockDataCreator creator = new MockDataCreator();
        creator.addDefaultCreator(new LocalDateCreator());
        creator.useExtendData();
        for (int i = 0; i < 100; i++) {
            List data = creator.mock(new ArrayList<Person>());
            System.out.println(data);
        }
    }

    @Test
    public void simpleTest() throws IllegalAccessException {
        MockDataCreator creator = new MockDataCreator();
        creator.useBaseData();
        System.out.println(creator.mock(Person::getId));
    }

    @Before
    public void startStopwatch() {
        Stopwatch.start("this");
    }

    @After
    public void stopStopwatch() {
        Stopwatch stopwatch = Stopwatch.get("this");
        stopwatch.stop();
        System.out.println("---- 累计耗时 >> " + stopwatch.getIntervalLine(TimeUnit.MILLISECONDS) + "  毫秒");
    }
}
