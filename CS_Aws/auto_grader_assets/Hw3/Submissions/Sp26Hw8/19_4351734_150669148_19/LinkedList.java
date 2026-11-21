public class LinkedList {
    private class Node {
        int data;
        Node next;

        public Node(int data) {
            this.data = data;
            this.next = null;
        }
    }
    private Node head;
    private Node tail;
    private int size;

    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    public void addFirst(int data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = tail = newNode;
        }else{
            newNode.next = head;
            head = newNode;
        }
        size++;
    }
    // tail pointer is used to maintian O(1) time complexity
    public void addLast(int data) {
        Node newNode = new Node(data);
        if (head == null) {
            head = tail = newNode;
        } else {
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }

    public int getFirst() {
        if (head == null) {
            throw new RuntimeException("List is empty");
        }
        return head.data;
    }

    public void removeFirst() {
        if (head == null) {
            return;
        }
        head = head.next;
        if (head == null) {
            tail = null;
        }
        size--;
    }
    public int getSize(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public boolean isSorted(){
        if (head == null || head.next == null) {
            return true;
        }
        Node curr = head;
        while (curr.next != null) {
            if (curr.data > curr.next.data) {
                return false;
            }
            curr = curr.next;
        }
        return true;
    }

    public void MergeSort(){
        Queue q = new Queue();
        Node curr = this.head;

        // Enqueue each data object as a single node linked list
        while (curr != null) {
            LinkedList newList = new LinkedList();
            newList.addFirst(curr.data);
            q.enqueue(newList);
            curr = curr.next;
        }

        // merge sublists until only one remains in the queue
        while (q.size() > 1){
            LinkedList sublist1 = q.dequeue();
            LinkedList sublist2 = q.dequeue();
            LinkedList tempList = merge(sublist1, sublist2);
            q.enqueue(tempList);
        }

        // Dequeue sorted list and update current object
        if (q.size() == 1){
            LinkedList sortedList = q.dequeue();
            this.head = sortedList.head;
            this.tail = sortedList.tail;
            this.size = sortedList.size;
        }
    }//end mergeSort
    private LinkedList merge(LinkedList A, LinkedList B) {
        LinkedList S = new LinkedList();

        while (!A.isEmpty() && !B.isEmpty()) {
            int fa = A.getFirst();
            int fb = B.getFirst();

            if(fa < fb){
                A.removeFirst();
                S.addLast(fa);
            } else {
                B.removeFirst();
                S.addLast(fb);
            }
        }
        while(!A.isEmpty()){
            int fa = A.getFirst();
            S.addLast(fa);
            A.removeFirst();
        }
        while(!B.isEmpty()){
            int fb = B.getFirst();
            S.addLast(fb);
            B.removeFirst();
        }

        return S;
    } // end Merge

    public void InsertionSort(){
        if (head == null || head.next == null) {
            return;
        }
        Node sortedHead = null;
        Node curr = head;

        while(curr != null){
            Node nextNode = curr.next;

            if(sortedHead == null || sortedHead.data >= curr.data){
                curr.next = sortedHead;
                sortedHead = curr;
            } else {
                Node search = sortedHead;
                while(search.next != null && search.next.data < curr.data){
                    search = search.next;
                }
                curr.next = search.next;
                search.next = curr;
            }
            curr = nextNode;
        }
        this.head = sortedHead;

        Node t = head;
        int count = 0;
        while(t != null && t.next != null){
            t = t.next;
            count++;
        }
        if (t != null) count++;
        this.tail = t;
        this.size = count;
    }

}//end class
