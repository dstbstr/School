public class Queue {
    private class Node{

        private Object data;
        private Node next;

        public Node(Object elem){
            this.data = elem;
            this.next = null;
        }
    }
    protected Node head;
    protected Node tail;
    protected int size;

    public Queue(){
        this.head = null;
        this.tail = null;
        this.size = 0;
    }

    public int size(){
        return this.size;
    }

    public boolean isEmpty(){
        if(this.head == null && this.tail == null || this.size == 0){
            return true;
        }
        else {
            return false;
        }
    }

    public void enqueue(Object elem){
        Node node = new Node (elem);

        if(size == 0){
            head = node;
        }else{
            tail.next = node;
        }
        tail = node;
        this.size ++;
    }

    public Object front() throws Exception {
        if(size == 0){
            throw new Exception("Queue is Empty");
        }
        return head.data;
    }

    public Object dequeue() throws Exception{
        if(size == 0){
            throw new Exception("Queue is Empty");
        }
        Object temp = this.head.data;
        head = this.head.next;
        size --;

        if(size == 0){
            tail = null;
        }
        return temp;
    }
}
