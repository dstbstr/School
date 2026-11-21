import org.w3c.dom.Node;

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
        // throws exception if list is empty
        if(size == 0)
            throw new Exception("List is empty");
        // stores first node's data in object
        Object data = head.next.data;
        // removes first node after dummy head
        head.next = head.next.next;
        size --;
        // return data of removed node
        return data;
	}
	
	// Returns true if this list contains the specified element o. 
	// More formally, returns true if and only if this list contains at least one element e 
	// such that (o==null ? e==null : o.equals(e)).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean contains(Object o) {
        // for loop to walk through the list by shifting which node is cur
        for(ListNode prev = this.head, cur = this.head.next;
            cur != null; prev = cur, cur = cur.next){
            // incase object o is null
            if(o == null){
                if(cur.data == null){
                    return true;
                }
            } else {
                // if the walk finds o in the current node it's on
                if(cur.data.equals(o)){
                    return true;
                }
            }
        }
        // incase the data element is not found
        return false;
	}
	
	// Removes the first occurrence of the specified element o from this list and returns true, if it is present. 
	// If this list does not contain the element o, it is unchanged and the method returns false.
	// More formally, removes the element o with the lowest index i such that 
	//     (o==null ? get(i)==null : o.equals(get(i))) (if such an element exists).
	// Note: you have to handle the case where a list node stores null data element.
	public boolean remove(Object o) {
        // for loop to walk through the list by shifting which node is cur
        for(ListNode prev = this.head, cur = this.head.next;
            cur != null; prev = cur, cur = cur.next){
            // handles null values
            if(o == null){
                if(cur.data == null){
                    // removes cur by relinking prev to point to the node after cur
                    prev.next = cur.next;
                    size--;
                    return true;
                }
            } else {
                // if the walk finds o in the current node it's on
                if(cur.data.equals(o)){
                    prev.next = cur.next;
                    this.size--;
                    return true;
                }
            }
        }
        // incase the data element is not found
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
	public boolean removeAllCopies( Object o ) {
        boolean hasBeenRemoved = false;
        while(remove(o)){
            hasBeenRemoved = true;
        }
		return false;
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
        // creates a new, third list
        MyLinkedList C = new MyLinkedList();
        // points two the first nodes of A and B
        ListNode curA = A.head.next;
        ListNode curB = B.head.next;
        // points to the dummy head of C
        ListNode curC = C.head;

        while(curA != null || curB != null){
            if(curA != null){
                curC.next = C.new ListNode(curA.data);
                curC = curC.next;
                C.size++;
                curA = curA.next;
            }
            if(curB != null){
                curC.next = C.new ListNode(curB.data);
                curC = curC.next;
                C.size++;
                curB = curB.next;
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
	public void add(int index, Object o) {
        // edge case
        if(index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }

        ListNode cur = this.head;
        int i = 0;

        while(i < index){
            cur = cur.next;
            i++;
        }
        // creates new node and inserts into list
        ListNode newNode = new ListNode(o);
        newNode.next = cur.next;
        cur.next = newNode;

        // updates size
        this.size++;
	}
	

	// Returns the element at the specified index in this list.
	// Be noted that the listnode at head.next has index 0 and 
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public Object get(int index) throws IndexOutOfBoundsException{
        // throws exception
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        ListNode cur = this.head.next;
        int i = 0;
        // same loop as add method
        while(i < index){
            cur = cur.next;
            i++;
        }
        // returns data
		return cur.data;
	}
	
	// Removes (cuts out) the list node at the specified index in this list. 
	// Returns the data element in the node that is removed.
	// Be noted that the list node at head.next has index 0 and 
	//      the last list node has index of size()-1.
	// if index < 0 or index >= this.size, throws IndexOutOfBoundsException.
	public Object remove(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        ListNode cur = this.head;
        int i = 0;
        while(i < index){
            cur = cur.next;
            i++;
        }
        // cut the node out
        ListNode removable = cur.next;
        cur.next = removable.next;
        // reduce size
        this.size--;
		// returns data of removed node
		return removable.data;
	}

	
	//Add the object e to the end of this list.
	// it returns true, after e is successfully added.
	public boolean add(Object e) {
        ListNode cur = this.head;
        // move to the last node
        while(cur.next != null){
            cur = cur.next;
        }
        // add the new node
        cur.next = new ListNode(e);
        // increase size
        this.size++;
		
		return true;
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
