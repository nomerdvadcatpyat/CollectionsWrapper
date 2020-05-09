package wrappers;

import org.junit.runners.Suite;
import org.junit.runner.RunWith;
import wrappers.collections.*;
import wrappers.map.MapWrapperTest;

@RunWith(Suite.class)
@Suite.SuiteClasses(
        {
                ListWrapperTest.class,
                QueueWrapperTest.class,
                SetWrapperTest.class,
                MapWrapperTest.class
        })

public class TestSuite {
}

