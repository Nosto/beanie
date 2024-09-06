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
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public class InvalidSnakeCasePropertyNamingStrategiesTest extends AbstractJacksonBeanTest<InvalidSnakeCasePropertyNamingStrategiesTest.TestBean> {

    public InvalidSnakeCasePropertyNamingStrategiesTest() {
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

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TestBean extends AbstractTestBean {

        private final String propertyA;
        private final String propertyB;

        @JsonCreator
        public TestBean(@JsonProperty("propertyA") String propertyA, @JsonProperty("propertyB") String propertyB) {
            this.propertyA = propertyA;
            this.propertyB = propertyB;
        }

        @SuppressWarnings("unused")
        public String getPropertyA() {
            return propertyA;
        }

        @SuppressWarnings("unused")
        public String getPropertyB() {
            return propertyB;
        }
    }
}
