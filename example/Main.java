package org.example;
import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(new File("array.txt"));
        int[] arr = new int[10000];
        for(int i = 0; i < arr.length; i++){
            arr[i] = sc.nextInt();
        }
        TwoThreeTree ttt = new TwoThreeTree();
        //добавление
        FileWriter fileWriter1 = new FileWriter("insertionIter.txt");
        for(int i = 0; i < arr.length; i++){
            //long start = System.nanoTime();
            ttt.insert(arr[i]);
            //long end = System.nanoTime();
            fileWriter1.write(ttt.insIterations + "\n");
            ttt.insIterations = 0;
        }
        fileWriter1.close();
        System.out.println("Операция добавления выполнена успешно");

        Random r = new Random();
        //поиск 100 элементов
        int[] elemsForSearch = new int[100];
        for(int i = 0; i < elemsForSearch.length; i++){
            elemsForSearch[i] = arr[r.nextInt(arr.length)];
        }
        FileWriter fileWriter2 = new FileWriter("searchIter.txt");
        for(int i = 0; i < elemsForSearch.length; i++){
            //long start = System.nanoTime();
            ttt.search(elemsForSearch[i]);
            //long end = System.nanoTime();
            //fileWriter2.write(end - start + "\n");  ЗАКОММИТИЛ, тк сначала в файл записывал время,
            // затем итерации для каждого элемента, можно наоборот
            fileWriter2.write(ttt.searchIterations + "\n");
            ttt.searchIterations = 0;
        }
        fileWriter2.close();
        System.out.println("Операция поиска 100 элементов выполнена успешно");

        //удаление 1000 элементов
        FileWriter fileWriter3 = new FileWriter("removingIter.txt");
        int[] elemsForDelete = new int[100];
        for(int i = 0; i < elemsForDelete.length; i++){
            elemsForDelete[i] = arr[r.nextInt(arr.length)];
        }
        for(int i = 0; i < elemsForDelete.length; i++) {
            long start = System.nanoTime();
            ttt.remove(elemsForDelete[i]);
            long end = System.nanoTime();
            //fileWriter3.write(end - start + "\n");
            fileWriter3.write(ttt.remIterations + "\n");
            ttt.remIterations = 0;
        }
        fileWriter3.close();
        System.out.println("Операция удаления 100 элементов выполнена успешно");
    }
}