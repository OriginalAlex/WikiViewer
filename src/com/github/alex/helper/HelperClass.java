package com.github.alex.helper;

import org.jsoup.Jsoup;

/**
 * Created by Alex on 21/08/2017.
 */
public class HelperClass {

    public static boolean isValidURL(String URL) {
        try {
            Jsoup.connect(URL).get();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
