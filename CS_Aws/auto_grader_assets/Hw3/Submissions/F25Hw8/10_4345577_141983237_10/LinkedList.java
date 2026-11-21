public class LinkedList {

    // Stores an integer and a pointer into the next node
    private class Node {

        int data;
        Node next;

        Node(int d) {
            data = d;
            next = null;
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

    // If the size of the list is 0, it's empty
    public boolean isEmpty() {
        return size == 0;
    }

    // Returns the number of integers in the list
    public int getSize() {
        return size;
    }


    public void addFirst(int d) {
        Node node = new Node(d);
        node.next = head;
        head = node;

        // If the list is empty, the tail will now go to a new node
        if (tail == null)
            tail = head;

        size++;
    }


    public void addLast(int d) {
        Node node = new Node(d);

        if (tail == null) {
            head = tail = node;

        } else {
            tail.next = node;
            tail = node;
        }

        size++;

    }


    public int removeFirst() {

        if (isEmpty())
            throw new RuntimeException("removeFirst on empty list");

        int val = head.data;
        head = head.next;
        size--;

        if (head == null)
            tail = null;

        return val;

    }


    public int getFirst() {
        if (isEmpty())
            throw new RuntimeException("getFirst on empty list");

        return head.data;
    }


    public void clear() {
        head = tail = null;
        size = 0;
    }

    // Ascending-order check
    public boolean isSorted() {

        if (size <= 1) return true;
        Node curr = head;

        while (curr.next != null) {

            if (curr.data > curr.next.data)
                return false;

            curr = curr.next;

        }

        return true;

    }


    private void insertInOrder(int val) {

        if (isEmpty() || val <= head.data) {
            addFirst(val);
            return;
        }

        Node prev = head;
        Node curr = head.next;

        while (curr != null && curr.data < val) {
            prev = curr;
            curr = curr.next;
        }

        Node n = new Node(val);
        prev.next = n;
        n.next = curr;

        if (curr == null) tail = n;
        size++;

    }

    // Inserts the value into its sorted position
    public void InsertionSort() {
        LinkedList sorted = new LinkedList();

        while (!this.isEmpty()) {
            int val = this.removeFirst();
            sorted.insertInOrder(val);
        }

        this.head = sorted.head;
        this.tail = sorted.tail;
        this.size = sorted.size;

    }

    // Merges the two sorted lists into one single list with help from MergeSort
    private static LinkedList merge(LinkedList A, LinkedList B) {
        LinkedList C = new LinkedList(); // C represents the new sorted list

        // Compares the front elements of both A and B while both aren't empty
        while (!A.isEmpty() && !B.isEmpty()) {
            int fa = A.getFirst();
            int fb = B.getFirst();

            if (fa < fb) {
                A.removeFirst();
                C.addLast(fa);

            } else {
                B.removeFirst();
                C.addLast(fb);
            }
        }

        // Append the remaining results from A
        while (!A.isEmpty()) {
            C.addLast(A.removeFirst());
        }

        // Remaining results from B
        while (!B.isEmpty()) {
            C.addLast(B.removeFirst());
        }

        return C; // Returns the new merged list

    }


    public void MergeSort() {
        if (size <= 1)
            return;

        Queue q = new Queue();
        Node curr = this.head;

        // Enqueue each element in each list provided
        while (curr != null) {
            LinkedList newList = new LinkedList();
            newList.addLast(curr.data);
            q.enqueue(newList);
            curr = curr.next;
        }

        // Merged lists on repeat until one is left standing
        while (q.size() > 1) {
            LinkedList sub1 = q.dequeue();
            LinkedList sub2 = q.dequeue();
            LinkedList merged = merge(sub1, sub2);
            q.enqueue(merged);
        }

        // Gives out the final sorted list
        LinkedList sorted = q.dequeue();

        this.head = sorted.head;
        this.tail = sorted.tail;
        this.size = sorted.size;

    }

}// end LinkedList