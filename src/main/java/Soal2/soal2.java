package Soal2;

import java.util.Scanner;

public class soal2 {

    public static void main(String[] args) {
        double alas, tinggi, hasil;

        Scanner scanner = new Scanner(System.in);
        System.out.println("Jenis Segitiga = ");
        System.out.println("Alas = ");
        alas=scanner.nextDouble();
        System.out.println("Tinggi = ");
        tinggi=scanner.nextDouble();
        hasil = Math.sqrt(alas*alas + tinggi*tinggi);
        if (alas == tinggi ) {
            System.out.println("Segitiga Sama Sisi");
        } else {
            System.out.println("Hasil = " + hasil);
            scanner.close();
        }

    }

}
