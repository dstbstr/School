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
		if (data == null) {
			Node nn = new Node("null", this.head.prev, this.head);
			this.head.prev.next = nn;
			this.head.prev = nn;
			this.size ++;
		}else{
		Node nn = new Node(data, this.head.prev, this.head);
		this.head.prev.next = nn;
		this.head.prev = nn;
		this.size ++;
		}
	}
	
	// Write the subListOfSmallerValues method.  
	// It should return a CDoublyLinkedList object 
	//     containing data that is smaller than the value passed to the method.
        // If a null data element in this list is encountered, you can skip it.
        // You need to use the CompareTo() method to determine which object is smaller.
        // If list A contains {9, 11, 14, 6, 4, 7} and you call  A.subListOfSmallerValues(10),
        // the method call returns a list that contains data in A that is smaller than 10, the passed-in argument.
        // That is, the returned list contains { 9, 6, 4, 7}.

	// does not need to handle if data is null
	public CDoublyLinkedList subListOfSmallerValues(Comparable data) {
		CDoublyLinkedList result = new CDoublyLinkedList();
		Node cur = this.head.next;
		while (cur != this.head) {

			if (cur.data == null){
				cur = cur.next;
			}
			if (data.compareTo(cur.data) > 0) { // If the data is bigger than the data at cur
				result.addLast(cur.data);
				cur = cur.next;
				result.size++;
			}else{
				cur = cur.next;
			}

		}
		return result;
	}
	
	// This method should remove the first occurrence of the data from the list, 
        //      starting at the *BACK* of the list. 
        // It scans the list from back to the front by following the prev links. 
	// The method should return true if successful, false otherwise. 
	// Note that list node may contain null data element. Please handle this edge case.
	public boolean removeStartingAtBack(Object dataToRemove) {
		Node cur = this.head.prev;
		while (cur != this.head) {
			if (cur.data == null && dataToRemove == null) {
				cur.next.prev = cur.prev;
				cur.prev.next = cur.next;
				this.size --;
				return true;
			}
			if (cur.data == dataToRemove) {
				cur.next.prev = cur.prev;
				cur.prev.next = cur.next;
				this.size --;
				return true;
			}else{
				cur = cur.prev;
			}

		}
		return false;
	}
	
	// Returns the index of the last occurrence of the specified element in this list, 
	//     or -1 if this list does not contain the element. 
	// More formally, returns the highest index i 
	//     such that (o==null ? get(i)==null : o.equals(get(i))), 
	//     or -1 if there is no such index.
	// Note: a list node may store a null data element. Please handle this edge case.
	public int lastIndexOf(Object o) {
		Node cur = this.head.next;
		int return_variable = -1;
		int index = 0;
		while (cur != this.head) {
			if (cur.data == null && o == null) {
				return_variable = index;
			}
			if (cur.data == o) {
				return_variable = index;
			}
			cur = cur.next;
			index++;
		}

		return return_variable; //change this as needed.
	}
	
	
	// Removes from this list all of its elements that 
	//    are NOT contained in the specified linkedlist other.
	// If any element has been removed from this list,
	//    returns true. Otherwise returns false.
	// If other list is null, throws NullPointerException.
        // Helper methods are allowed. :)

	public boolean retainAll(CDoublyLinkedList other) throws NullPointerException {
		if (other.size == 0) {
			throw new NullPointerException();
		}

		boolean result = false;
		Object[] array = listToArray(other); // Turns the list into an array with listToArray()
		Node cur = this.head.next;

		while (cur != this.head) {
			if (matchChecker(cur.data, array)) { // uses matchChecker()
				cur = cur.next;
			}else{
				cur.prev.next = cur.next;
				cur.next.prev = cur.prev;
				cur = cur.next;
				this.size --;
				result = true; // There was something to remove
			}
		} // so matchChecker takes in cur.data and the array that is the linked list elements
		return result;
	}
	

        // Write this method to sort this list using insertion sort algorithm, 
        //      as we have learned in the classroom.
	public void insertionSort() {
		// We need to declare the variables for lastSorted, sortedWalker, and firstUnsortedData
		// We need to loop through
		// We need to loop through the list until we get to the last node
		// we need to set the firstUnsortedData equal to lastSorted.next.data

		// We need a variable to start the sorted section, which is the first variable in the list always
		// We need a variable to walk through the sorted list when it comes time to compare data
		// We need a variable to represent the unsorted section's first data

		Node lastSorted, sortedWalker;
		Comparable firstUnsortedData;
		for(lastSorted =this.head.next; lastSorted != this.head.prev; lastSorted =lastSorted.next){
			firstUnsortedData = (Comparable) lastSorted.next.data;
			for (sortedWalker = lastSorted; sortedWalker != this.head && (firstUnsortedData.compareTo(sortedWalker.data) < 0); sortedWalker = sortedWalker.prev){
				sortedWalker.next.data = sortedWalker.data;
			}
			sortedWalker.next.data = firstUnsortedData;
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


	  // Takes in the list, creates an array of objects size of the list
	  // Sets cur at the lists first node
	  // while cur is not at the head(the end of the list)
	  // The array at the index gets the data at cur
	  // Then it just returns the list in array format so it is easier to loop through
	  public Object[] listToArray(CDoublyLinkedList list) {
		Object[] array = new Object[list.size];
		Node cur = list.head.next;
		int index = 0;
		while (cur != list.head) {
			array[index] = cur.data;
			cur = cur.next;
			index++;
		}
		return array;
	  }

	  // So the method needs to take in cur.data and also the array from the linked list
	  // The method will then search the entire list and if there is anything that matches at all then it will set
	  // a boolean to true, otherwise the boolean is false and then the method returns this boolean value
	  // So when it gets back to retainAll, it should check if the matchChecker was true, if the match Checker
	  // is true, then it just moves on to the next node, if the match checker is false, then it needs to
	  // remove cur from the list
	public boolean matchChecker(Object data, Object[] array) {
		boolean result = false;
        for (Object o : array) {
			if (data == null && o == null) {
				result = true;
				break;
			}
            if (data.equals(o)) {
                result = true;
                break;
            }
        }
		return result;
	}
}

			