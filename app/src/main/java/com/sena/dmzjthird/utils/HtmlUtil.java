package com.sena.dmzjthird.utils;

/**
 * FileName: HtmlUtil
 * Author: JiaoCan
 * Date: 2022/3/11 16:17
 */

public class HtmlUtil {

    public static String convertSpecialCharacters(String originalStr) {


        return originalStr
                .replace("&nbsp;", " ")
                .replace("&hellip;", "...")
                .replace("&bull;", "·")
                .replace("&ldquo;", "「")
                .replace("&rdquo;", "」")
                .replace("&mdash;", "-")
                .replace("&lsquo;", "‘")
                .replace("&rsquo;", "’")
                .replace("&amp;", "&")
                .replace("&middot;", "·")
                .replace("&#12316;", "〜");

    }

}
