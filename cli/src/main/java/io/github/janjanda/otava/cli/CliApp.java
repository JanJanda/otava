package io.github.janjanda.otava.cli;

import io.github.janjanda.otava.library.Manager;
import io.github.janjanda.otava.library.Outcome;
import io.github.janjanda.otava.library.ValidationSuite;
import io.github.janjanda.otava.library.exceptions.ValidatorException;
import io.github.janjanda.otava.library.locales.CzechLocale;
import io.github.janjanda.otava.library.locales.EnglishLocale;
import org.apache.commons.cli.*;

import java.time.Duration;
import java.time.Instant;

public class CliApp {
    public static void main(String[] args) {
        // define options
        Option help = Option.builder("help").desc("print this description of the options").build();
        Option tablesTree = Option.builder("tablestree").desc("print the tree of validation checks for tables").build();
        Option descsTree = Option.builder("descstree").desc("print the tree of validation checks for descriptors").build();
        Option fullTree = Option.builder("fulltree").desc("print the full tree of validation checks").build();
        Option lang = Option.builder("lang").desc("set the language of the results, en is default").hasArg().argName("language tag [en|cs]").build();
        Option saveMem = Option.builder("savemem").desc("do not load tables from active descriptors to memory").build();
        Option tables = Option.builder("tables").desc("validate only passive tables alone").build();
        Option descs = Option.builder("descs").desc("validate only passive descriptors alone").build();
        Option full = Option.builder("full").desc("perform full validation with tables and descriptors").build();
        Option text = Option.builder("text").desc("print result as plain text").build();
        Option json = Option.builder("json").desc("print result as JSON").build();
        Option turtle = Option.builder("turtle").desc("print result as RDF 1.1 Turtle").build();
        Option sep = Option.builder("sep").desc("set separator between name and alias of a table or a descriptor, ; is default, regex parameter to String.split(String regex)").hasArg().argName("regex").build();
        Option plit = Option.builder("plit").desc("add passive local in-memory table to validation suite").hasArgs().argName("path[;alias]...").build();
        Option plot = Option.builder("plot").desc("add passive local out-of-memory table to validation suite").hasArgs().argName("path[;alias]...").build();
        Option poit = Option.builder("poit").desc("add passive online in-memory table to validation suite").hasArgs().argName("url[;alias]...").build();
        Option poot = Option.builder("poot").desc("add passive online out-of-memory table to validation suite").hasArgs().argName("url[;alias]...").build();
        Option alit = Option.builder("alit").desc("add active local in-memory table to validation suite").hasArgs().argName("path[;alias]...").build();
        Option alot = Option.builder("alot").desc("add active local out-of-memory table to validation suite").hasArgs().argName("path[;alias]...").build();
        Option aoit = Option.builder("aoit").desc("add active online in-memory table to validation suite").hasArgs().argName("url[;alias]...").build();
        Option aoot = Option.builder("aoot").desc("add active online out-of-memory table to validation suite").hasArgs().argName("url[;alias]...").build();
        Option pld = Option.builder("pld").desc("add passive local descriptor to validation suite").hasArgs().argName("path[;alias]...").build();
        Option pod = Option.builder("pod").desc("add passive online descriptor to validation suite").hasArgs().argName("url[;alias]...").build();
        Option ald = Option.builder("ald").desc("add active local descriptor to validation suite").hasArgs().argName("path[;alias]...").build();
        Option aod = Option.builder("aod").desc("add active online descriptor to validation suite").hasArgs().argName("url[;alias]...").build();

        // add options
        Options options = new Options();
        options.addOption(help).addOption(tablesTree).addOption(descsTree).addOption(fullTree).addOption(lang).addOption(saveMem)
                .addOption(tables).addOption(descs).addOption(full).addOption(text).addOption(json).addOption(turtle)
                .addOption(sep).addOption(plit).addOption(plot).addOption(poit).addOption(poot).addOption(alit)
                .addOption(alot).addOption(aoit).addOption(aoot).addOption(pld).addOption(pod).addOption(ald).addOption(aod);

        // create help formatter
        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setWidth(110);
        final String cmdLineSyntax = "otava [-help] [-tablestree -descstree -fulltree] [-lang en|cs] [-savemem] (-tables | -descs | -full) " +
                "[-text | -json | -turtle] [-sep <char>] [-plit <path[;alias]...>] [-plot <path[;alias]...>] [-poit <url[;alias]...>] " +
                "[-poot <url[;alias]...>] [-alit <path[;alias]...>] [-alot <path[;alias]...>] [-aoit <url[;alias]...>] [-aoot <url[;alias]...>] " +
                "[-pld <path[;alias]...>] [-pod <url[;alias]...>] [-ald <path[;alias]...>] [-aod <url[;alias]...>]";

        // parse command line
        CommandLineParser parser = new DefaultParser();
        CommandLine line;
        try {
            line = parser.parse(options, args);
        }
        catch (ParseException e) {
            System.out.println("Malformed arguments! Use -help");
            return;
        }

        // show help
        if (line.hasOption(help)) {
            helpFormatter.printHelp(cmdLineSyntax, options);
            return;
        }

        // show checks tree
        if (line.hasOption(tablesTree)) {
            System.out.println(Manager.printTablesOnlyValidationTree());
            return;
        }
        if (line.hasOption(descsTree)) {
            System.out.println(Manager.printDescriptorsOnlyValidationTree());
            return;
        }
        if (line.hasOption(fullTree)) {
            System.out.println(Manager.printFullValidationTree());
            return;
        }

        // set language
        if (line.hasOption(lang)) {
            boolean langSet = setLanguage(line.getOptionValue(lang));
            if (!langSet) {
                System.out.println("Incorrect language tag! Use -help");
                return;
            }
        }

        // check validation kind options
        int validationOptions = 0;
        if (line.hasOption(tables)) validationOptions++;
        if (line.hasOption(descs)) validationOptions++;
        if (line.hasOption(full)) validationOptions++;
        if (validationOptions != 1) {
            System.out.println("Exactly one option must be set out of -tables -descs -full options! Use -help");
            return;
        }

        // set separator
        String separator = line.getOptionValue(sep, ";");

        // crate validation suite
        ValidationSuite.Builder vsBuilder = new ValidationSuite.Builder();
        if (line.hasOption(saveMem)) vsBuilder.setSaveMemory();
        addTables(line.getOptionValues(plit), false, true, false, separator, vsBuilder);
        addTables(line.getOptionValues(plot), false, true, true, separator, vsBuilder);
        addTables(line.getOptionValues(poit), false, false, false, separator, vsBuilder);
        addTables(line.getOptionValues(poot), false, false, true, separator, vsBuilder);
        addTables(line.getOptionValues(alit), true, true, false, separator, vsBuilder);
        addTables(line.getOptionValues(alot), true, true, true, separator, vsBuilder);
        addTables(line.getOptionValues(aoit), true, false, false, separator, vsBuilder);
        addTables(line.getOptionValues(aoot), true, false, true, separator, vsBuilder);
        addDescriptors(line.getOptionValues(pld), false, true, separator, vsBuilder);
        addDescriptors(line.getOptionValues(pod), false, false, separator, vsBuilder);
        addDescriptors(line.getOptionValues(ald), true, true, separator, vsBuilder);
        addDescriptors(line.getOptionValues(aod), true, false, separator, vsBuilder);
        ValidationSuite vs = vsBuilder.build();

        // perform validation
        Manager manager = new Manager();
        Outcome[] outcomes = new Outcome[0];
        Instant validationStart = Instant.now();
        try {
            if (line.hasOption(tables)) outcomes = manager.tablesOnlyValidation(vs);
            if (line.hasOption(descs)) outcomes = manager.descriptorsOnlyValidation(vs);
            if (line.hasOption(full)) outcomes = manager.fullValidation(vs);
        }
        catch (ValidatorException e) {
            outcomes = new Outcome[]{e};
        }
        Instant validationEnd = Instant.now();

        // write outcomes
        printDuration(Duration.between(validationStart, validationEnd));
        boolean outputWritten = false;
        if (line.hasOption(text)) {
            for (Outcome outcome : outcomes) System.out.println(outcome.asText());
            outputWritten = true;
        }
        if (line.hasOption(json)) {
            for (Outcome outcome : outcomes) System.out.println(outcome.asJson());
            outputWritten = true;
        }
        if (line.hasOption(turtle)) {
            for (Outcome outcome : outcomes) System.out.println(outcome.asTurtle());
            outputWritten = true;
        }
        if (!outputWritten) System.out.println("No output format was selected! Use -help");
    }

