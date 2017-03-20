package track.lessons.lesson3;

import org.junit.Assert;

import org.junit.Test;

import java.util.NoSuchElementException;

/**
 * Created by priboi on 20.03.17.
 */
public class MyStackTest {

    @Test
    public void testPopPush() {
        MyStack stack = new MyStack();
        Assert.assertTrue(stack.size() == 0);
        stack.push(1);
        stack.push(2);
        Assert.assertTrue(stack.size() == 2);
        stack.pop();
        Assert.assertTrue(stack.size() == 1);
        stack.pop();
        Assert.assertTrue(stack.size() == 0);
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemove() {
        MyStack stack = new MyStack();
        Assert.assertTrue(stack.size() == 0);
        stack.pop();
    }

    @Test
    public void testElements() {
        MyStack stack = new MyStack();
        stack.push(1);
        stack.push(2);
        stack.push(3);

        Assert.assertEquals(3,stack.size());

        Assert.assertEquals(3,stack.pop());
        Assert.assertEquals(2,stack.pop());
        Assert.assertEquals(1,stack.pop());

        Assert.assertEquals(0,stack.size());
    }

}
