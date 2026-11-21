public class Main {
    public static void main(String[] args) {
        LinkedList TestingList = new LinkedList();
        TestingList.addFirst(new ListNode(4));
        TestingList.addFirst(new ListNode(9));
        TestingList.addFirst(new ListNode(2));
        TestingList.addFirst(new ListNode(6));
        TestingList.addFirst(new ListNode(47));
        TestingList.addFirst(new ListNode(4));
        TestingList.addFirst(new ListNode(9));
        TestingList.addFirst(new ListNode(2));
        TestingList.addFirst(new ListNode(6));
        TestingList.addFirst(new ListNode(47));
        TestingList.mergeSort();
        System.out.println(TestingList.toString());
        LinkedList TestingList2 = new LinkedList();
        TestingList2.addFirst(new ListNode(4));
        TestingList2.addFirst(new ListNode(9));
        TestingList2.addFirst(new ListNode(2));
        TestingList2.addFirst(new ListNode(6));
        TestingList2.addFirst(new ListNode(47));
        TestingList2.insertionSort();
        System.out.println(TestingList2.toString());
    }
}