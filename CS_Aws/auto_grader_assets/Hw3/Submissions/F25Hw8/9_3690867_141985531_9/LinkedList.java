public class LinkedList {

    private class Node {
        int data;
        Node next;

        Node(int d) { data = d; }
    }

    private Node head;
    private Node tail;
    private int size;

    public LinkedList() {
        head = tail = null;
        size = 0;
    }

    public void addFirst(int d) {
        Node n = new Node(d);
        n.next = head;
        head = n;
        if (size == 0) tail = n;
        size++;
    }

    public void addLast(int d) {
        Node n = new Node(d);
        if (size == 0) {
            head = tail = n;
        } else {
            tail.next = n;
            tail = n;
        }
        size++;
    }

    public int getFirst() {
        return head.data;
    }

    public int removeFirst() {
        if (size == 0) throw new RuntimeException("Empty");
        int v = head.data;
        head = head.next;
        size--;
        if (size == 0) tail = null;
        return v;
    }

    public boolean isEmpty() { return size == 0; }
    public int getSize() { return size; }

    private static LinkedList merge(LinkedList A, LinkedList B) {


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

    public void MergeSort() {

        if (this.size <= 1) return;

        // Step 1: Create a Queue object q, then enqueue each data object in the linked list to be sorted, as a single-node LinkedList.
        Queue q = new Queue();

        Node curr = this.head;
        while (curr != null) {
            LinkedList newList = new LinkedList();
            newList.addFirst(curr.data);
            q.enqueue(newList);
            curr = curr.next;
        }

        // Step 2: repeatedly merge
        while (q.size() > 1) {
            LinkedList sub1 = q.dequeue();
            LinkedList sub2 = q.dequeue();

            LinkedList merged = merge(sub1, sub2);

            q.enqueue(merged);
        }

        // Step 3: Copy sorted list back into THIS list
        LinkedList sorted = q.dequeue();

        this.head = sorted.head;
        this.tail = sorted.tail;
        this.size = sorted.size;
    }

    public void InsertionSort() {
        if (size <= 1) return;

        LinkedList sorted = new LinkedList();

        while (!this.isEmpty()) {
            int val = this.removeFirst();

            // insert val into sorted list
            if (sorted.isEmpty() || sorted.head.data >= val) {
                sorted.addFirst(val);
            } else {
                Node cur = sorted.head;
                while (cur.next != null && cur.next.data < val) {
                    cur = cur.next;
                }

                Node n = new Node(val);
                n.next = cur.next;
                cur.next = n;

                if (cur == sorted.tail)
                    sorted.tail = n;

                sorted.size++;
            }
        }

        // copy sorted back
        this.head = sorted.head;
        this.tail = sorted.tail;
        this.size = sorted.size;
    }

    public boolean isSorted() {
        if (size <= 1) return true;

        Node cur = head;
        while (cur.next != null) {
            if (cur.data > cur.next.data)
                return false;
            cur = cur.next;
        }
        return true;
    }
}
