# Sitodo

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=addianto_sitodo&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=addianto_sitodo)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=addianto_sitodo&metric=coverage)](https://sonarcloud.io/summary/new_code?id=addianto_sitodo)
[![OpenSSF Scorecard](https://api.scorecard.dev/projects/github.com/addianto/sitodo/badge)](https://scorecard.dev/viewer/?uri=github.com/addianto/sitodo)

A basic todo app project for teaching basic Web programming, Git workflows, code quality,
and CI/CD. Heavily inspired by the running example in "Test-Driven Development with Python" book by Harry Percival.

## Setting Up Development Environment

The following tools need to be installed in order to build and run the project:

-  [Java 17 JDK (Java Development Kit)](https://adoptium.net)
-  [Apache Maven 3.8.5](https://maven.apache.org/download.cgi)
-  [Mozilla Firefox](https://www.mozilla.org/en-US/firefox/)
   - Required by the functional (Selenium) test suite

Ensure `java`, `javac`, and `mvn` commands can be invoked from inside the shell:

```shell
$ java --version
openjdk 17.0.3 2022-04-19
OpenJDK Runtime Environment Temurin-17.0.3+7 (build 17.0.3+7)
OpenJDK 64-Bit Server VM Temurin-17.0.3+7 (build 17.0.3+7, mixed mode, sharing)
$ javac --version
javac 17.0.3
$ mvn --version
Apache Maven 3.8.2 (ea98e05a04480131370aa0c110b8c54cf726c06f)
```

We recommend [IntelliJ IDEA](https://www.jetbrains.com/idea/) Community Edition
as the IDE for developing the project. Other IDE or text editors, such as Eclipse
and Visual Studio Code, might work. However, we may not be able to help troubleshoot
any IDE-related issues. In addition, we include IntelliJ-specific **run configurations**
in the codebase that will add shortcuts for running the test suites and coverage
reporting from within IntelliJ.

## Getting Started

To run the whole test suite, execute:

```shell
mvn test
```

> To run a select test suite, e.g. unit or functional test, add `-Dgroups`
> parameter. For example, to run only the unit test suite, execute
> `mvn test -Dgroups=unit`.  Similarly, to run only the functional test suite,
> execute `mvn test -Dgroups=e2e`.

To build an executable Spring Boot application, execute:

```shell
mvn package -DskipTests
```

> The `-DskipTests` option lets `package` task to build the app into executable
> JAR file without running all test suites. If the option was omitted, then
> all test suites will run, thus increasing the duration of the building process,
> especially the functional test suite that runs much longer than the unit test
> suite.

The JAR file will be generated at [`./target`](./target) directory. To run it,
execute:

```shell
java -jar sitodo.jar
```

You can customise the configuration by providing an `application.properties`
file in the same directory as the executable JAR file. See the built-in
configuration in the [source code](./src/main/resources/application.properties).

## Running Example

See the running example based on the main branch at [Heroku](https://sitodo-example.herokuapp.com).

## License

This project is licensed under the terms of the [MIT license](./LICENSE). The
included [static site](./docs) is licensed under [Creative Commons Attribution-ShareAlike 4.0](./docs/LICENSE).
