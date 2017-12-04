/*
 * Copyright (c) 2012 IYE Technologies Limited
 * All rights reserved.
 *
 */
package com.captain.practice.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Derek Zheng
 */
public abstract class NumberIncrementalUtils {

    private static String MAX_UNIQUE_NUMBER="9999999";
    private static int MAX_SIZE = 10;
    private static char LETTER_CHAR_A='A';
    private static char LETTER_CHAR_Z='Z';

    public static String fillToFixedSize(Number input) {
        String text = String.valueOf(input);
        int length = text.length();
        if (MAX_SIZE <= length) {
            return text;
        }

        int padding = MAX_SIZE - length;
        for (int i = 0; i < padding; i++) {
            text = "0" + text;
        }

        return text;
    }


    public static String autoIncrease(String input) {
        if (!StringUtils.hasText(input)) {
            return "";
        }
        String numberToken = extractNumber(input);
        if (StringUtils.hasText(numberToken)) {
            String increamental = String.valueOf(Integer.parseInt(numberToken) + 1);
            int padding = numberToken.length() - increamental.length();
            for (int i = 0; i < padding; i++) {
                increamental = "0" + increamental;
            }

            String prefix = extractPrefix(input, numberToken);
            //if the number has reached the max amount,the prefix need to be increased
            if(MAX_UNIQUE_NUMBER.equals(numberToken)){
                prefix = processPrefix(prefix);
            }
            String suffix = extractSuffix(input, numberToken);
            return prefix + increamental + suffix;
        }
        return "";
    }

    /**
     * prefix with char will also need to auto increase
     * ie: AB after increase 1,the result should be AC
     *     and AZ after increase 1,the result should be BA
     * @param prefix the prefix need to be increased
     * @return increased prefix
     */
    protected static String processPrefix(String prefix){
        StringBuffer sb = new StringBuffer(prefix);
        int zPosition = findNonZPosition(sb, sb.length() - 1);
        for(int i=sb.length()-1;i>zPosition;i--){
            sb.setCharAt(i,LETTER_CHAR_A);
        }
        char curChar = sb.charAt(zPosition);
        int oldChar = (int) curChar;
        char newChar = (char)(oldChar+1);
        sb.setCharAt(zPosition,newChar);
        return sb.toString();
    }

    private static int findNonZPosition(StringBuffer sb, int curPos){
        char c = sb.charAt(curPos);
        for(curPos=sb.length()-1;curPos>0;curPos--){
            if(LETTER_CHAR_Z !=c){
                return curPos;
            }
        }
        return curPos;
    }
    protected static String extractPrefix(String input, String numberToken) {
        if (StringUtils.hasText(input) && StringUtils.hasText(numberToken)) {
            int end = input.lastIndexOf(numberToken);
            return input.substring(0, end);
        }
        return input;
    }

    protected static String extractNumber(String input) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(input);
        String lastToken = null;
        while (m.find()) {
            lastToken = m.group();
        }
        return lastToken;
    }

    protected static String extractSuffix(String input, String token) {
        if (StringUtils.hasText(input) && StringUtils.hasText(token)) {
            int from = input.lastIndexOf(token);
            return input.substring(from + token.length());
        }
        return input;
    }
}
