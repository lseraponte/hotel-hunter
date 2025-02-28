package com.lseraponte.cupidapi.hh.util;

@FunctionalInterface
public interface ValuesMerger<T> {
    T merge(T newValues, T currentValues);
}
