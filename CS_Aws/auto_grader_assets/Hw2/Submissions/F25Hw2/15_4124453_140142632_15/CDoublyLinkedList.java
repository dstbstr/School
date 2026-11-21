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
        // creates new node
        Node nn = new Node(data, this.head.prev, this.head);
        // the previously last node in the list now points to the new node
        this.head.prev.next = nn;
        // the head node points backwards to the new node
        this.head.prev = nn;
        // increases size
        this.size ++;
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
        // create a list to hold the output
        CDoublyLinkedList output = new CDoublyLinkedList();
        // cur skips dummy head since dummy holds nothing
        Node cur = this.head.next;
        // walk the list until we reach the dummy head a.k.a. the beginning
        while(cur != this.head) {
            if(cur.data != null){
                // if cur.data is less than passed in number then it is added to the output list
                if(((Comparable)cur.data).compareTo(data) < 0){
                    output.addLast(cur.data);
                }
            }
            cur = cur.next;
        }
		return output;
	}
	
	// This method should remove the first occurrence of the data from the list, 
        //      starting at the *BACK* of the list. 
        // It scans the list from back to the front by following the prev links. 
	// The method should return true if successful, false otherwise. 
	// Note that list node may contain null data element. Please handle this edge case.
	public boolean removeStartingAtBack(Object dataToRemove) {
        // cur starts at the end of the list
        Node cur = this.head.prev;
        // walk the list until we reach the dummy head a.k.a. the beginning
        while(cur != this.head) {
            // edge case
            if(cur.data == null && dataToRemove == null){
                cur.prev.next = cur.next;
                cur.next.prev = cur.prev;
                this.size --;
                return true;
            }
            // removes first occurrence of node with passed in data
            if(cur.data != null && cur.data.equals(dataToRemove)){
                cur.prev.next = cur.next;
                cur.next.prev = cur.prev;
                this.size --;
                return true;
            }
            // moves cur so while loop can work properly
            cur = cur.prev;
        }
        // if no nodes are removed due to data not being found
		return false;
	}
	
	// Returns the index of the last occurrence of the specified element in this list, 
	//     or -1 if this list does not contain the element. 
	// More formally, returns the highest index i 
	//     such that (o==null ? get(i)==null : o.equals(get(i))), 
	//     or -1 if there is no such index.
	// Note: a list node may store a null data element. Please handle this edge case.
	public int lastIndexOf(Object o) {
        // cur starts at the end of the list
        Node cur = this.head.prev;
        // index rests at last node which is cur
        int index = this.size - 1;
        // walk the list backwards until the dummy is reached
        while(cur != this.head) {
            // edge case
            if(cur.data == null && o == null){
                return index;
            }
            // if an index is found it is returned
            if(cur.data != null && cur.data.equals(o)){
                return index;
            }
            // moves cur so while loop works properly
            cur = cur.prev;
            index--;
        }
        // if an index is not found that matches
		return -1;
	}
	
	
	// Removes from this list all of its elements that 
	//    are NOT contained in the specified linkedlist other.
	// If any element has been removed from this list,
	//    returns true. Otherwise returns false.
	// If other list is null, throws NullPointerException.
        // Helper methods are allowed.
	public boolean retainAll(CDoublyLinkedList other) throws NullPointerException {
        // edge case
        if(other == null){
            throw new NullPointerException("Can't be null");
        }
        // keep track if anything has been removed
        boolean removed = false;
        // cur starts after dummy head
        Node cur = this.head.next;
        // walks the list until the beginning is reached
        while(cur != this.head) {
            // stores the node that might be deleted just in case
            Node nextNode = cur.next;
            // if a node is not contained in the other list it is removed
            if(!other.contains(cur.data)){
                cur.prev.next = cur.next;
                cur.next.prev = cur.prev;
                this.size--;
                removed = true;
            }
            cur = cur.next;
        }
	    return false;
	}
    // added contains class to help out with retainAll class
    public boolean contains(Object o){
        Node cur = this.head.next;
        while(cur != this.head){
            if(cur.data == null && o == null){
                return true;
            }
            if(cur.data != null && cur.data.equals(o)){
                return true;
            }
            cur = cur.next;
        }
        return false;
    }
	

        // Write this method to sort this list using insertion sort algorithm, 
        //      as we have learned in the classroom.
	public void insertionSort() {
        Node lastSorted, sortedWalker;
        Comparable firstUnsortedData;
        for(lastSorted=this.head.next; lastSorted != this.head.prev; lastSorted=lastSorted.next){
            firstUnsortedData = (Comparable)lastSorted.next.data;
            for(sortedWalker = lastSorted; sortedWalker != head &&
                    ((Comparable)sortedWalker.data).compareTo(firstUnsortedData) > 0;
                sortedWalker = sortedWalker.prev){
                sortedWalker.next.data = sortedWalker.data;
            }
            sortedWalker.next.data =  firstUnsortedData;
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
			