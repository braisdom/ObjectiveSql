/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.braisdom.objsql.util;

import java.util.function.Consumer;
import java.util.function.Function;
/**
 * Generated from Function
 * Extends java.util.function.Function to allow for a checked exception.
 *
 * @param <T> some generic flag
 * @param <R> some generic flag
 * @param <E> The extension
 */
@FunctionalInterface
@SuppressWarnings({"WeakerAccess"})
public interface FunctionWithThrowable<T, R, E extends Throwable> extends Function<T, R> {

    /**
     * Utility method to mark lambdas of type FunctionWithThrowable
     *
     * @param functionwiththrowable The interface instance
     * @param <T> Generic that corresponds to the same generic on Function
     * @param <R> Generic that corresponds to the same generic on Function
     * @param <E> The type this interface is allowed to throw
     * @return the cast interface
     */
    static <T, R, E extends Throwable> FunctionWithThrowable<T, R, E> castFunctionWithThrowable(final FunctionWithThrowable<T, R, E> functionwiththrowable) {
        return functionwiththrowable;
    }

    /**
     * Utility method to unwrap lambdas of type Function and withUncheckedThrowable any Exception
     *
     * @param functionwiththrowable The interface instance
     * @param <T> Generic that corresponds to the same generic on Function
     * @param <R> Generic that corresponds to the same generic on Function
     * @param <E> The type this interface is allowed to throw
     * @throws E the original Exception from functionwiththrowable
     * @return the cast interface
     */
    static <T, R, E extends Throwable> Function<T, R> aFunctionThatUnsafelyThrowsUnchecked(final FunctionWithThrowable<T, R, E> functionwiththrowable) throws E {
        return functionwiththrowable.thatUnsafelyThrowsUnchecked();
    }

    /**
     * Utility method to convert FunctionWithThrowable
     * @param function The interface instance
     * @param <T> Generic that corresponds to the same generic on Function
     * @param <R> Generic that corresponds to the same generic on Function
     * @param <E> The type this interface is allowed to throw
     * @return the cast interface
     */
    static <T, R, E extends Throwable> FunctionWithThrowable<T, R, E> asFunctionWithThrowable(final Function<T, R> function) {
        return function::apply;
    }

    /**
     * Overridden method of FunctionWithThrowable that will call applyWithThrowable, but catching any exceptions.
     *
     * @param v1 parameter to overridden method
     * @return the value
     */
    @Override
    default R apply(final T v1) {
        try {
            return applyWithThrowable(v1);
        } catch (final RuntimeException | Error exception) {
            throw exception;
        } catch (final Throwable throwable) {
            throw new SuppressedException(throwable);
        }
    }

    /**
     * Functional method that will throw exceptions.
     *
     * @param v1 parameter to overridden method
     * @return the value
     * @throws E some exception
     */
    R applyWithThrowable(final T v1) throws E;


    /**
     * @return An interface that will wrap the result in an optional, and return an empty optional when an exception occurs.
     */
    default Function<T, java.util.Optional<R>>    thatReturnsOptional() {
        return (final T v1)     -> {
            try {
                return java.util.Optional.ofNullable(applyWithThrowable(v1));
            } catch(Throwable throwable) {
                return java.util.Optional.empty();
            }
        };
    }


    /**
     * @param defaultReturnValue A value to return if any throwable is caught.
     * @return An interface that returns a default value if any exception occurs.
     */
    default Function<T, R> thatReturnsOnCatch(final R defaultReturnValue) {
        return (final T v1) -> {
            try {
                return applyWithThrowable(v1);
            } catch(final Throwable throwable) {
                return defaultReturnValue;
            }
        };
    }


    /**
     * @throws E if an exception E has been thrown, it is rethrown by this method
     * @return An interface that is only returned if no exception has been thrown.
     */
    default Function<T, R> thatUnsafelyThrowsUnchecked() throws E {
        return (final T v1) -> {
            try {
                return applyWithThrowable(v1);
            } catch(final Throwable throwable) {
                SuppressedException.throwUnsafelyAsUnchecked(throwable);
                return null;
            }
        };
    }


//    /**
//     * @param logger The logger to log exceptions on
//     * @param message A message to use for logging exceptions
//     * @return An interface that will log all exceptions to given logger
//     */
//    @SuppressWarnings("Duplicates")
//    default FunctionWithThrowable<T, R, E> withLogging(final Logger logger, final String message) {
//        return (final T v1) -> {
//            try {
//                return applyWithThrowable(v1);
//            } catch (final Throwable throwable) {
//                logger.error(message, v1, throwable);
//                throw throwable;
//            }
//        };
//    }
//
//
//    /**
//     * Will log WARNING level exceptions on logger if they occur within the interface
//     * @param logger The logger instance to log exceptions on
//     * @return An interface that will log exceptions on given logger
//     */
//    default FunctionWithThrowable<T, R, E> withLogging(final Logger logger) {
//        return withLogging(logger, "Exception in FunctionWithThrowable with the argument [{}]");
//    }
//
//
//    /**
//     * Will log WARNING level exceptions on logger if they occur within the interface
//     * @return An interface that will log exceptions on global logger
//     */
//    default FunctionWithThrowable<T, R, E> withLogging() {
//        return withLogging(LoggerFactory.getLogger(getClass()));
//    }



    /**
     * @param consumer An exception consumer.
     * @return An interface that will log all exceptions to given logger
     */
    @SuppressWarnings("Duplicates")
    default FunctionWithThrowable<T, R, E> onException(final Consumer<Throwable> consumer) {
        return (final T v1) -> {
            try {
                return applyWithThrowable(v1);
            } catch (final Throwable throwable) {
                consumer.accept(throwable);
                throw throwable;
            }
        };
    }


    /**
     * @param consumer An exception consumer.
     * @return An interface that will log all exceptions to given logger
     */
    @SuppressWarnings("Duplicates")
    default FunctionWithThrowable<T, R, E> onException(final java.util.function.BiConsumer<Throwable, Object[]> consumer) {
        return (final T v1) -> {
            try {
                return applyWithThrowable(v1);
            } catch (final Throwable throwable) {
                consumer.accept(throwable, new Object[]{v1});
                throw throwable;
            }
        };
    }
}
