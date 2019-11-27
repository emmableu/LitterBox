package utils;

import java.util.Arrays;
import java.util.List;

public class Preconditions {

    public static <T> T checkNotNull(T o) {
        if (o == null) {
            throw new NullPointerException("Variable must not be null");
        }
        return o;
    }

    public static void checkArgument(boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException("Invalid argument!");
        }
    }

    public static void checkArgument(boolean condition, String message, Object ... args) {
        if (!condition) {
            throw new IllegalArgumentException(String.format(message, args));
        }
    }

    public static <T> List<T> checkAllArgsNotNull(List<T> args) {
        int i = 0;
        for (Object o: args) {
            if (o == null) {
                throw new NullPointerException(String.format("Argument %d must not be null", i));
            }
            i ++;
        }
        return args;
    }

    public static <T> T[] checkAllArgsNotNull(T[] args) {
        int i = 0;
        for (Object o: args) {
            if (o == null) {
                throw new NullPointerException(String.format("Argument %d must not be null", i));
            }
            i ++;
        }
        return args;
    }

    public static void checkState(boolean condition, String msg, Object ... args) {
        if (!condition) {
            throw new IllegalStateException(String.format(msg, args));
        }
    }
}