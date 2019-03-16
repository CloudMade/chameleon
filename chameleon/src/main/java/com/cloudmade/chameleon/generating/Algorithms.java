package com.cloudmade.chameleon.generating;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Algorithms {

    static List<List<String>> permute(List<String> stringsToPermute) {
        List<List<String>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        for (String string : stringsToPermute) {
            List<List<String>> current = new ArrayList<>();
            for (List<String> l : result) {
                for (int j = 0; j < l.size() + 1; j++) {
                    l.add(j, string);
                    List<String> temp = new ArrayList<>(l);
                    current.add(temp);
                    l.remove(j);
                }
            }
            result = new ArrayList<>(current);
        }
        return result;
    }

    static String joinString(List<String> strings) {
        StringBuilder sb = new StringBuilder();
        for (String string : strings) {
            sb.append(string);
        }
        return sb.toString();
    }

    static List<List<String>> powerSet(List<String> list) {
        List<List<String>> ps = new ArrayList<>();
        ps.add(new ArrayList<>());
        for (String item : list) {
            List<List<String>> newPs = new ArrayList<>();
            for (List<String> subset : ps) {
                newPs.add(subset);
                List<String> newSubset = new ArrayList<>(subset);
                newSubset.add(item);
                newPs.add(newSubset);
            }
            ps = newPs;
        }
        return ps;
    }

    static String capitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    static String joinCollectionToStringWrappedInQuotes(List<String> stringToJoin) {
        StringBuilder sb = new StringBuilder();
        Iterator<String> iterator = stringToJoin.iterator();
        while (iterator.hasNext()) {
            sb.append(String.format("\"%s\"", iterator.next()));
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
