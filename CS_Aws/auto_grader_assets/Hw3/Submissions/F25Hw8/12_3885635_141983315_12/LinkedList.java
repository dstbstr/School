public class LinkedList extends Queue {

    private ListNode head;
    private int size;

    public class ListNode{
        private Object data;
        private ListNode next;
        private ListNode(Object d){
            this.data = d;
            this.next = null;
        }
    }

    public LinkedList(){
        this.head = new ListNode(null);
        this.size = 0;
    }

    public int getSize(){
        return this.size;
    }

    public void addFirst(Object e){
        ListNode newNode = new ListNode(e);
        newNode.next = this.head.next;
        this.head.next = newNode;
        size ++;
    }

    public void addLast(Object e){
        ListNode newNode = new ListNode(e);
        ListNode cur = this.head;

        while(cur.next != null){
            cur = cur.next;
        }
        cur.next = newNode;
        size++;
    }

    public Object removeFirst() throws Exception {
        ListNode firstNode;
        if (this.size == 0) {
            throw new Exception("List is Empty");
        } else {
            firstNode = this.head.next;
            this.head.next = firstNode.next;
            size--;
        }
        return firstNode.data;
    }

    public Object getFirst()throws Exception{

        ListNode cur = this.head.next;
        if(cur == null){
            throw new Exception("No Data Available");
        }
            return cur.data;
    }


    public void MergeSort() throws Exception {

        ListNode cur = this.head.next;
        Queue q = new Queue();

        while(cur != null){
            LinkedList newList = new LinkedList();
            newList.addFirst(cur.data);
            q.enqueue(newList);
            cur = cur.next;
        }

        while(q.size > 1){
            LinkedList sublist1 = (LinkedList)q.dequeue();
            LinkedList sublist2 = (LinkedList)q.dequeue();
            LinkedList tempList = merge(sublist1, sublist2);
            q.enqueue(tempList);
        }

        LinkedList sortedList = (LinkedList) q.dequeue();
        this.head.next = null;
        this.size = 0;

        ListNode walker = sortedList.head.next;
        while(walker != null){
            this.addLast(walker.data);
            walker = walker.next;
        }

    }

    public LinkedList merge(LinkedList A, LinkedList B) throws Exception{
        LinkedList Slist = new LinkedList();

        while(A.size!=0 && B.size != 0){
            Comparable fa = (Comparable) A.getFirst();
            Comparable fb = (Comparable) B.getFirst();
            if(fa.compareTo(fb) < 0){
                A.removeFirst();
                Slist.addLast(fa);
            }else{
                B.removeFirst();
                Slist.addLast(fb);
            }
        }
        while(A.size != 0){
            Object fa = A.getFirst();
            Slist.addLast(fa);
            A.removeFirst();
        }
        while(B.getSize() != 0){
            Object fb = B.getFirst();
            Slist.addLast(fb);
            B.removeFirst();
        }
        return Slist;

    }
    public void InsertionSort(){
        if(head == null || head.next == null){
            return;
        }
        ListNode sorted = this.head.next;

        while(sorted.next != null){
            ListNode Insert = sorted.next;
            Comparable Data = (Comparable)Insert.data;

            if(((Comparable) sorted.data).compareTo(Data) <= 0){
                sorted = sorted.next;

            }else if(((Comparable)head.next.data).compareTo(Data) > 0){
                sorted.next = Insert.next;
                Insert.next = head.next;
                head.next = Insert;
            }else{
                ListNode walker = head.next;

                while(((Comparable) walker.next.data).compareTo(Data) <= 0){
                    walker = walker.next;
                }
                sorted.next = Insert.next;
                Insert.next = walker.next;
                walker.next = Insert;
            }
        }
    }

    public boolean isSorted(){
        ListNode cur = this.head.next;

        while(cur.next != null){
            ListNode Walker = cur.next;

            Comparable curData = (Comparable)cur.data;
            Comparable curnextData = (Comparable) cur.next.data;

            if(curData.compareTo(curnextData) > 0){
                return false;
            }
            cur = cur.next;
        }
        return true;
    }

    }
