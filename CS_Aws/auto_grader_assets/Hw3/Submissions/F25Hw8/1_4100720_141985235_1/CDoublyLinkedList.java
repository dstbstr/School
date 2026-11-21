public class CDoublyLinkedList {

    private class Node {
        private Comparable data;
        private Node next, prev;
        private Node(Comparable data, Node prev, Node next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node head;
    private int size;

    public CDoublyLinkedList() {
        this.head = new Node(null, null, null);
        this.head.next = this.head;
        this.head.prev = this.head;
        this.size = 0;
    }

    public boolean isEmpty() {
        return head.next == head;
    }

    public int size() {
        return size;
    }

    public void addLast(Comparable x) {
        Node p = new Node(x, head.prev, head);
        head.prev.next = p;
        head.prev = p;
        size++;
    }

    public void addFirst(Comparable x) {
        Node p = new Node(x, head, head.next);
        head.next.prev = p;
        head.next = p;
        size++;
    }

    public Comparable removeFirst() {
        if (isEmpty()) return null;
        Node p = head.next;
        p.next.prev = head;
        head.next = p.next;
        size--;
        return p.data;
    }

    public Comparable first() {
        if (isEmpty()) return null;
        return head.next.data;
    }

    public void addOrdered(Comparable x) {
        CDoublyLinkedList temp = new CDoublyLinkedList();
        boolean added = false;
        Node p = head.next;
        while (p != head) {
            if (!added && x.compareTo(p.data) <= 0) {
                temp.addLast(x);
                added = true;
            }
            temp.addLast(p.data);
            p = p.next;
        }
        if (!added) temp.addLast(x);
        this.head = temp.head;
        this.size = temp.size;
    }

    public void MergeSort() {
        if (size <= 1) return;
        Queue q = new Queue();
        Node p = head.next;
        while (p != head) {
            CDoublyLinkedList singleton = new CDoublyLinkedList();
            singleton.addLast(p.data);
            q.enqueue(singleton);
            p = p.next;
        }
        while (q.size() > 1) {
            CDoublyLinkedList a = (CDoublyLinkedList) q.dequeue();
            CDoublyLinkedList b = (CDoublyLinkedList) q.dequeue();
            CDoublyLinkedList c = merge(a, b);
            q.enqueue(c);
        }
        CDoublyLinkedList sorted = (CDoublyLinkedList) q.dequeue();
        this.head = sorted.head;
        this.size = sorted.size;
    }

    public void InsertionSort() {
        if (size <= 1) return;
        CDoublyLinkedList temp = new CDoublyLinkedList();
        Node p = head.next;
        while (p != head) {
            temp.addOrdered(p.data);
            p = p.next;
        }
        this.head = temp.head;
        this.size = temp.size;
    }

    public boolean isSorted() {
        if (size <= 1) return true;
        Node p = head.next;
        while (p.next != head) {
            if (p.data.compareTo(p.next.data) > 0) return false;
            p = p.next;
        }
        return true;
    }

    private CDoublyLinkedList merge(CDoublyLinkedList a, CDoublyLinkedList b) {
        CDoublyLinkedList c = new CDoublyLinkedList();
        Node p = a.head.next;
        Node q = b.head.next;
        while (p != a.head && q != b.head) {
            if (p.data.compareTo(q.data) <= 0) {
                c.addLast(p.data);
                p = p.next;
            } else {
                c.addLast(q.data);
                q = q.next;
            }
        }
        while (p != a.head) {
            c.addLast(p.data);
            p = p.next;
        }
        while (q != b.head) {
            c.addLast(q.data);
            q = q.next;
        }
        return c;
    }
}
