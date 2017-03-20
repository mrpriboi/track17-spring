package track.lessons.lesson3;

import org.junit.Assert;

import org.junit.Test;

import java.util.NoSuchElementException;

/**
 * Created by priboi on 20.03.17.
 */
public class MyQueueTest {
    @Test
    public void testInOut() {
        MyQueue queue = new MyQueue();

        Assert.assertTrue(queue.size() == 0);
        queue.enqueue(1);
        Assert.assertTrue(queue.size() == 1);
        queue.dequeu();
        Assert.assertTrue(queue.size() == 0);
    }

    @Test(expected = NoSuchElementException.class)
    public void testException() {
        MyQueue queue = new MyQueue();

        Assert.assertTrue(queue.size() == 0);
        queue.dequeu();
    }

    @Test
    public void testElements() {
        MyQueue queue = new MyQueue();

        queue.enqueue(1);
        Assert.assertEquals(1,queue.size());
        queue.enqueue(2);
        Assert.assertEquals(2,queue.size());
        queue.enqueue(3);
        Assert.assertEquals(3,queue.size());

        Assert.assertEquals(1,queue.dequeu());
        Assert.assertEquals(2,queue.dequeu());
        Assert.assertEquals(3,queue.dequeu());

        Assert.assertEquals(0,queue.size());
    }
}