    private static boolean setLanguage(String langTag) {
        if (langTag == null) return false;
        if (langTag.equals("en")) {
            Manager.setLocale(new EnglishLocale());
            return true;
        }
        if (langTag.equals("cs")) {
            Manager.setLocale(new CzechLocale());
            return true;
        }
        return false;
    }

    private static void addTables(String[] specs, boolean active, boolean local, boolean outOfMemory, String separator, ValidationSuite.Builder vsBuilder) {
        if (specs == null) return;
        for (String spec : specs) {
            String[] splitSpec = spec.split(separator);
            if (splitSpec.length == 1) {
                if (active) vsBuilder.addActiveTable(splitSpec[0], null, local, outOfMemory);
                else vsBuilder.addPassiveTable(splitSpec[0], null, local, outOfMemory);
            }
            if (splitSpec.length == 2) {
                if (active) vsBuilder.addActiveTable(splitSpec[0], splitSpec[1], local, outOfMemory);
                else vsBuilder.addPassiveTable(splitSpec[0], splitSpec[1], local, outOfMemory);
            }
        }
    }

    private static void addDescriptors(String[] specs, boolean active, boolean local, String separator, ValidationSuite.Builder vsBuilder) {
        if (specs == null) return;
        for (String spec : specs) {
            String[] splitSpec = spec.split(separator);
            if (splitSpec.length == 1) {
                if (active) vsBuilder.addActiveDescriptor(splitSpec[0], null, local);
                else vsBuilder.addPassiveDescriptor(splitSpec[0], null, local);
            }
            if (splitSpec.length == 2) {
                if (active) vsBuilder.addActiveDescriptor(splitSpec[0], splitSpec[1], local);
                else vsBuilder.addPassiveDescriptor(splitSpec[0], splitSpec[1], local);
            }
        }
    }

    private static void printDuration(Duration duration) {
        double time = duration.toMillis() / 1000d;
        System.out.println("Total validation time: " + time + " s");
    }
}
