package com.hackathon.watertestinginstant;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaTest {
    public static void main(String[] args) {
//        Pattern pattern = Pattern.compile("0\\d{8}^[470]");

        System.out.println(isARainbow("redredorangeyellowgreenblueindigoviolet"));
        System.out.println(isARainbow("redyelloworangeprange"));
        System.out.println(isARainbow("redorangeyellowgreenblueredyellow"));


        System.out.println(purchaseInShop(new int[]{1, 1, 2, 3, 5, 8, 13, 21, 34}, 21));
    }

    static boolean validPhoneNumber(String phone) {
        Pattern pattern = Pattern.compile("^0\\d{8}^[470]$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    static boolean isARainbow(String rangeColor) {
        String[] rain = new String[]{"red", "orange", "yellow", "green", "blue", "indigo", "violet"};
//        Matcher matcher =
        for (String i : rain) {
            Pattern pt = Pattern.compile(i);
            Matcher matcher = pt.matcher(i);
//            while (matcher.start()){
//                rangeColor.
//            }
        }

        return true;
    }


    static int purchaseInShop(int[] a, int m) {
        int count = 0;
        for (int i = 0; i < a.length; i++) {
            int sum = 0;
            if (sum == m) count += 1;
            for (int j = i; j < a.length; j++) {
                sum += a[j];
                if(sum == m){
                    count += 1;
                    break;
                }
            }
        }
        return count;
    }

}

class Test {

    @org.junit.Test
    public void test() {

    }
}