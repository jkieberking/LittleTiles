package com.creativemd.littletiles.common.type;

import java.util.Map.Entry;

public class PairProxy<K, V> implements Entry<K, V> {

    public final K key;
    public V value;

    public PairProxy(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        this.value = value;
        return value;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    public boolean is(K key) {
        if (this.key != null)
            return this.key.equals(key);
        return this.key == key;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PairProxy)
            return key.equals(((PairProxy) obj).key);
        return false;
    }

    @Override
    public String toString() {
        return "[" + key + "=" + value + "]";
    }
}
