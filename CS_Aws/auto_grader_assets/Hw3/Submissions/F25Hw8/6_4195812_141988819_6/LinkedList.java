public class LinkedList {
    private class Node {
        int data;
        Node next;

        Node(int d){ data = d; }
    }

    private Node head;
    private Node tail;
    private int size;

    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public void addFirst(int value) {
        Node newNode = new Node(value);
        newNode.next = head;
        head = newNode;
        if (tail == null) tail = newNode;
        size++;
    }

    public void addLast(int value) {
        Node nn = new Node(value);
        if(tail == null){
            head = tail = nn;
        } else{
            tail.next = nn;
            tail = nn;
        }
        size++;
    }

    public int getFirst() {
        return head.data;
    }

    public int removeFirst() {
        int value = head.data;
        head = head.next;
        if(head == null) tail = null;
        size--;
        return value;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    // Merge two sorted lists
    private LinkedList merge(LinkedList A, LinkedList B) {
        LinkedList S = new LinkedList();
        while (!A.isEmpty() && !B.isEmpty()) {
            int fa = A.getFirst();
            int fb = B.getFirst();
            if (fa <= fb) {
                S.addLast(A.removeFirst());
            } else {
                S.addLast(B.removeFirst());
            }
        }
        while (!A.isEmpty()) {
            S.addLast(A.removeFirst());
        }
        while (!B.isEmpty()) {
            S.addLast(B.removeFirst());
        }
        return S;
    }

    // MergeSort using Queue
    public void MergeSort() {
        Queue q = new Queue();
        Node cur = head;
        while (cur != null) {
            LinkedList single = new LinkedList();
            single.addFirst(cur.data);
            q.enqueue(single);
            cur = cur.next;
        }

        while (q.size() > 1) {
            LinkedList sublist1 = q.dequeue();
            LinkedList sublist2 = q.dequeue();
            LinkedList merged = merge(sublist1, sublist2);
            q.enqueue(merged);
        }
        LinkedList sorted = q.dequeue();

        this.head = this.tail = null;
        this.size = 0;
        Node C = sorted.head;
        while (C != null) {
            this.addLast(C.data);
            C = C.next;
        }
    }


    // InsertionSort

    public void InsertionSort() {
        LinkedList S = new LinkedList();
        while (!this.isEmpty()) {
            int value = this.removeFirst();
            if (S.isEmpty() || value <= S.head.data) {
                S.addFirst(value);
            } else {
                Node cur = S.head;
                while (cur.next != null && cur.next.data < value) {
                    cur = cur.next;
                }
                Node nn = new Node(value);
                nn.next = cur.next;
                cur.next = nn;
                if (nn.next == null) S.tail = nn;
                S.size++;
            }
        }

        this.head = S.head;
        this.tail = S.tail;
        this.size = S.size;
    }


    // Check if list is sorted
    public boolean isSorted() {
        if(size <= 1) return true;
        Node cur = head;
        while(cur.next != null) {
            if(cur.data > cur.next.data ) return false;
            cur = cur.next;
        }
        return true;
    }
    }
