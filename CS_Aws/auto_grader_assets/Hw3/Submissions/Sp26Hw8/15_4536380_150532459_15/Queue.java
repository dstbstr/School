public class Queue {
    private CircularDoublyLinkedList list = new CircularDoublyLinkedList();
    //With dummy node head and tail in LL class
    public void enqueue(Object data){//Adds to end of list
        CircularDoublyLinkedList.Node nn = new CircularDoublyLinkedList.Node(data,list.head.prev,list.head);
        //Dummy head so don't need to consider when list is empty
        list.head.prev.next = nn;
        list.head.prev = nn;
        list.size++;
    }
    public int getSize(){
        return list.size;
    }
    public Object dequeue(){
        if(list.size == 0){
            return null;
        }
        Object temp = list.head.next.data;
        list.head.next = list.head.next.next;
        list.head.next.prev = list.head;
        list.size--;
        if(list.size == 0){
            list.head.next = list.head;
            list.head.prev = list.head;
        }
        return temp;
    }
    public Object front(){//returns first no delete
        if(list.size == 0){
            return null;
           // throw new Exception("Queue empty");
        }
        return list.head.next.data;
    }
    public CircularDoublyLinkedList getList() {
        return this.list;
    }

}
