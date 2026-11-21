public class LinkedList {

    private class Node {
        int data;
        Node next;

        public Node(int data) {
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

        if(head == null) {
            head = tail = newNode;
        }
        else {
            newNode.next = head;
            head = newNode;
        }

        size++;
    }

    public void addLast(int data) {

        Node newNode = new Node(data);

        if(head == null) {
            head = tail = newNode;
        }
        else {
            tail.next = newNode;
            tail = newNode;
        }

        size++;
    }

    public int getFirst() {

        if(head == null)
            throw new RuntimeException("List Empty");

        return head.data;
    }

    public int removeFirst() {

        if(head == null)
            throw new RuntimeException("List Empty");

        int value = head.data;

        head = head.next;

        if(head == null)
            tail = null;

        size--;

        return value;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    /*
     * merge(A,B)
     * follows the homework pseudocode
     */
    private LinkedList merge(LinkedList A, LinkedList B) {

        LinkedList S = new LinkedList();

        while(!A.isEmpty() && !B.isEmpty()) {

            int fa = A.getFirst();
            int fb = B.getFirst();

            if(fa < fb) {
                A.removeFirst();
                S.addLast(fa);
            }
            else {
                B.removeFirst();
                S.addLast(fb);
            }
        }

        while(!A.isEmpty()) {

            int fa = A.getFirst();

            S.addLast(fa);

            A.removeFirst();
        }

        while(!B.isEmpty()) {

            int fb = B.getFirst();

            S.addLast(fb);

            B.removeFirst();
        }

        return S;
    }

    /*
     * MergeSort
     * follows the exact queue-based approach
     * from the assignment
     */
    public void MergeSort() {

        Queue q = new Queue();

        Node current = head;

        while(current != null) {

            LinkedList newList = new LinkedList();

            newList.addFirst(current.data);

            q.enqueue(newList);

            current = current.next;
        }

        while(q.size() > 1) {

            LinkedList sublist1 = q.dequeue();

            LinkedList sublist2 = q.dequeue();

            LinkedList tempList = merge(sublist1, sublist2);

            q.enqueue(tempList);
        }

        LinkedList sortedList = q.dequeue();

        if(sortedList != null) {
            this.head = sortedList.head;
            this.tail = sortedList.tail;
            this.size = sortedList.size;
        }
    }

    /*
     * Insertion Sort
     */
    public void InsertionSort() {

        LinkedList sorted = new LinkedList();

        while(!this.isEmpty()) {

            int value = this.removeFirst();

            if(sorted.isEmpty()) {
                sorted.addFirst(value);
            }
            else if(value <= sorted.head.data) {
                sorted.addFirst(value);
            }
            else {

                Node current = sorted.head;

                while(current.next != null &&
                      current.next.data < value) {
                    current = current.next;
                }

                Node temp = new Node(value);

                temp.next = current.next;

                current.next = temp;

                if(current == sorted.tail)
                    sorted.tail = temp;

                sorted.size++;
            }
        }

        this.head = sorted.head;
        this.tail = sorted.tail;
        this.size = sorted.size;
    }

    /*
     * O(n)
     */
    public boolean isSorted() {

        if(head == null || head.next == null)
            return true;

        Node current = head;

        while(current.next != null) {

            if(current.data > current.next.data)
                return false;

            current = current.next;
        }

        return true;
    }
}
