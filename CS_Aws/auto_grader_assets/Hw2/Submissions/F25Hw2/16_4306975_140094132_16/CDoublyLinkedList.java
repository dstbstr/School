public class CDoublyLinkedList {

    private class Node {
        private Object data;   //Assume data implemented Comparable
        private Node next, prev;
        private Node(Object data, Node pref, Node next)
        {
            this.data = data;
            this.prev = pref;
            this.next = next;
        }
    }

    private Node head;
    private int size;

    public CDoublyLinkedList() {
        this.head = new Node(null, null, null );
        this.head.next = this.head;
        this.head.prev=this.head;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.head == this.head.next;
    }

    public void addFirst(Object data) {
        Node nn = new Node(data, this.head, this.head.next);
        this.head.next.prev = nn;
        this.head.next = nn;
        this.size ++;
    }

    // 1). addLast(data) method:
    //
    // make new node with data, previous = head.prev, next = head
    // head.prev.next gets new node
    // head.prev gets new node
    // size gets size + 1
    public void addLast(Object data) {
        Node nn = new Node(data, this.head.prev, this.head);
        this.head.prev.next = nn;
        this.head.prev = nn;
        this.size++;
    }

    // 2). subListOfSmallerValues(data) method:
    //
    // make new CDoublyLinkedList called newList
    // current gets head.next
    //
    // while current is not head
    //     if current.data is not null
    //         if (current.data as Comparable) less than data
    //         newList.addLast(current.data)
    //         move current to current.next
    // return newList
    public CDoublyLinkedList subListOfSmallerValues(Comparable data) {
        CDoublyLinkedList newList = new CDoublyLinkedList();
        Node current = this.head.next;
        while (current != this.head) {
            if (current.data != null) {
                if (((Comparable) current.data).compareTo(data) < 0) {
                    newList.addLast(current.data);
                }
            }
            current = current.next;
        }
        return newList;
    }

    // 3). removeStartingAtBack(dataToRemove) method:
    //
    // current gets head.prev
    //
    // while current is not head
    //     if dataToRemove is null and current.data is null
    //         current.prev.next gets current.next
    //         current.next.prev gets current.prev
    //         size gets size - 1
    //         return true
    //     else if dataToRemove is not null and dataToRemove equals current.data
    //         current.prev.next gets current.next
    //         current.next.prev gets current.prev
    //         size gets size - 1
    //         return true
    // move current to current.prev
    //
    // return false
    public boolean removeStartingAtBack(Object dataToRemove) {
        Node current = this.head.prev;
        while (current != this.head) {
            if (dataToRemove == null && current.data == null) {
                current.prev.next = current.next;
                current.next.prev = current.prev;
                this.size--;
                return true;
            } else if (dataToRemove != null && dataToRemove.equals(current.data)) {
                current.prev.next = current.next;
                current.next.prev = current.prev;
                this.size--;
                return true;
            }
            current = current.prev;
        }
        return false;
    }

    // 4). lastIndexOf(o) method:
    //
    // index gets size - 1
    // current gets head.prev
    //
    // while current is not head
    //     if o is null and current.data is null
    //         return index
    //     else if o is not null and o equals current.data
    //         return index
    // move current to current.prev
    // index gets index - 1
    //
    // return -1
    public int lastIndexOf(Object o) {
        int index = this.size - 1;
        Node current = this.head.prev;
        while (current != this.head) {
            if (o == null && current.data == null) {
                return index;
            } else if (o != null && o.equals(current.data)) {
                return index;
            }
            current = current.prev;
            index--;
        }
        return -1;
    }


    // 5). retainAll(other) method:
    //
    // if other is null
    //     throw NullPointerException
    //
    // modified gets false
    // current gets head.next
    //
    // while current is not head
    //     nextNode gets current.next
    //     if other does not contain current.data
    //         current.prev.next gets current.next
    //         current.next.prev gets current.prev
    //         size gets size - 1
    //         modified gets true
    //         current gets nextNode
    // return modified
    public boolean retainAll(CDoublyLinkedList other) throws NullPointerException {
        if (other == null) throw new NullPointerException();
        boolean modified = false;
        Node current = this.head.next;
        while (current != this.head) {
            Node nextNode = current.next;
            if (!other.contains(current.data)) {
                current.prev.next = current.next;
                current.next.prev = current.prev;
                this.size--;
                modified = true;
            }
            current = nextNode;
        }
        return modified;
    }

    // 6). contains(data) method:
    //
    // node gets head.next
    //
    // while node is not head
    //     if data is null and node.data is null
    //         return true
    //     if data is not null and data equals node.data
    //         return true
    // move node to node.next
    // return false
    public boolean contains(Object data) {
        Node node = this.head.next;
        while (node != this.head) {
            if (data == null && node.data == null) return true;
            if (data != null && data.equals(node.data)) return true;
            node = node.next;
        }
        return false;
    }

    // 7). insertionSort() method:
    //
    // if size less than or equal to 1
    //     return
    // current gets head.next.next
    // while current is not head
    //     key gets current.data
    //     previous gets current.prev
    // while previous is not head and previous.data is not null and key is not null and (previous.data as Comparable) greater than key
    //     previous gets previous.prev
    // nextNode gets current.next
    // current.prev.next gets current.next
    // current.next.prev gets current.prev
    // current.next gets previous.next
    // current.prev gets previous
    // previous.next.prev gets current
    // previous.next gets current
    // current gets nextNode
    // return
    public void insertionSort() {
        if (this.size <= 1) return;
        Node current = this.head.next.next;
        while (current != this.head) {
            Object key = current.data;
            Node previous = current.prev;
            while (previous != this.head && previous.data != null && key != null && ((Comparable) previous.data).compareTo(key) > 0) {
                previous = previous.prev;
            }
            Node nextNode = current.next;
            current.prev.next = current.next;
            current.next.prev = current.prev;
            current.next = previous.next;
            current.prev = previous;
            previous.next.prev = current;
            previous.next = current;
            current = nextNode;
        }
    }

    @Override
    public String toString() {
        String result = "{";
        for (Node node = this.head.next; node != this.head; node = node.next) {
            if(node.next != this.head)
                result += node.data + "->";
            else
                result += node.data;
        }
        return result + "}";
    }
}
