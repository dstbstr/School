
public class LinkedList {

    private static class Node {
        int data;
        Node next;

        Node(int data) {
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

    public int getSize() {
        return size;
    }


    public void addFirst(int data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;

        if (tail == null) {
            tail = newNode;
        }
        size++;
    }


    public void addLast(int data) {
        Node newNode = new Node(data);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }


    public void addOrdered(int data) {
        if (head == null || data <= head.data) {
            addFirst(data);
            return;
        }
        Node current = head;
        while (current.next != null && current.next.data < data) {
            current = current.next;
        }
        if (current.next == null) {
            addLast(data);
        } else {
            Node newNode = new Node(data);
            newNode.next = current.next;
            current.next = newNode;
            size++;
        }
    }

    public boolean isSorted() {
        Node current = head;
        while (current != null && current.next != null) {
            if (current.data > current.next.data) {
                return false;
            }
            current = current.next;
        }
        return true;
    }

    private static LinkedList merge(LinkedList A, LinkedList B) {
        Node curA = A.head;
        Node curB = B.head;

        LinkedList c = new LinkedList();

        while (curA != null && curB != null) {
            if (curA.data < curB.data) {
                c.addLast(curA.data);
                curA = curA.next;
            } else {
                c.addLast(curB.data);
                curB = curB.next;
            }
        }

        while (curA != null) {
            c.addLast(curA.data);
            curA = curA.next;
        }
        while (curB != null) {
            c.addLast(curB.data);
            curB = curB.next;
        }

        return c;

    }

    public void MergeSort() {
        if (size <= 1) {
            return;
        }

        Queue<LinkedList> q = new Queue<LinkedList>();
        Node current = head;
        while (current != null) {
            LinkedList newList = new LinkedList();
            newList.addFirst(current.data);
            q.enqueue(newList);
            current = current.next;
        }

        while (q.getSize() > 1) {
            LinkedList sublist1 = q.dequeue();
            LinkedList sublist2 = q.dequeue();
            LinkedList tempList = merge(sublist1, sublist2);
            q.enqueue(tempList);
        }

        LinkedList sorted = q.dequeue();
        this.head = sorted.head;
        this.tail = sorted.tail;
        this.size = sorted.size;
    }

    public void InsertionSort() {
        if (size <= 1) {
            return;
        }
        LinkedList sorted = new LinkedList();
        Node current = head;
        while (current != null) {
            sorted.addOrdered(current.data);
            current = current.next;
        }
        this.head = sorted.head;
        this.tail = sorted.tail;
        this.size = sorted.size;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}
