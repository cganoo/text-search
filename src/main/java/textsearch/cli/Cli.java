package textsearch.cli;

import com.google.common.base.Joiner;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;
import textsearch.api.Search;
import textsearch.exceptions.IllegalCliOptionException;
import textsearch.exceptions.NonreadableCliOptionException;
import textsearch.exceptions.UnsupportedCliOptionException;
import textsearch.utils.Constants;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by cganoo on 28/01/15.
 */
@Controller
public class Cli implements CommandLineRunner {
    final static Logger log = LoggerFactory.getLogger(Cli.class);

    final Joiner joiner = Joiner.on("_").skipNulls();
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("HH_mm_ss");

    @Autowired
    private Search search;

    private String[] args = null;
    private Options options = new Options();

    @Override
    public void run(String... args) {

        if (args == null) {
            log.error("Refusing to process illegal command line arguments");
            /* Be nice and display the expected usage */
            help();
            throw new IllegalCliOptionException();
        }

        this.args = args;

        /* Define necessary options */
        Option help = new Option("h", "help", false, "show help.");
        Option search = OptionBuilder.withArgName("searchEngine documentFile queryFile")
                .hasArgs(Constants.SEARCH_ARG_NUMBER)
                .withValueSeparator(Constants.SEARCH_ARG_SEPARATOR)
                .withDescription("textsearch.search for query in document using the specified searchEngine and highlight strategy")
                .create("search");

        /* Register the defined options */
        this.options.addOption(help);
        this.options.addOption(search);

        /* Let the parsing begin */
        parse();
    }

    /**
     * Private utility method for validating cmd line options and applying business rules
     * to them.
     */
    private void parse() {

        /* Create a parser */
        CommandLineParser parser = new GnuParser();

        try {
            /* Parse the available command line arguments */
            CommandLine cmd = parser.parse(options, args);

            /* Rules for the 'h/help' option */
            if (cmd.hasOption(Constants.OPTION_HELP_SHORT)) {
                help();
            }
             /* Rules for the 'textsearch.search' option */
            else if (cmd.hasOption(Constants.OPTION_SEARCH)) {

                /* Retrieve the user supplied arguments */
                String[] searchParams = cmd.getOptionValues(Constants.OPTION_SEARCH);

                /* Basic validation */
                if (searchParams == null || searchParams.length != Constants.SEARCH_ARG_NUMBER) {
                    log.error("Refusing to apply textsearch.search functionality for illegal command line arguments");
                    /* Be nice and display the expected usage */
                    help();
                    throw new IllegalCliOptionException("Refusing to apply textsearch.search functionality for illegal command line arguments");
                }

                for (final String s : searchParams) {
                    if (StringUtils.isEmpty(s)) {
                        throw new IllegalArgumentException("Refusing to apply textsearch.search functionality for malformed input");
                    }
                }

                final String searchEngine = searchParams[0];
                final String highlightStrategy = searchParams[1];
                final String documentFile = searchParams[2];
                final String queryFile = searchParams[3];

                List<String> documentContents;
                List<String> queryContents;

                /* Read the contents of the document and query texts */
                try {
                    /* Dont rely on platform default encoding. Be nice and specify a charset */
                    documentContents = Files.readAllLines(Paths.get(documentFile), StandardCharsets.UTF_8);
                    queryContents = Files.readAllLines(Paths.get(queryFile), StandardCharsets.UTF_8);
                } catch (IOException e) {
                    log.error("Encountered IO exception while reading file contents [{}]", e.getMessage());
                    throw new NonreadableCliOptionException("Could not read the contents pointed to by the supplied parameters");
                }

                /* Massage the List<String> obtained above as a String for further use */
                final String document = StringUtils.join(documentContents, ' ');
                final String query = StringUtils.join(queryContents, ' ');

                /* Delegate to the textsearch.search API for generating relevant match */
                final String generatedSnippet = search.generateSnippets(searchEngine, highlightStrategy, document, query);

                /* Display the generated relevant match */
                log.info("##### Relevant Match ##### : {}", generatedSnippet);

                final String fileName = "./data/" + joiner.join(searchEngine, highlightStrategy, sdf.format(cal.getTime())) + ".txt";
                outputToFile(fileName, searchEngine, highlightStrategy, document, query, generatedSnippet);
            }
            /* Unsupported CLI option */
            else {
                log.error("Encountered unsupported CLI option");
                throw new UnsupportedCliOptionException("Encountered unsupported CLI option");
            }
        } catch (NullPointerException | ParseException e) {
            log.error("Encountered Exception [{}]", e);
            /* Nothing else can be done. Spring will terminate the application at this point */
        }

    }

    /**
     * Convenience utility method to display correct CLI usage
     */
    private void help() {
        /* Automatically generate the help statement */
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Cli", options, true);
    }

    private void outputToFile(final String fileName, final String searchEngine, final String highlightStrategy, final String document, final String query, final String match) {
        log.info("Attempting to store output at {}", fileName);
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)));
            out.println("##### SearchEngine #####");
            out.println(searchEngine);
            out.println();
            out.println("##### Highlight Strategy #####");
            out.println(highlightStrategy);
            out.println();
            out.println("##### Document #####");
            out.println(document);
            out.println();
            out.println("##### Query ##### ");
            out.println(query);
            out.println();
            out.println("##### Relevant Match ##### ");
            out.println(match);
            out.close();
        } catch (IOException e) {
            log.error("Encountered IOException when writing output file {}", e);
            /* Nothing more to be done here. Not throwing an exception since this is relatively benign */
        }
    }
}
