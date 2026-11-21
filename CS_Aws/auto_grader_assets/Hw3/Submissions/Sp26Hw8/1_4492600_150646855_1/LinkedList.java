import org.w3c.dom.Node;

public class LinkedList {
    ListNode head;
    ListNode tail;

    public LinkedList() {
        head = null;
        tail = null;
    }

    public void addFirst(ListNode node) {
        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
    }

    public void addLast(ListNode node) {
        if (head == null) {
            head = node;
            tail = node;
        } else {
            node.prev = tail;
            tail.next = node;
            tail = node;
        }
    }

    public ListNode removeFirst() {
        if (head == null) {
            return null;
        } else {
            ListNode tmep = head;
            head = head.next;
            tmep.next = null;
            tmep.prev = null;
            if (head == null) {
                tail = null;
                return tmep;
            }
            head.prev = null;
            return tmep;
        }
    }


    public LinkedList mergeLists(LinkedList list1, LinkedList list2) {
        LinkedList S = new LinkedList();
        while (list1.head != null && list2.head != null) {
            if (list1.head.data < list2.head.data) {
                ListNode temp = new ListNode(list1.head.data);
                S.addLast(temp);
                list1.removeFirst();
            } else {
                ListNode temp = new ListNode(list2.head.data);
                S.addLast(temp);
                list2.removeFirst();
            }
        }
        while (list1.head != null) {
            ListNode temp = new ListNode(list1.head.data);
            S.addLast(temp);
            list1.removeFirst();
        }
        while (list2.head != null) {
            ListNode temp = new ListNode(list2.head.data);
            S.addLast(temp);
            list2.removeFirst();
        }
        return S;
    }

    public void mergeSort() {
        Queue q = new Queue();
        ListNode current = this.head;
        while (current != null) {
            LinkedList temp = new LinkedList();
            ListNode toAdd = new ListNode(current.data);
            temp.addFirst(toAdd);
            q.enqueue(temp);
            current = current.next;
        }
        if (q.head != null) {
            while (q.head.next != null) {
                LinkedList sublist1 = q.dequeue();
                LinkedList sublist2 = q.dequeue();
                LinkedList tempList = mergeLists(sublist1, sublist2);
                q.enqueue(tempList);
            }
        }
        LinkedList Finale = q.dequeue();
        if (Finale != null) {
            this.head = Finale.head;
            this.tail = Finale.tail;
        }
    }

    @Override
    public String toString() {
        StringBuilder Strung = new StringBuilder();
        ListNode current = head;
        Strung.append("List Nodes:\n");
        if (head == null) {
            return Strung.toString();
        }
        Strung.append("[").append(current.data).append("] ");
        while (current.next != null) {
            current = current.next;
            Strung.append("-> [").append(current.data).append("] ");
        }
        return Strung.toString();
    }

    /*
    1, Create a Queue object q, then enqueue each data object in the linked list to be sorted, as a
single-node LinkedList. The following piece of pseudo code illustrates this step.
{
Queue q = new Queue()
For each data object D in the linked list to be sorted
first create a new linked list object, newList = new LinkedList(),
perform newList.addFirst(D),
q.enqueue(newList)
End For
}
2, while there is more than one items in the Queue q {
a. dequeue and assign to a LinkedList reference sublist1, (already sorted)
b. dequeue again and assign to another LinkedList reference sublist2, (already sorted)
c. walk through sorted sublist1 and sorted sublist2 and "merge" them into a larger sorted
list tempList, with tempList = merge(sublist1, sublist2 )
d. q.enqueue( tempList )
}
3, dequeue your sorted linked list. Here think about how to make this list sorted, because the
dequeued list and this list are different object. Hint: we learned this technique when we used
addOrdered() method to sort an linked list. */
    /*
The logic idea of the merge() method in the step 2.c above is also provided in the below.
Algorithm merge(A, B)
Input sorted SubList A and sorted SubList B
Output sorted sequence S as Union A and B
S <--create an empty list
while (A is not Empty and B is not Empty ) {
fa = A.getFirst(); //return the first piece of data element in A, but not delete
fb = B.getFirst();
if fa < fb {
A.removeFirst() //A becomes shorter
S.addLast(fa) // supposed to have O(1) time complexity
}
else {
B.removeFirst() //B becomes shorter
S.addLast(fb) // supposed to have O(1) time complexity
}
}
while A is not Empty {
fa = A.getFirst();
S.addLast( fa)// supposed to have O(1) time complexity
A.removeFirst();
}
while B is not Empty() {
fb = B.getFirst();
S.addLast(fb) // supposed to have O(1) time complexity
B.removeFirst();
}
return S;
     */
    /*
    Please implement the InsertionSort() method in your LinkedList class that is designed for
sorting a LinkedList object with InsertionSort Algorithm, as we learned in the classroom.
     */
    public void insert(ListNode node) {
        if (head == null) {
            head = node;
            tail = node;
        }
        else {
            if (head.data > node.data) {
                node.next = head;
                head.prev = node;
                head = node;
                return;
            }
            ListNode temp = head;
            while (temp.next != null && temp.next.data < node.data) {
                temp = temp.next;
            }
            if (temp.next == null) {
                tail = node;
            }
            else {
                temp.next.prev = node;
            }
            node.next = temp.next;
            node.prev = temp;
            temp.next = node;
        }
    }
    public void insertionSort() {
        LinkedList S = new LinkedList();
        ListNode current = this.head;
        while (current != null) {
           ListNode temp = new ListNode(current.data);
           S.insert(temp);
           current = current.next;
        }
        head = S.head;
        tail = S.tail;
    }
    /*
    Please write a method boolean isSorted() in your LinkedList class to verify whether this
LinkedList is correctly sorted in an ascending order or not.
boolean isSorted()
Note: isSorted() returns true, if this list has been sorted in ascending order. Otherwise it
returns false.
Note: your isSorted() has to run in the time complexity of O(n), where n is the size of the
input LinkedList. In other words, you scan this input list once, you should know whether
it is sorted or not.
     */
    public boolean isSorted() {
        if (head == null) {
            return true;
        }
        ListNode current = head.next;
        while (current != null) {
            if (current.data < current.prev.data) {
                return false;
            }
            current = current.next;
        }
        return true;
    }
}