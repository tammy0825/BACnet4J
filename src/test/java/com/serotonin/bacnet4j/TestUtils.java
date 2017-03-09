package com.serotonin.bacnet4j;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

import org.junit.Assert;

import com.serotonin.bacnet4j.exception.BACnetErrorException;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.type.constructed.DateTime;
import com.serotonin.bacnet4j.type.constructed.TimeStamp;
import com.serotonin.bacnet4j.type.enumerated.ErrorClass;
import com.serotonin.bacnet4j.type.enumerated.ErrorCode;
import com.serotonin.bacnet4j.type.primitive.Time;

public class TestUtils {
    public static <T, U> void assertListEqualsIgnoreOrder(final List<T> expectedList, final List<U> actualList,
            final BiPredicate<T, U> predicate) {
        Assert.assertEquals(expectedList.size(), actualList.size());
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

    public static <T> void assertListEqualsIgnoreOrder(final List<T> expectedList, final List<T> actualList) {
        Assert.assertEquals(expectedList.size(), actualList.size());
        final List<T> actualListCopy = new ArrayList<>(actualList);
        for (final T expected : expectedList) {
            // Find an element in the actual copy list which equals the expected.
            final int index = indexOf(actualListCopy, expected);
            if (index == -1)
                Assert.fail("Did not find " + expected + " in actual list");
            actualListCopy.remove(index);
        }
    }

    public static <T> int indexOf(final List<T> list, final T key) {
        for (int i = 0; i < list.size(); i++) {
            final T value = list.get(i);
            if (Objects.equals(value, key))
                return i;
        }
        return -1;
    }

    public static void assertEquals(final TimeStamp expected, final TimeStamp actual, final int deadbandHundredths) {
        if (expected.isDateTime() && actual.isDateTime())
            assertEquals(expected.getDateTime(), actual.getDateTime(), deadbandHundredths);
        else if (expected.isTime() && actual.isTime())
            assertEquals(expected.getTime(), actual.getTime(), deadbandHundredths);
        else
            Assert.assertEquals(expected, actual);
    }

    public static void assertEquals(final DateTime expected, final DateTime actual, final int deadbandHundredths) {
        final long expectedMillis = expected.getGC().getTimeInMillis();
        final long actualMillis = actual.getGC().getTimeInMillis();

        long diff = expectedMillis - actualMillis;
        if (diff < 0)
            diff = -diff;
        diff /= 10;
        if (diff > deadbandHundredths) {
            // This will fail
            Assert.assertEquals(expected, actual);
        }
    }

    public static void assertEquals(final Time expected, final Time actual, final int deadbandHundredths) {
        // Can only compare this way if the times are fully specified.
        if (expected.isFullySpecified() && actual.isFullySpecified()) {
            final int diff = expected.getSmallestDiff(actual);
            if (diff > deadbandHundredths) {
                // This will fail
                Assert.assertEquals(expected, actual);
            }
        } else {
            Assert.assertEquals(expected, actual);
        }
    }

    @SafeVarargs
    public static <T> List<T> toList(final T... elements) {
        final List<T> result = new ArrayList<>(elements.length);
        for (final T e : elements)
            result.add(e);
        return result;
    }

    public static void assertBACnetServiceException(final BACnetServiceException e, final ErrorClass errorClass,
            final ErrorCode errorCode) {
        Assert.assertEquals(errorClass, e.getErrorClass());
        Assert.assertEquals(errorCode, e.getErrorCode());
    }

    public static void assertBACnetServiceException(final ServiceExceptionCommand command, final ErrorClass errorClass,
            final ErrorCode errorCode) {
        try {
            command.call();
            fail("BACnetServiceException was expected");
        } catch (final BACnetServiceException e) {
            assertBACnetServiceException(e, errorClass, errorCode);
        }
    }

    @FunctionalInterface
    public static interface ServiceExceptionCommand {
        void call() throws BACnetServiceException;
    }

    public static void assertRequestHandleException(final RequestHandleExceptionCommand command,
            final ErrorClass errorClass, final ErrorCode errorCode) {
        try {
            command.call();
            fail("BACnetException was expected");
        } catch (final BACnetErrorException e) {
            if (e.getCause() instanceof BACnetServiceException) {
                assertBACnetServiceException((BACnetServiceException) e.getCause(), errorClass, errorCode);
            } else {
                fail("Cause is not a BACnetServiceException: " + e.getCause());
            }
        } catch (final BACnetException e) {
            fail("Not a BACnetErrorException: " + e);
        }
    }

    @FunctionalInterface
    public static interface RequestHandleExceptionCommand {
        void call() throws BACnetException;
    }
}
