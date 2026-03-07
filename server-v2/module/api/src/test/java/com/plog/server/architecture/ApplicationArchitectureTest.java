package com.plog.server.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.plog.server")
public class ApplicationArchitectureTest {

    @ArchTest
    static final ArchRule application_should_not_depend_on_other_layers_except_domain = noClasses()
            .that()
            .resideInAPackage("..application..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..api..", "..infrastructure..", "..security..");
}
