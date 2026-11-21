package aidanbhw2.zip;

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
		Node nn = new Node(data, this.head.prev, this.head); //make new node poitning to head and last node
		this.head.prev.next = nn; // last node points to nn
		this.head.prev = nn; //head points to nn
		this.size ++; //increase size
	}

	/*
	CUSTOM REMOVE METHOD TO MAKE OTHER STUFF EASIER
	 */
	public void removeAll(Object data)
	{
		Node cur = head.next;
		while (cur.data != null && cur.data != head.data) { //while cur is not null and equal to head
			if (cur.data.equals(data)) //if value of cur equals data
			{
				cur.prev.next = cur.next;  //skip over current node next
				cur.next.prev = cur.prev;  //skip over current node prev
				size--; //descrease size
			}
				cur = cur.next; //go next and do all this until size is reached
			}
	}


		/*
		CUSTOM COPY METHOD
		 */
	private CDoublyLinkedList copy() {
		CDoublyLinkedList copy = new CDoublyLinkedList(); //new list
		Node cur = head.next;
		while(cur != head) //while cur not equal head
		{
			copy.addLast(cur.data); //add each node from the list into the new linked list
			cur = cur.next;
		}
		return copy; //return copied list
	}

	public boolean remove(Object dataToRemove) {
		Node cur = head.next;
		for (int i = 0;i < size; i++) //while i is less than size
		{
			if (cur.data == dataToRemove) //if value of current node == data we want gone
			{
				cur.prev.next = cur.next;
				cur.next.prev = cur.prev; //cut out that node
				size--;
				return true; //return that list with cut out data
			}
			cur = cur.next; // go next node if no node has been removed yet
		}
		return false;//return false if list does not have any node equal to data we want to remove
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
		CDoublyLinkedList fake = copy();//make a copy of the linked list
		Node cur = head.next;

		for(int i = 0; i < size; i++) //for i less than size (incremental)
		{
			if (cur.data != null && data.compareTo(cur.data) <= 0) // if  cur.data not null and data is is equal or more than cur.data
			{
				fake.removeAll(cur.data);//remove current value from fake list
			}
			cur = cur.next; //go to the next node
		}
		return fake; //return the fake list to not permanently change the real list
	}
	
	// This method should remove the first occurrence of the data from the list, 
        //      starting at the *BACK* of the list. 
        // It scans the list from back to the front by following the prev links. 
	// The method should return true if successful, false otherwise. 
	// Note that list node may contain null data element. Please handle this edge case.
	public boolean removeStartingAtBack(Object dataToRemove)
	{
		Node cur = head.prev;
		for (int i = 0; i < size; i++) // for i less than size (incremental)
		{
			if (cur.data == dataToRemove) //cur.data == data we want to remove
			{
				cur.prev.next = cur.next; // cut (next) over cur node
				cur.next.prev = cur.prev;// cut (prev) over cur node
				size--; //minus size
				return true;//return true
			}
			cur = cur.prev; // go to the previous node
		}
		return false;//return if there are no node with the value with data
	}
	
	// Returns the index of the last occurrence of the specified element in this list, 
	//     or -1 if this list does not contain the element. 
	// More formally, returns the highest index i 
	//     such that (o==null ? get(i)==null : o.equals(get(i))), 
	//     or -1 if there is no such index.
	// Note: a list node may store a null data element. Please handle this edge case.
	public int lastIndexOf(Object o) {
		Node cur = head.prev;
		for (int i = 1; i <= size; i++) // if i is less than size (incremental) (starting with i at one)
		{
			if (cur.data == o && cur != null) // if cur value is equal to inserted value
			{
				return size - i; //return the index of object
			}
			cur = cur.prev; // go to previous node
		}
		return -1; //return node -1 if not equal to anything, return -1;.
	}
	
	
	// Removes from this list all of its elements that 
	//    are NOT contained in the specified linkedlist other.
	// If any element has been removed from this list,
	//    returns true. Otherwise returns false.
	// If other list is null, throws NullPointerException.
        // Helper methods are allowed.
	public boolean retainAll(CDoublyLinkedList other) throws NullPointerException {
		boolean flag = true;// true or false getter
		Node cur = head.next;
		Node Wanted; //cur for other list
		if (other == null)
		{
			throw new NullPointerException("Inserted Link list is null"); //throw if list is null
		}
			for (int i = 0; i < size && cur.data != null; i++) //for i less than size (incremental) and cur value no equal null
			{
				Wanted = other.head; //apply cur for other list
				flag = true; //make flag equal true
				for (int j = 0; j < other.size; j++) // for j less than other list size (incremental)
				{
					Wanted = Wanted.next; //move wanted to the next node in other list
					if (cur.data.equals(Wanted.data)) //if wanted node is equal to curent node
					{
							flag = false; //make boolean false
					}
				}
				if (flag != false) //if flag true
				{
					remove(cur.data); //remove value of current data
					size ++;// this cancels out the size decease of the remove function, thus not altering the for loop
				}
				cur = cur.next; //move cur forward.
			}
		return flag; //return the value of flag
	}
	

        // Write this method to sort this list using insertion sort algorithm, 
        //      as we have learned in the classroom.
	public void insertionSort()
	{
		Node cur = head.next;
		Object temp; //temp obj to save cur.data
		while (cur != head) //while cur not equal to head
		{
			temp = cur.data; //save the data of cur
			while (cur.prev.data != null && ((Comparable)temp).compareTo(cur.prev.data) < 0) //if cur.data is less than cur,prev.data and cur.prev.data is not null
			{
					cur.data = cur.prev.data; //make current data equal to previous node data
					cur.prev.data = temp; //make previous node data equal to current data
					cur = cur.prev; //make cur go back one in the sorted list
			}
			cur = cur.next; // make current node next node when previous node is not more than current node
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
			