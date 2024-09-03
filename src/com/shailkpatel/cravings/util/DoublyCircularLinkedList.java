package com.shailkpatel.cravings.util;

public class DoublyCircularLinkedList<T> {
    Node head;
    int size = 0;

    public class Node {
        public T data;
        public Node next;
        Node prev;

        Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    public void addFirst(T data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            head.next = head;
            head.prev = head;
        } else {
            Node tail = head.prev;
            newNode.next = head;
            newNode.prev = tail;
            head.prev = newNode;
            tail.next = newNode;
            head = newNode;
        }
        size++;
    }

    public void addLast(T data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = newNode;
            head.next = head;
            head.prev = head;
        } else {
            Node tail = head.prev;
            tail.next = newNode;
            newNode.prev = tail;
            newNode.next = head;
            head.prev = newNode;
        }
        size++;
    }

    public void removeFirst() {
        if (head == null)
            return;

        if (head.next == head) {
            head = null;
        } else {
            Node tail = head.prev;
            head = head.next;
            tail.next = head;
            head.prev = tail;
        }
        size--;
    }

    public void remove(T key) {
        if (head == null)
            return;

        Node current = head;
        do {
            if (current.data.equals(key)) {
                if (current == head && current.next == head) {
                    head = null;
                } else {
                    current.prev.next = current.next;
                    current.next.prev = current.prev;
                    if (current == head) {
                        head = current.next;
                    }
                }
                size--;
                return;
            }
            current = current.next;
        } while (current != head);
    }

    public void removeLast() {
        if (head == null)
            return;

        if (head.next == head) {
            head = null;
        } else {
            Node tail = head.prev;
            tail.prev.next = head;
            head.prev = tail.prev;
        }
        size--;
    }

    public void display() {
        if (head == null) {
            System.out.println("List is empty.");
            return;
        }
        Node current = head;
        do {
            System.out.print(current.data + " ");
            current = current.next;
        } while (current != head);
        System.out.println();
    }

    public int getSize() {
        return size;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        Node current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    public T getHead() {
        if (head == null) {
            throw new IllegalStateException("List is empty.");
        }
        return head.data;
    }

    public Node getHeadNode() {
        if (head == null) {
            throw new IllegalStateException("List is empty.");
        }
        return head;
    }
}