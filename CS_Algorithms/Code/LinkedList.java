public class LinkedList {
    public class Node {
        int Value;
        Node next = null;
    }

    private Node head;
    private Node tail;

    void Add(int val) {
        Node node = new Node();
        node.Value = val;
        if(tail == null) {
            tail = node;
            if(head == null) {
                head = tail;
            }
        } else {
            tail.next = node;
        }
    }

    Node Find(int val) {
        Node curr = head;
        while(curr != null) {
            if(curr.Value == val) return curr;
            curr = curr.next;
        }
        return null;
    }
    
    void Remove(int val) {
        Node curr = head;
        Node prev = null;
        while(curr != null) {
            if(curr.Value == val) {
                if(curr == head) {
                    head = curr.next;
                } else {
                    prev.next = curr.next;
                }
                return;
            } else {
                prev = curr;
                curr = curr.next;
            }
        }
    }

    int size() {
        int result = 0;
        Node current = head;
        while(current != null) {
            result++;
            current = current.next;
        }
        return result;
    }
}
