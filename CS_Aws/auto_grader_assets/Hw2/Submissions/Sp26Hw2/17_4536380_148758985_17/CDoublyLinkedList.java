public class CDoublyLinkedList {

	private class Node {
		private Object data;   //Assume data implemented Comparable
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
	
	// Add Object data to start of this LinkedList
	// Please DO NOT change this addFirst() method.
	// You must keep and include this provided addFirst() method
	//      in your source code.
	public void addFirst(Object data) {
		Node nn = new Node(data, this.head, this.head.next);
		this.head.next.prev = nn;
		this.head.next = nn;
		this.size ++;
	}

	// write a method void addLast(Object data) that will insert 
	// the piece of data at the end of the current list.
	// Note: this list is allowed to store null data element in its list node.
	public void addLast(Object data) {
        Node endOfList = this.head.prev;
        Node nn = new Node(data, endOfList,this.head);
        endOfList.next = nn;
        this.head.prev = nn;
        this.size++;
	}
	
	// Write the subListOfSmallerValues method.  
	// It should return a CDoublyLinkedList object 
	//     containing data that is smaller than the value passed to the method.
        // If a null data element in this list is encountered, you can skip it.
        // You need to use the CompareTo() method to determine which object is smaller.
        // If list A contains {9, 11, 14, 6, 4, 7} and you call  A.subListOfSmallerValues(10),
        // the method call returns a list that contains data in A that is smaller than 10, the passed-in argument.
        // That is, the returned list contains { 9, 6, 4, 7}.
	public CDoublyLinkedList subListOfSmallerValues(Comparable data) {
	    if(data == null){ return null;}
        //Create a new List
        CDoublyLinkedList newList = new CDoublyLinkedList();
        //Have a for loop that iterates all the way through this. list
        for(Node cur = this.head.next; cur != this.head; cur = cur.next) {
            //Need to safley check because it can contain null if so skip it.
            if(cur.data == null){
                //Do nothing just iterate to next node
            }else if(((Comparable)cur.data).compareTo(data) < 0){
                //Each time check if cur is smaller than data if so add to new list.
                newList.addLast(cur.data);
            }

        }
        //return list.
		return newList; //change this as needed.
	}
	
	// This method should remove the first occurrence of the data from the list, 
        //      starting at the *BACK* of the list. 
        // It scans the list from back to the front by following the prev links. 
	// The method should return true if successful, false otherwise. 
	// Note that list node may contain null data element. Please handle this edge case.
	public boolean removeStartingAtBack(Object dataToRemove) {
	    //Iterate through list backwards checking if dataToRemove is null and if cur.data is null
        //Handle accordinglny
        Boolean toRemove = false;
        for(Node cur = this.head.prev; cur != this.head;cur = cur.prev){
            if(dataToRemove == null){
                if(cur.data == null){
                    toRemove = true;
                }
            }else if(cur.data == null){
                //Do nothing
            }else if(((Comparable) cur.data).compareTo(dataToRemove) == 0){//If there equivalent
                toRemove = true;
            }

            if(toRemove){
                //Remove node
                cur.prev.next = cur.next;
                cur.next.prev = cur.prev;
                size --;
                return true;
            }
        }

		return toRemove;
	}
	
	// Returns the index of the last occurrence of the specified element in this list, 
	//     or -1 if this list does not contain the element. 
	// More formally, returns the highest index i 
	//     such that (o==null ? get(i)==null : o.equals(get(i))), 
	//     or -1 if there is no such index.
	// Note: a list node may store a null data element. Please handle this edge case.
	public int lastIndexOf(Object o) {
        int highestMatch = -1;
        Node cur = this.head.next;
        for(int i = 0; i < this.size;i++) {
            if (o == null) {
                if (cur.data == null) {
                    highestMatch = i;
                }
            } else if (cur.data == null) {
                //Do nothing
            } else if (((Comparable) cur.data).compareTo(o) == 0) {//If there equivalent
                highestMatch = i;
            }
            cur = cur.next;
        }
		return highestMatch; //change this as needed.
	}
	
	
	// Removes from this list all of its elements that 
	//    are NOT contained in the specified linkedlist other.
	// If any element has been removed from this list,
	//    returns true. Otherwise returns false.
	// If other list is null, throws NullPointerException.
        // Helper methods are allowed.
	public boolean retainAll(CDoublyLinkedList other) throws NullPointerException {
        if(other == null){throw new NullPointerException();}
        //Iterate through this.list checking each element if its in the given list. If its in the given list
        //Iterate otherwise remove all occurences( call remove starting at back until it returns -1)
       boolean somethingRemoved = false;
        for(Node cur = this.head.next; cur != this.head; cur = cur.next){
            if(!contains(other.head.next,cur.data)){//Is the current data in list other
                //If it isn't in the list
                //call removeStartingAtBack until it returns false
                while(removeStartingAtBack(cur.data)){

                }
                somethingRemoved = true;
            }
        }
	    return somethingRemoved; //change this as needed.
	}
    //If current data is in list then return true otherwise return false
	public static boolean contains(Node firstNode, Object data){

        for(Node cur = firstNode; cur != firstNode.prev; cur = cur.next){
            //Safley checking if data == data in current node
            if (data == null) {
                if (cur.data == null) {
                    return true;
                }
            } else if (cur.data == null) {
                //Do nothing
            } else if (((Comparable) cur.data).compareTo((Comparable)data) == 0) {//If there equivalent
                return true;
            }
        }
        return false;
    }
    public boolean remove(int index){
        if(index < 0 || index >= size){ return false;}

        Node cur = this.head.next;
        int i = 0;
        while(i<index && cur != this.head){
            cur = cur.next;
            i++;
        }
        cur.prev.next = cur.next;
        cur.next.prev = cur.prev;
        size--;
        return true;
    }

        // Write this method to sort this list using insertion sort algorithm, 
        //      as we have learned in the classroom.
	public void insertionSort() {
        Node  unsorted = this.head.next.next, FUD = unsorted;
        int indexOfFUD = 1;//Used to remove FUD from list
        while(unsorted != this.head){//Goes until 2nd to last node
            //While loop that inserts FUD in sorted section of list. Go from one previous of unsorted to begining
            Node iterator = unsorted.prev;
            while(iterator != this.head){

                if(((Comparable)FUD.data).compareTo((Comparable)iterator.data) >= 0){
                    remove(indexOfFUD);
                    //Insert
                    Node NN = new Node(unsorted.data,iterator, iterator.next);
                    iterator.next = NN;
                    NN.next.prev = NN;
                    size++;
                    break;

                }
                //If FUD needs to go first position edge case
                if((iterator.prev == this.head && ((Comparable)FUD.data).compareTo((Comparable)iterator.data) < 0)){
                    remove(indexOfFUD);
                    //Insert
                    Node NN = new Node(unsorted.data,this.head, iterator);
                    iterator.prev = NN;
                    this.head.next = NN;
                    size++;
                    break;
                }
                iterator = iterator.prev;
            }
            unsorted = unsorted.next;
            FUD = unsorted;
            indexOfFUD++;
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
}
	