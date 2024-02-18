public class List {
    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;

    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }

    /** Returns the number of elements in this list. */
    public int getSize() {
        return size;
    }

    /** Returns the first element in the list */
    public char getFirst() {
        if (first != null)
            return first.cp.chr;
        else
            throw new RuntimeException("List is empty");
    }

    /** Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        Node newNode = new Node(new CharData(chr), first);
        first = newNode;
        size++;
    }

    /** Textual representation of this list. */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = first;
        while (current != null) {
            sb.append(current.cp).append(" ");
            current = current.next;
        }
        return sb.toString();
    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char chr) {
        Node current = first;
        int index = 0;
        while (current != null) {
            if (current.cp.chr == chr)
                return index;
            current = current.next;
            index++;
        }
        return -1;
    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
        int index = indexOf(chr);
        if (index != -1) {
            Node current = first;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            // increment the counter directly on Node
            current.cp.count++;
        } else {
            addFirst(chr);
        }
    }

    /** If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
        Node current = first;
        Node prev = null;
        while (current != null) {
            if (current.cp.chr == chr) {
                if (prev != null)
                    prev.next = current.next;
                else
                    first = current.next;
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    /** Returns the CharData object at the specified index in this list.
     *  If the index is negative or is greater than the size of this list,
     *  throws an IndexOutOfBoundsException. */
    public CharData get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.cp;
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
        CharData[] arr = new CharData[size];
        Node current = first;
        int i = 0;
        while (current != null) {
            arr[i++] = current.cp;
            current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
    public char[] listIterator(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        char[] iterator = new char[size - index];
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        int i = 0;
        while (current != null) {
            iterator[i++] = current.cp.chr;
            current = current.next;
        }
        return iterator;
    }

    /** Inner class representing a node in the list. */
    private static class Node {
        CharData cp;
        Node next;

        Node(CharData cp, Node next) {
            this.cp = cp;
            this.next = next;
        }
    }
}