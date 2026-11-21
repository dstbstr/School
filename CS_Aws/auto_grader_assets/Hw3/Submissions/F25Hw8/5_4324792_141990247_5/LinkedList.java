public class LinkedList {
    private class Node {
        private Object data;
        private Node next, prev;
        private Node(Object data, Node next, Node prev) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }
    private Node head;
    private int size;
    public int getSize() {return size;}
    public LinkedList() {
        this.head = new Node(null, null, null);
        this.head.next = this.head;
        this.head.prev = this.head;
        this.size = 0;
    }
    public void addFirst(Object data) {
        Node nn = new Node(data, this.head, this.head.next);
        this.head.next.prev = nn;
        this.head.next = nn;
        this.size ++;
    }
    public void addLast(Object data) {
        Node nn = new Node(data, this.head.prev, this.head);
        this.head.prev.next = nn;
        this.head.prev = nn;
        this.size++;
    }
    public boolean remove(int index) throws IllegalArgumentException {
        if (index < 0 || index >= this.size) throw new IllegalArgumentException("Wrong index value");
        Node cur = this.head.next;
        for (int i = 0; i < index; i++) cur = cur.next;
        cur.prev.next = cur.next;
        cur.next.prev = cur.prev;
        this.size--;
        return true;
    }
    public void add(Object data, int index) {
        if (index < 0 || index >= this.size) throw new IllegalArgumentException("Wrong index value");
        Node cur = this.head;
        for (int i = 0; i < index; i++) cur = cur.next;
        Node newNode = new Node(data, cur, cur.next);
        cur.next.prev = newNode;
        cur.next = newNode;
        this.size++;
    }
    public boolean isSorted() {
        Node cur = this.head.next;
        while (cur.next != this.head) {
            if ((int)cur.data > (int)cur.next.data) return false;
            cur = cur.next;
        }
        return (int) cur.data <= (int) this.head.next.data;
    }
    public void InsertionSort() {
        Node lastSorted, sortedWalker;
        for (lastSorted = this.head.next; lastSorted != this.head; lastSorted = lastSorted.next) {
            int firstUnsorted = (int)lastSorted.data;
            for (sortedWalker = lastSorted.prev; sortedWalker != this.head && (int)sortedWalker.data > firstUnsorted; sortedWalker = sortedWalker.prev) {
                sortedWalker.next.data = sortedWalker.data;
            }
            sortedWalker.next.data = firstUnsorted;
        }
    }
    public void removeFirst() {
        if (this.head.next == this.head) return;
        Node first = this.head.next;
        first.next.prev = this.head;
        this.head.next = first.next;
        this.size--;
    }
    public int getFirst() {
        return (int)this.head.next.data;
    }
    public void MergeSort() {
        Queue q = new Queue();
        for (Node cur = this.head.next; cur != this.head; cur = cur.next) {
            LinkedList newLL = new LinkedList();
            newLL.addLast(cur.data);
            q.enqueue(newLL);
        }
        while (q.size() > 1) {
            LinkedList subList1 = (LinkedList) q.dequeue();
            LinkedList subList2 = (LinkedList) q.dequeue();
            LinkedList tempLL = merge(subList1, subList2);
            q.enqueue(tempLL);
        }
        LinkedList finalLL = (LinkedList) q.dequeue();
        this.head =  finalLL.head;
    }
    public LinkedList merge(LinkedList A, LinkedList B) {
        LinkedList S = new LinkedList();
        int fa, fb;
        while (A.size > 0 && B.size > 0) {
            fa = A.getFirst();
            fb = B.getFirst();
            if (fa < fb) {
                A.removeFirst();
                S.addLast(fa);
            } else {
                B.removeFirst();
                S.addLast(fb);
            }
        }
        while (A.size > 0) {
            fa = A.getFirst();
            A.removeFirst();
            S.addLast(fa);
        }
        while (B.size > 0) {
            fb = B.getFirst();
            B.removeFirst();
            S.addLast(fb);
        }
        return S;
    }
    /*
    Merge sort for LL:
    Create a Queue object q, then enqueue each data object in the LL to be sorted, as a single node LL.
    Pseudo Code:
    {
        Queue q = new Queue()
        For each data object D in the LL to be sorted
        First create a new LL object , newList = new LL
        perform newList.addFirst(D)
        q.enqueue(newList)
    }
    While there is more than one item in the Queue q {
        dequeue and assign to a LL reference sublist1, (already sorted)
        dequeue again and assign to another LL reference sublist2, (already sorted)
        walk through sorted sublist1 and sorted sublist2 and merge them into a large sorted list tempList with tempList = merge(sublist1, sublist2)
        q.enqueue(tempList)
    }
    dequeue your sorted LL. Here think about how to make this list sorted, because the dequeued list and this list are different object.
    Hint: we learned this technique when we used addObject() method to sort a LL.
    How to track time:
    Double then = System.out.currentTimeMillis();
        A.MergeSort();
    Double now = System.out.currentTimeMillis();
        System.out.println("Then: " + then + ", Now: " + now + ", Time taken: " + now  - then);
     */
}
