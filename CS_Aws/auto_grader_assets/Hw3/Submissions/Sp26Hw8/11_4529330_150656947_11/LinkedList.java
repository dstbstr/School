// LinkedList class: singly linked with a tail pointer (keeps addLast O(1)).
public class LinkedList {

    private class Node {
        Object data;
        Node next;
        Node(Object data) { this.data = data; this.next = null; }
    }

    private Node head;
    private Node tail;
    private int size;

    public LinkedList() { head = null; tail = null; size = 0; }

    public boolean isEmpty() { return size == 0; }
    public int getSize()     { return size; }

    public void addFirst(Object data) {
        Node n = new Node(data);
        if (!isEmpty()) {
            n.next = head;
            head = n;
        }
        if (isEmpty()) {
            head = n;
            tail = n;
        }
        size++;
    }

    public void addLast(Object data) {
        Node n = new Node(data);
        if (isEmpty()) {
            head = n;
            tail = n;
        }
        if (!isEmpty()) {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    public Object removeFirst() {
        if (isEmpty()) throw new RuntimeException("List is empty");
        Object data = head.data;
        head = head.next;
        size--;
        if (isEmpty()) tail = null;
        return data;
    }

    public Object getFirst() {
        if (isEmpty()) throw new RuntimeException("List is empty");
        Object data = head.data;
        return data;
    }

    public boolean isSorted() {
        boolean sorted = true;
        Node cur = head;
        if (isEmpty()) return true;
        while (cur.next != null) {
            if (((Comparable) cur.data).compareTo(cur.next.data) > 0) {
                return false;
            }
            cur = cur.next;
        }
        return sorted;
    }

    public void InsertionSort() {
        LinkedList sorted = new LinkedList();
        while (!isEmpty()) {
            insertInOrder(sorted, removeFirst());
        }
        this.head = sorted.head;
        this.tail = sorted.tail;
        this.size = sorted.size;
    }

    // insert val into list, keeping list in ascending order
    private void insertInOrder(LinkedList list, Object val) {
        Node n = new Node(val);
        if (list.head == null || ((Comparable) val).compareTo(list.head.data) <= 0) {
            n.next = list.head;
            list.head = n;
            if (list.tail == null) list.tail = n;
            list.size++;
            return;
        }
        Node cur = list.head;
        while (cur.next != null && ((Comparable) cur.next.data).compareTo(val) < 0) {
            cur = cur.next;
        }
        n.next = cur.next;
        cur.next = n;
        if (n.next == null) list.tail = n;
        list.size++;
    }

    // queue-based bottom-up merge sort
    public void MergeSort() {
        Queue q = new Queue();
        while (!isEmpty()) {
            LinkedList one = new LinkedList();
            one.addFirst(removeFirst());
            q.enqueue(one);
        }
        while (q.size() > 1) {
            LinkedList a = (LinkedList) q.dequeue();
            LinkedList b = (LinkedList) q.dequeue();
            q.enqueue(merge(a, b));
        }
        if (!q.isEmpty()) {
            LinkedList sorted = (LinkedList) q.dequeue();
            this.head = sorted.head;
            this.tail = sorted.tail;
            this.size = sorted.size;
        }
    }

    // merge two already-sorted lists into one sorted list
    private LinkedList merge(LinkedList a, LinkedList b) {
        LinkedList s = new LinkedList();
        while (!a.isEmpty() && !b.isEmpty()) {
            int fa = (Integer) a.getFirst();
            int fb = (Integer) b.getFirst();
            if (fa <= fb) s.addLast(a.removeFirst());
            else          s.addLast(b.removeFirst());
        }
        while (!a.isEmpty()) s.addLast(a.removeFirst());
        while (!b.isEmpty()) s.addLast(b.removeFirst());
        return s;
    }
}
