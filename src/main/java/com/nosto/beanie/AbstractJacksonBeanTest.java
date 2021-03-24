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
import java.lang.reflect.Modifier;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nullable;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.nosto.beanie.jeasy.ExcludedMapAndCollectionsAsEmptyRandomizerRegistry;
import com.nosto.beanie.jeasy.ForceAllNonPrimitivesAsNullRandomizerRegistry;

@SuppressWarnings("UseOfObsoleteDateTimeApi")
public abstract class AbstractJacksonBeanTest<T, U extends T> {

    private static final Random RANDOM = new SecureRandom();
    private static final Supplier<EasyRandomParameters> EASY_RANDOM_PARAMETERS;

    static {
        EASY_RANDOM_PARAMETERS = () -> new EasyRandomParameters()
                .collectionSizeRange(1, 3)
                .randomize(OffsetDateTime.class, () -> OffsetDateTime
                        .ofInstant(Instant.ofEpochSecond(RANDOM.nextLong() % Instant.now().getEpochSecond()), ZoneOffset.UTC))
                .randomize(java.util.Date.class, () -> new java.util.Date(RANDOM.nextLong() % Instant.now().getEpochSecond()));
    }

    private final Class<? extends T> deserClass;
    private final Class<? extends U> concreteClass;

    @SuppressWarnings({"ConstructorNotProtectedInAbstractClass", "JUnitTestCaseWithNonTrivialConstructors"})
    public AbstractJacksonBeanTest(Class<? extends U> clazz) {
        this(clazz, clazz);
    }

    @SuppressWarnings({"ConstructorNotProtectedInAbstractClass", "JUnitTestCaseWithNonTrivialConstructors"})
    public AbstractJacksonBeanTest(Class<? extends T> deserClass, Class<? extends U> concreteClass) {
        this.deserClass = deserClass;
        this.concreteClass = concreteClass;
    }

    /**
     * Assert all properties have an equivalent
     * {@link com.fasterxml.jackson.annotation.JsonCreator}
     * parameter
     */
    @Test
    public void constructorParameters() {
        getDescription().findProperties()
                .stream()
                .filter(property -> Optional.ofNullable(property.getAccessor())
                        .map(prop -> !prop.hasAnnotation(JacksonOnlySerialize.class))
                        .orElse(false))
                .forEach(property -> Assert.assertTrue(String.format("Property %s of %s lacks constructor argument",
                        property.getName(), concreteClass.getName()), property.hasConstructorParameter()));
    }

    /**
     * Generate multiple random objects of the given class
     * and assert serializing and deserializing back returns
     * the original object.
     */
    @Test
    public void serde() {
        EasyRandomParameters randomParameters = EASY_RANDOM_PARAMETERS.get();
        randomParameters.randomizerRegistry(new ExcludedMapAndCollectionsAsEmptyRandomizerRegistry());
        @SuppressWarnings("UnsecureRandomNumberGeneration")
        EasyRandom easyRandom = new EasyRandom(randomParameters);
        IntStream.range(0, 10)
                .forEach(ignored -> {
                    T bean = easyRandom.nextObject(concreteClass);
                    String json = getBeanieProvider().toPrettyJSON(bean);
                    try {
                        T fromJson = getBeanieProvider().fromJSON(json, deserClass);
                        Assert.assertEquals(json, bean, fromJson);
                    } catch (IOException e) {
                        throw new RuntimeException("Could not deserialize + " + json, e);
                    }
                });
    }

    /**
     * Test that all properties of a bean are named with a consistent naming strategy.
     */
    @Test
    public void namingStrategy() {
        Map<PropertyNamingStrategy, List<String>> cases = getDescription().findProperties()
                .stream()
                .map(BeanPropertyDefinition::getName)
                .collect(Collectors.groupingBy(name -> {
                    if (name.contains("_") && !name.toLowerCase().equals(name)) {
                        return PropertyNamingStrategies.SNAKE_CASE;
                    } else {
                        return PropertyNamingStrategies.LOWER_CAMEL_CASE;
                    }
                }));
        Assert.assertEquals(cases.toString(), 1, cases.size());
    }

