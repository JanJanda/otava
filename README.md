# Open Table Validator

[![Java CI with Maven](https://github.com/JanJanda/otava/actions/workflows/maven.yml/badge.svg)](https://github.com/JanJanda/otava/actions/workflows/maven.yml)

## Objective

The [CSV on the Web](https://www.w3.org/TR/2016/NOTE-tabular-data-primer-20160225/) W3C recommendations specify, how to describe CSV files published on the Web using JSON-LD descriptors containing important metadata, such as column names, data types and more. The goal of this project is to implement a validator of CSV files based on the CSV on the Web W3C recommendations.

## Status of Development

Java version 17.0.1 is used in this project.

The library part of this project is developed. There are automatic unit tests with great coverage. It is possible to execute individual parts of the library in the tests. The tests can be modified to try different behavior. There are also resources for the tests. It is easy to find the files because the project follows the [Standard Maven Layout](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html). The command `mvn test` executes the tests.

The command line interface is also developed.

The web application is currently being developed.

## The Library

The library contains the core functionality of this project. It performs the validation and other supporting tasks. The library is used via the manager in the class `Manager` in the main package `io.github.janjanda.otava.library`. The manager provides simple public methods for users, and it hides the internal complexity of the library. The names of the methods are self-explanatory, and there are comments with additional instructions. The library is ready for international environment. Users can set locale in the manager. Users can use implemented locales in the subpackage `locales` or implement their own locale. The default locale is English, and there is also a Czech locale.

### Validation Principles

There are three styles of the validation: tables only, descriptors only, and full validation. Each style has a corresponding method with comments and descriptions in the manager. The validation itself is implemented in many individual classes. This ensures great extensibility. Each class represent a so-called validation check, and it checks a particular feature of tables and descriptors. Some checks do not make sense if other checks fail. For this reason, a check can depend on other checks. These dependencies create a tree, and the checks in the tree are executed from leaves to the root. If more checks depend on one check, the one check appears on more places in the tree, but it is executed only once for effectiveness. The manager can print the validations trees for each style of the validation. The methods for the validation itself require one parameter with a validation suite. A validation suite contains documents and other input for the validation, and it has a convenient builder. The validation returns a validation report with results and other data. Each validation check creates one result. The report can be shown in several formats. If a validation cannot be performed, an exception is thrown. The exception can also be shown in several formats just like the report.

### Documents

It is important to understand the documents in the library. There are following types of documents: active table, passive table, active descriptor, and passive descriptor. An active table attempts to locate its descriptor, a passive table does not. An active descriptor attempts to locate its tables, a passive descriptor does not. Every document is either local or online. Every document has a name. A name is an actual location of the document (path in a filesystem or URL). Every document may have an alias. An alias is an alternative name. If the alias is present, it is used in the validation instead of the real name. This is useful when a user wants to validate a table which has not been published yet. The name of the table is the path to the actual file, and the alias is the future URL of the table. The table is located and loaded according to its name, but it pretends that it is on a different location according to the alias. Tables are either in memory or out of memory. In-memory tables are loaded completely into memory, out-of-memory tables are not. This can be used to adjust performance of the library.

## Command Line Application

The command line application provides user access to the features of the library. It can be compiled and executed respectively with the following commands.

```
mvn package
java -jar cli/target/cli-1.0-jar-with-dependencies.jar -help
```

The option `-help` shows possible options and their syntax with arguments. The names and descriptions of the command line options clearly refer to the corresponding concepts in the library from the previous section.

### Examples

Perform full validation with the provided active online descriptor, and write the report as plain text.
```
java -jar cli/target/cli-1.0-jar-with-dependencies.jar -full -text -aod https://w3c.github.io/csvw/tests/test011/tree-ops.csv-metadata.json
```

The following examples are executed in the directory `library/src/test/resources`.

Perform full validation with the provided passive local in-memory table and the provided passive local descriptor, and write the report as plain text. The table and the descriptor have both name and alias because the links in the descriptor would not be valid otherwise.
```
java -jar ../../../../cli/target/cli-1.0-jar-with-dependencies.jar -full -text -plit tables/table005.csv;https://example.org/tree-ops.csv -pld metadata/metadata001.json;https://example.org/metadata001.json
```

Perform tables only validation with the provided tables, and write the report as plain text. It is possible to validate multiple documents together.
```
java -jar ../../../../cli/target/cli-1.0-jar-with-dependencies.jar -tables -text -plit tables/table002.csv -plit tables/table013.csv -plit tables/table014.csv -plit tables/table015.csv
```
