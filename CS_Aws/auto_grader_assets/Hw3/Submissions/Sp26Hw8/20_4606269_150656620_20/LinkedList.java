public class LinkedList {
    private ListNode head;
    private ListNode tail;
    private int size;

    // This is the ListNode class
    private class ListNode {
        private Object data;
        private ListNode next;

        private ListNode(Object d) { // ListNode constructor
            this.data = d; // Every Node will have data
            this.next = null; // every node will point to the next Node
        }
    }

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public Object getFirst() {
        if (head == null) {
            return null;
        }
        return head.data;
    } // end of getFirst method

    public void addLast(Object data) {
        ListNode toAdd = new ListNode(data);
        if (head == null) {
            head = toAdd;
            tail = toAdd;
        } else {
            tail.next = toAdd;
            tail = toAdd;
        }
        size++;
    } // end of addLast method

    public boolean isSorted() {
        ListNode current = head;
        while (current != null && current.next != null) {
            Comparable currentData = (Comparable) current.data;
            Comparable nextData = (Comparable) current.next.data;
            if (currentData.compareTo(nextData) > 0) {
                return false;
            }
            current = current.next;
        }
        return true;
    } // last of isSorted method

    public void addFirst(Object data) {
        ListNode nn = new ListNode(data);
        nn.next = head;
        head = nn;
        if (tail == null) {
            tail = nn;
        }
        size++;
    }

    public Object removeFirst() {
        if (head == null) {
            return null;
        }
        Object toRemove = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
        return toRemove;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    public void InsertionSort() {
        // InsertionSort builds a new sorted list one item at a time.
        // It repeatedly removes the first node from this list and inserts
        // that value into its correct position in the 'sorted' list.
        LinkedList sorted = new LinkedList();

        while (!this.isEmpty()) {
            Object item = this.removeFirst();
            sorted.addOrdered(item);
        }

        // Replace this list's internal pointers with the sorted result.
        this.head = sorted.head;
        this.tail = sorted.tail;
        this.size = sorted.size;
    }

    public void MergeSort() {
        // MergeSort uses a queue of LinkedList objects.
        // Step 1: convert each data item into a single-node LinkedList and enqueue it.
        // Step 2: repeatedly dequeue two already-sorted sublists, merge them,
        // and enqueue the larger sorted sublist.
        // Step 3: when one list remains, copy it back into this list.
        if (this.size <= 1) {
            return;
        }

        LinkedQueue q = new LinkedQueue();
        ListNode cursor = this.head;

        // Build queue of single-node linked lists.
        while (cursor != null) {
            LinkedList newList = new LinkedList();
            newList.addFirst(cursor.data);
            q.enqueue(newList);
            cursor = cursor.next;
        }

        // Merge sublists pairwise until one sorted list remains.
        while (q.size() > 1) {
            LinkedList sublist1;
            LinkedList sublist2;
            try {
                sublist1 = (LinkedList) q.dequeue();
                sublist2 = (LinkedList) q.dequeue();
            } catch (Exception e) {
                throw new RuntimeException("Unexpected queue state during MergeSort", e);
            }

            LinkedList tempList = merge(sublist1, sublist2);
            q.enqueue(tempList);
        }

        LinkedList sorted;
        try {
            sorted = (LinkedList) q.dequeue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract final sorted list", e);
        }

        this.clear();
        while (!sorted.isEmpty()) {
            this.addLast(sorted.removeFirst());
        }
    }

    private LinkedList merge(LinkedList a, LinkedList b) {
        // merge() is the helper used by MergeSort.
        // Input: two sorted linked lists 'a' and 'b'.
        // Output: one sorted linked list containing all elements from both.
        // It compares first elements, moves the smaller one to the result,
        // then appends any leftovers after one input list becomes empty.
        LinkedList s = new LinkedList();

        while (!a.isEmpty() && !b.isEmpty()) {
            Comparable fa = (Comparable) a.getFirst();
            Comparable fb = (Comparable) b.getFirst();

            if (fa.compareTo(fb) < 0) {
                s.addLast(fa);
                a.removeFirst();
            } else {
                s.addLast(fb);
                b.removeFirst();
            }
        

        while (!a.isEmpty()) {
            s.addLast(a.getFirst());
            a.removeFirst();
        }

        while (!b.isEmpty()) {
            s.addLast(b.getFirst());
            b.removeFirst();
        }

        return s;
    }

    private void addOrdered(Object data) {
        ListNode nn = new ListNode(data);
        Comparable newValue = (Comparable) data;

        if (head == null) {
            head = nn;
            tail = nn;
            size++;
            return;
        }

        Comparable headValue = (Comparable) head.data;
        if (newValue.compareTo(headValue) <= 0) {
            nn.next = head;
            head = nn;
            size++;
            return;
        }

        ListNode prev = head;
        ListNode curr = head.next;

        while (curr != null) {
            Comparable currValue = (Comparable) curr.data;
            if (newValue.compareTo(currValue) <= 0) {
                break;
            }
            prev = curr;
            curr = curr.next;
        }

        prev.next = nn;
        nn.next = curr;
        if (curr == null) {
            tail = nn;
        }
        size++;
    }

}