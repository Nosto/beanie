/*
 *  Copyright (c) 2024 Nosto Solutions Ltd All Rights Reserved.
 *
 *  This software is the confidential and proprietary information of
 *  Nosto Solutions Ltd ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the agreement you entered into with
 *  Nosto Solutions Ltd.
 */
package com.nosto.beanie;

import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * @author mridang
 */
public final class DefaultBeanieProvider implements BeanieProvider {

    private final ObjectMapper objectMapper;

    public DefaultBeanieProvider() {
        this(objectMapper -> {
            //
        });
    }

    public DefaultBeanieProvider(Consumer<ObjectMapper> mapperConsumer) {
        ObjectMapper objectMapper = new ObjectMapper();
        mapperConsumer.accept(objectMapper);
        this.objectMapper = objectMapper;
    }


    @Override
    public ObjectMapper getMapper() {
        return objectMapper;
    }
}
