import java.util.NoSuchElementException;
//I ATTEMPTED EXTRA CREDIT

public class MyLinkedList<E> {


	private ListNode<E> head;
	private int size;
	
	//inner class for ListNode
	private class ListNode<E> {
		private E data;
		private ListNode<E> next;
		private ListNode(E d) {
			this.data = d;
			this.next = null;
		}
	}


	public MyLinkedList() {
		this.head = new ListNode<E>(null); //with a dummy head node
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

	public void addFirst(E e)
	{
		ListNode<E> node = new ListNode<E>(e); // makes object a new node
		node.next = head.next;
		head.next = node; //puts new node infront of dummy head.
		size++; //increases size
	}
	
	// Remove(cut out) the first data node(the node succeeding the dummy node) 
	//       in this list, then returns the data in the node remoListNode node = new ListNode(e);ved.
	// If the size of this list is zero, throws an Exception.
	public E removeFirst() throws Exception
	{
		if (size == 0) // checks if linkedlist size is zero
		{
			throw new Exception("Exception: LinkedList is empty!");
		}
		ListNode<E> cur = head.next; // makes cur the first node
		E data = cur.data; //makes the node equal to cur
		head.next = cur.next; // head skips over the first node, thus removing it
		size --; //reduces size of linked list
		return cur.data; //returns the node as added first to linked list
	}
	
	// Returns true if this list contains the specified element o. 
	// More formally, returns true if and only if this list contains at least one element e 
	// such that (o==null ? e==null : o.equals(e)).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean contains(E o) {
		ListNode<E> node = new ListNode<E>(o); // make object a node
		ListNode<E> cur = head.next;
		while (cur != null) {
			if (cur.data == null) { //while content of cur is null
				if (o == null) { //if obj is null, and cur is also null, return true
					return true;
				}
			} else {
				{
					if (cur.data.equals(o)) { // if cur equals obj and isn't null, return true
						return true;
					}
				}
			}
			cur = cur.next; //go to the next node if cur isn't equal to obj
		}
		return false; //return false, if onj is not in list
	}
	
	// Removes the first occurrence of the specified element o from this list and returns true, if it is present. 
	// If this list does not contain the element o, it is unchanged and the method returns false.
	// More formally, removes the element o with the lowest index i such that 
	//     (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean remove(E o)
	{
		ListNode<E> cur = head.next;
		ListNode<E> prev = head;
		while (cur != null)
		{
			if (cur.data == o) // if content of data is equal to the current node
			{
				prev.next = cur.next; //cut out the current node
				size--; //reduce size of list
				return true;
			}
			prev = cur; //go to next node if obj does not equal cur
			cur = prev.next;
		}
		return false; //return false if obj is not inside list
	}

	// Removes all copies of o from this linked list.
	// You have to handle the cases where Object o may 
	//        have zero, one or multiple copies in this list.
	// If any element has been removed from this list, returns true. 
	//        Otherwise returns false.
	// Note: be careful when multiple copies of Object o are stored
	//        in consecutive(adjacent) list nodes.
	//        E.g. []->["A"]->["A"]->["B"]. 
	//        Be careful when removing all "A"s in this example.
	// Note: This list may contains zero, one or multiple copies of null data elements.
	public boolean removeAllCopies( E o ) { //passed test
		boolean flag = false; //true or false maker
			while (remove(o)) //remove obj while cur it is equal to cur, through out the whole list
			{
				flag = true; //make flag true if obj
			}
		return flag; //return the false or true flag
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
	public static MyLinkedList<String> interleave(MyLinkedList<String> A, MyLinkedList<String> B) {
		MyLinkedList<String> C = new MyLinkedList<String>(); //C list
		int Csize = A.size() + B.size(); //make  c list size = to A and B list size
		int fakeSizeA = A.size();
		int fakeSizeB = B.size(); // making ints of B and A size to not change the real B and A list sizes

		for (int i = 0; i < Csize; i++)
		{
			if (fakeSizeA <= 0 && fakeSizeB > 0) // if size A is zero but B still has nodes
			{
				C.add(B.get(i)); // add B(i) to C list
			}
			if (fakeSizeB <= 0 && fakeSizeA > 0) //if size B is zero but A still has nodes
			{
				C.add(A.get(i)); //add A(i) to C list
			}
			if (fakeSizeA > 0 && fakeSizeB > 0) //if there are stilll nodes inside both A and B list
			{
				C.add(A.get(i));
				C.add(B.get(i)); //add both A(i) and B(i) (in that order) to C list
			}
			fakeSizeA -= 1; //subtract A list size by one;
			fakeSizeB -= 1; //subtract B list size by one
		}
		return C; //return C list.
	}
	
	// Inserts the specified element at the specified position in this list. 
	// Shifts the element currently at that position (if any) and any subsequent
	//     elements to the right (adds one to their indices).
	// if(index < 0 or index > this.size), throws IndexOutOfBoundsException.
	
	// E.g, if this list is [dummy]->["A"]->["B"]->["C"] with size = 3.
	//   add(0,D) will result in [dummy]->["D"]->["A"]->["B"]->["C"].
	//   Continuing on the previous add() call, add(1,"E") will
	//   change the existing list to [dummy]->["D"]->["E"]->["A"]->["B"]->["C"].

	public void add(int index, E o) {
		ListNode<E> cur = head.next;
		ListNode<E> prev = head;
		ListNode<E> node = new ListNode<E>(o); // make obj a node

		if (index < 0 || index > this.size) //if index number is less than zero or if it is more than the list size
		{
			throw new IndexOutOfBoundsException("Index Passed in not valid!"); //throw an Out of Bounds exception
		}
			for (int i = 0; i < index; i++) // go up the list for index number of times until reach index
			{
				prev = cur;
				cur = cur.next;
			}
			node.next = cur;
			prev.next = node;//place the node where cur is
			size ++; //increase size
	}
	// Returns the element at the specified index in this list.
	// Be noted that the listnode at head.next has index 0 and 
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.

	public E get(int index) throws IndexOutOfBoundsException
	{
		ListNode<E> cur = head.next;

		if (index > size - 1) //if index is more than list and the null at the end of the linked list
		{
			throw new IndexOutOfBoundsException("Provided index is out of bounds!"); //throw out of bounds exception
		}

		for (int i = 0; i < index; i++) // go up the list index amount of times
		{
			cur = cur.next; //making cur go to next node
		}
		return cur.data; //return the content of the node that we are at currently
	}
	
	// Removes (cuts out) the list node at the specified index in this list. 
	// Returns the data element in the node that is removed.
	// Be noted that the list node at head.next has index 0 and 
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public E remove(int index) throws IndexOutOfBoundsException
	{
		E returnable= (get(index));
		remove(get(index));
		return returnable; //remove the index that we get with get Method
	}

	
	//Add the object e to the end of this list.
	// it returns true, after e is successfully added.
	public boolean add(E e) {
		ListNode<E> node = new ListNode<E>(e); //make obj a node
		ListNode<E> cur = head;
		ListNode<E> prev = head;
		while (cur.next != null) //go through list until cur is null
		{
			prev = cur;
			cur = prev.next;
		}
			cur.next = node; // add node to in
			size++; //increase size to compensate
		return true; //this could prob be a void function lol.
	}
	
        //Please DO NOT Change the following toString() method!!!
        //You must include the following toString() method in your source code.
	@Override
	public String toString() {
		String result = "{";
	    for (ListNode<E> node = this.head.next; node != null; node = node.next) {
	    		if(node.next != null)
	    			result += node.data + "->"; 
	    		else
	    			result += node.data;
	    }
	    return result + "}";
	  }
}
