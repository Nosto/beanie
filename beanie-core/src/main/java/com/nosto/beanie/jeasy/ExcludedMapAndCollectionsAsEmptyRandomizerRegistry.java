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
import java.util.Map;

import javax.annotation.Nullable;

import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.annotation.Exclude;
import org.jeasy.random.annotation.Priority;
import org.jeasy.random.api.Randomizer;
import org.jeasy.random.api.RandomizerRegistry;
import org.jeasy.random.randomizers.AbstractRandomizer;
import org.jeasy.random.util.ReflectionUtils;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * A custom randomizer registry that forces all fields annotated with @Exclude
 * and of type {@link Collection} or {@link Map} to be just null.
 *
 * @author mridang
 */
@Priority(2)
public class ExcludedMapAndCollectionsAsEmptyRandomizerRegistry implements RandomizerRegistry {

    @SuppressFBWarnings("URF_UNREAD_FIELD")
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private EasyRandomParameters easyRandomParameters;

    @Override
    public void init(EasyRandomParameters easyRandomParameters) {
        this.easyRandomParameters = easyRandomParameters;
    }

    @Nullable
    @SuppressWarnings({"ReturnOfInnerClass"})
    @Override
    public Randomizer<?> getRandomizer(Field field) {
        if (ReflectionUtils.isCollectionType(field.getType()) && field.isAnnotationPresent(Exclude.class)) {
            return new AbstractRandomizer<Collection<?>>() {
                @Override
                public Collection<?> getRandomValue() {
                    return ReflectionUtils.getEmptyImplementationForCollectionInterface(field.getType());
                }
            };
        } else if (ReflectionUtils.isMapType(field.getType()) && field.isAnnotationPresent(Exclude.class)) {
            return new AbstractRandomizer<Map<?, ?>>() {
                @Override
                public Map<?, ?> getRandomValue() {
                    return ReflectionUtils.getEmptyImplementationForMapInterface(field.getType());
                }
            };
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public Randomizer<?> getRandomizer(Class<?> aClass) {
        return null;
    }
}
