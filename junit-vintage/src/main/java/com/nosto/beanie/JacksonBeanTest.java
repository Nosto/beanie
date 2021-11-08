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


import static org.junit.Assert.assertFalse;

import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

@RunWith(Parameterized.class)
public abstract class JacksonBeanTest extends AbstractJacksonBeanTest<JacksonBean> {

    private static final Logger logger = LogManager.getLogger(JacksonBeanTest.class);
    private static final Classes CLASSES;

    static {
        Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forPackage("com.nosto")));
        CLASSES = reflections::getSubTypesOf;
    }

    public JacksonBeanTest(Class<? extends JacksonBean> concreteClass, @SuppressWarnings("unused") String testName) {
        super(concreteClass);
    }

    @Parameterized.Parameters(name = "{1}")
    public static Collection<Object[]> data() {
        Set<Class<? extends JacksonBean>> jacksonBeans = CLASSES.getSubTypesOf(JacksonBean.class)
                .stream()
                .filter(c -> !Modifier.isAbstract(c.getModifiers()))
                .collect(Collectors.toSet());
        logger.info("Found beans {}", jacksonBeans);
        assertFalse("No beans found", jacksonBeans.isEmpty());
        return jacksonBeans.stream()
                .map(c -> new Object[]{findRootType(c).orElse(c), c, c.getName()})
                .collect(Collectors.toList());
    }

    @SuppressWarnings("unused")
    private static Optional<Class<?>> findRootType(Class<?> clazz) {
        return Optional.empty();
    }

    private interface Classes {
        <T> Set<Class<? extends T>> getSubTypesOf(Class<T> type);
    }
}
