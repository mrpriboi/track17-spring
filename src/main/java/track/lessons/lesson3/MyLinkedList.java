package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 * Односвязный список
 */
public class MyLinkedList extends List {

    /**
     * private - используется для сокрытия этого класса от других.
     * Класс доступен только изнутри того, где он объявлен
     * <p>
     * static - позволяет использовать Node без создания экземпляра внешнего класса
     */
    private static class Node {
        Node prev;
        Node next;
        int val;

        Node(Node prev, Node next, int val) {
            this.prev = prev;
            this.next = next;
            this.val = val;
        }
    }

    private Node head = null;
    private Node tail = null;

    public MyLinkedList() {
    }

    @Override
    void add(int item) {
        size += 1;
        if (tail == null) {
            Node element = new Node(null,null,item);
            head = element;
            tail = element;
            return;
        } else {
            Node element = new Node(tail,null,item);
            tail.next = element;
            tail = element;
            return;
        }
    }

    private Node getnode(int idx) {
        Node current;
        if (idx < size / 2) {
            current = head;
            while (idx > 0) {
                current = current.next;
                idx -= 1;
            }
        } else {
            idx = size - idx - 1;
            current = tail;
            while (idx > 0) {
                current = current.prev;
                idx -= 1;
            }
        }
        return current;
    }

    @Override
    int remove(int idx) throws NoSuchElementException {
        if (idx >= size || idx < 0) {
            throw new NoSuchElementException();
        }
        Node idxnode = getnode(idx);
        if (idxnode.next != null) {
            idxnode.next.prev = idxnode.prev;
        }
        if (idxnode.prev != null) {
            idxnode.prev.next = idxnode.next;
        }
        if (idx == 0) {
            head = head.next;
        }
        if (idx == size - 1) {
            tail = tail.prev;
        }
        size -= 1;
        return idxnode.val;
    }

    @Override
    int get(int idx) throws NoSuchElementException {
        if (idx >= size || idx < 0 ) {
            throw new NoSuchElementException();
        }
        return getnode(idx).val;
    }

}
