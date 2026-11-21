public class Queue{

    private class Node{
        Object data;
        Node next;

        public Node(Object data){
            this.data = data;
            this.next = null;
        }

    }

    private Node front;
    private Node rear;
    private int size;

    public Queue() {
        front = null;
        rear = null;
        size = 0;
    }

    public boolean isEmpty(){
        return size == 0;
    }  

    public int size(){
        return size;
    }

    public void enqueue(Object data) {
        Node newNode = new Node(data);

        if (front == null){
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }

        size++;
    }

    public Object dequeue() {
        if (isEmpty()){
            return null;
        }

        Object value = front.data;

        front = front.next;
        size--;

        if (front == null) {
            rear = null;
        }
        return value;
    }

}