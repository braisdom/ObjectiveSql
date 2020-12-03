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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

/**
 * ArrayUtils contains some methods that you can call to find out
 * the most efficient increments by which to grow arrays.
 */
public final class ArrayUtil {
    private static final int CACHE_SIZE = 73;
    private static Object[] sCache = new Object[CACHE_SIZE];

    private ArrayUtil() { /* cannot be instantiated */ }

    /**
     * Checks if the beginnings of two byte arrays are equal.
     *
     * @param array1 the first byte array
     * @param array2 the second byte array
     * @param length the number of bytes to check
     * @return true if they're equal, false otherwise
     */
    public static boolean equals(byte[] array1, byte[] array2, int length) {
        if (length < 0) {
            throw new IllegalArgumentException();
        }

        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null || array1.length < length || array2.length < length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if given array is null or has zero elements.
     */
    public static <T> boolean isEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Checks if given array is null or has zero elements.
     */
    public static boolean isEmpty(int[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Checks if given array is null or has zero elements.
     */
    public static boolean isEmpty(long[] array) {
        return array == null || array.length == 0;
    }

    /**
     * Checks that value is present as at least one of the elements of the array.
     * @param array the array to check in
     * @param value the value to check for
     * @return true if the value is present in the array
     */
    public static <T> boolean contains(T[] array, T value) {
        return indexOf(array, value) != -1;
    }

    /**
     * Return first index of {@code value} in {@code array}, or {@code -1} if
     * not found.
     */
    public static <T> int indexOf(T[] array, T value) {
        if (array == null) {
            return -1;
        }
        for (int i = 0; i < array.length; i++) {
            if (Objects.equals(array[i], value)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Test if all {@code check} items are contained in {@code array}.
     */
    public static <T> boolean containsAll(T[] array, T[] check) {
        if (check == null) {
            return true;
        }
        for (T checkItem : check) {
            if (!contains(array, checkItem)) {
                return false;
            }
        }
        return true;
    }

    public static boolean contains(int[] array, int value) {
        if (array == null) {
            return false;
        }
        for (int element : array) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(long[] array, long value) {
        if (array == null) {
            return false;
        }
        for (long element : array) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }

    public static long total(long[] array) {
        long total = 0;
        for (long value : array) {
            total += value;
        }
        return total;
    }

    @SuppressWarnings("unchecked")
    public static  <T> T[] appendElement(Class<T> kind,  T[] array, T element) {
        final T[] result;
        final int end;
        if (array != null) {
            end = array.length;
            result = (T[])Array.newInstance(kind, end + 1);
            System.arraycopy(array, 0, result, 0, end);
        } else {
            end = 0;
            result = (T[])Array.newInstance(kind, 1);
        }
        result[end] = element;
        return result;
    }

    @SuppressWarnings("unchecked")
    public static  <T> T[] aheadElement(Class<T> kind,  T[] array, T element) {
        final T[] result;
        final int pos;
        if (array != null) {
            pos = 0;
            result = (T[])Array.newInstance(kind, array.length + 1);
            System.arraycopy(array, 0, result, 1, array.length);
        } else {
            pos = 0;
            result = (T[])Array.newInstance(kind, 1);
        }
        result[pos] = element;
        return result;
    }

    /**
     * Removes value from given array if present, providing set-like behavior.
     */
    @SuppressWarnings("unchecked")
    public static  <T> T[] removeElement(Class<T> kind,  T[] array, T element) {
        if (array != null) {
            if (!contains(array, element)) {
                return array;
            }
            final int length = array.length;
            for (int i = 0; i < length; i++) {
                if (Objects.equals(array[i], element)) {
                    if (length == 1) {
                        return null;
                    }
                    T[] result = (T[])Array.newInstance(kind, length - 1);
                    System.arraycopy(array, 0, result, 0, i);
                    System.arraycopy(array, i + 1, result, i, length - i - 1);
                    return result;
                }
            }
        }
        return array;
    }

    public static <T> ArrayList<T> add(ArrayList<T> cur, T val) {
        if (cur == null) {
            cur = new ArrayList<>();
        }
        cur.add(val);
        return cur;
    }

    public static <T> ArrayList<T> remove(ArrayList<T> cur, T val) {
        if (cur == null) {
            return null;
        }
        cur.remove(val);
        if (cur.isEmpty()) {
            return null;
        } else {
            return cur;
        }
    }

    public static <T> boolean contains(ArrayList<T> cur, T val) {
        return (cur != null) ? cur.contains(val) : false;
    }

    /**
     * Returns true if the two ArrayLists are equal with respect to the objects they contain.
     * The objects must be in the same order and be reference equal (== not .equals()).
     */
    public static <T> boolean referenceEquals(ArrayList<T> a, ArrayList<T> b) {
        if (a == b) {
            return true;
        }

        final int sizeA = a.size();
        final int sizeB = b.size();
        if (a == null || b == null || sizeA != sizeB) {
            return false;
        }

        boolean diff = false;
        for (int i = 0; i < sizeA && !diff; i++) {
            diff |= a.get(i) != b.get(i);
        }
        return !diff;
    }
}
