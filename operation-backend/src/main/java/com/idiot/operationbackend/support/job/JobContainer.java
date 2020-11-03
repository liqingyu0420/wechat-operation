package com.idiot.operationbackend.support.job;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * job 容器 最大size 默认是100  LRU 移除算法
 * @author wang xiao
 * @date Created in 16:03 2020/10/27
 */
public class JobContainer<K,V> {

    /**
     * 容器
     */
    private Map<K, V> container;

    /**
     * 容量
     */
    private final int capacity ;

    public JobContainer(int capacity) {
        this.capacity = capacity;
        container = new LinkedHashMap<K, V>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > capacity;
            }
        };
    }

    public JobContainer() {
        this.capacity = 100;
        container = new LinkedHashMap<K, V>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > capacity;
            }
        };
    }

    public V get(K key) {
        return container.get(key);
    }

    public void put(K key, V value) {
        container.put(key, value);
    }
    public void remove(K key){
        container.remove(key);
    }
    public boolean containsKey (K key) {
        return container.containsKey(key);
    }
}
