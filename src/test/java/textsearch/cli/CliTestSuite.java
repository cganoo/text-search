package textsearch.cli;

import org.junit.*;

import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import textsearch.exceptions.UnsupportedCliOptionException;


/**
 * Created by cganoo on 28/01/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class CliTestSuite {

    final static Logger log = LoggerFactory.getLogger(CliTestSuite.class);

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test(expected = UnsupportedCliOptionException.class)
    public void malfomedInput() {
        new Cli().run("");
    }
}
