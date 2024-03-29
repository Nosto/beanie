/*
 *  Copyright (c) 2021 Nosto Solutions Ltd All Rights Reserved.
 *
 *  This software is the confidential and proprietary information of
 *  Nosto Solutions Ltd ("Confidential Information"). You shall not
 *  disclose such Confidential Information and shall use it only in
 *  accordance with the terms of the agreement you entered into with
 *  Nosto Solutions Ltd.
 */
package com.nosto.beanie.jeasy;

import java.lang.reflect.Field;
import java.util.Collection;

import javax.annotation.Nullable;

import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.annotation.Exclude;
import org.jeasy.random.annotation.Priority;
import org.jeasy.random.api.Randomizer;
import org.jeasy.random.api.RandomizerRegistry;
import org.jeasy.random.randomizers.AbstractRandomizer;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

@Priority(2)
public class ForceAllNonPrimitivesAsNullRandomizerRegistry implements RandomizerRegistry {

    @Nullable
    @SuppressFBWarnings("URF_UNREAD_FIELD")
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private EasyRandomParameters easyRandomParameters;

    @Override
    @SuppressFBWarnings(value = {"EI_EXPOSE_REP", "EI_EXPOSE_REP2"})
    public void init(EasyRandomParameters easyRandomParameters) {
        this.easyRandomParameters = easyRandomParameters;
    }

    @Nullable
    @SuppressWarnings({"ReturnOfInnerClass", "UseOfObsoleteDateTimeApi"})
    @Override
    public Randomizer<?> getRandomizer(Field field) {
        if (field.getType().isPrimitive() || field.getType().isAssignableFrom(java.util.Date.class) || field.isAnnotationPresent(Exclude.class)) {
            return null;
        } else {
            return new AbstractRandomizer<Collection<?>>() {
                @Nullable
                @Override
                public Collection<?> getRandomValue() {
                    return null;
                }
            };
        }
    }

    @Nullable
    @Override
    public Randomizer<?> getRandomizer(Class<?> aClass) {
        return null;
    }
}
