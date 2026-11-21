public class LinkedList {

    private class Node {
        int data;
        Node next;
        Node(int d) { data = d; }
    } // end of node

    // place holders
    private Node head;
    private Node tail;
    private int size;

    public LinkedList() {
        head = tail = null;
        size = 0;
    } // end of linkedList

    public void addFirst(int x) {
        Node nn= new Node(x);
        nn.next = head;
        head = nn;
        if (tail == null) tail = nn;
        size++;

    } // end of add first

    public void addLast(int x) {
        Node n = new Node(x);
        if (tail == null) {
            head = tail = n;
        } else {
            tail.next = n;
            tail = n;
        }
        size++;

    } // end of addLast

    public int getFirst() {
        return head.data;

    } // end of getFirst

    public int removeFirst() {
        int x = head.data;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return x;

    } // end of removeFirst

    public boolean isEmpty() {
        return size == 0;
    } // end if isEmpty

    public int getSize() {
        return size;
    } // end of getSize

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
            } // end of if else

        } // end o fwhile

        while (!A.isEmpty()) {
            S.addLast(A.getFirst());
            A.removeFirst();
        } // end of while

        while (!B.isEmpty()) {
            S.addLast(B.getFirst());
            B.removeFirst();
        } // end of hwile
        return S;

    } // end of merge

    public void MergeSort() {
        Queue q = new Queue();

        Node curr = head;

        while (curr != null) {
            LinkedList single = new LinkedList();
            single.addFirst(curr.data);
            q.enqueue(single);
            curr = curr.next;
        } // end of while

        while (q.size() > 1) {
            LinkedList s1 = q.dequeue();
            LinkedList s2 = q.dequeue();
            LinkedList merged = merge(s1, s2);
            q.enqueue(merged);
        } // end of while

        LinkedList sorted = q.dequeue();

        this.head = this.tail = null;
        this.size = 0;

        Node c = sorted.head;
        while (c != null) {
            this.addLast(c.data);
            c = c.next;
        } // end of while

    } // end of mergeSort

    public void InsertionSort() {
        LinkedList S = new LinkedList();

        while (!this.isEmpty()) {
            int x = this.removeFirst();

            if (S.isEmpty() || x <= S.head.data) {
                S.addFirst(x);
            } else {
                Node curr = S.head;
                while (curr.next != null && curr.next.data < x) {
                    curr = curr.next;
                } // end of while
                Node n = new Node(x);
                n.next = curr.next;
                curr.next = n;
                if (n.next == null) S.tail = n;
                S.size++;

            } // end of if else
        } // end of while

        this.head = S.head;
        this.tail = S.tail;
        this.size = S.size;
    } // end of Insertion sort

    public boolean isSorted() {
        if (size <= 1) return true;
        Node curr = head;

        while (curr.next != null) {
            if (curr.data > curr.next.data) return false;
            curr = curr.next;
        } // end of while

        return true;
    } // end of isSorted

} // end of LinkedList