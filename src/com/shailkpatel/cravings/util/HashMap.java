package com.shailkpatel.cravings.util;

public class HashMap<K, V> {
    private Node<K, V>[] buckets;
    private int size;
    private static final int INITIAL_CAPACITY = 16;

    public HashMap() {
        buckets = new Node[INITIAL_CAPACITY];
        size = 0;
    }

    private static class Node<K, V> {
        final K key;
        V value;
        Node<K, V> next;

        Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> node = buckets[bucketIndex];

        // If the bucket is empty, create a new node and put it in the bucket
        if (node == null) {
            buckets[bucketIndex] = new Node<>(key, value, null);
            size++;
        } else {
            // Iterate through the linked list to check if the key exists
            while (node != null) {
                if (node.key.equals(key)) {
                    // Key exists, update the value
                    node.value = value;
                    return;
                }
                if (node.next == null)
                    break;
                node = node.next;
            }
            // Key does not exist, add a new node at the end of the linked list
            node.next = new Node<>(key, value, null);
            size++;
        }
        ensureCapacity();
    }

    public V get(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> node = buckets[bucketIndex];

        while (node != null) {
            if (node.key.equals(key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    public boolean remove(K key) {
        int bucketIndex = getBucketIndex(key);
        Node<K, V> node = buckets[bucketIndex];
        Node<K, V> prev = null;

        while (node != null) {
            if (node.key.equals(key)) {
                if (prev == null) {
                    buckets[bucketIndex] = node.next;
                } else {
                    prev.next = node.next;
                }
                size--;
                return true;
            }
            prev = node;
            node = node.next;
        }
        return false;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }

    public int size() {
        return size;
    }

    // Using custom hashset
    public HashSet<K> keySet() {
        HashSet<K> keySet = new HashSet<>();
        for (Node<K, V> node : buckets) {
            while (node != null) {
                keySet.add(node.key);
                node = node.next;
            }
        }
        return keySet;
    }

    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode() % buckets.length);
    }

    private void ensureCapacity() {
        if (size >= buckets.length * 0.75) {
            Node<K, V>[] oldBuckets = buckets;
            buckets = new Node[buckets.length * 2];
            size = 0;

            for (Node<K, V> node : oldBuckets) {
                while (node != null) {
                    put(node.key, node.value);
                    node = node.next;
                }
            }
        }
    }

    public void printHashMap() {
        System.out.println("Contents of the HashMap:");
        for (Node<K, V> bucket : buckets) {
            Node<K, V> node = bucket;
            while (node != null) {
                System.out.println("ID: " + node.key + ", Value: " + node.value);
                node = node.next; // Move to the next node in the linked list
            }
        }
    }

    public boolean containsValue(V value) {
        for (Node<K, V> bucket : buckets) {
            Node<K, V> node = bucket;
            while (node != null) {
                if (node.value.equals(value)) {
                    return true;
                }
                node = node.next;
            }
        }
        return false;
    }

}