# Open Table Validator

[![Java CI with Maven](https://github.com/JanJanda/otava/actions/workflows/maven.yml/badge.svg)](https://github.com/JanJanda/otava/actions/workflows/maven.yml)

## Objective

The [CSV on the Web](https://www.w3.org/TR/2016/NOTE-tabular-data-primer-20160225/) W3C recommendations specify, how to describe CSV files published on the Web using JSON-LD descriptors containing important metadata, such as column names, data types and more. The goal of this project is to implement a validator of CSV files based on the CSV on the Web W3C recommendations.

## Structure of the Project

This project achieves the goal and adds several extra features. Most of the project is implemented in Java, specifically Java version 17.0.1 is used and tested. The core is in a library. The library can be used with the included command line interface application. The project also contains a web application implemented in JavaScript. The web application provides a graphical user interface for better user experience. There is a Java web worker in the backend of the web application. It uses the library to process requests from the web. The project uses [Docker](https://www.docker.com/) to simplify development and deployment, but it can be used without Docker as well.

## Library

The library contains the core functionality of this project. It performs the validation and other supporting tasks. The library is used via the manager in the class `Manager` in the main package `io.github.janjanda.otava.library`. The manager provides simple public methods for users, and it hides the internal complexity of the library. The names of the methods are self-explanatory, and there are comments with additional instructions. The library is ready for international environment. Users can set a locale in the manager. Users can use implemented locales in the subpackage `locales` or implement their own locale. The default locale is English, and there is also a Czech locale.

### Validation Principles

There are three styles of the validation: tables only, descriptors only, and full validation. Each style has a corresponding method with comments and descriptions in the manager. The validation itself is implemented in many individual classes. This ensures great extensibility. Each class represent a so-called validation check, and it checks a particular feature of tables and descriptors. Some checks do not make sense if other checks fail. For this reason, a check can depend on other checks. These dependencies create a tree, and the checks in the tree are executed from leaves to the root. If more checks depend on one check, the one check appears on more places in the tree, but it is executed only once for effectiveness. The manager can print the validations trees for each style of the validation. The methods for the validation itself require one parameter with a validation request. A validation request contains documents and other input for the validation, and it has a convenient builder. The validation returns a validation report with results and other data. Each validation check creates one result. The report can be shown in several formats. If a validation cannot be performed, an exception is thrown. The exception can also be shown in several formats just like the report.

### Documents

It is important to understand the documents in the library. There are following types of documents: active table, passive table, active descriptor, and passive descriptor. An active table attempts to locate and load its descriptor, a passive table does not. An active descriptor attempts to locate and load its tables, a passive descriptor does not. Every document is either local or online. Every document has a name. A name is an actual location of the document (path in a filesystem or URL). Every document may have an alias. An alias is an alternative name. If an alias is present, it is used in the validation instead of the real name. This is useful when a user wants to validate a table which has not been published yet. The name of the table is the path to the actual file, and the alias is the future URL of the table. The table is located and loaded according to its name, but it pretends that it is on a different location according to the alias. The same thing can be done with a descriptor. Tables are either in memory or out of memory. In-memory tables are loaded completely into memory, out-of-memory tables are not. This can be used to adjust performance of the library.

## Command Line Application

The command line application provides user access to the features of the library. It can be compiled and executed respectively with the following commands in the directory `java-code`.

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

The following examples are executed in the directory `java-code/library/src/test/resources`.

Perform full validation with the provided passive local in-memory table and the provided passive local descriptor, and write the report as plain text. The table and the descriptor have both name and alias because the links in the descriptor would not be valid otherwise.
```
java -jar ../../../../cli/target/cli-1.0-jar-with-dependencies.jar -full -text -plit tables/table005.csv;https://example.org/tree-ops.csv -pld metadata/metadata001.json;https://example.org/metadata001.json
```

Perform tables only validation with the provided tables, and write the report as plain text. It is possible to validate multiple documents together.
```
java -jar ../../../../cli/target/cli-1.0-jar-with-dependencies.jar -tables -text -plit tables/table002.csv -plit tables/table013.csv -plit tables/table014.csv -plit tables/table015.csv
```

## Web Application

This section describes the deployment process of the web application in ten easy steps.

1. Make sure that [Docker Engine](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/install/) are running and ready on your machine.
2. Choose a directory for the application.
3. Open your command line in that directory.
4. Run `git clone https://github.com/JanJanda/otava.git` to download the source code of the project.
5. Use `cd otava` to go to the directory of the project.
6. Edit the downloaded file `docker-compose.yml` like so: Come up with an original password, and replace every occurrence of `my-password` in the file with your password.
7. *Optional:* Increase the `replicas` value under `web_worker` in the file `docker-compose.yml` to spawn more workers and increase validation performance.
8. Run `docker compose up` in the directory `otava` to start everything and wait. It can take a long time. Command line prints "Database is ready" and stops printing other text when the application is ready.
9. Open your web browser and go to the address `localhost` to see the web application.
10. Open another command line in the `otava` directory and run `docker compose down` to stop everything.

The application uses the created directory `database-volume` to store its data. An instruction manual is in the web application. Repeat the steps 7-10 to start the application again. If you want to deploy a new version of the application, delete the two built otava images, run `git pull` in the directory `otava` to download the new version from this repository, and then use the steps 7-10.

There is a [C4](https://c4model.com/) diagram of the web application in the directory `web-app`. It can be rendered with [Structurizr](https://structurizr.com/dsl). There is an [Openapi](https://www.openapis.org/) documentation of the server web interface in the directory `web-app/server`. It can be rendered with [Swagger](https://swagger.io/).
