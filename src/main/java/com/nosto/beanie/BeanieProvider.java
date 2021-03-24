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

import java.io.IOException;

import javax.annotation.Nullable;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public interface BeanieProvider {

    ObjectMapper getMapper();

    default String toPrettyJSON(@Nullable Object out) {
        try {
            return getMapper().writerWithDefaultPrettyPrinter().writeValueAsString(out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    default <T> T fromJSON(String json, Class<T> type) throws IOException {
        return getMapper().readValue(json, type);
    }

    default BeanDescription getBeanDescription(Class<?> clazz) {
        JavaType javaType = getMapper()
                .getTypeFactory()
                .constructType(clazz);
        return getMapper().getSerializationConfig().introspect(javaType);
    }
}
