package io.github.janjanda.otava.cli;

import io.github.janjanda.otava.library.Manager;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.exceptions.ValidatorException;
import io.github.janjanda.otava.library.locales.CzechLocale;
import org.apache.commons.cli.*;
import java.util.Set;

public class CliApp {
    public static void main(String[] args) {
        final String csTag = "cs";
        Option help = new Option("h", "print this message");
        Option tableNames = Option.builder("t").hasArgs().argName("file names").desc("file names of tables").build();
        Option tableAliases = Option.builder("T").hasArgs().argName("aliases").desc("aliases of the tables").build();
        Option descriptorNames = Option.builder("d").hasArgs().argName("file names").desc("file names of descriptors").build();
        Option descriptorAliases = Option.builder("D").hasArgs().argName("aliases").desc("aliases of the descriptors").build();
        Option language = Option.builder("l").hasArg().argName("language tag").desc("change the language of the result, use " + csTag + " for Czech").build();
        Options options = new Options();
        options.addOption(help).addOption(tableNames).addOption(tableAliases).addOption(descriptorNames).addOption(descriptorAliases).addOption(language);
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);
            if (line.hasOption(help)) {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("otava-cli", options, true);
                return;
            }
            String[] tables = line.getOptionValues(tableNames);
            if (tables == null) {
                System.out.println("Missing table names!");
                return;
            }
            String[] tAliases = initArrayStart(tables.length, line.getOptionValues(tableAliases));
            String[] descriptors = line.getOptionValues(descriptorNames);
            if (descriptors == null) {
                System.out.println("Missing descriptor names!");
                return;
            }
            String[] dAliases = initArrayStart(descriptors.length, line.getOptionValues(descriptorAliases));
            String lang = line.getOptionValue(language);
            Manager m;
            if (csTag.equals(lang)) m = new Manager(new CzechLocale());
            else m = new Manager();
            Set<Result> results = m.manualLocalValidation(tables, tAliases, descriptors, dAliases);
            for (Result result : results) {
                System.out.println(result.asText());
            }
        }
        catch (ParseException | ValidatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String[] initArrayStart(int length, String[] arrayStart) {
        String[] result = new String[length];
        if (arrayStart == null) return result;
        for (int i = 0; i < result.length && i < arrayStart.length; i++) {
            result[i] = arrayStart[i];
        }
        return result;
    }
}
