package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Created by priboi on 17.03.17.
 */
public class MyQueue implements Queue {

    private MyLinkedList list = new MyLinkedList();

    @Override
    public void enqueue(int value) {
        list.add(value);
    }

    @Override
    public int dequeu() throws NoSuchElementException {
        return list.remove(0);
    }

    @Override
    public int size() {
        return list.size();
    }
}
