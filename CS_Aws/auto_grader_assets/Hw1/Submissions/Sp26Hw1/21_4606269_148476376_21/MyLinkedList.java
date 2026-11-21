import java.util.ArrayList;
import java.util.NoSuchElementException;

public class MyLinkedList {

	private ListNode head;
	private int size;

	//inner class for ListNode
	static class ListNode {
		private Object data;
		private ListNode next;

		ListNode(Object d) {
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


	public void addFirst(Object e) {
		ListNode node = new ListNode(e);
		node.next = head.next;
		head.next = node;
		size++;
	}


	public Object removeFirst() throws Exception {
		if (size == 0) {
			throw new Exception("LinkedList is empty");
		}
		ListNode temp = head.next;
		head.next = temp.next;
		size--;

		return temp.data;
	}


	public boolean contains(Object o) {
		ListNode current = head.next;
		while (current != null) {
			if (current.data == null) {
				if (o == null) {
					return true;
				}
			} else if (current.data.equals(o)) {
				return true;
			}
			current = current.next;
		}
		return false;
	}



	public boolean remove(Object o) {
		ListNode current = head.next;
		ListNode previous = head;
		while (current != null) {
			if (o == null && current.data == null) {
				previous.next = current.next;
				size--;
				return true;
			} else if (o != null) {
				if (o.equals(current.data)) {
					previous.next = current.next;
					size--;
					return true;
				}
			}
			previous = current;
			current = current.next;
		}

		return false;
	}


	public boolean removeAllCopies(Object o) { //passed test
		ListNode previous = head;
		ListNode current = head.next;
		while (current != null) {
			if (current.data == null && o == null) {
				previous.next = current.next;
				size--;
				current = previous.next;
			} else if (current.data != null && current.data.equals(o)) {
					previous.next = current.next;
					size--;
					current = previous.next;
					return true;
				}else{
					previous = current;
					current = current.next;
					return true;
				}

			}



		return false;
	}

	

	public static MyLinkedList interleave(MyLinkedList A, MyLinkedList B) {
		MyLinkedList c = new MyLinkedList();

		ListNode a = A.head.next;
		ListNode b = B.head.next;

		while(a != null || b != null){
			if(a != null){
				c.add(a.data);
				a = a.next;
			}
			if(b != null){
				c.add(b.data);
				b = b.next;
			}
		}

		
		return c;
	}

	public void add(int index, Object o) {
		if(index < 0 || index > this.size){
			throw new IndexOutOfBoundsException("Index passed in not valid");
		}
		ListNode current = head;
		int i = 0;
		while(i < index){
			current = current.next;
			i++;
		}
		ListNode newNode = new ListNode(o);
		newNode.next = current.next;
		current.next = newNode;
		size++;
		
	}


	public Object get(int index) throws IndexOutOfBoundsException{
		if(index < 0 || index >= this.size){
			throw new IndexOutOfBoundsException("Index passed in not valid");
		}
		ListNode current = head.next;
		for(int i = 0; i < index; i++){
			current = current.next;
		}

		return current.data;
	}
	

	public Object remove(int index) throws IndexOutOfBoundsException {
		if(index < 0 || index >= this.size){
			throw new IndexOutOfBoundsException("Index passed in not valid");
		}
		ListNode previous= head;
		ListNode current = head.next;
		for(int i = 0; i < index; i++){
			previous = current;
			current = current.next;
		}
		ListNode removed = current;
		previous.next = current.next;
		size--;
		return removed.data;
	}

	

	public boolean add(Object e) {
		add(size,e);
		
		return true;
	}
	

	@Override
	public String toString() {
		String result = "{";
	    for (MyLinkedList.ListNode node = this.head.next; node != null; node = node.next) {
	    		if(node.next != null)
	    			result += node.data + "->"; 
	    		else
	    			result += node.data;
	    }
	    return result + "}";
	  }
}
