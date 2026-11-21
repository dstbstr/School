public class LinkedList {

    private class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public void addFirst(int data) {
        Node newNode = new Node(data);

        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }

        size++;
    }
    public void addLast(int data) {
        Node newNode = new Node(data);

        if (isEmpty()) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    public int removeFirst() {
        if (isEmpty())
            throw new RuntimeException("List is empty");

        int value = head.data;

        head = head.next;
        size--;

        if (head == null)
            tail = null;

        return value;
    }

    public int getFirst() {
        if (isEmpty())
            throw new RuntimeException("List is empty");

        return head.data;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

        private void insertOrdered(int value) {
            Node newNode = new Node(value);

            // Empty list or insert at front
            if (head == null || value <= head.data) {
                newNode.next = head;
                head = newNode;

                if (tail == null)
                    tail = newNode;

                size++;
                return;
            }

            Node current = head;

            while (current.next != null && current.next.data < value) {
                current = current.next;
            }

            newNode.next = current.next;
            current.next = newNode;

            if (newNode.next == null)
                tail = newNode;

            size++;
        }

    public void InsertionSort() {
        LinkedList sorted = new LinkedList();

        Node current = head;

        while (current != null) {
            sorted.insertOrdered(current.data);
            current = current.next;
        }

        this.head = sorted.head;
        this.tail = sorted.tail;
        this.size = sorted.size;
    }


    public void MergeSort() {
        if (size <= 1)
            return;

        LinkedList left = new LinkedList();
        LinkedList right = new LinkedList();

        int mid = size / 2;

        Node current = head;

        for (int i = 0; i < mid; i++) {
            left.addLast(current.data);
            current = current.next;
        }

        while (current != null) {
            right.addLast(current.data);
            current = current.next;
        }

        left.MergeSort();
        right.MergeSort();

        LinkedList merged = merge(left, right);

        this.head = merged.head;
        this.tail = merged.tail;
        this.size = merged.size;
    }

    public static LinkedList merge(LinkedList A, LinkedList B) {
        LinkedList result = new LinkedList();

        Node a = A.head;
        Node b = B.head;

        while (a != null && b != null) {
            if (a.data <= b.data) {
                result.addLast(a.data);
                a = a.next;
            } else {
                result.addLast(b.data);
                b = b.next;
            }
        }

        while (a != null) {
            result.addLast(a.data);
            a = a.next;
        }

        while (b != null) {
            result.addLast(b.data);
            b = b.next;
        }

        return result;
    }

    public boolean isSorted() {
        if (head == null || head.next == null)
            return true;

        Node current = head;

        while (current.next != null) {
            if (current.data > current.next.data)
                return false;

            current = current.next;
        }

        return true;
    }
}