package SonR.Testing;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        TestDataFrame.class,
        TestMapping.class,
        TestPipeline.class,
})

public class FrameworkTestSuite {
    // the class remains empty,
    // used only as a holder for the above annotations
}