import java.util.Iterator;

public class LinkedList<T extends Comparable<T>> implements Iterable<T> {
    private static class Node<T extends Comparable<T>> {
        T data;
        Node<T> prev;
        Node<T> next;

        public Node(T data, Node<T> prev, Node<T> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Node{");
            sb.append("data=").append(data);
            sb.append(", prev=").append(prev == null ? "none" : prev.data);
            sb.append(", next=").append(next == null ? "none" : next.data);
            sb.append('}');
            return sb.toString();
        }
    }

    private Node<T> head;
    private int size;

    public LinkedList() {
        this.head = new Node<>(null, null, null);
        this.head.next = this.head;
        this.head.prev = this.head;

        this.size = 0;
    }

    private Node<T> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        if (index == 0) return this.head.next;
        if (index == this.size - 1) return this.head.prev;

        // TODO: traverse in the shorter direction
        Node<T> current = this.head.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }

        return current;
    }

    private Node<T> findNode(T data, Node<T> startNode) {
        Node<T> current = startNode;
        while (current != this.head) {
            if (current.data.equals(data)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    private Node<T> findNode(T data) {
        return this.findNode(data, this.head.next);
    }

    private void insertNode(T data, Node<T> precedingNode) {
        Node<T> newNode = new Node<>(data, precedingNode, precedingNode.next);
        precedingNode.next.prev = newNode;
        precedingNode.next = newNode;
        this.size++;
    }

    private void removeNode(Node<T> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        this.size--;
    }

    public void add(T data, int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        Node<T> precedingNode = (index == this.size) ? this.head.prev : this.getNode(index).prev;
        this.insertNode(data, precedingNode);
    }

    public void add(T data) {
        this.insertNode(data, this.head.prev);
    }

    public void addFront(T data) {
        this.insertNode(data, this.head);
    }

    public void addAll(LinkedList<T> other) {
//        if (other.isEmpty()) return;
        for (T item : other) {
            this.add(item);
        }
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        Node<T> current = this.getNode(index);
        return current.data;
    }

    public T getFirst() {
        if (isEmpty()) throw new IndexOutOfBoundsException("List is empty");
        return this.head.next.data;
    }

    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index out of bounds: " + index);
        }

        Node<T> current = this.getNode(index);

        this.removeNode(current);
        return current.data;
    }

    public T removeFirst() {
        if (isEmpty()) throw new IndexOutOfBoundsException("List is empty");

        Node<T> first = this.head.next;
        this.removeNode(first);
        return first.data;
    }

    public T removeItem(T data) {
        Node<T> node = this.findNode(data);
        if (node == null) {
            return null;
        }

        this.removeNode(node);
        return node.data;
    }

    public void removeAllItems(T data) {
        Node<T> node = this.findNode(data);
        while (node != null) {
            this.removeNode(node);
            node = this.findNode(data);
        }
    }

    public boolean contains(T data) {
        return this.findNode(data) != null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isSorted() {
        T prev = null;
        for (T item : this) {
            if (prev != null && item.compareTo(prev) < 0) {
                return false;
            }
            prev = item;
        }
        return true;
    }

    private static <T extends Comparable<T>> LinkedList<T> merge(LinkedList<T> left, LinkedList<T> right) {
        LinkedList<T> merged = new LinkedList<>();

        while (!left.isEmpty() && !right.isEmpty()) {
            if (left.getFirst().compareTo(right.getFirst()) <= 0) {
                merged.add(left.removeFirst());
            } else {
                merged.add(right.removeFirst());
            }
        }

        merged.addAll(left);
        merged.addAll(right);

        return merged;
    }

    public void mergeSort() {
        if (this.size() <= 1) return;

        Queue<LinkedList<T>> queue = new Queue<>();
        for (T item : this) {
            LinkedList<T> newList = new LinkedList<>();
            newList.add(item);
            queue.enqueue(newList);
        }

        while (queue.size() > 1) {
            LinkedList<T> left = queue.dequeue();
            LinkedList<T> right = queue.dequeue();
            LinkedList<T> merged = merge(left, right);
            queue.enqueue(merged);
        }

        LinkedList<T> sorted = queue.dequeue();
        this.head = sorted.head;
    }

    public void insertionSort() {
        LinkedList<T> sorted = new LinkedList<>();

        for (T item : this) {
            int i = 0;
            for (T sortedItem : sorted) {
                if (item.compareTo(sortedItem) >= 0) {
                    i++;
                } else {
                    break;
                }
            }
            sorted.add(item, i);
        }

        this.head = sorted.head;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CircularDoublyLinkedList{");
        sb.append("head=").append(head);
        sb.append(", size=").append(size);
        sb.append(", items=[");

        boolean addedItem = false;
        for (T item : this) {
            sb.append(item).append(", ");
            addedItem = true;
        }

        if (addedItem) {
            sb.replace(sb.length() - 2, sb.length(), "]");
        }
        sb.append("}");

        return sb.toString();
    }

    private static class CircularDoublyLinkedListIterator<T extends Comparable<T>> implements Iterator<T> {
        private final LinkedList<T> list;
        private Node<T> current;

        public CircularDoublyLinkedListIterator(LinkedList<T> list) {
            this.list = list;
            this.current = list.head.next;
        }

        @Override
        public boolean hasNext() {
            return current != list.head;
        }

        @Override
        public T next() {
            T data = current.data;
            current = current.next;
            return data;
        }

        @Override
        public void remove() {
            list.removeNode(current);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new CircularDoublyLinkedListIterator<>(this);
    }
}
