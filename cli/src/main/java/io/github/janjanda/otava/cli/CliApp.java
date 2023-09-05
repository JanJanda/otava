package io.github.janjanda.otava.cli;

import org.apache.commons.cli.*;

public class CliApp {
    public static void main(String[] args) {
        Option help = Option.builder("help").desc("print this description of the options").build();
        Option lang = Option.builder("lang").desc("set the language of the results, en is default").hasArg().argName("language tag [en|cs]").build();
        Option saveMem = Option.builder("savemem").desc("do not load tables from active descriptors to memory").build();
        Option tables = Option.builder("tables").desc("validate only tables alone").build();
        Option descs = Option.builder("descs").desc("validate only descriptors alone").build();
        Option full = Option.builder("full").desc("perform full validation with tables and descriptors").build();
        Option sep = Option.builder("sep").desc("set separator between name and alias of a table or a descriptor, : is default").hasArg().argName("one non-whitespace character").build();
        Option plit = Option.builder("plit").desc("add passive local in-memory table to validation suite").hasArgs().argName("path[:alias]...").build();
        Option plot = Option.builder("plot").desc("add passive local out-of-memory table to validation suite").hasArgs().argName("path[:alias]...").build();
        Option poit = Option.builder("poit").desc("add passive online in-memory table to validation suite").hasArgs().argName("url[:alias]...").build();
        Option poot = Option.builder("poot").desc("add passive online out-of-memory table to validation suite").hasArgs().argName("url[:alias]...").build();
        Option alit = Option.builder("alit").desc("add active local in-memory table to validation suite").hasArgs().argName("path[:alias]...").build();
        Option alot = Option.builder("alot").desc("add active local out-of-memory table to validation suite").hasArgs().argName("path[:alias]...").build();
        Option aoit = Option.builder("aoit").desc("add active online in-memory table to validation suite").hasArgs().argName("url[:alias]...").build();
        Option aoot = Option.builder("aoot").desc("add active online out-of-memory table to validation suite").hasArgs().argName("url[:alias]...").build();
        Option pld = Option.builder("pld").desc("add passive local descriptor to validation suite").hasArgs().argName("path[:alias]...").build();
        Option pod = Option.builder("pod").desc("add passive online descriptor to validation suite").hasArgs().argName("url[:alias]...").build();
        Option ald = Option.builder("ald").desc("add active local descriptor to validation suite").hasArgs().argName("path[:alias]...").build();
        Option aod = Option.builder("aod").desc("add active online descriptor to validation suite").hasArgs().argName("url[:alias]...").build();

        Options options = new Options();
        options.addOption(help).addOption(lang).addOption(saveMem).addOption(tables).addOption(descs).addOption(full)
                .addOption(sep).addOption(plit).addOption(plot).addOption(poit).addOption(poot).addOption(alit)
                .addOption(alot).addOption(aoit).addOption(aoot).addOption(pld).addOption(pod).addOption(ald)
                .addOption(aod);

        HelpFormatter helpFormatter = new HelpFormatter();
        helpFormatter.setWidth(150);
        final String cmdLineSyntax = "otava [-help] [-lang en|cs] [-savemem] -tables | -descs | -full [-sep <char>] " +
                "[-plit <path[:alias]...>] [-plot <path[:alias]...>] [-poit <url[:alias]...>] [-poot <url[:alias]...>] " +
                "[-alit <path[:alias]...>] [-alot <path[:alias]...>] [-aoit <url[:alias]...>] [-aoot <url[:alias]...>] " +
                "[-pld <path[:alias]...>] [-pod <url[:alias]...>] [-ald <path[:alias]...>] [-aod <url[:alias]...>]";

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);

        }
        catch (ParseException e) {
            helpFormatter.printHelp(cmdLineSyntax, options);
        }
    }
}
