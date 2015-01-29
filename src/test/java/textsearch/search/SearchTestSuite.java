package textsearch.search;

import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import textsearch.api.Search;
import textsearch.api.Tokenizer;
import textsearch.exceptions.NoMatchFoundException;
import textsearch.utils.Constants;
import textsearch.utils.Interpreter;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 *
 *
 * Note: The AnnotationConfigContextLoader implies that ApplicationContext will be loaded
 * from the static inner ContextConfiguration class
 *
 * Created by cganoo on 28/01/15.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class SearchTestSuite {

    @Configuration
    static class ContextConfiguration {

        /* Create the required beans for testing */
        @Bean
        public Search search() {
            Search search = new Search();
            return search;
        }

        @Bean
        public Tokenizer tokenizer() {
            Tokenizer tokenizer = new Tokenizer();
            return tokenizer;
        }

        @Bean
        public Interpreter interpreter() {
            Interpreter interpreter = new Interpreter();
            return interpreter;
        }

        @Bean
        public IndexWriter indexWriter() {
            return null;
        }

        @Bean
        public IndexSearcher indexSearcher() {
            return null;
        }

        @Bean
        public QueryParser queryParser() {
            return null;
        }

    }

    final static Logger log = LoggerFactory.getLogger(SearchTestSuite.class);

    @Autowired
    private Search search;

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

    @Test
    public void canonicalMatch() {
        final String document = "Our luxury loft-style apartments were constructed as condominiums, so your new residence will have: Solid floors and walls (this will be the quietest apartment you've EVER lived in); Premium stainless steel designer appliances; Distinctive accent walls and hardwood flooring; A kitchen that most chefs would drool over with easy to clean gas stove and countertops; Walk in closets with built in storage; Full size washer and dryer in each apartment home. In addition, all residents will enjoy use of our top-notch amenities, including reserved parking, cutting-edge fitness center, wireless internet cafe/business center, and rooftop lounge to soak up the sun!";
        final String query = "designer kitchen";
        final String canonicalMatch = "Premium stainless steel designer appliances; Distinctive accent walls and hardwood flooring; A kitchen that most chefs would drool over with easy to clean gas stove and countertops;";
        final String match = search.generateSnippets(Constants.SEARCH_ENGINE_DEFAULT, Constants.HIGHLIGHT_STRATEGY_INTERPOLATED, document, query);

        assertThat(match, notNullValue());
        assertThat(match, is(equalTo(canonicalMatch)));
    }

    @Test(expected = NoMatchFoundException.class)
    public void noMatchWithGoodQueryTest() {
        final String document = "The sun rises in the east!";
        final String query = "moon";
        search.generateSnippets(Constants.SEARCH_ENGINE_DEFAULT, Constants.HIGHLIGHT_STRATEGY_INTERPOLATED, document, query);
    }

    @Test(expected = IllegalArgumentException.class)
    public void malformedInputTest() {
        final String document = "The sun rises in the east!";
        final String query = "";
        search.generateSnippets(Constants.SEARCH_ENGINE_DEFAULT, null, document, query);
    }

    @Test
    public void singleQueryWordInterpolatedTest() {
        final String document = "clean kitchen! Room with a view; dirty kitchen:so much food!";
        final String query = "kitchen";
        final String relevantMatch = "clean kitchen! Room with a view; dirty kitchen:";
        final String match = search.generateSnippets(Constants.SEARCH_ENGINE_DEFAULT, Constants.HIGHLIGHT_STRATEGY_INTERPOLATED, document, query);

        assertThat(match, notNullValue());
        assertThat(match, is(equalTo(relevantMatch)));
    }

    @Test
    public void singleQueryWordDistinctTest() {
        final String document = "clean kitchen! Room with a view; dirty kitchen:so much food!";
        final String query = "kitchen";
        final String relevantMatch = "clean kitchen! dirty kitchen:";
        final String match = search.generateSnippets(Constants.SEARCH_ENGINE_DEFAULT, Constants.HIGHLIGHT_STRATEGY_DISTINCT, document, query);

        assertThat(match, notNullValue());
        assertThat(match, is(equalTo(relevantMatch)));
    }
}
