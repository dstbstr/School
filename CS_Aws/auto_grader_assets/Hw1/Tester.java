import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

import java.beans.Transient;

public class Tester {

	private MyLinkedList list;
	private MyLinkedList listAbc;

	@Before
	public void setUp() {
		list = new MyLinkedList();
		listAbc = new MyLinkedList();
		listAbc.add("A");
		listAbc.add("B");
		listAbc.add("C");
	}

	@Test
	public void AddFirst_ThenTraverse_GetsReversedElements() {
		assertEquals(listAbc.toString(), "{A->B->C}");
	}

	@Test
	public void AddFirst_WithNull_KeepsNulls() {
		list.addFirst("A");
		list.addFirst(null);
		list.addFirst("B");
		list.addFirst(null);
		
		assertEquals(list.toString(), "{null->B->null->A}");
	}
	
	@Test
	public void AddLast_InsertsElements_AtEnd() { 
		list.add("A");
		assertEquals(list.toString(), "{A}");

		list.add("B");
		assertEquals(list.toString(), "{A->B}");

		list.add("C");
		assertEquals(list.toString(), "{A->B->C}");
	}
	
	@Test
	public void AddIndex_BeyondSize_ThrowsException() {
		try {
			list.add(5, "A5");
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//pass
		}
	}

	@Test
	public void AddIndex_NegativeIndex_ThrowsException() {
		try {
			list.add(-1, "A-1");
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//pass
		}
	}

	@Test
	public void AddIndex_With1_InsertsAfterZerothElement() {
		listAbc.add(1, "D");
		assertEquals(listAbc.toString(), "{A->D->B->C}");
	}

	@Test
	public void AddIndex_With0_InsertsAtBeginning() {
		listAbc.add(0, "D");

		assertEquals(listAbc.toString(), "{D->A->B->C}");
	}
	
	@Test
	public void RemoveIndex_FirstElement_RemovesFirstElement() {
		listAbc.remove(0);
		assertEquals(listAbc.toString(), "{B->C}");
	}

	@Test
	public void RemoveIndex_LastElement_RemovesLastElement() {
		listAbc.remove(2);
		assertEquals(listAbc.toString(), "{A->B}");
	}

	@Test
	public void RemoveIndex_InvalidElement_ThrowsException() {

		try {
			listAbc.remove(5);
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//pass
		}

		try {
			list.remove(-1);
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//pass
		}
	}

	@Test
	public void GetIndex_WithValid_ReturnsElement() {
		assertEquals(listAbc.get(0), "A");
		assertEquals(listAbc.get(1), "B");
		assertEquals(listAbc.get(2), "C");
	}
	
	@Test
	public void GetIndex_WithInvalid_ThrowsException() {
		try {
			listAbc.get(5);
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//pass
		}
		try {
			listAbc.get(-1);
			fail("Expected IndexOutOfBoundsException");
		} catch (IndexOutOfBoundsException e) {
			//pass
		}
	}

	@Test
	public void Contains_WithValidElement_ReturnsTrue() {
		assertTrue(listAbc.contains("A"));
		assertTrue(listAbc.contains("B"));
		assertTrue(listAbc.contains("C"));
	}

	@Test
	public void Contains_WithNullElement_ReturnsTrue() {
		list.add(null);
		assertTrue(list.contains(null));
	}

	@Test
	public void Contains_WithInvalidElement_ReturnsFalse() {
		assertFalse(listAbc.contains("D"));
		assertFalse(listAbc.contains(null));
	}

	@Test
	public void RemoveObject_WithValidElement_RemovesElement() {
		listAbc.remove("B");
		assertEquals(listAbc.toString(), "{A->C}");
		
		listAbc.remove("C");
		assertEquals(listAbc.toString(), "{A}");
		
		listAbc.remove("A");
		assertEquals(listAbc.toString(), "{}");
	}

	@Test
	public void RemoveObject_WithMissingElement_DoesNothing() {
		listAbc.remove("D");
		assertEquals(listAbc.toString(), "{A->B->C}");
		listAbc.remove(null);
		assertEquals(listAbc.toString(), "{A->B->C}");
	}

	@Test
	public void RemoveObject_WithNullElement_RemovesNull() {
		list.add("A");
		list.add(null);
		list.add("C");
		list.remove(null);
		assertEquals(list.toString(), "{A->C}");
	}

	@Test
	public void RemoveObject_WithDuplicateElements_RemovesFirst() {
		listAbc.add("B");
		
		listAbc.remove("B");
		assertEquals(listAbc.toString(), "{A->C->B}");
	}

	@Test
	public void RemoveFirst_WithValidElement_RemovesAndReturnsFirst() {
		try {
			Object t = listAbc.removeFirst();
			assertEquals(t.toString(), "A");
			assertEquals(listAbc.toString(), "{B->C}");

			t = listAbc.removeFirst();
			assertEquals(t.toString(), "B");
			assertEquals(listAbc.toString(), "{C}");

			t = listAbc.removeFirst();
			assertEquals(t.toString(), "C");
			assertEquals(listAbc.toString(), "{}");
		} catch(Exception e) {
			fail("Remove First threw an exception");
		}
	}

	@Test
	public void RemoveAllCopies_WithDuplicatedElement_RemovesAll() {
		listAbc.add("B");
		listAbc.add("B");
		listAbc.add("B");
		listAbc.removeAllCopies("B");
		assertEquals(listAbc.toString(), "{A->C}");
	}

	@Test
	public void RemoveAllCopies_WithNonExistentElement_DoesNothing() {
		listAbc.removeAllCopies("D");
		assertEquals(listAbc.toString(), "{A->B->C}");
	}

	@Test
	public void RemoveAllCopies_WithNullElement_RemovesAllNulls() {
		list.add("A");
		list.add(null);
		list.add(null);
		list.add("C");
		list.removeAllCopies(null);
		assertEquals(list.toString(), "{A->C}");
	}

	@Test
	public void Interleave_WithEmptyList_ReturnsOriginal() {
		MyLinkedList result = MyLinkedList.interleave(list, listAbc);
		assertEquals(result.toString(), "{A->B->C}");
	}

	@Test
	public void Interleave_WithSameSizeLists_Interleaves() {
		MyLinkedList list123 = new MyLinkedList();
		list123.add("1");
		list123.add("2");
		list123.add("3");
		
		MyLinkedList result = MyLinkedList.interleave(listAbc, list123);
		assertEquals(result.toString(), "{A->1->B->2->C->3}");
	}

	@Test
	public void Interleave_WithDifferentSizeLists_KeepsExtrasAtTheEnd() {
		MyLinkedList list12 = new MyLinkedList();
		list12.add("1");
		list12.add("2");

		MyLinkedList result = MyLinkedList.interleave(list12, listAbc);
		assertEquals(result.toString(), "{1->A->2->B->C}");
	}
}
