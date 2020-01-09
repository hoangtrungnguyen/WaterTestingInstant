package com.hackathon.watertestinginstant;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaTest {
    public static void main(String[] args) {
//        Pattern pattern = Pattern.compile("0\\d{8}^[470]");

        System.out.println(isARainbow("redredorangeyellowgreenblueindigoviolet"));
        System.out.println(isARainbow("redyelloworangeprange"));
        System.out.println(isARainbow("redorangeyellowgreenblueredyellow"));
    }

    static boolean validPhoneNumber(String phone) {
        Pattern pattern = Pattern.compile("^0\\d{8}^[470]$");
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    static boolean isARainbow(String rangeColor) {
        String[] rain = new String[]{"red", "orange", "yellow", "green", "blue", "indigo", "violet"};
        while (rangeColor.length() > 0) {
            for (String i : rain) {
                if (rangeColor.contains(i)) {
                    rangeColor = rangeColor.replace(i, "");
                } else {
                    return false;
                }
            }
        }

        return true ;
    }
}

class Test {

    @org.junit.Test
    public void test() {

    }
}