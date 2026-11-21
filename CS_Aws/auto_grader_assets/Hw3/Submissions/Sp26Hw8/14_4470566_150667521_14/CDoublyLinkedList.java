// Circular Doubly Linked List implementation with nessisarry added methods, and helper methods
    public class CDoublyLinkedList {

	private class Node {
		private Object data;   
		private Node next, prev;
		private Node(Object data, Node pref, Node next)
		{
			this.data = data;
			this.prev = pref;
			this.next = next;
		}
	}

	private Node head;
	private int size;

	public CDoublyLinkedList() {
		this.head = new Node(null, null, null );
		this.head.next = this.head;
		this.head.prev=this.head;
		this.size = 0;
	}

	public boolean isEmpty() {
		return this.head == this.head.next;
	} 
	
	
	public void addFirst(Object data) {
		Node nn = new Node(data, this.head, this.head.next);
		this.head.next.prev = nn;
		this.head.next = nn;
		this.size ++;
	}

	
	public void addLast(Object data) { 
		Node cur = this.head.prev;
		Node nn = new Node(data, cur, this.head);
		cur.next = nn;
		head.prev = nn;
		size++;
	}
	
	
	public CDoublyLinkedList subListOfSmallerValues(Comparable data) { 
		CDoublyLinkedList nl = new CDoublyLinkedList();
		Node cur = this.head.next;
		while (cur != this.head){
			if(cur.data!=null){
				if(((Comparable) cur.data).compareTo(data)<0){
					nl.addLast(cur.data);
				}
			}
			cur = cur.next;
		}
		return nl; 
	}
	
	
	public boolean removeStartingAtBack(Object dataToRemove) {
		Node cur = this.head.prev;
		while(cur!= this.head){
			if(dataToRemove==null){
				if(cur.data == null){
					cur.prev.next =cur.next;
					cur.next.prev = cur.prev;
					size--;
					return true;
				}
			}else if(cur.data!=null){
				if(cur.data.equals(dataToRemove)){
					cur.prev.next = cur.next;
					cur.next.prev = cur.prev;
					size--;
					return true;
				}
			}
			
			
			cur = cur.prev;
		}
		return false;
	}
	
	
	public int lastIndexOf(Object o) { 
		Node cur = head.prev;
		for(int i = this.size-1; i >= 0; i--){
			if(cur.data == null){
				if (o == null){
					return i;
				}
			}else{
				if(cur.data.equals(o)){
					return i;
				}
			}
			cur = cur.prev;
		}


		return -1; 
	}
	
	public boolean contains(Object o){ // helper method for retainAll
		Node cur = this.head.next;
		while(cur!= this.head){
			if(cur.data==null){
				if(o == null) return true;
			}
			else if(cur.data.equals(o)){
				return true;
			}
			cur = cur.next;
		}
		return false;
	}
    public Object getFirst(){
        return this.head.next.data;
    }
	
	public boolean retainAll(CDoublyLinkedList other) throws NullPointerException { 
		if(other == null){
			throw new NullPointerException("Null Pointer Exception");
		}
		int startSize = this.size;
		Node cur = this.head.next;
		while(cur != this.head){
			if(!other.contains(cur.data)){
				cur.prev.next = cur.next;
				cur.next.prev = cur.prev;
				this.size--;
			}
			cur = cur.next;
		}
		if(startSize!=this.size){
			return true;
		}
	    return false; 
	}
    public void removeFirst(){
		if(this.isEmpty()) return;
        if(size ==1){
            this.head.next = this.head;
			this.head.prev = this.head;
            size--;
            return;
        }
        Node cur = head.next;
        cur.next.prev = this.head;
        head.next = cur.next;
        size--;
    }
	public int getSize(){
		return this.size;
	}
        
	

       
	public void insertionSort() { 
		Node lastSorted;
		Node sortedWalker;
		Comparable FUD;
		for(lastSorted = this.head.next;lastSorted!=this.head.prev;lastSorted = lastSorted.next){
			FUD = (Comparable)lastSorted.next.data;
			for(sortedWalker = lastSorted;sortedWalker!=head&&((Comparable)sortedWalker.data).compareTo(FUD)>0;
			sortedWalker = sortedWalker.prev){
				sortedWalker.next.data = sortedWalker.data;
			}
			sortedWalker.next.data = FUD;
		}
	}
	
	@Override
	public String toString() {
		String result = "{";
	    for (Node node = this.head.next; node != this.head; node = node.next) {
	    		if(node.next != this.head)
	    			result += node.data + "->"; 
	    		else
	    			result += node.data;
	    }
	    return result + "}";
	  }
      public CDoublyLinkedList merge(CDoublyLinkedList A, CDoublyLinkedList B){
        CDoublyLinkedList S = new CDoublyLinkedList();
        while(!A.isEmpty()&&!B.isEmpty()){
            Object fa = A.getFirst();
            Object fb = B.getFirst();
            if( ((Comparable) fa).compareTo(fb) <= 0){
                A.removeFirst();
                S.addLast(fa);
            }else{
                B.removeFirst();
                S.addLast(fb);
            }

        }
        while(!A.isEmpty()){
            Object fa = A.head.next.data;
            A.removeFirst();
            S.addLast(fa);
        }
        while(!B.isEmpty()){
            Object fb = B.head.next.data;
            B.removeFirst();
            S.addLast(fb);
        }
        return S;
      }
      public void mergeSort() throws Exception{
		if(size <= 1) return;
        LinkedQueue Q = new LinkedQueue();
        for(Node cur = head.next; cur!=head; cur = cur.next){
            CDoublyLinkedList list = new CDoublyLinkedList();
            list.addFirst(cur.data);
            Q.enqueue(list);
        }
        while(Q.size() > 1){
            CDoublyLinkedList sub1 = (CDoublyLinkedList) Q.dequeue();
            CDoublyLinkedList sub2 = (CDoublyLinkedList) Q.dequeue();
            CDoublyLinkedList temp = merge(sub1,sub2);
            Q.enqueue(temp);
        }
        CDoublyLinkedList sorted = (CDoublyLinkedList) Q.dequeue();
        this.head = sorted.head;
		this.size = sorted.size;

      }

      public boolean isSorted(){
		if(size <= 1) return true;
        Node cur = head.next;
        while( cur.next != this.head){
            if(((Comparable) cur.data).compareTo(cur.next.data)>0){
                return false;
            }
            cur = cur.next;
        }
        return true;
      }
}

			