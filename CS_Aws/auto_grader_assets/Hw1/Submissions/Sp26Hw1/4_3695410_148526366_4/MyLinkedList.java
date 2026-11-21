import java.util.NoSuchElementException;

public class MyLinkedList {
	
	private ListNode head;
	private int size;
	
	//inner class for ListNode
	private class ListNode {
		private Object data;
		private ListNode next;
		private ListNode(Object d) {
			this.data = d;
			this.next = null;
		}
	}
	
	public MyLinkedList() {
		this.head = new ListNode(null); //with a dummy head node
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
	public void addFirst(Object e)
	{
		ListNode node = new ListNode(e);
		node.next = head.next;
		head.next = node;
		size++;
	}
	
	// Remove(cut out) the first data node(the node succeeding the dummy node) 
	//       in this list, then returns the data in the node removed.
	// If the size of this list is zero, throws an Exception.
	public Object removeFirst() throws Exception {
		if(size == 0){
			throw new NoSuchElementException("LinkedList is Empty!");
		}

		ListNode node = head.next;
		head.next = node.next; //cuts out the first data node here
		size--;
		return node.data;
		
	}
	
	// Returns true if this list contains the specified element o. 
	// More formally, returns true if and only if this list contains at least one element e 
	// such that (o==null ? e==null : o.equals(e)).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean contains(Object o) {
		ListNode cur = head.next;

		while (cur != null){
			if(o == null){ // checks if o is null, then compares of to whether cur.data is null
				if(cur.data == null){
					return true;
				}
			}else{ //if o is not null, this then compares if the data is equal to the non-null data
				if(o.equals(cur.data)){
					return true;
				}
			}
			cur = cur.next; // moves cur to the next node
		}

		
		return false; 
	}
	
	// Removes the first occurrence of the specified element o from this list and returns true, if it is present. 
	// If this list does not contain the element o, it is unchanged and the method returns false.
	// More formally, removes the element o with the lowest index i such that 
	//     (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean remove(Object o) {
		ListNode prev = head;
		ListNode cur = head.next; // starts cur ahead of prev, setting up the ability to remove the current node if it matches o

		while(cur != null){
			if(o==null){ // checks if o is null, then compares of to whether cur.data is null
				if(cur.data == null){
					prev.next = cur.next; // cuts out the current node by setting the previous node's next to the current node's next
					size--;
					return true;
				}
			}else{
				if(o.equals(cur.data)){
					prev.next = cur.next; // same as above for cutting out the current node if its equal to what is being searched for
					size--;
					return true;
				}
			}

			prev = cur;
			cur = cur.next; // moves cur to the next node, and prev to the current node
		}

		return false; 
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
	public boolean removeAllCopies( Object o ) { //passed test
		
	ListNode prev = head;
	ListNode cur = head.next;
	boolean removed = false; // boolean to keep track of whether any nodes have been removed

		while(cur != null){
			if(o==null){ // checks if o is null, then compares of to whether cur.data is null
				if(cur.data == null){
					prev.next = cur.next; // cuts out the current node by setting the previous node's next to the current node's next
					cur = cur.next;
					size--;
					removed = true;
				}else{
					prev = cur;
					cur = cur.next; // moves cur to the next node, and prev to the current node in the case of no match
				}
			}else{
				if(o.equals(cur.data)){
					prev.next = cur.next; // cuts off node if it matches, same as above
					cur = cur.next; 
					size--;
					removed = true;
				}else{
					prev = cur;
					cur = cur.next; // moves cur to the next node, in the case of no match, same as above
				}
			}
		}

		return removed;
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
	public static MyLinkedList interleave(MyLinkedList A, MyLinkedList B) {
		MyLinkedList C = new MyLinkedList();

		ListNode tail = C.head; // tail is used to keep track of the last node in C
		ListNode curA = A.head.next; // curA and B are used to traverse through lists A and B
		ListNode curB = B.head.next;

		while(curA != null && curB != null){ // this loop alternates between A and B until one of them runs out of nodes
			ListNode newNode = C.new ListNode(curA.data);
			tail.next = newNode;
			tail = newNode;
			curA = curA.next;

			newNode = C.new ListNode(curB.data);
			tail.next = newNode;
			tail = newNode;
			curB = curB.next;
		}
		// once one is out, each loop below adds the remaining nodes of the other list to C, if there are any
		while(curA != null){
			ListNode newNode = C.new ListNode(curA.data);
			tail.next = newNode;
			tail = newNode;
			curA = curA.next;
		}
		while(curB != null){
			ListNode newNode = C.new ListNode(curB.data);
			tail.next = newNode;
			tail = newNode;
			curB = curB.next;
		}
		return C; // returns the new list
	}
	
	// Inserts the specified element at the specified position in this list. 
	// Shifts the element currently at that position (if any) and any subsequent
	//     elements to the right (adds one to their indices).
	// if(index < 0 or index > this.size), throws IndexOutOfBoundsException.
	
	// E.g, if this list is [dummy]->["A"]->["B"]->["C"] with size = 3.
	//   add(0,D) will result in [dummy]->["D"]->["A"]->["B"]->["C"].
	//   Continuing on the previous add() call, add(1,"E") will
	//   change the existing list to [dummy]->["D"]->["E"]->["A"]->["B"]->["C"].
	public void add(int index, Object o) {
		ListNode cur = head;
		int i = 0;

		if(index < 0 || index > size){
			throw new IndexOutOfBoundsException("Index is out of bounds for size");
		}
		while(i<index){
			cur = cur.next;
			i++;
		}
		ListNode nn = new ListNode(o);
		nn.next = cur.next;
		cur.next = nn;
		size++;
	}
	

	// Returns the element at the specified index in this list.
	// Be noted that the listnode at head.next has index 0 and 
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public Object get(int index) throws IndexOutOfBoundsException{

		if(index < 0 || index >= size){
			throw new IndexOutOfBoundsException("Index is out of bounds for size");
		}

		ListNode cur = head.next;
		int i = 0;
		while(i<index){
			cur = cur.next;
			i++;
		}
		return cur.data;
	
	}
	
	// Removes (cuts out) the list node at the specified index in this list. 
	// Returns the data element in the node that is removed.
	// Be noted that the list node at head.next has index 0 and 
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public Object remove(int index) throws IndexOutOfBoundsException {
		
		if(index < 0 || index >= size){
			throw new IndexOutOfBoundsException("Index is out of bounds for size");
		}

		ListNode removed;
		ListNode cur = head.next;
		ListNode prev = head;
		int i = 0;
		while(i<index){
			prev = cur;
			cur = cur.next; // moves cur to traverse the list until the index is reached
			i++;
		}
		removed = cur;
		prev.next = cur.next; // cuts out the node at the index
		size--;
		return removed.data; // returns the data of the removed node as requested

	}

	
	//Add the object e to the end of this list.
	// it returns true, after e is successfully added.
	public boolean add(Object e) {
		ListNode cur = head;
		while(cur.next!=null){
			cur = cur.next; // traverses to the end of the list
		}

		ListNode nn = new ListNode(e);
		cur.next = nn; // adds the new node to the end of the list
		size++; // updates list size
		
		return true; //change this as you need.
	}
	
        //Please DO NOT Change the following toString() method!!!
        //You must include the following toString() method in your source code.
	@Override
	public String toString() {
		String result = "{";
	    for (ListNode node = this.head.next; node != null; node = node.next) {
	    		if(node.next != null)
	    			result += node.data + "->"; 
	    		else
	    			result += node.data;
	    }
	    return result + "}";
	  }
}
