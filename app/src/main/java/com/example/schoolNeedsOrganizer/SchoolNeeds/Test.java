package com.example.schoolNeedsOrganizer.SchoolNeeds;

import java.util.TreeMap;

public class Test extends Need {
    private final boolean IS_IMPORTANT = true;
    private static TreeMap<String,Test> needTreeSet;
    private static int currentTest = 0;

    public boolean isIS_IMPORTANT() {
        return IS_IMPORTANT;
    }

    public static TreeMap<String, Test> getTestTreeSet() {
        return needTreeSet;
    }

    public static void setTestTreeSet(Test test, String s) {
        needTreeSet.put(s,test);
    }

    public static void maidTreeMap(){
        needTreeSet = new TreeMap<>();
    }

    public static void delete(String name){
        needTreeSet.remove(name);
    }

    public static int getCurrentTest(){
        return currentTest;
    }

    public static int addToCurrentTest(int value){
        if((currentTest + value) < 0){
            currentTest = needTreeSet.size()-1;
        } else if((currentTest + value) == needTreeSet.size()){
            currentTest = 0;
        } else {
            currentTest += value;
        }
        return currentTest;
    }

    public static void setCurrentTest(int value){
        currentTest = value;
    }
}
