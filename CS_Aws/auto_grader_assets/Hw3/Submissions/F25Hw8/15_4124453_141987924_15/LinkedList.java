
public class LinkedList {

    // NODE
    private static class Node {
        int data;
        Node next;

        Node(int data) {
            this.data = data;
            next = null;
        }
    }

    // SOME FIELDS
    private Node head;
    private Node tail;
    private int size;

    // THE BASIC CONSTRUCTOR
    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // BASIC OPERATIONS

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    // ADD AT FRONT
    public void addFirst(int data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
        if (tail == null) {
            tail = newNode;
        }
        size++;
    }

    // ADD AT END
    public void addLast(int data) {
        Node newNode = new Node(data);
        if (tail == null) {
            head = tail =  newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    // LOOK AT FIRST ELEMENT
    public int getFirst() {
        if (isEmpty()) {
            throw new RuntimeException("Empty list");
        }
        return head.data;
    }


    // REMOVE FIRST ELEMENT
    public int removeFirst() {
        if (isEmpty()) {
            throw new RuntimeException("Empty list");
        }
        int data = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return data;
    }

    // CLEAR LIST
   private void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    // IS SORTED
    public boolean isSorted() {
        if(size <= 1)
            return true;

        Node cur =  head;
        int prev = cur.data;
        cur = cur.next;

        while (cur != null) {
            if (prev > cur.data) {
                return false;
            }
            prev = cur.data;
            cur = cur.next;
        }
        return true;
    }


    // MERGE SORT
    public void MergeSort() {
        if (size <= 1)
            return;
        // enqueue each data as its own single node list
        Queue q = new Queue();
        Node cur = head;
        while (cur != null) {
            LinkedList newList = new LinkedList();
            newList.addFirst(cur.data);
            q.enqueue(newList);
            cur = cur.next;
        }

        clear();
        // repeatedly merge pairs from queue
        while (q.getSize() > 1) {
            LinkedList sub1 = q.dequeue();
            LinkedList sub2 = q.dequeue();
            LinkedList merged = merge(sub1, sub2);
            q.enqueue(merged);
        }
        // dequeue final sorted list and copy contents back into list
        LinkedList sorted = q.dequeue();
        Node node = sorted.head;
        while (node != null) {
            this.addLast(node.data);
            node = node.next;
        }
    }


    // MERGE A AND B
    private static LinkedList merge(LinkedList A, LinkedList B) {
        LinkedList C = new LinkedList();

        // while both are non-empty, smaller front element is picked
        while (!A.isEmpty() && !B.isEmpty()) {
            int fa = A.getFirst();
            int fb = B.getFirst();

            if (fa < fb) {
                A.removeFirst(); // A gets shorter
                C.addLast(fa);
            } else {
                B.removeFirst(); // B gets shorter
                C.addLast(fb);
            }
        }
        // if any remaining elements, they get drained
        while (!A.isEmpty()) {
            int fa =  A.getFirst();
            C.addLast(fa);
            A.removeFirst();
        }
        // if any remaining elements, they get drained
        while (!B.isEmpty()) {
            int fb =  B.getFirst();
            C.addLast(fb);
            B.removeFirst();
        }

        return C;
    }

    // INSERTION SORT
    public void InsertionSort() {
        if (size <= 1)
            return;

        LinkedList sorted =  new LinkedList();

        Node cur = this.head;
        while (cur != null) {
            int data =  cur.data;
            sorted.insertIntoSortedList(data);
            cur = cur.next;
        }

        this.head = sorted.head;
        this.tail = sorted.tail;
        this.size = sorted.size;
    }

    // HELPER FOR INSERTION SORT
    private void insertIntoSortedList(int data) {
        if(isEmpty() || data <= head.data) {
            addFirst(data);
            return;
        }

        Node prev = head;
        Node cur = head.next;

        while (cur != null && cur.data <= data) {
            prev = cur;
            cur = cur.next;
        }

        Node newNode = new Node(data);
        prev.next = newNode;
        newNode.next = cur;

        if (cur == null) {
            tail = newNode;
        }

        size++;
    }
}
