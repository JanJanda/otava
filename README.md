# Open Table Validator

[![Java CI with Maven](https://github.com/JanJanda/otava/actions/workflows/maven.yml/badge.svg)](https://github.com/JanJanda/otava/actions/workflows/maven.yml)

## Objective

The [CSV on the Web](https://www.w3.org/TR/2016/NOTE-tabular-data-primer-20160225/) W3C recommendations specify, how to describe CSV files published on the Web using JSON-LD descriptors containing important metadata, such as column names, data types and more. The goal of this project is to implement a validator of CSV files based on the CSV on the Web W3C recommendations.

## Status of development

The library part of this project is currently being developed. There are automatic unit tests with great coverage. It is possible to execute individual parts of the library in the tests. The tests can be modified to try different behavior. There are also resources for the tests. It is easy to find the files because the project follows the [Standard Maven Layout](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html). The command `mvn test` executes the tests.

The command line interface is also currently being developed. It can be built and executed with the following commands.

```
mvn package
java -jar cli/target/cli-1.0-SNAPSHOT-jar-with-dependencies.jar -h
java -jar cli/target/cli-1.0-SNAPSHOT-jar-with-dependencies.jar -tree
java -jar cli/target/cli-1.0-SNAPSHOT-jar-with-dependencies.jar -t library/src/test/resources/tables/table004.csv -T https://example.org/tree-ops.csv -d library/src/test/resources/metadata/metadata001.json -D https://example.org/metadata001.json -l cs
```

## Progress

- Library
  - [x] Design extensible architecture
  - [x] Implement interface for validation checks
  - [x] Implement basic data model
  - [x] Implement basic local file discovery
  - [x] Add support for locales
  - [x] Create first working set of validation checks
  - [x] Add unit tests
  - [x] Configure CI testing
  - [x] Create satisfactory set of validation checks
  - [x] Finish data model
  - [x] Extend file discovery possibilities
  - [x] Create unified library interface
  - [x] Inspect test coverage
  - [x] Review code
  - [x] Add documentation
