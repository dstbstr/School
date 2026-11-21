public class LinkedList {

    private class ListNode {
        private Object data;
        private ListNode next;

        private ListNode(Object data) {
            this.data = data;
            this.next = null;
        }
    }

    private ListNode head;
    private ListNode tail;
    private int size;

    public LinkedList() {
        this.head = new ListNode(null);
        this.tail = this.head;
        this.size = 0;
    }

    public int getSize() {
        return this.size;
    }


    public void addFirst(Object o) {
        ListNode node = new ListNode(o);
        node.next = head.next;
        head.next = node;
        if (tail == head) {
            tail = node;
        }
        size++;
    }

    public void addLast(Object o) {
        ListNode node = new ListNode(o);
        tail.next = node;
        tail = node;
        size++;
    }

    public Object getFirst() {
        if (head.next == null) throw new RuntimeException("The list is empty");

        return head.next.data;
    }

    public Object removeFirst() {
        if (head.next == null) {
            throw new RuntimeException("The list is empty");
        }
        ListNode first = head.next;
        head.next = first.next;
        if (first == tail) {
            tail = head;
        }
        first.next = null;
        size--;
        return first.data;
    }

    public boolean isSorted() {
        if (size <= 1) return true;

        ListNode cur = head.next;

        Comparable prevData = (Comparable) cur.data;
        cur = cur.next;

        while (cur != null) {
            Comparable curData = (Comparable) cur.data;
            if (prevData.compareTo(curData) > 0) {
                return false;
            }
            prevData = curData;
            cur = cur.next;
        }
        return true;
    }

    public void MergeSort() {
        if (this.size <= 1) return;

        Queue q = new Queue();

        for (ListNode cur = this.head.next; cur != null; cur = cur.next) {
            LinkedList singlElem = new LinkedList();
            singlElem.addFirst(cur.data);
            q.enqueue(singlElem);
        }

        this.head.next = null;
        this.tail = this.head;
        this.size = 0;

        while (q.size() > 1) {
            LinkedList sub1 = q.dequeue();
            LinkedList sub2 = q.dequeue();
            LinkedList merged = merge(sub1, sub2);
            q.enqueue(merged);
        }

        if (q.size() == 1) {

            LinkedList sorted = q.dequeue();

            for (ListNode cur = sorted.head.next; cur != null; cur = cur.next) {
                this.addLast(cur.data);
            }
        }
    }


    public void InsertionSort() {
        if (this.size <= 1) return;

        ListNode sortedDummy = new ListNode(null);

        ListNode firstUnsorted = this.head.next;

        while (firstUnsorted != null) {
            ListNode nextUnsorted = firstUnsorted.next;

            ListNode lastSorted = sortedDummy;
            ListNode sortedWalker;

            for (sortedWalker = sortedDummy.next;
                 sortedWalker != null && compare(sortedWalker.data, firstUnsorted.data) <= 0;
                 sortedWalker = sortedWalker.next) {

                lastSorted = sortedWalker;
            }

            firstUnsorted.next = sortedWalker;
            lastSorted.next = firstUnsorted;

            firstUnsorted = nextUnsorted;
        }

        this.head.next = sortedDummy.next;

        this.tail = this.head;
        ListNode walker = this.head.next;
        while (walker != null) {
            this.tail = walker;
            walker = walker.next;
        }
    }

    private int compare(Object a, Object b) {

        if (a == null && b == null) return 0;
        if (a == null) return -1;
        if (b == null) return 1;

        return ((Comparable) a).compareTo(b);
    }

    private static LinkedList merge(LinkedList A, LinkedList B) {
        LinkedList S = new LinkedList();

        while (A.getSize() > 0 && B.getSize() > 0) {
            Comparable fa = (Comparable) A.getFirst();
            Comparable fb = (Comparable) B.getFirst();

            if (fa.compareTo(fb) < 0) {
                A.removeFirst();
                S.addLast(fa);
            } else {
                B.removeFirst();
                S.addLast(fb);
            }
        }

        while (A.getSize() > 0) {
            Comparable fa = (Comparable) A.getFirst();
            S.addLast(fa);
            A.removeFirst();
        }

        while (B.getSize() > 0) {
            Comparable fb = (Comparable) B.getFirst();
            S.addLast(fb);
            B.removeFirst();
        }

        return S;
    }



}
















