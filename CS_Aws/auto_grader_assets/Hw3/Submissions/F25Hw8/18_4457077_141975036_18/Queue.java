public class Queue {
    private class QueueNode{

        private LinkedList data;
        private QueueNode next;

        QueueNode(LinkedList data){
            this.data = data;
            this.next = null;
        }
    }
    private QueueNode front;
    private QueueNode rear;
    private int size;

    public Queue(){
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    public int size(){
        return this.size;
    }

    public void enqueue(LinkedList data){
        QueueNode nn = new QueueNode(data);
        if(this.rear == null){
            this.rear = nn;
            this.front = nn;
            this.size++;
        }else{
            this.rear.next = nn;
            this.rear = nn;
            this.size++;
        }
    }

    public LinkedList dequeue(){
        if(this.front == null){
            return null;
        }
        LinkedList removed = this.front.data;
        this.front = this.front.next;
        this.size--;
        if(this.size == 0){
            this.rear = null;
        }
        return removed;

    }

    public LinkedList peek(){
        if(this.front == null){
            return null;
        }else{
            return this.front.data;
        }
    }

}
