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
	/**
	 * Adds the specified data to the end of this list.
	 * @param data
	 */
	public void addLast(Object data) {
		Node tail = this.head.prev;
		Node nn = new Node(data, tail, this.head);

		tail.next = nn;
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
		/**
		 * Returns a new CDoublyLinkedList containing data that is smaller than the specified value.
		 * @param data
		 * @return result list
		 */
	public CDoublyLinkedList subListOfSmallerValues(Comparable data) {

	CDoublyLinkedList result = new CDoublyLinkedList();

	Node cur = this.head.next;
	while (cur != this.head) {
		if (cur.data != null && ((Comparable)cur.data).compareTo(data) < 0) {
			result.addLast(cur.data);
		}
		cur = cur.next;
	}
		return result;

	}
	
	// This method should remove the first occurrence of the data from the list, 
        //      starting at the *BACK* of the list. 
        // It scans the list from back to the front by following the prev links. 
	// The method should return true if successful, false otherwise. 
	// Note that list node may contain null data element. Please handle this edge case.
	/**
	 * Removes the first occurrence of the specified data from this list, starting at the back.
	 * @param dataToRemove
	 * @return true if the data was found and removed, false otherwise
	 */
	public boolean removeStartingAtBack(Object dataToRemove) {
		Node cur = this.head.prev;

		while(cur!= head){
			if((cur.data == null && dataToRemove == null) || (cur.data != null && cur.data.equals(dataToRemove))){
				
				cur.prev.next = cur.next;
				cur.next.prev = cur.prev; // these two lines of code remove cur from the list by changing the links of its previous and next nodes to bypass cur.
				
				this.size--;
				return true;
			}
				cur = cur.prev;//moves/traverses cur to the previous node
		}
		return false;
	}
	
	// Returns the index of the last occurrence of the specified element in this list, 
	//     or -1 if this list does not contain the element. 
	// More formally, returns the highest index i 
	//     such that (o==null ? get(i)==null : o.equals(get(i))), 
	//     or -1 if there is no such index.
	// Note: a list node may store a null data element. Please handle this edge case.
	/**
	 * Returns the index of the last occurrence of the specified element in this list, or -1 if this list does not contain the element.
	 * @param o
	 * @return
	 */
	public int lastIndexOf(Object o) {
		Node cur = this.head.prev;
		int index = this.size - 1;

		while(cur!= head){
			if((cur.data == null && o == null) || (cur.data != null && cur.data.equals(o))){
				
				return index;
			}

			cur = cur.prev; // allows reverse traversal of the list
			index--; // decrements index to keep track of the current position in the list
		}
		return -1; // returns -1 if the specified element is not found in the list after traversing the list
	}
	
	
	// Removes from this list all of its elements that 
	//    are NOT contained in the specified linkedlist other.
	// If any element has been removed from this list,
	//    returns true. Otherwise returns false.
	// If other list is null, throws NullPointerException.
        // Helper methods are allowed.
		/**
		 * Removes from this list all of its elements that are NOT contained in the specified linkedlist other.
		 * @param other the list to compare against
		 * @return true if any element was removed from this list, false otherwise
		 * @throws NullPointerException
		 */
	public boolean retainAll(CDoublyLinkedList other) throws NullPointerException {
		if(other == null){
			throw new NullPointerException("Error");
		}

		boolean removedSomething = false;
		Node cur = this.head.next;
		while(cur != this.head){
			Node nextNode = cur.next;

			boolean found = false;
			Node otherCur = other.head.next;

			while(otherCur != other.head){
				if((otherCur.data == null && cur.data == null) || (otherCur.data != null && otherCur.data.equals(cur.data))){
					found = true;
					break;
				}
				otherCur = otherCur.next; // allows traversal of the other list to check for cur's data in the other list
			}

			if(!found){
				cur.prev.next = cur.next;
				cur.next.prev = cur.prev; // these two lines of code remove cur from the list by changing the links of its previous and next nodes to bypass cur.
				this.size--;
				removedSomething = true;
			}

			cur = nextNode; // moves/traverses cur to the next node
		}

	    return removedSomething; // returns true if any element was removed from this list, false otherwise
	}
	

        // Write this method to sort this list using insertion sort algorithm, 
        //      as we have learned in the classroom.
	public void insertionSort() {
		if(this.size <= 1) {
			return; // List is already sorted if it has 0 or 1 element
		}
	
		Node cur = this.head.next.next; // Start from the second element
		while (cur != this.head) {
			Node nn = cur.next;
			
			cur.prev.next = cur.next;
			cur.next.prev = cur.prev; // these two lines of code remove cur from the list by changing the links of its previous and next nodes to bypass cur.

			Node s = cur.prev;
			while (s != this.head){

				if(cur.data == null){
					break;
				}

				if(s.data!=null && ((Comparable)cur.data).compareTo(s.data) >= 0){
					break;
				}
				s = s.prev; // moves/traverses s to the previous node`
			}

			cur.next = s.next;
			cur.prev = s;
			s.next.prev = cur;
			s.next = cur; // these four lines of code insert cur back into the list 

			cur = nn;
		}
	}

	// I accidentally implemented selection sort instead of insertion sort. However, the results  matched the expected output from the provided word doc, so I left the code commented out cause I thought it was funny it still worked.
	// 	if(this.size <= 1) {
	// 		return; // List is already sorted if it has 0 or 1 element
	// 	}

	// 	Node start, cur, smallest;
	// 	Object temp;

	// 	for(start = this.head.next; start.next!=this.head;start=start.next) {
	// 		smallest = start;
	// 		for(cur = start.next; cur!=this.head;cur=cur.next) {
	// 			if(((Comparable)cur.data).compareTo(smallest.data) < 0) {

	// 				if(smallest.data == null){
	// 					smallest = cur;
					
	// 				}else if (cur.data != null && ((Comparable)cur.data).compareTo(smallest.data)<0) {
	// 					smallest = cur;
	// 				}
					
	// 			}


	// 		}
	// 			temp = (Comparable)start.data;
	// 			start.data = smallest.data;
	// 			smallest.data = temp;
	// 	}
	// }
	
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
			