public class LinkedList {

    //Node class
    private static class Node {
        int data; //node data
        Node next; //reference to the next node

        Node(int d) { //object element
            data = d; //stores the integer
            next = null; //not connected to a node
        }
    }

    //fields
    private Node head; //pointer to first node, traversal starts here
    private Node tail; //pointer to last node
    private int size; //track number of elements in list

    //constructor
    public LinkedList(){
        this.head = null; //no nodes
        this.tail = null; //no last node
        this.size = 0; //no elements
    }

    public int getSize(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public void addLast(int value){
        Node newNode = new Node(value);
        if(tail == null) {
            head = tail = newNode;
        }else{
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public void addFirst(int value){
        Node newNode = new Node(value);
        newNode.next = head;
        head = newNode;
        if(tail == null) {
            tail = head;
        }
        size++;
    }

    public int getFirst(){
        if(head == null){
            throw new RuntimeException("List is empty");
        }
        return head.data;
    }

    public int removeFirst() {
        if (head == null) {
            throw new RuntimeException("List is empty");
        }
        int value = head.data;
        head = head.next;
        size--;
        if (head == null)
            tail = null;
        return value;
    }

    public void clear(){
        head = null;
        tail = null;
        size = 0;
    }


    public boolean isSorted() {
        if (head == null || head.next == null)
            return true;

        Node cur = head;
        while (cur.next != null) {
            if (cur.data > cur.next.data)
                return false;
            cur = cur.next;
        }
        return true;
    }

    public void InsertionSort(){

        //base case
        if(head == null || head.next == null)
            return;
        Node sorted = null;
        Node cur = head;

        while(cur != null){
            Node next = cur.next;
            if(sorted == null || cur.data <= sorted.data){
                cur.next = sorted;
                sorted = cur;
            }else{
                Node p = sorted;
                while(p.next != null && p.next.data < cur.data){
                    p = p.next;
                }
                cur.next = p.next;
                p.next = cur;
            }
            cur = next;
        }
        head = sorted;

        tail = head;
        if(tail != null){
            while(tail.next != null){
                tail = tail.next;
            }
        }
    }

    public void MergeSort() {

        if (size <= 1)
            return;

        //create queue of single-element lists
        Queue q = new Queue();
        Node cur = head;

        while (cur != null) {
            LinkedList newList = new LinkedList();
            newList.addLast(cur.data);
            q.enqueue(newList);
            cur = cur.next;
        }

        //merge until one list remains
        while (q.getSize() > 1) {
            LinkedList A = q.dequeue();
            LinkedList B = q.dequeue();
            LinkedList merged = merge(A, B);
            q.enqueue(merged);
        }

        //copy sorted list into THIS list
        LinkedList sorted = q.dequeue();

        this.clear();

        Node p = sorted.head;
        while (p != null) {
            this.addLast(p.data);
            p = p.next;
        }
    }

    //merge(A,B)
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

        //remaining nodes
        while (!A.isEmpty()) {
            S.addLast(A.removeFirst());
        }

        while (!B.isEmpty()) {
            S.addLast(B.removeFirst());
        }

        return S;
    }



}

