public class LinkedList {
    private class Node{
        int data;
        Node next;

        public Node(int data){
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public LinkedList(){
        head = null;
        tail = null;
        size = 0;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int getSize(){
        return size;
    }

    public int getFirst(){
        if(head == null){
            throw new RuntimeException("This an empty list.");
        }
        return head.data;
    }

    public void addFirst(int data){
        Node newNode = new Node(data);
        if(head == null){
            head = tail = newNode;
        }else{
            newNode.next = head;
            head = newNode;
        }
        size++;
    }

    public void addLast(int data) {

        Node newNode = new Node(data);
        if (head == null) {
            head = tail = newNode;
        }
        else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public void removeFirst() {
        if (head == null) {
            return;
        }
        head = head.next;
        size--;

        if (head == null) {
            tail = null;
        }
    }

    public boolean isSorted() {

        if (head == null || head.next == null) {
            return true;
        }

        Node temp = head;
        while (temp.next != null) {
            if (temp.data > temp.next.data) {
                return false;
            }
            temp = temp.next;
        }
        return true;
    }

    public void addOrdered(int value) {

        Node newNode = new Node(value);
        if (head == null || value <= head.data) {
            addFirst(value);
            return;
        }

        Node current = head;

        while (current.next != null && current.next.data < value) {
            current = current.next;
        }

        newNode.next = current.next;
        current.next = newNode;

        if (newNode.next == null) {
            tail = newNode;
        }
        size++;
    }

    public void InsertionSort() {

        LinkedList sorted = new LinkedList();

        while (!this.isEmpty()) {
            int value = this.getFirst();
            this.removeFirst();
            sorted.addOrdered(value);
        }

        this.head = sorted.head;
        this.tail = sorted.tail;
        this.size = sorted.size;
    }

    public static LinkedList merge(LinkedList A, LinkedList B) {

        LinkedList S = new LinkedList();
        while (!A.isEmpty() && !B.isEmpty()) {
            if (A.getFirst() < B.getFirst()) {
                S.addLast(A.getFirst());
                A.removeFirst();
            }
            else {
                S.addLast(B.getFirst());
                B.removeFirst();
            }
        }

        while (!A.isEmpty()) {
            S.addLast(A.getFirst());
            A.removeFirst();
        }

        while (!B.isEmpty()) {
            S.addLast(B.getFirst());
            B.removeFirst();
        }
        return S;
    }

    public void MergeSort() {
        Queue q = new Queue();
        Node temp = head;

        while (temp != null) {
            LinkedList single = new LinkedList();
            single.addFirst(temp.data);
            q.enqueue(single);
            temp = temp.next;
        }

        while (q.getSize() > 1) {
            LinkedList list1 =  q.dequeue();
            LinkedList list2 = q.dequeue();
            LinkedList merged = merge(list1, list2);
            q.enqueue(merged);
        }

        LinkedList sorted = q.dequeue();
        this.head = sorted.head;
        this.tail = sorted.tail;
        this.size = sorted.size;
    }
}
