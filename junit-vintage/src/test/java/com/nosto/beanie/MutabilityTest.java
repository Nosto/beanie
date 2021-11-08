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
 * @author ollik1
 */
public class MutabilityTest extends AbstractJacksonBeanTest<MutabilityTest.MutableBean> {

    public MutabilityTest() {
        super(MutableBean.class);
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    @Test(expected = AssertionError.class)
    public void noSetters() {
        super.noSetters();
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    @Test(expected = AssertionError.class)
    public void finalProperties() {
        super.finalProperties();
    }

    @Override
    public BeanieProvider getBeanieProvider() {
        return new DefaultBeanieProvider();
    }

    public static class MutableBean extends AbstractTestBean {
        private final int x;
        private int y;

        @JsonCreator
        public MutableBean(@JsonProperty("x") int x, @JsonProperty("y") int y) {
            this.x = x;
            this.y = y;
        }

        @SuppressWarnings("unused")
        public int getX() {
            return x;
        }

        @SuppressWarnings("unused")
        public int getY() {
            return y;
        }

        @SuppressWarnings("unused")
        public void setY(int y) {
            this.y = y;
        }
    }
}
