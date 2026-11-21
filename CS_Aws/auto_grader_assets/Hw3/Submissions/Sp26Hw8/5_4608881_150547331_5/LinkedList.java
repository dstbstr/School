public class LinkedList {
    private static class ListNode {
        int data;
        ListNode next;

        ListNode(int data) {
            this.data = data;
            this.next = null;
        }
    }

    private ListNode head;
    private ListNode tail;
    private int size;

    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public void addFirst(int value) {
        ListNode newNode = new ListNode(value);
        newNode.next = head;
        head = newNode;
        if (tail == null) {
            tail = newNode;
        }
        size++;
    }

    public void addLast(int value) {
        ListNode newNode = new ListNode(value);
        if (tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public int getFirst() {
        if (head == null) {
            throw new IllegalStateException("List is empty");
        }
        return head.data;
    }

    public void removeFirst() {
        if (head == null) {
            throw new IllegalStateException("List is empty");
        }
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // --- Merge Sort (Queue-based) ---
    public void MergeSort() {
        if (head == null || head.next == null) {
            return;
        }

        Queue q = new Queue();

        ListNode current = head;
        while (current != null) {
            ListNode next = current.next;
            current.next = null;
            LinkedList newList = new LinkedList();
            newList.addFirst(current.data);
            q.enqueue(newList);
            current = next;
        }

        while (q.size() > 1) {
            LinkedList sublist1 = q.dequeue();
            LinkedList sublist2 = q.dequeue();
            LinkedList tempList = merge(sublist1, sublist2);
            q.enqueue(tempList);
        }

        LinkedList sortedList = q.dequeue();
        this.head = sortedList.head;
        this.tail = sortedList.tail;
        this.size = sortedList.size;
    }

    private LinkedList merge(LinkedList A, LinkedList B) {
        LinkedList S = new LinkedList();
        while (!A.isEmpty() && !B.isEmpty()) {
            int fa = A.getFirst();
            int fb = B.getFirst();
            if (fa < fb) {
                S.addLast(fa);
                A.removeFirst();
            } else {
                S.addLast(fb);
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

    // --- Insertion Sort ---
    public void InsertionSort() {
        if (head == null || head.next == null) {
            return;
        }

        ListNode sorted = null;
        ListNode current = head;

        while (current != null) {
            ListNode next = current.next;
            sorted = addOrdered(sorted, current);
            current = next;
        }

        head = sorted;
        updateTail();
    }

    private ListNode addOrdered(ListNode sorted, ListNode newNode) {
        newNode.next = null;

        if (sorted == null || newNode.data <= sorted.data) {
            newNode.next = sorted;
            return newNode;
        }

        ListNode current = sorted;
        while (current.next != null && current.next.data < newNode.data) {
            current = current.next;
        }

        newNode.next = current.next;
        current.next = newNode;
        return sorted;
    }

    private void updateTail() {
        tail = head;
        if (tail == null) return;
        while (tail.next != null) {
            tail = tail.next;
        }
    }

    // --- isSorted ---
    public boolean isSorted() {
        if (head == null || head.next == null) {
            return true;
        }
        ListNode current = head;
        while (current.next != null) {
            if (current.data > current.next.data) {
                return false;
            }
            current = current.next;
        }
        return true;
    }
}
