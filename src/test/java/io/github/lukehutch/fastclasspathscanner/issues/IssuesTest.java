package io.github.lukehutch.fastclasspathscanner.issues;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import io.github.lukehutch.fastclasspathscanner.FastClasspathScanner;
import io.github.lukehutch.fastclasspathscanner.test.external.ExternalSuperclass;
import io.github.lukehutch.fastclasspathscanner.test.internal.InternalExtendsExternal;
import io.github.lukehutch.fastclasspathscanner.test.whitelisted.Impl1;
import io.github.lukehutch.fastclasspathscanner.test.whitelisted.Impl1Sub;

public class IssuesTest {
    @Test
    public void testIssuesScan() {
        new FastClasspathScanner().whitelistPackages("io.github.lukehutch.fastclasspathscanner.issues").scan();
    }

    @Test
    public void issue70() {
        assertThat(new FastClasspathScanner().whitelistPackages(Impl1.class.getPackage().getName()).scan()
                .getSubclasses(Object.class.getName()).getClassNames()).contains(Impl1.class.getName());
    }

    @Test
    public void issue70EnableExternalClasses() {
        assertThat(new FastClasspathScanner().whitelistPackages(Impl1.class.getPackage().getName())
                .enableExternalClasses().scan().getSubclasses(Object.class.getName()).getClassNames())
                        .contains(Impl1.class.getName());
        assertThat(new FastClasspathScanner().whitelistPackages(Impl1.class.getPackage().getName())
                .enableExternalClasses().scan().getSuperclasses(Impl1Sub.class.getName()).getClassNames())
                        .containsOnly(Impl1.class.getName());
    }

    @Test
    public void extendsExternal() {
        assertThat(
                new FastClasspathScanner().whitelistPackages(InternalExtendsExternal.class.getPackage().getName())
                        .scan().getSuperclasses(InternalExtendsExternal.class.getName()).getClassNames()).isEmpty();
    }

    @Test
    public void nonStrictExtendsExternal() {
        assertThat(new FastClasspathScanner()
                .whitelistPackages(InternalExtendsExternal.class.getPackage().getName()).enableExternalClasses()
                .scan().getSuperclasses(InternalExtendsExternal.class.getName()).getClassNames())
                        .containsOnly(ExternalSuperclass.class.getName());
    }

    @Test
    public void extendsExternalSubclass() {
        assertThat(
                new FastClasspathScanner().whitelistPackages(InternalExtendsExternal.class.getPackage().getName())
                        .scan().getSubclasses(ExternalSuperclass.class.getName()).getClassNames())
                                .containsOnly(InternalExtendsExternal.class.getName());
    }

    @Test
    public void nonStrictExtendsExternalSubclass() {
        assertThat(new FastClasspathScanner()
                .whitelistPackages(InternalExtendsExternal.class.getPackage().getName()).enableExternalClasses()
                .scan().getSubclasses(ExternalSuperclass.class.getName()).getClassNames())
                        .containsOnly(InternalExtendsExternal.class.getName());
    }
}
