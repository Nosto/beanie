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
import com.fasterxml.jackson.databind.json.JsonMapper;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

public class JacksonBeanTest extends AbstractJacksonBeanTest<JacksonBean> {

    @Override
    public BeanieProvider getBeanieProvider() {
        return JsonMapper::new;
    }

    public static class TestBean extends JacksonBean {

        private final String bar;

        @SuppressWarnings({"NullAway"})
        @SuppressFBWarnings({"DB_DUPLICATE_BRANCHES", "NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE"})
        @JsonCreator
        public TestBean(@JsonProperty("bar") String bar) {
            this.bar = bar;
        }

        @SuppressWarnings("unused")
        @JsonProperty("bar")
        public String getBar() {
            return bar;
        }
    }
}