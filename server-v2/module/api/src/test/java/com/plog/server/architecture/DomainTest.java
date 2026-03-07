package com.plog.server.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class DomainTest {

    @Test
    void domain_should_be_independent() {

        JavaClasses classes = new ClassFileImporter().importPackages("com.plog.server");

        noClasses()
                .that()
                .resideInAPackage("..domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("..api..", "..application..", "..infrastructure..", "..security..")
                .allowEmptyShould(true)
                .check(classes);
    }

    @Test
    void domain_should_not_depend_on_frameworks() {

        JavaClasses classes = new ClassFileImporter().importPackages("com.plog.server");

        noClasses()
                .that()
                .resideInAPackage("..domain..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("org.springframework..", "jakarta.persistence..")
                .allowEmptyShould(true)
                .check(classes);
    }
}
