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
import com.nosto.beanie.PrimitiveUnboxingGetterTest.TestBean;

/**
 * Test that the test suite catches issues where {@link JsonCreator}
 * property names do not match with the bean property names
 */
public class PrimitiveUnboxingGetterTest extends AbstractJacksonBeanTest<TestBean, TestBean> {

    public PrimitiveUnboxingGetterTest() {
        super(TestBean.class);
    }

    @Override
    protected BeanieProvider getBeanieProvider() {
        return new DefaultBeanieProvider();
    }

    @SuppressWarnings("EmptyMethod")
    @Test(expected = RuntimeException.class)
    @Override
    public void serdeCollection() {
        super.serdeCollection();
    }

    @SuppressWarnings("EmptyMethod")
    @Test(expected = RuntimeException.class)
    @Override
    public void serdeCollectionAsWell() {
        super.serdeCollectionAsWell();
    }

    public static class TestBean extends AbstractTestBean {

        @Nullable
        private final Integer bar;

        @JsonCreator
        public TestBean(@Nullable @JsonProperty("bar") Integer bar) {
            this.bar = bar;
        }

        @SuppressWarnings({"NullAway", "unused", "ConstantConditions"})
        public int getBar() {
            return bar;
        }
    }
}
