import java.util.NoSuchElementException;

//EXTRA CREDIT ATTEMPTED

//Author Of Most Of The Code: Emmett James Kopczynski


public class MyLinkedList <I>{
	
	private ListNode<I> head;
	private int size;
	
	//inner class for ListNode
	private class ListNode<I> {
		private I data;
		private ListNode<I> next;
		private ListNode(I d) {
			this.data = d;
			this.next = null;
		}
	}
	
	public MyLinkedList() {
		this.head = new ListNode<I>(null); //with a dummy head node
		this.size = 0;
	}
	
	public int size() {
		return this.size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	// Add Object e to start of this LinkedList
	// Please DO NOT change this addFirst() method.
	// You must keep and include this provided addFirst() method in your source code.
	public void addFirst(I e)
	{
		ListNode<I> node = new ListNode<I>(e);
		node.next = head.next;
		head.next = node;
		size++;
	}
	
	// Remove(cut out) the first data node(the node succeeding the dummy node) 
	//       in this list, then returns the data in the node removed.
	// If the size of this list is zero, throws an Exception.
	public I removeFirst() throws Exception {

		if (this.size == 0) { //if the list is empty, an exception is thrown
			throw new Exception("LinkedList is empty!");
		}

		return this.remove(0); //removes the node at index 0
	}
	
	// Returns true if this list contains the specified element o. 
	// More formally, returns true if and only if this list contains at least one element e 
	// such that (o==null ? e==null : o.equals(e)).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean contains(I o) {

		ListNode<I> cur = this.head.next;


		while (cur != null) {

			if (o == null && cur.data == null) { //ensures that o and cur.data are equal to null

				return true; // returns true because a node containing null is in the list


			} else if (o != null && cur.data != null) { //ensures that o and cur.data are not equal to null, and that cur.data.equals(o) wont throw an exception

				if (cur.data.equals(o)) { // checks if cur.data and o are equal
					return true;
				}
			}

			cur = cur.next; // cur was not equal to o this round, so onto the next node
		}

		//gets here when cur == null, or at the end of the list
		return false;
	}
	
	// Removes the first occurrence of the specified element o from this list and returns true, if it is present. 
	// If this list does not contain the element o, it is unchanged and the method returns false.
	// More formally, removes the element o with the lowest index i such that 
	//     (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean remove(Object o) {

		ListNode<I> prev = this.head;
		ListNode<I> cur = this.head.next;

		while (cur != null){ //this loop iterates through the list until the end or until it breaks early from cur.data being equal to o

			if (cur.data == null && o == null){
				break; //break 1, reached if cur.data and o are both equal to null
			}

			if (o != null && cur.data != null){
				if (cur.data.equals(o)){
					break; //break 2, reached if o and cur.data are not null, and if cur.data is equal to o
				}
			}

			prev = prev.next;
			cur = cur.next;
		}

