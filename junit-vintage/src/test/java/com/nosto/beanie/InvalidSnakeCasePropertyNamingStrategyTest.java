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
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

public class InvalidSnakeCasePropertyNamingStrategyTest extends AbstractJacksonBeanTest<InvalidSnakeCasePropertyNamingStrategyTest.TestBean> {

    public InvalidSnakeCasePropertyNamingStrategyTest() {
        super(TestBean.class);
    }

    @Test(expected = AssertionError.class)
    @Override
    public void namingStrategy() {
        super.namingStrategy();
    }

    @Override
    public BeanieProvider getBeanieProvider() {
        return new DefaultBeanieProvider();
    }

    @SuppressWarnings("deprecation") // it's the point of the test, whether it's backwards compatible
    @JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
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
