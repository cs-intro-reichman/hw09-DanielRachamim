/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
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
            return first.character;
        else
            throw new NoSuchElementException("List is empty");
    }

    /** Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
        Node newNode = new Node(chr);
        newNode.next = first;
        first = newNode;
        size++;
    }

    /** Textual representation of this list. */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node current = first;
        while (current != null) {
            sb.append(current.character).append(" ");
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
            if (current.character == chr)
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
            current.count++;
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
            if (current.character == chr) {
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
    public char get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.character;
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public char[] toArray() {
        char[] arr = new char[size];
        Node current = first;
        int i = 0;
        while (current != null) {
            arr[i++] = current.character;
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
            iterator[i++] = current.character;
            current = current.next;
        }
        return iterator;
    }

    /** Inner class representing a node in the list. */
    private static class Node {
        char character;
        public int count;
        Node next;

        Node(char character) {
            this.character = character;
            this.next = null;
        }
    }
}
