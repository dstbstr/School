public class LinkedList {
    private class Node {
        private int data;
        private Node next;

        public Node(int d) {
            this.data = d;
            this.next = null;
        }
    }

    protected Node head;//, tail;
    protected int size;

    public LinkedList() {
        head = new Node(00);
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    public void addFirst(int d) {
        Node newNode = new Node(d);
        newNode.next = head.next;
        head.next = newNode;
        size++;
    }

    public void addLast(int d) {
        Node newNode = new Node(d);
        Node cur = head;
        while (cur.next != null) {
            cur = cur.next;
        }
        cur.next = newNode;
        size++;
    }

    public int removeFirst() {
        if (isEmpty()) {
            System.out.println("List is empty");
            return -1;
        }
        int cur = head.next.data;
        head.next = head.next.next;
        size--;
        return cur;
    }

    public int getFirst() {
        if (isEmpty()) {
            System.out.println("List is empty");
            return -1;
        }
        return head.next.data;
    }

    private void replaceOrig(LinkedList clone) {
        this.head = clone.head;
        this.size = clone.size;
    }

    public static LinkedList merge(LinkedList A, LinkedList B) {
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

        while (!A.isEmpty()) {
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

    public void MergeSort() {
        if (size <= 1) {
            return;
            }
        Queue q = new Queue();
        for (Node curr = head.next; curr != null; curr = curr.next) {
            LinkedList newList = new LinkedList();
            newList.addFirst(curr.data);
            q.enqueue(newList);
        }
        while (q.size() > 1) {
            LinkedList L1 = q.dequeue();
            LinkedList L2 = q.dequeue();
            LinkedList tempList = merge(L1, L2);
            q.enqueue(tempList);
        }

        LinkedList sorted = q.dequeue();
        this.replaceOrig(sorted);
    }

    public void InsertionSort() {
        if (size <= 1) {
            return;
        }
        LinkedList sorted = new LinkedList();
        Node curr = head.next;
        while (curr != null) {
            int data = curr.data;
            Node prev = sorted.head;
            Node scan = sorted.head.next;

            while (scan != null && scan.data < data) {
                prev = scan;
                scan = scan.next;
            }
            Node newNode = new Node(data);
            newNode.next = prev.next;
            prev.next = newNode;
            sorted.size++;
            curr = curr.next;
        }
        this.replaceOrig(sorted);
    }

    public boolean isSorted() {
        if (size <= 1) {
            return true;
        }
        Node curr = head.next;
        while (curr != null && curr.next != null) {
            if (curr.data > curr.next.data) {
                return false;
            }
            curr = curr.next;
        }
        return true;
    }
}
