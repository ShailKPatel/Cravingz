package com.shailkpatel.cravings.util;

public class HashSet<T> {
    private Node<T>[] buckets;
    private int size;
    private static final int INITIAL_CAPACITY = 16;

    public HashSet() {
        buckets = new Node[INITIAL_CAPACITY];
        size = 0;
    }

    private static class Node<T> {
        final T value;
        Node<T> next;

        Node(T value, Node<T> next) {
            this.value = value;
            this.next = next;
        }
    }

    public boolean add(T value) {
        int bucketIndex = getBucketIndex(value);
        Node<T> node = buckets[bucketIndex];

        while (node != null) {
            if (node.value.equals(value)) {
                return false; // Value already exists, do not add
            }
            node = node.next;
        }

        // Value does not exist, add a new node at the head of the linked list
        Node<T> newNode = new Node<>(value, buckets[bucketIndex]);
        buckets[bucketIndex] = newNode;
        size++;
        ensureCapacity();
        return true;
    }

    public boolean remove(T value) {
        int bucketIndex = getBucketIndex(value);
        Node<T> node = buckets[bucketIndex];
        Node<T> prev = null;

        while (node != null) {
            if (node.value.equals(value)) {
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

    public boolean contains(T value) {
        int bucketIndex = getBucketIndex(value);
        Node<T> node = buckets[bucketIndex];

        while (node != null) {
            if (node.value.equals(value)) {
                return true;
            }
            node = node.next;
        }
        return false;
    }

    public int size() {
        return size;
    }

    private int getBucketIndex(T value) {
        return value.hashCode() % buckets.length;
    }

    private void ensureCapacity() {
        if (size >= buckets.length * 0.75) {
            Node<T>[] oldBuckets = buckets;
            buckets = new Node[buckets.length * 2];
            size = 0;

            for (Node<T> node : oldBuckets) {
                while (node != null) {
                    add(node.value);
                    node = node.next;
                }
            }
        }
    }
}