		if (cur == null) { //true if the while loop ended from cur being equal to null, returns false as o was not in the list
			return false;
		} else { //else statement runs if o was found in the list, this removes the item and returns true
			prev.next = cur.next;
			this.size --;
			return true;
		}
	}

	// Removes all copies of o from this linked list.
	// You have to handle the cases where Object o may 
	//        have zero, one or multiple copies in this list.
	// If any element has been removed from this list, returns true. 
	//        Otherwise, returns false.
	// Note: be careful when multiple copies of Object o are stored
	//        in consecutive(adjacent) list nodes.
	//        E.g. []->["A"]->["A"]->["B"]. 
	//        Be careful when removing all "A"s in this example.
	// Note: This list may contains zero, one or multiple copies of null data elements.
	public boolean removeAllCopies( I o ) { //passed test

		boolean returnable = this.remove(o); //this removes one of the values from the list, verifying that it exists if it returns true, and showing that it doesn't if it returns false

		while(this.remove(o)){
			//This while loop runs as long as there are more copies of the value to remove from the list
		}

		return returnable; //returns true if something was deleted from the list, false if otherwise
	}
	
	// Insert data elements from linkedlist A and B alternately into 
	//    a new linkedlist C, then returns C.
        // Follow the pattern to pick items in list A and B, 
        //        linkedlist A->linkedlist B->linkedlist A->linkedlist B …
	// If A is longer than B, append remaining items in A to C
	//     when the end of B is first reached.
	// If B is longer than A, append remaining items in B to C
	//     when the end of A is first reached.
	// E.g1 A = {1, 3, 5, 7, 9} and B = {2, 4, 6}; and 
	//       C will be {1, 2, 3, 4, 5, 6, 7, 9}.
        //
	// E.g2 A = {1, 3, 5} and B = {2, 4, 6, 8, 10}; and 
	//       C will be {1, 2, 3, 4, 5, 6, 8, 10}.
	// Note: after this method is called, both list A and B are UNCHANGED.
	public static <I> MyLinkedList<I> interleave(MyLinkedList<I> A, MyLinkedList<I> B) {

		MyLinkedList<I> C = new MyLinkedList<I>(); //Linked List to be returned

		int ASize = A.size();
		int BSize = B.size();
		int max = ASize + BSize - 2; // gets the maximum index of the returned linked list

		int curI = 0; //current index tracker for returned linked list

		//current index trackers for A and B linked lists
		int curA = 0;
		int curB = 0;


		while (curI <= max) {

			if(curI % 2 == 0){ //changes every cycle in order to go back and forth from adding to list A or list B
				C.add(A.get(curA));
				curA ++;
			}else {
				C.add(B.get(curB));
				curB++;
			}

			if(curA >= ASize || curB >= BSize){ //if one of the lists is out of stuff to add the loop breaks
				break;
			}

			curI ++;
		}

		if (curA < ASize) { // this if else tree checks if list A or list B have elements that have yet to be added, and adds them if neccesary
			while(curA < ASize){
				C.add(A.get(curA));
				curA ++;
			}
		} else if (curB < BSize) {
			while(curB < BSize){
				C.add(B.get(curB));
				curB ++;
			}
		}




		return C;
	}
	
	// Inserts the specified element at the specified position in this list. 
	// Shifts the element currently at that position (if any) and any subsequent
	//     elements to the right (adds one to their indices).
	// if(index < 0 or index > this.size), throws IndexOutOfBoundsException.
	
	// E.g, if this list is [dummy]->["A"]->["B"]->["C"] with size = 3.
	//   add(0,D) will result in [dummy]->["D"]->["A"]->["B"]->["C"].
	//   Continuing on the previous add() call, add(1,"E") will
	//   change the existing list to [dummy]->["D"]->["E"]->["A"]->["B"]->["C"].
	public void add(int index, I o) {


		if(index > this.size || index < 0){ //validates index
			throw new IndexOutOfBoundsException("Index Passed is not valid!");
		}

		if (index == 0) { //may not be needed, look into it
			this.addFirst(o);
			return; // returns from the function
		}

		if (index == size) { //if we are trying to add to max index +1, we just add to the end of the list
			add(o);
			return; // returns from the function
		}

		int i = 0;
		ListNode<I> prev = this.head;
		ListNode<I> cur = this.head.next;
		ListNode<I> newNode = new ListNode<I>(o);

		while (i < index){
			prev = prev.next;
			cur = cur.next;
			i ++;
		}

		prev.next = newNode;
		newNode.next = cur;

		size ++;
	}
	

	// Returns the element at the specified index in this list.
	// Be noted that the listnode at head.next has index 0 and 
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public I get(int index) throws IndexOutOfBoundsException{

		if (index >= this.size || index < 0){ // error thrown if index is not valid
			throw new IndexOutOfBoundsException("Provided index is out of bounds! " + index);
		}

		ListNode<I> cur = this.head.next;
		int i = 0;

		while( i < index){ //while loop goes until cur is equal to the node at the given index
			cur = cur.next;
			i ++;
		}

		return cur.data;
	}
	
	// Removes (cuts out) the list node at the specified index in this list. 
	// Returns the data element in the node that is removed.
	// Be noted that the list node at head.next has index 0 and 
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public I remove(int index) throws IndexOutOfBoundsException {

		if (index < 0  || index >= this.size){
			throw new IndexOutOfBoundsException("Provided index is out of bounds! " + index);
		}

		ListNode<I> cur = head.next;
		ListNode<I> prev = head;
		int i = 0;

		while (i < index){ //moves cur to the given indexes node and prev to the node behind it
			cur = cur.next;
			prev = prev.next;
			i ++;
		}

		prev.next = cur.next;

		this.size --;

		return cur.data;
	}

	
	//Add the object e to the end of this list.
	// it returns true, after e is successfully added.
	public boolean add(I e) {

		ListNode<I> newNode = new ListNode<I>(e);
		ListNode<I> cur = head;

		while (cur.next != null) {
			cur = cur.next;
		}

		cur.next = newNode;

		return true; //change this as you need.
	}
	
        //Please DO NOT Change the following toString() method!!!
        //You must include the following toString() method in your source code.
	@Override
	public String toString() {
		String result = "{";
	    for (ListNode<I> node = this.head.next; node != null; node = node.next) {
	    		if(node.next != null)
	    			result += node.data + "->";
	    		else
	    			result += node.data;
	    }
	    return result + "}";
	  }
}
