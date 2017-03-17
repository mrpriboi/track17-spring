package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Created by priboi on 17.03.17.
 */

interface Queue {

    void enqueue(int value);

    int dequeu() throws NoSuchElementException;

    int size();
}
