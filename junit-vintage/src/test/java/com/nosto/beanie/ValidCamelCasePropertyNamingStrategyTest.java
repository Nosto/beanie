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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ValidCamelCasePropertyNamingStrategyTest extends AbstractJacksonBeanTest<ValidCamelCasePropertyNamingStrategyTest.TestBean> {
    public ValidCamelCasePropertyNamingStrategyTest() {
        super(TestBean.class);
    }

    @Override
    public BeanieProvider getBeanieProvider() {
        return new DefaultBeanieProvider();
    }

    public static class TestBean extends AbstractTestBean {

        private final String propertyA;
        private final String propertyB;
        private final String foo;

        @JsonCreator
        public TestBean(
                @JsonProperty("propertyA") String propertyA,
                @JsonProperty("someProperty") String propertyB,
                @JsonProperty("foo") String foo) {
            this.propertyA = propertyA;
            this.propertyB = propertyB;
            this.foo = foo;
        }

        @SuppressWarnings("unused")
        public String getPropertyA() {
            return propertyA;
        }

        @SuppressWarnings("unused")
        @JsonProperty("someProperty")
        public String getPropertyB() {
            return propertyB;
        }

        @SuppressWarnings("unused")
        public String getFoo() {
            return foo;
        }
    }
}
