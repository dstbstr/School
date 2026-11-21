import java.util.NoSuchElementException;

public class MyLinkedList {

    private ListNode head;
    private int size;

    // inner class for ListNode
    private class ListNode {
        private Object data;
        private ListNode next;

        private ListNode(Object d) {
            this.data = d;
            this.next = null;
        }
    }

    public MyLinkedList() {
        this.head = new ListNode(null); // dummy head node
        this.size = 0;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    // Provided addFirst (DO NOT CHANGE)
    public void addFirst(Object e) {
        ListNode node = new ListNode(e);
        node.next = head.next;
        head.next = node;
        size++;
    }

    // 1). removeFirst() method:
    // if list is empty
    //     throw exception
    // else
    //     temp gets head.data
    //     move head to head.next
    //     return temp
    public Object removeFirst() throws Exception {
        if (size == 0)
            throw new NoSuchElementException("List is empty.");

        ListNode first = head.next;
        head.next = first.next;
        size--;
        return first.data;
    }

    // 2). contains(Object targetData) method:
    // cur gets head
    // while cur is not null
    //     if cur.data equals targetData
    //         return true
    //     move cur to cur.next
    // return false
    public boolean contains(Object o) {
        for (ListNode cur = head.next; cur != null; cur = cur.next) {
            if (o == null ? cur.data == null : o.equals(cur.data)) {
                return true;
            }
        }
        return false;
    }

    // 3). remove(Object targetData) method:
    // if list is empty
    //     return false
    // cur gets head
    // while cur.next is not null
    //     if cur.next.data equals targetData
    //         cur.next gets cur.next.next
    //         return true
    //     move cur to cur.next
    // return false
    public boolean remove(Object o) {
        if (size == 0)
            return false;

        ListNode prev = head;
        while (prev.next != null) {
            // for the sake of efficiency I'm using ":" to iterate in what would otherwise be a "for" loop.
            // Let me know if I should use a traditional control structure for homework in the future please
            // In addition while playing with operators I figured out how to use the "?" operator for logic operations
            // Pretty cool stuff :)
            if (o == null ? prev.next.data == null : o.equals(prev.next.data)) {
                prev.next = prev.next.next;
                size--;
                return true;
            }
            prev = prev.next;
        }
        return false;
    }

    // 4). removeAllCopies(Object targetData) method:
    // found gets false
    // cur gets head
    // while cur.next is not null
    //     if cur.next.data equals targetData
    //         cur.next gets cur.next.next
    //         found gets true
    //     else
    //         move cur to cur.next
    // return found
    public boolean removeAllCopies(Object o) {
        boolean found = false;
        ListNode prev = head;

        while (prev.next != null) {
            if (o == null ? prev.next.data == null : o.equals(prev.next.data)) {
                prev.next = prev.next.next;
                size--;
                found = true;
            } else {
                prev = prev.next;
            }
        }
        return found;
    }

    // 5). interleave(MyLinkedList A, MyLinkedList B) method:
    // make new list result
    // pointerA gets A.head
    // pointerB gets B.head
    // while pointerA is not null or pointerB is not null
    //     if pointerA is not null
    //         add pointerA.data to result
    //         move pointerA to pointerA.next
    //     if pointerB is not null
    //         add pointerB.data to result
    //         move pointerB to pointerB.next
    // return result
    public static MyLinkedList interleave(MyLinkedList A, MyLinkedList B) {
        MyLinkedList C = new MyLinkedList();

        ListNode curA = A.head.next;
        ListNode curB = B.head.next;

        while (curA != null || curB != null) {
            if (curA != null) {
                C.add(curA.data);
                curA = curA.next;
            }
            if (curB != null) {
                C.add(curB.data);
                curB = curB.next;
            }
        }

        return C;
    }

    // 6). add(int index, Object targetData) method:
    // if index < 0 or index > this.size
    //     throw IndexOutOfBoundsException
    // cur gets head
    // for i from 0 to index-1
    //     move cur to cur.next
    // make new node with data targetData
    // newNode.next gets cur.next
    // cur.next gets newNode
    public void add(int index, Object o) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Invalid index: " + index);

        ListNode prev = head;
        for (int i = 0; i < index; i++) {
            prev = prev.next;
        }

        ListNode newNode = new ListNode(o);
        newNode.next = prev.next;
        prev.next = newNode;
        size++;
    }

    // 7). get(int index) method:
    // if index < 0 or index >= this.size
    //     throw IndexOutOfBoundsException
    // cur gets head
    // for i from 0 to index
    //     move cur to cur.next
    // return cur.data
    public Object get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Invalid index: " + index);

        ListNode cur = head.next;
        for (int i = 0; i < index; i++) {
            cur = cur.next;
        }

        return cur.data;
    }

    // 8). remove(int index) method:
    // if index < 0 or index >= this.size
    //     throw IndexOutOfBoundsException
    // cur gets head
    // for i from 0 to index-1
    //     move cur to cur.next
    // temp gets cur.next.data
    // cur.next gets cur.next.next
    // return temp
    public Object remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Invalid index: " + index);

        ListNode prev = head;
        for (int i = 0; i < index; i++) {
            prev = prev.next;
        }

        ListNode target = prev.next;
        prev.next = target.next;
        size--;
        return target.data;
    }

    // 9). add(Object e) method:
    // make new node with data e
    // if list is empty
    //     head gets new node
    //     return true
    // cur gets head
    // while cur.next is not null
    //     move cur to cur.next
    // cur.next gets new node
    // return true
    public boolean add(Object e) {
        ListNode cur = head;
        while (cur.next != null) {
            cur = cur.next;
        }

        cur.next = new ListNode(e);
        size++;
        return true;
    }

    // DO NOT CHANGE
    @Override
    public String toString() {
        String result = "{";
        for (ListNode node = this.head.next; node != null; node = node.next) {
            if (node.next != null)
                result += node.data + "->";
            else
                result += node.data;
        }
        return result + "}";
    }
}
