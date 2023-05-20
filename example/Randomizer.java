
package org.example;

import java.io.*;
import java.util.Random;

public class Randomizer { //нужен для заполнения файла случайными числами,
    // который мы передаем для заполнения дерева
    private static final int COUNT_OF_NUMBERS = 10000;

    public static void main(String[] args) {
        Random random = new Random();
        File file = new File("array.txt");
        int[] randomNumbers = new int[COUNT_OF_NUMBERS];

// Заполняем массив случайными числа
        for (int i = 0; i < COUNT_OF_NUMBERS; i++) {
            randomNumbers[i] = random.nextInt(100);
        }



// Записываем в файл
        try (FileWriter writer = new FileWriter(file)) {
            for (int i = 0; i < COUNT_OF_NUMBERS; i++) {
                writer.write(randomNumbers[i] + "\n");
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


