package vsu.cs.soshich;


import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void printTests(MyBigInteger int1, MyBigInteger int2) {
        MyBigInteger int3 = int1.add(int2);
        System.out.println("Сумма: " + int3.getValue());

        int3 = int1.subtract(int2);
        System.out.println("Разность: " + int3.getValue());

        int3 = int1.multiply(int2);
        System.out.println("Произведение: " + int3.getValue());

        int3 = int1.integerDivide(int2);
        System.out.println("Целочисленное деление: " + int3.getValue());

        int3 = int1.getDivisionRemainder(int2);
        System.out.println("Остаток от целочисленного деление: " + int3.getValue());
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Locale.setDefault(Locale.ROOT);

        MyBigInteger int1 = new MyBigInteger("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
        MyBigInteger int2 = new MyBigInteger("222222222222222222222222222222222222222222222222222229999999999999999999999999999999999999999999999999");

        printTests(int1, int2);
    }
}