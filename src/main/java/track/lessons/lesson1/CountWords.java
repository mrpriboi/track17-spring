package track.lessons.lesson1;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.StringBuilder;
import java.lang.Integer;

/**
 * Задание 1: Реализовать два метода
 *
 * Формат файла: текстовый, на каждой его строке есть (или/или)
 * - целое число (int)
 * - текстовая строка
 * - пустая строка (пробелы)
 *
 *
 * Пример файла - words.txt в корне проекта
 *
 * ******************************************************************************************
 *  Пожалуйста, не меняйте сигнатуры методов! (название, аргументы, возвращаемое значение)
 *
 *  Можно дописывать новый код - вспомогательные методы, конструкторы, поля
 *
 * ******************************************************************************************
 *
 */
public class CountWords {

    /**
     * Метод на вход принимает объект File, изначально сумма = 0
     * Нужно пройти по всем строкам файла, и если в строке стоит целое число,
     * то надо добавить это число к сумме
     * @param file - файл с данными
     * @return - целое число - сумма всех чисел из файла
     */
    public long countNumbers(File file) throws Exception {
        int sum = 0;
        String line;
        BufferedReader br = new BufferedReader(new FileReader(file));
        while ((line = br.readLine()) != null) {
            try {
                sum += Integer.parseInt(line);
            } catch (NumberFormatException e) {
            }
        }
        return sum;
    }


    /**
     * Метод на вход принимает объект File, изначально результат= ""
     * Нужно пройти по всем строкам файла, и если в строка не пустая и не число
     * то надо присоединить ее к результату через пробел
     * @param file - файл с данными
     * @return - результирующая строка
     */
    public String concatWords(File file) throws Exception {
        StringBuilder result = new StringBuilder("");
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.length() > 0) {
                    try {
                        Integer.parseInt(line);
                    } catch (NumberFormatException e) {
                        if (result.length() > 0) {
                            result.append(" ");
                        }
                        result.append(line);
                    }
                }
            }
        } catch (Exception e) {
        }
        String sum = new String(result);
        return sum;
    }
}