    /**
     * Generate multiple random objects of the given class
     * and assert serializing and deserializing back returns
     * the original object.
     */
    @Test
    public void serdeCollection() {
        EasyRandomParameters randomParameters = EASY_RANDOM_PARAMETERS.get();
        randomParameters.randomizerRegistry(new ForceAllNonPrimitivesAsNullRandomizerRegistry());
        @SuppressWarnings("UnsecureRandomNumberGeneration")
        EasyRandom easyRandom = new EasyRandom(randomParameters);
        IntStream.range(0, 10)
                .forEach(ignored -> {
                    T bean = easyRandom.nextObject(concreteClass);
                    String json = getBeanieProvider().toPrettyJSON(bean);
                    try {
                        T actualObj = getBeanieProvider().fromJSON(json, deserClass);
                        T expectedObj = getBeanieProvider().fromJSON(json, deserClass);
                        Assert.assertEquals(json, expectedObj, actualObj);
                    } catch (IOException e) {
                        throw new RuntimeException("Could not deserialize + " + json, e);
                    }
                });
    }

    /**
     * Generate multiple random objects of the given class
     * and assert serializing and deserializing back returns
     * the original object.
     */
    @Test
    public void serdeCollectionAsWell() {
        EasyRandomParameters randomParameters = EASY_RANDOM_PARAMETERS.get();
        randomParameters.randomizerRegistry(new ForceAllNonPrimitivesAsNullRandomizerRegistry());
        @SuppressWarnings("UnsecureRandomNumberGeneration")
        EasyRandom easyRandom = new EasyRandom(randomParameters);
        IntStream.range(0, 10)
                .forEach(ignored -> {
                    T bean = easyRandom.nextObject(concreteClass);
                    String json = getBeanieProvider().toPrettyJSON(bean);
                    try {
                        T actualObj = getBeanieProvider().fromJSON(json, deserClass);
                        List<String> properties = getDescription().findProperties()
                                .stream()
                                .filter(property -> property.getPrimaryType().isCollectionLikeType())
                                .filter(property -> property.getAccessor().getValue(actualObj) == null)
                                .filter(property -> !property.getAccessor().hasAnnotation(Nullable.class))
                                .map(BeanPropertyDefinition::getName)
                                .collect(Collectors.toList());
                        Assert.assertTrue("Following properties return null for collection types " + properties, properties.isEmpty());
                    } catch (IOException e) {
                        throw new RuntimeException("Could not deserialize + " + json, e);
                    }
                });
    }

    /**
     * Check that there is no setters
     */
    @Test
    public void noSetters() {
        List<BeanPropertyDefinition> properties = getDescription().findProperties()
                .stream()
                .filter(BeanPropertyDefinition::hasSetter)
                .collect(Collectors.toList());
        Assert.assertTrue(String.format("Properties should be immutable but the following properties have setters: %s",
                properties
                        .stream()
                        .map(BeanPropertyDefinition::getName)
                        .collect(Collectors.joining(","))), properties.isEmpty());
    }

    /**
     * All fields should be final
     */
    @Test
    public void finalProperties() {
        List<AnnotatedField> fields = getDescription().findProperties()
                .stream()
                .flatMap(x -> Optional.ofNullable(x.getField())
                        .stream())
                .filter(x -> !Modifier.isFinal(x.getModifiers()))
                .collect(Collectors.toList());
        Assert.assertTrue(String.format("The following fields are not final: %s",
                fields.stream()
                        .map(AnnotatedField::getName)
                        .collect(Collectors.joining(", "))), fields.isEmpty());
    }

    private BeanDescription getDescription() {
        JavaType javaType = getMapper().getTypeFactory()
                .constructType(concreteClass);
        return getMapper()
                .getSerializationConfig()
                .introspect(javaType);
    }

    protected ObjectMapper getMapper() {
        return getBeanieProvider().getMapper();
    }

    @SuppressWarnings("unused")
    protected Class<? extends T> getDeserClass() {
        return deserClass;
    }

    @SuppressWarnings("unused")
    protected Class<? extends U> getConcreteClass() {
        return concreteClass;
    }

    protected abstract BeanieProvider getBeanieProvider();
}
