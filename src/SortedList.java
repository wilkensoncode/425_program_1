
public class SortedList {
    Node head;
    int size;

    public SortedList() {
        head = null;
        size = 0;
    }

    public SortedList(Node t) {
        head = t;
        size = 0;
    }

    public void add(Node t) {
        size++;
        if (head == null) {
            head = t;
            t.next = null;
            return;
        } else {
            Node previous = null;
            Node current = head;

            while (current != null && current.processRunTime < t.processRunTime) {
                previous = current;
                current = current.next;
            }
            if (previous == null)     // node to be added as the first node
            {
                t.next = head;
                head = t;

            } else {
                previous.next = t;
                t.next = current;
            }
        }
    }

    public void displayList() {
        Node t = head;

        while (t != null) {
            System.out.print(" " + t.processRunTime + " ,");
            t = t.next;
        }
        System.out.println();
    }

    public Node delete() {

        if (head == null) {
            return null;
        } else {
            Node p = head;
            head = p.next;
//			displayList();
            size--;
            return p;
        }
    }

}
