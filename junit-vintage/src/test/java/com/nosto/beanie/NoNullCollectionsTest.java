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

import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Test that the test suite catches issues where {@link JsonCreator} property names do not match with the bean property names
 */
public class NoNullCollectionsTest extends AbstractJacksonBeanTest<NoNullCollectionsTest.TestBean> {

    public NoNullCollectionsTest() {
        super(NoNullCollectionsTest.TestBean.class);
    }

    @SuppressWarnings("EmptyMethod")
    @Test(expected = AssertionError.class)
    @Override
    public void serdeCollectionAsWell() {
        super.serdeCollectionAsWell();
    }

    @Override
    public BeanieProvider getBeanieProvider() {
        return new DefaultBeanieProvider();
    }

    public static class TestBean extends AbstractTestBean {
        private final List<String> bars;

        @JsonCreator
        @SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
        public TestBean(@JsonProperty("bars") List<String> bars) {
            this.bars = bars;
        }

        @SuppressWarnings("unused")
        @SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
        public List<String> getBars() {
            return bars;
        }
    }
}
