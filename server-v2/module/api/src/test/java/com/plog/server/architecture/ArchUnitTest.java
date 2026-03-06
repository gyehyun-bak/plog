package com.plog.server.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

class ArchUnitTest {

    @Test
    void domain_should_not_depend_on_springframework() {
        JavaClasses importedClasses = new ClassFileImporter().importPackages("com.plog.server.domain");

        ArchRule rule = noClasses()
                .that()
                .resideInAPackage("..domain..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("org.springframework..")
                .allowEmptyShould(true);

        rule.check(importedClasses);
    }
}
