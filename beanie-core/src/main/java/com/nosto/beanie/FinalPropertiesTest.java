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

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.introspect.AnnotatedField;

public interface FinalPropertiesTest<T> extends BeanieTest<T> {

    /**
     * All fields should be final
     */
    default void testFinalProperties(Class<? extends T> concreteClass) {
        List<AnnotatedField> fields = getDescription(concreteClass).findProperties()
                .stream()
                .flatMap(x -> Optional.ofNullable(x.getField())
                        .stream())
                .filter(x -> !Modifier.isFinal(x.getModifiers()))
                .collect(Collectors.toList());
        assertTrue(fields.isEmpty(), String.format("The following fields are not final: %s",
                fields.stream()
                        .map(AnnotatedField::getName)
                        .collect(Collectors.joining(", "))));
    }
}
