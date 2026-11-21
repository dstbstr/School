// I have changed some of the variable names from the homework write up to make it more readable for myself.
// this took quite some time and became difficult to track.
// Some help was given through questions asked on stack overflow but implementations were written by me.

public class LinkedList {
    private class Node {
        int data;
        Node next;
        Node(int d) {
            data = d;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public LinkedList() {
        head = tail = null;
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addFirst(int d) {
        Node newNode = new Node(d);
        newNode.next = head;
        head = newNode;
        if (tail == null) {
            tail = head;
        }
        size++;
    }

    public void addLast(int d) {
        Node newNode = new Node(d);
        if (tail == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public int getFirst() {
        if (head == null) {
            throw new RuntimeException("This list is empty");
        }
        return head.data;
    }

    public int removeFirst() {
        if (head == null) {
            throw new RuntimeException("This list is empty");
        }
        int headData = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return headData;
    }
    public boolean isSorted() {
        if (size <= 1) {
            return true;
        }
        Node cur = head;
        while (cur.next != null) {
            if (cur.data > cur.next.data) {
                return false;
            }
            cur = cur.next;
        }
        return true;
    }
    public void InsertionSort() {
        if (head == null || head.next == null) {
            return;
        }

        Node sorted = null;

        while (head != null) {
            Node node = head;
            head = head.next;

            if (sorted == null || node.data < sorted.data) {
                node.next = sorted;
                sorted = node;
            } else {
                Node cur = sorted;
                while (cur.next != null && cur.next.data < node.data) {
                    cur = cur.next;
                }
                node.next = cur.next;
                cur.next = node;
            }
        }

        head = sorted;

        Node newNode = head;
        while (newNode.next != null) newNode = newNode.next;
        tail = newNode;

        size = 0;
        newNode = head;
        while (newNode != null) {
            size++;
            newNode = newNode.next;
        }
    }
    public void MergeSort() {
        if (size <= 1) return;

        Queue newQueue = new Queue();
        Node cur = head;

        while (cur != null) {
            LinkedList newList = new LinkedList();
            newList.addFirst(cur.data);
            newQueue.enqueue(newList);
            cur = cur.next;
        }

        while (newQueue.size() > 1) {
            LinkedList a = newQueue.dequeue();
            LinkedList b = newQueue.dequeue();
            LinkedList merged = merge(a, b);
            newQueue.enqueue(merged);
        }

        LinkedList result = newQueue.dequeue();
        this.head = result.head;
        this.tail = result.tail;
        this.size = result.size;
    }

    private LinkedList merge(LinkedList A, LinkedList B) {
        LinkedList newList = new LinkedList();

        while (!A.isEmpty() && !B.isEmpty()) {
            int aFirst = A.getFirst();
            int bFirst = B.getFirst();

            if (aFirst < bFirst) {
                A.removeFirst();
                newList.addLast(aFirst);
            } else {
                B.removeFirst();
                newList.addLast(bFirst);
            }
        }

        while (!A.isEmpty()) {
            int aFirst = A.removeFirst();
            newList.addLast(aFirst);
        }

        while (!B.isEmpty()) {
            int bFirst = B.removeFirst();
            newList.addLast(bFirst);
        }

        return newList;
    }
    public class Queue {

        private class QueueNode {
            LinkedList list;
            QueueNode next;
            QueueNode(LinkedList l) {
                list = l;
            }
        }

        private QueueNode front, rear;
        private int size;

        public Queue() {
            front = rear = null;
            size = 0;
        }

        public int size() {
            return size;
        }
        public boolean isEmpty() {
            return size == 0;
        }

        public void enqueue(LinkedList L) {
            QueueNode newQueueNode = new QueueNode(L);
            if (rear == null) {
                front = rear = newQueueNode;
            } else {
                rear.next = newQueueNode;
                rear = newQueueNode;
            }
            size++;
        }

        public LinkedList dequeue() {
            if (front == null) throw new RuntimeException("This queue is empty");
            LinkedList value = front.list;
            front = front.next;
            if (front == null) rear = null;
            size--;
            return value;
        }
    }
}