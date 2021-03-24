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

import javax.annotation.Nullable;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nosto.beanie.ConstructorNullPointerTest.TestBean;

/**
 * Test that the test suite catches issues where {@link JsonCreator}
 * property names do not match with the bean property names
 *
 * @author ollik1
 */
public class ConstructorNullPointerTest extends AbstractJacksonBeanTest<TestBean, TestBean> {

    public ConstructorNullPointerTest() {
        super(TestBean.class);
    }

    @Test(expected = RuntimeException.class)
    @Override
    public void serdeCollection() {
        super.serdeCollection();
    }

    @Test(expected = RuntimeException.class)
    @Override
    public void serdeCollectionAsWell() {
        super.serdeCollectionAsWell();
    }

    @Override
    protected BeanieProvider getBeanieProvider() {
        return new DefaultBeanieProvider();
    }

    public static class TestBean extends com.nosto.beanie.AbstractTestBean {

        @Nullable
        private final String bar;

        @SuppressWarnings({"ConditionalExpressionWithIdenticalBranches"})
        @JsonCreator
        public TestBean(@Nullable @JsonProperty("bar") String bar) {
            this.bar = bar.isEmpty() ? bar : bar;
        }

        @SuppressWarnings("unused")
        public String getBar() {
            return bar;
        }
    }
}
