package io.github.janjanda.otava.cli;

import io.github.janjanda.otava.library.Manager;
import io.github.janjanda.otava.library.Result;
import io.github.janjanda.otava.library.exceptions.ValidatorException;
import io.github.janjanda.otava.library.locales.CzechLocale;
import org.apache.commons.cli.*;
import java.util.Set;

public class CliApp {
    public static void main(String[] args) {
        Option tableNames = Option.builder("t").hasArgs().build();
        Option tableAliases = Option.builder("T").hasArgs().build();
        Option descriptorNames = Option.builder("d").hasArgs().build();
        Option descriptorAliases = Option.builder("D").hasArgs().build();
        Option language = Option.builder("l").hasArg().build();
        Options options = new Options();
        options.addOption(tableNames).addOption(tableAliases).addOption(descriptorNames).addOption(descriptorAliases).addOption(language);
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);
            String[] tables = line.getOptionValues(tableNames);
            String[] tAliases = line.getOptionValues(tableAliases);
            String[] descriptors = line.getOptionValues(descriptorNames);
            String[] dAliases = line.getOptionValues(descriptorAliases);
            String lang = line.getOptionValue(language);
            Manager m;
            if (lang != null && lang.equals("cs")) m = new Manager(new CzechLocale());
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
}
