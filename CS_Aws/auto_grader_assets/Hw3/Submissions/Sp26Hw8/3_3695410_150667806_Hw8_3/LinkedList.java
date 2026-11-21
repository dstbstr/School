public class LinkedList {

    private class Node{
        Object data;
        Node next;

        public Node(Object data){
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private Node tail;
    private int size;

    public LinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int getSize(){
        return size;
    }

    public boolean isSorted(){
        if (isEmpty() || size == 1){
            return true;
        }

        Node current = head;

        while (current.next != null) {
            Integer currentValue = (Integer)current.data;
            Integer nextValue = (Integer)current.next.data;

            if(currentValue > nextValue){
                return false;
            }

            current = current.next;
        }
        return true;
    }

    public void clear(){
        head = null;
        tail = null;
        size = 0;
    }

    public void addFirst(Object data){
        Node newNode = new Node(data);

        if (head == null){
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head = newNode;
        }
        size++;

    }

    public void addLast(Object data){
        Node newNode = new Node(data);

        if (head == null){
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public Object getFirst(){
        if(isEmpty()){
            return null;
        }
        return head.data;
    }

    public Object removeFirst(){
        if(isEmpty()){
            return null;
        }
        Object value = head.data;
        head = head.next;
        size--;
        if (head == null) {
            tail = null;
        }
        return value;
    }

    public void addOrdered(Integer value){

        if (isEmpty()){
            addFirst(value);
            return;
        }

        if (value <= (Integer)head.data) {
            addFirst(value);
            return;
        }

        Node current = head;

        while (current.next != null && value > (Integer)current.next.data) {
            current = current.next;
        }
        Node newNode = new Node(value);

        newNode.next = current.next;
        current.next = newNode;

        if (newNode.next == null) {
            tail = newNode;
        }

        size++;

    }

    private LinkedList Merge(LinkedList a, LinkedList b){
        LinkedList s = new LinkedList();

        while (!a.isEmpty() && !b.isEmpty()) {
            Integer fa = (Integer)a.getFirst();
            Integer fb = (Integer)b.getFirst();

            if (fa < fb) {
                a.removeFirst();
                s.addLast(fa);

            } else {
                b.removeFirst();
                s.addLast(fb);
            }
        }

        while (!a.isEmpty()) {
            Integer fa = (Integer)a.getFirst();
            s.addLast(fa);
            a.removeFirst();
        }
        while (!b.isEmpty()) {
            Integer fb = (Integer)b.getFirst();
            b.removeFirst();
            s.addLast(fb);
        }

        return s;

    }

    public void insertionSort() {
        LinkedList sorted = new LinkedList();

        while(!isEmpty()) {
            Integer value = (Integer)removeFirst();
            sorted.addOrdered(value);
        }

        while (!sorted.isEmpty()){
            addLast(sorted.removeFirst());
        }
    }

    public void MergeSort() {
        if (size <= 1) {
            return;
        }

        Queue q = new Queue();

        Node current = head;

        //Step 1
        while (current != null) {
            LinkedList newList = new LinkedList();

            newList.addFirst(current.data);
            q.enqueue(newList);
            current = current.next;
        }

        //Step 2
        while(q.size() > 1) {
            LinkedList a = (LinkedList)q.dequeue();
            LinkedList b = (LinkedList)q.dequeue();

            if(b == null) {
                q.enqueue(a);
                break;
            }

            LinkedList merged = Merge(a, b);
            q.enqueue(merged);
        }

        //Step 3
        LinkedList sorted = (LinkedList)q.dequeue();
        clear();
        while (!sorted.isEmpty()) {
            addLast(sorted.removeFirst());
        }

    }



    
}
