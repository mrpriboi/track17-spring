package track.lessons.lesson3;

import java.util.NoSuchElementException;

/**
 * Должен наследовать List
 *
 * Должен иметь 2 конструктора
 * - без аргументов - создает внутренний массив дефолтного размера на ваш выбор
 * - с аргументом - начальный размер массива
 */
public class MyArrayList extends List {

    private static final int defaultsize = 64;

    private int[] array;
    private int arraycapacity = 0; //размер массива

    public MyArrayList() {
        array = new int[defaultsize];
        arraycapacity = defaultsize;
    }

    public MyArrayList(int capacity) {
        array = new int[capacity];
        arraycapacity = capacity;
    }

    public void realloc() {
        arraycapacity *= 2;
        int[] newarray = new int[arraycapacity];
        System.arraycopy(array, 0, newarray, 0, size);
        array = newarray;
    }

    @Override
    void add(int item) {
        if (size == arraycapacity) {
            realloc();
        }
        array[size] = item;
        size++;
    }

    @Override
    int remove(int idx) throws NoSuchElementException {
        if (idx > size || idx < 0) {
            throw new NoSuchElementException();
        }
        System.arraycopy(array, idx + 1, array, idx, size - idx);
        array[size] = 0;
        size--;
        return array[idx];
    }

    @Override
    int get(int idx) throws NoSuchElementException {
        if (idx >= size || idx < 0) {
            throw new NoSuchElementException();
        }
        return array[idx];
    }

}
