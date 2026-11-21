import java.util.NoSuchElementException;

public class LinkedList {

    private class Node {
        int data;
        Node next;
        Node prev;
        Node (int d) {data = d;}
    }

    private Node head;
    private int size;

    public LinkedList() {
        head = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void addFirst(int data) {
        Node newNode = new Node(data);
        newNode.next = head;
        if (head == null) {
            head = newNode;
        }
        else {
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }

    public int removeFirst() {
        if (head == null) {
            throw new NoSuchElementException();
        }
        int  temp = head.data;
        head = head.next;
        if (head != null) {
            head.prev = null;
        }
        size--;
        return temp;
    }

    public int getFirst() {
        return head.data;
    }

    public void addLast(int data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
            head = newNode;
        }
        else {
            Node temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
            newNode.prev = temp;
        }
        size++;
    }

    public int getSize() {
        return size;
    }

    public void MergeSort() {
        Queue q = new Queue();

        Node curr =  head;
        while (curr != null) {
            LinkedList newList = new LinkedList();
            newList.addFirst(curr.data);
            q.enqueue(newList);
            curr = curr.next;
        }

        while (q.size() > 1) {
            LinkedList sublist1 = q.dequeue();
            LinkedList sublist2 = q.dequeue();
            LinkedList tempList = merge(sublist1, sublist2);
            q.enqueue(tempList);
        }

        LinkedList sorted = q.dequeue();
        this.head =  sorted.head;
        this.size = sorted.size;
    }

    private LinkedList merge(LinkedList A, LinkedList B) {
        LinkedList S = new LinkedList();

        while (!A.isEmpty() && !B.isEmpty()) {
            int fa = A.getFirst();
            int fb = B.getFirst();

            if (fa < fb) {
                A.removeFirst();
                S.addLast(fa);
            }
            else {
                B.removeFirst();
                S.addLast(fb);
            }
        }

        while (!A.isEmpty()){
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

    public void InsertionSort() {
        if (size <= 1) return;

        Node sorted = null;
        Node curr = head;

        while (curr != null) {

            Node next = curr.next;
            curr.prev = curr.next;

            if (sorted == null || curr.data < sorted.data) {
                curr.next = sorted;

                if (sorted != null) {
                    sorted.prev = curr;
                }

                sorted = curr;
            } else {
                Node temp = sorted;
                while (temp.next != null && temp.next.data < curr.data) {
                    temp = temp.next;
                }

                curr.next = temp.next;

                if (temp.next != null) {
                    temp.next.prev = curr;
                }

                temp.next = curr;
                curr.prev = temp;
            }

            curr = next;
        }

        head = sorted;
    }

    public boolean isSorted() {
        Node curr = head;
        while(curr.next != null) {
            if(curr.data > curr.next.data) return false;
            curr = curr.next;
        }
        return true;
    }
}