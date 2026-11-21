public class CDoublyLinkedList {

    private class Node {
        private Object data;
        private Node next, prev;

        private Node(Object data, Node prev, Node next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }

    private Node head;
    private int size;

    public CDoublyLinkedList() {
        this.head = new Node(null, null, null);
        this.head.next = this.head;
        this.head.prev = this.head;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.head == this.head.next;
    }

    public void addFirst(Object data) {
        Node nn = new Node(data, this.head, this.head.next);
        this.head.next.prev = nn;
        this.head.next = nn;
        this.size++;
    }

    public void addLast(Object data) {
        Node nn = new Node(data, this.head.prev, this.head);
        this.head.prev.next = nn;
        this.head.prev = nn;
        this.size++;
    }

    public CDoublyLinkedList subListOfSmallerValues(Comparable data) {
        CDoublyLinkedList result = new CDoublyLinkedList();
        Node curr = this.head.next;
        while (curr != this.head) {
            if (curr.data != null) {
                Comparable c = (Comparable) curr.data;
                if (c.compareTo(data) < 0) {
                    result.addLast(curr.data);
                }
            }
            curr = curr.next;
        }
        return result;
    }

    public boolean removeStartingAtBack(Object dataToRemove) {
        Node curr = this.head.prev;
        while (curr != this.head) {
            if ((curr.data == null && dataToRemove == null)
                    || (curr.data != null && curr.data.equals(dataToRemove))) {
                curr.prev.next = curr.next;
                curr.next.prev = curr.prev;
                size--;
                return true;
            }
            curr = curr.prev;
        }
        return false;
    }

    public int lastIndexOf(Object o) {
        int index = this.size - 1;
        Node curr = this.head.prev;
        while (curr != this.head) {
            if ((o == null && curr.data == null)
                    || (o != null && o.equals(curr.data))) {
                return index;
            }
            index--;
            curr = curr.prev;
        }
        return -1;
    }

    public boolean retainAll(CDoublyLinkedList other) {
        if (other == null) {
            throw new NullPointerException();
        }
        boolean changed = false;
        Node curr = this.head.next;
        while (curr != this.head) {
            Node next = curr.next;
            boolean found = false;
            Node oCurr = other.head.next;
            while (oCurr != other.head) {
                if ((curr.data == null && oCurr.data == null)
                        || (curr.data != null && curr.data.equals(oCurr.data))) {
                    found = true;
                    break;
                }
                oCurr = oCurr.next;
            }
            if (!found) {
                curr.prev.next = curr.next;
                curr.next.prev = curr.prev;
                size--;
                changed = true;
            }
            curr = next;
        }
        return changed;
    }

    public void insertionSort() {
        if (size <= 1) {
            return;
        }
        Node curr = head.next.next;
        while (curr != head) {
            Object val = curr.data;
            Node nextNode = curr.next;

            curr.prev.next = curr.next;
            curr.next.prev = curr.prev;

            Node move = curr.prev;
            while (move != head && move.data != null
                    && ((Comparable) move.data).compareTo(val) > 0) {
                move = move.prev;
            }

            Node newNode = new Node(val, move, move.next);
            move.next.prev = newNode;
            move.next = newNode;

            curr = nextNode;
        }
    }

    @Override
    public String toString() {
        String result = "{";
        for (Node node = head.next; node != head; node = node.next) {
            if (node.next != head) {
                result += node.data + "->";
            } else {
                result += node.data;
            }
        }
        return result + "}";
    }
}