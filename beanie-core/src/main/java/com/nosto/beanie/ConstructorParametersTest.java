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

import java.util.Optional;

public interface ConstructorParametersTest<T> extends BeanieTest<T> {

    default void testConstructorParameters(Class<? extends T> concreteClass) {
        getDescription(concreteClass).findProperties()
                .stream()
                .filter(property -> Optional.ofNullable(property.getAccessor())
                        .map(prop -> !prop.hasAnnotation(JacksonOnlySerialize.class))
                        .orElse(false))
                .forEach(property -> assertTrue(property.hasConstructorParameter(), String.format("Property %s of %s lacks constructor argument",
                        property.getName(), concreteClass.getName())));
    }
}
