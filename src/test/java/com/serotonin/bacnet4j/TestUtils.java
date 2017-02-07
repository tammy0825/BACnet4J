package com.serotonin.bacnet4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import org.junit.Assert;

import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;

public class TestUtils {
    public static <T, U> void assertListEqualsIgnoreOrder(final List<T> expectedList, final List<U> actualList,
            final BiPredicate<T, U> predicate) {
        assertEquals(expectedList.size(), actualList.size());
        final List<U> actualListCopy = new ArrayList<>(actualList);
        for (final T expected : expectedList) {
            // Find an element in the actual copy list where the predicate returns true.
            final int index = indexOf(actualListCopy, expected, predicate);
            if (index == -1)
                Assert.fail("Did not find " + expected + " in actual list");
            actualListCopy.remove(index);
        }
    }

    public static <T, U> int indexOf(final List<U> list, final T key, final BiPredicate<T, U> predicate) {
        for (int i = 0; i < list.size(); i++) {
            final U value = list.get(i);
            if (predicate.test(key, value))
                return i;
        }
        return -1;
    }

    @SafeVarargs
    public static <T> List<T> toList(final T... elements) {
        final List<T> result = new ArrayList<>(elements.length);
        for (final T e : elements)
            result.add(e);
        return result;
    }

    public static void assertBACnetServiceException(final ServiceExceptionCommand command, final ErrorClass errorClass,
            final ErrorCode errorCode) {
        try {
            command.call();
            fail("BACnetServiceException was expected");
        } catch (final BACnetServiceException e) {
            assertEquals(errorClass, e.getErrorClass());
            assertEquals(errorCode, e.getErrorCode());
        }
    }

    @FunctionalInterface
    public static interface ServiceExceptionCommand {
        void call() throws BACnetServiceException;
    }
}
