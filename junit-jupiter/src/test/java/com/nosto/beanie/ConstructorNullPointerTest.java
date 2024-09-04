/*
 *  Copyright (c) 2021 Nosto Solutions Ltd All Rights Reserved.
 *
 *  This software is the confidential and proprietary information of
 *  Nosto Solutions Ltd ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the agreement you entered into with
 *  Nosto Solutions Ltd.
 */
package com.nosto.beanie;

import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Test that the test suite catches issues where {@link JsonCreator}
 * property names do not match with the bean property names
 *
 * @author ollik1
 */
public class ConstructorNullPointerTest implements JupiterBeanieTest<AbstractTestBean>
        , ConstructorParametersTest<AbstractTestBean> {

    @ParameterizedTest
    @MethodSource
    public void constructorParameters(Class<AbstractTestBean> concreteClass) {
        testConstructorParameters(concreteClass);
    }

    @SuppressWarnings("unused")
    public static Stream<Class<? extends AbstractTestBean>> constructorParameters() {
        return Stream.of(AbstractTestBean.class);
    }

    public static class TestBean extends AbstractTestBean {

        @Nullable
        private final String bar;

        @SuppressWarnings({"NullAway", "ConditionalExpressionWithIdenticalBranches", "ConstantConditions"})
        @SuppressFBWarnings({"DB_DUPLICATE_BRANCHES", "NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE"})
        @JsonCreator
        public TestBean(@Nullable @JsonProperty("bar") String bar) {
            this.bar = bar.isEmpty() ? bar : bar;
        }

        @Nullable
        @SuppressWarnings("unused")
        public String getBar() {
            return bar;
        }
    }
}
