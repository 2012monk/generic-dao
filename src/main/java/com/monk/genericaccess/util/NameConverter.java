package com.monk.genericaccess.util;

import java.util.Locale;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NameConverter {

    public String convertClassNameToDbName (String input) {
        Matcher matcher = Pattern
                .compile("[A-Z][a-z]*")
                .matcher(input);
        StringJoiner joiner= new StringJoiner("_");
        while (matcher.find()){
            joiner.add(matcher.group().toUpperCase(Locale.ROOT));
        }
        return joiner.toString();
    }

    public String convertToDbNameConvention(String input){
        Matcher matcher = Pattern
                .compile("[a-z]*[a-z]|[A-Z][a-z]*")
                .matcher(input);
        StringJoiner joiner = new StringJoiner("_");
        while (matcher.find()) {
            joiner.add(matcher.group().toUpperCase(Locale.ROOT));
        }
        return joiner.toString();
    }

    public String convertToCamelCase(String input) {
        Matcher matcher = Pattern.compile("_[a-z]").matcher(input.toLowerCase(Locale.ROOT));
        String i = input.toLowerCase(Locale.ROOT);
        while (matcher.find()) {
            i = i.replaceAll(matcher.group(), matcher.group().substring(1).toUpperCase(Locale.ROOT));
        }
        return i;
    }
}
