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

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Test that the test suite catches issues where getter returns different value than was given in constructor.
 */
public class InconsistentPropertyNamesTest extends AbstractJacksonBeanTest<InconsistentPropertyNamesTest.TestBean> {

    public InconsistentPropertyNamesTest() {
        super(TestBean.class);
    }

    @SuppressWarnings("EmptyMethod")
    @Test(expected = AssertionError.class)
    @Override
    public void serde() {
        super.serde();
    }

    @Override
    public BeanieProvider getBeanieProvider() {
        return new DefaultBeanieProvider();
    }

    public static class TestBean extends AbstractTestBean {
        private final String foo;
        @SuppressWarnings({"FieldCanBeLocal", "UnusedVariable", "unused"})
        private final String bar;

        @JsonCreator
        public TestBean(@JsonProperty("foo") String foo, @JsonProperty("bar") String bar) {
            this.foo = foo;
            this.bar = bar;
        }

        @SuppressWarnings("unused")
        public String getFoo() {
            return foo;
        }

        @SuppressWarnings("unused")
        public String getBar() {
            return foo; // "accidentally" return the wrong value
        }
    }
}
