//BASE CASE IS USUALLY CONDITION
//BASE CASE CONDITION MUST(!) USE THE INPUT PARAMETERS
//Because the condition is always changing
//For example, void printReverse(Node head)
 
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

	/**
	 * @author Spencer Olson
	 * @param data is the data passed through for the node that will be added to the end of the list
	 * addLast method adds a new node storing Object 'data' at the end of a circularly linked list
	 */
	public void addLast(Object data) {
		Node newNode = new Node(data, head.prev, head);
		head.prev.next = newNode;
		head.prev = newNode;
		size++;
	}
	
	/**
	 * @author Spencer Olson
	 * @param data is the data passed through to check for numbers smaller than data to remove
	 * @return Returns list'sublist' once the method execute
	 * subListOfSmallerValues method compares current objects with Comparable data to see if they need to remove, and returns a list with the remaining values
	 */
	public CDoublyLinkedList subListOfSmallerValues(Comparable data) {
		Node cur = head.next;
		CDoublyLinkedList subList = new CDoublyLinkedList();

		while(cur != head) {

        if(cur.data != null && ((Comparable) cur.data).compareTo(data) < 0) {
            subList.addLast(cur.data);
        }
        cur = cur.next;
    }

	return subList;
			}

	/**
	 * @author Spencer Olson
	 * @param dataToRemove the object that stores the data to remove the last insance of
	 * @return method will return true if removal of the element was successful and false if not
	 * removeStartingAtBack method will start at the back of the list and remove the first insance of the dataToRemove
	 */
	public boolean removeStartingAtBack(Object dataToRemove) {
		Node cur = head.prev;
		
		for(int i = size - 1; i >= 0; i--){
			if(dataToRemove == null){

				if(cur.data == null){
					cur.prev.next = cur.next;
					cur.next.prev = cur.prev;
					size--;

					return true;
				}} else {
					if(dataToRemove.equals(cur.data)){

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
	
	/**
	 * @author Spencer Olson
	 * @param o is the object in which to remove
	 * @return Returns the integer index of the elmenet removed
	 * lastIndexOf method starts at the back of the list and removes the first element of specified Object 'o' and returns -1 if there was no such element
	 */
	public int lastIndexOf(Object o) {

		Node cur = head.prev;
		for(int i = size - 1; i >= 0; i--){
			if(o == null){
				if(cur.data == null){
					return i;
				}
			} else {
				if(o.equals(cur.data)){
					return i;
				}
			}
			cur = cur.prev;
		}
		return -1;
	}


	/**
	 * @author Spencer Olson
	 * @param other The initial linked list to check
	 * @param otherData The data to check against the list
	 * @return Returns true if the element 'otherData' is stored inside of 'other' list
	 * contains() method returns a boolean value based on if the specified 'otherData' is contained within the list. This is a helper method for the retainAll method
	*/
	public boolean contains(CDoublyLinkedList other, Object otherData){

		Node cur = other.head.next;

		while(cur != other.head){

		if(otherData == null){
			if(cur.data == null){
				cur.prev.next = cur.next;
				cur.next.prev = cur.prev;
				size--;
				return true;

			}} else {
				if(otherData.equals(cur.data)){
				return true; 
			}
		}
		cur = cur.next;
	}
	return false;
	}
			
	/**
	 * @author Spencer Olson
	 * @param other The initial list to check
	 * @return Returns true if any element if removed from the list and false otherwise
	 * @throws NullPointerException Throws NPE if the 'other' list is null
	 * method retainAll removes every instance of any element that does not exist in list 'other'
	 */
	public boolean retainAll(CDoublyLinkedList other) throws NullPointerException {
		
		Node cur = head.next;
		int orgSize = this.size;

		if(other == null){
			throw new NullPointerException("The 'other' list is null.");
		}
		while (cur != this.head){
			if(!contains(other, cur.data)){
					cur.prev.next = cur.next;
					cur.next.prev = cur.prev;
					size--;
			}
			cur = cur.next;
		}
		return this.size < orgSize;
	}

	/**
	 * @author Spencer Olson
	 * insertionSort method sorts a list through creating and pushing two sections of sorted and unsorted sections of the list that compare elements
	 */
	public void insertionSort() {

	 if (this.size == 0 || this.size == 1) {
        return;
    }
    
    Node cur = head.next;
	Node sorted = cur;
	Node unsorted = cur.next;
    
    while (cur != head) {
        sorted = cur.next;
        unsorted = cur.prev;
        
        while (unsorted != head) {
            if (unsorted.data == null || cur.data == null) {
                return;
            }
            if (((Comparable) cur.data).compareTo(unsorted.data) < 0) {
                unsorted = unsorted.prev;
            } else {
				break;
			}
		}
		
		//Move cur
		cur.prev.next = cur.next;
		cur.next.prev = cur.prev;
		
		//Rewire inserted element
		cur.prev = unsorted;
		cur.next = unsorted.next;
		unsorted.next.prev = cur;
		unsorted.next = cur;
		
		//Push sorted section
		cur = sorted;
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