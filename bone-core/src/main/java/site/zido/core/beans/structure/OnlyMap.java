package site.zido.core.beans.structure;

import java.util.HashMap;
import java.util.Map;

public class OnlyMap<K, V> {
    private K key;
    private V value;

    public OnlyMap() {

    }

    public OnlyMap(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public void push(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public Map<K, V> toMap() {
        Map<K, V> map = new HashMap<>();
        return putToMap(map);
    }

    public Map<K, V> putToMap(Map<K, V> map) {
        if (key == null || value == null) {
            return null;
        }
        map.put(key, value);
        return map;
    }

    @Override
    public String toString() {
        return "OnlyMap{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof OnlyMap))
            return false;
        OnlyMap<K, V> other = (OnlyMap<K, V>) obj;
        return (other).key == key && (other).value == value;
    }
}
