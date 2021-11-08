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

import org.jeasy.random.api.RandomizerRegistry;
import org.jeasy.random.randomizers.registry.CustomRandomizerRegistry;
import org.junit.Assert;

@SuppressWarnings("unused")
public interface VintageBeanieTest<T> extends BeanieTest<T> {

    @Override
    default RandomizerRegistry getRandomizerRegistry() {
        return new CustomRandomizerRegistry();
    }

    @Override
    default void assertEquals(Object expected, Object actual) {
        Assert.assertEquals(expected, actual);
    }

    @Override
    default void assertTrue(boolean condition, String message) {
        Assert.assertTrue(message, condition);
    }

    @Override
    default void assertEquals(Object expected, Object actual, String message) {
        Assert.assertEquals(message, expected, actual);
    }
}
