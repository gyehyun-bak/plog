package com.plog.server.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.plog.server")
public class SecurityArchitectureTest {

    @ArchTest
    static final ArchRule security_should_not_depend_on_api_or_infrastructure = noClasses()
            .that()
            .resideInAPackage("..security..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..api..", "..infrastructure..");
}
