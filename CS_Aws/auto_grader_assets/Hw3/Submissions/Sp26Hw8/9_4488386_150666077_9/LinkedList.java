//ryan larkin
public class LinkedList {
    private class Node {
        int data;
        Node next;

        Node(int data) { this.data = data; }
    }

    private Node head;
    private Node tail;
    private int size;

    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public int getSize() { return size; }
    public boolean isEmpty() { return size == 0; }

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

    public int getFirst() {
        if (isEmpty()) { throw new RuntimeException("List empty"); }

        return head.data;
    }

    public int removeFirst() {
        if (isEmpty()) { throw new RuntimeException("List empty"); }

        int value = head.data;
        head = head.next;
        size--;

        if (size == 0) { tail = null; }

        return value;
    }

    public boolean isSorted() {
        if (size <= 1) { return true; }

        Node current = head;

        while (current.next != null) {
            if (current.data > current.next.data) { return false; }
            current = current.next;
        }
        return true;
    }

    public void InsertionSort() {
        if (size <= 1) { return; }

        Node sorted = null;
        Node current = head;

        while (current != null) {
            Node next = current.next;

            if (sorted == null || current.data < sorted.data) {
                current.next = sorted;
                sorted = current;
            } else {
                Node temp = sorted;

                while (temp.next != null && temp.next.data <= current.data) {
                    temp = temp.next;
                }
                current.next = temp.next;
                temp.next = current;
            }
            current = next;
        }
        head = sorted;
        tail = head;

        while (tail.next != null) { tail = tail.next; }
    }

    public void MergeSort() {
        if (size <= 1) { return; }

        Queue q = new Queue();
        Node current = head;

        while (current != null) {
            LinkedList newList = new LinkedList();

            newList.addFirst(current.data);
            q.enqueue(newList);

            current = current.next;
        }

        while (q.size() > 1) {
            LinkedList sublist1 = q.dequeue();
            LinkedList sublist2 = q.dequeue();
            LinkedList tempList = merge(sublist1, sublist2);

            q.enqueue(tempList);
        }
        LinkedList sortedList = q.dequeue();

        this.head = sortedList.head;
        this.tail = sortedList.tail;
        this.size = sortedList.size;
    }

    private LinkedList merge(LinkedList A, LinkedList B) {
        LinkedList S = new LinkedList();

        while (!A.isEmpty() && !B.isEmpty()) {
            int fa = A.getFirst();
            int fb = B.getFirst();

            if (fa < fb) {
                A.removeFirst();
                S.addLast(fa);
            } else {
                B.removeFirst();
                S.addLast(fb);
            }
        }

        while (!A.isEmpty()) {
            int fa = A.getFirst();
            S.addLast(fa);
            A.removeFirst();
        }

        while (!B.isEmpty()) {
            int fb = B.getFirst();
            S.addLast(fb);
            B.removeFirst();
        }
        return S;
    }
}