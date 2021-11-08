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
 * @author mridang
 */
public class MixedPropertyNamingStrategyTest extends AbstractJacksonBeanTest<MixedPropertyNamingStrategyTest.TestBean> {

    public MixedPropertyNamingStrategyTest() {
        super(TestBean.class);
    }

    @SuppressWarnings("EmptyMethod")
    @Test(expected = AssertionError.class)
    @Override
    public void namingStrategy() {
        super.namingStrategy();
    }

    @Override
    public BeanieProvider getBeanieProvider() {
        return new DefaultBeanieProvider();
    }

    @SuppressWarnings("QuestionableName")
    public static class TestBean extends AbstractTestBean {

        private final String foo;
        private final String bar;

        @JsonCreator
        public TestBean(@JsonProperty("fo_o") String foo, @JsonProperty("b_Ar") String bar) {
            this.foo = foo;
            this.bar = bar;
        }

        @JsonProperty("fo_o")
        public String getFo() {
            return foo;
        }

        @JsonProperty("b_Ar")
        public String getBar() {
            return bar;
        }
    }
}
