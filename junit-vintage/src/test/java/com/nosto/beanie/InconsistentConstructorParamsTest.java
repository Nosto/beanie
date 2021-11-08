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
 * Test that the test suite catches issues where {@link JsonCreator}
 * property names do not match with the bean property names
 *
 * @author ollik1
 */
public class InconsistentConstructorParamsTest extends AbstractJacksonBeanTest<InconsistentConstructorParamsTest.TestBean> {

    public InconsistentConstructorParamsTest() {
        super(TestBean.class);
    }

    @SuppressWarnings("EmptyMethod")
    @Test(expected = AssertionError.class)
    @Override
    public void constructorParameters() {
        super.constructorParameters();
    }

    @Override
    public BeanieProvider getBeanieProvider() {
        return new DefaultBeanieProvider();
    }

    public static class TestBean extends AbstractTestBean {
        private final String foo;
        private final String bar;

        @JsonCreator
        public TestBean(@JsonProperty("foo") String foo, @JsonProperty("baz") String bar) {
            this.foo = foo;
            this.bar = bar;
        }

        @SuppressWarnings("unused")
        public String getFoo() {
            return foo;
        }

        @SuppressWarnings("unused")
        public String getBar() {
            return bar;
        }
    }
}
