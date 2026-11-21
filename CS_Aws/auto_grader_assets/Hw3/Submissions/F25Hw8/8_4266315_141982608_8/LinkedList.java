public class LinkedList {

    private final ListNode head;
    private int size;

    public int getSize() {
        return this.size;
    }

    private static class ListNode {
        private final Integer data;
        private ListNode prev;
        private ListNode next;

        private ListNode(Integer data, ListNode prev, ListNode next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    public LinkedList() {
        head = new ListNode(null, null, null);
        head.next = head;
        head.prev = head;
        size = 0;
    }

    public void addFirst(Integer value) {
        ListNode first = head.next;
        ListNode newNode = new ListNode(value, head, first);

        head.next = newNode;
        first.prev = newNode;

        if (size == 0) head.prev = newNode;

        size++;
    }

    public Integer getFirst() {
        return (size == 0) ? null : head.next.data;
    }

    public Integer removeFirst() {
        if (size == 0) return null;

        Integer result = head.next.data;

        head.next = head.next.next;
        head.next.prev = head;

        size--;
        return result;
    }

    public boolean isSorted() {

        if (size <= 1) return true;

        ListNode cur = head.next;

        while (cur.next != head) {

            if (cur.data.compareTo(cur.next.data) > 0) {
                return false;
            }

            cur = cur.next;
        }

        return true;
    }

    public void addLast(Integer value) {
        ListNode last = head.prev;

        ListNode newNode = new ListNode(value, last, head);

        last.next = newNode;
        head.prev = newNode;

        size++;
    }


    private static LinkedList merge(LinkedList listA, LinkedList listB) {

        LinkedList toReturn = new LinkedList();

        while (listA.size > 0 && listB.size > 0) {
            Integer firstA = listA.getFirst();
            Integer firstB = listB.getFirst();

            if (firstA.compareTo(firstB) <= 0) {
                listA.removeFirst();
                toReturn.addLast(firstA);
            } else {
                listB.removeFirst();
                toReturn.addLast(firstB);
            }
        }

        while (listA.size > 0) {
            toReturn.addLast(listA.getFirst());
            listA.removeFirst();
        }

        while (listB.size > 0) {
            toReturn.addLast(listB.getFirst());
            listB.removeFirst();
        }

        return toReturn;
    }

    public void mergeSort() {

        if (size <= 1) return;


        Queue<LinkedList> queue = new Queue<>();

        while (this.size > 0) {
            Integer data = this.removeFirst();
            LinkedList newList = new LinkedList();
            newList.addFirst(data);
            queue.enqueue(newList);
        }

        while (queue.getSize() > 1) {

            LinkedList sub1 = queue.dequeue();
            LinkedList sub2 = queue.dequeue();

            LinkedList merged = merge(sub1, sub2);

            queue.enqueue(merged);
        }

        LinkedList sorted = queue.dequeue();

        while (sorted.size > 0) {
            this.addLast(sorted.getFirst());
            sorted.removeFirst();
        }
    }

    public void insertionSort() {

        if (size <= 1) return;

        ListNode current = head.next.next;

        while (current != head) {
            ListNode next = current.next;
            ListNode scan = current.prev;

            while (scan != head && current.data.compareTo(scan.data) < 0) {
                scan = scan.prev;
            }

            if (scan == current.prev) {
                current = next;
                continue;
            }

            current.prev.next = current.next;
            current.next.prev = current.prev;

            current.next = scan.next;
            current.prev = scan;

            scan.next.prev = current;
            scan.next = current;

            current = next;
        }
    }
}

