package com.example.schoolNeedsOrganizer.SchoolNeeds;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeMap;

public class HomeWork extends Need implements Comparable<HomeWork> {
    private static TreeMap<String,HomeWork> needTreeSet;
    public static int currentWork = 0;
    public static TreeMap<String, HomeWork> getHWTreeSet() {
        return needTreeSet;
    }

    private static TreeMap<String, HomeWork> forTomorrow = new TreeMap<>();

    public static void setHWTreeSet(HomeWork test, String s) {
        needTreeSet.put(s,test);
    }

    public static void maidTreeMap(){
        needTreeSet = new TreeMap<>();
    }

    public static int getCurrentWork(){
        return currentWork;
    }

    public static void delete(String name){
        needTreeSet.remove(name);
        forTomorrow.remove(name);
    }

    public static int addToCurrentWork(int value){
        if((currentWork + value) < 0){
            currentWork = forTomorrow.size()-1;
        } else if((currentWork + value) == forTomorrow.size()){
            currentWork = 0;
        } else {
            currentWork += value;
        }
        return currentWork;
    }

    public static void setCurrentWork(int value){
        currentWork = value;
    }

    public static void addHomeWorkForTomorrow(){
        ArrayList<HomeWork> needList = new ArrayList<>();
        HomeWork.getHWTreeSet().forEach((t, t1) -> needList.add(t1));
        needList.stream().filter(t ->{
            Calendar date = t.getImplementationDate();
            Calendar currentDate = Calendar.getInstance();
            currentDate.add(Calendar.DAY_OF_YEAR, 1);
            return (date.get(Calendar.YEAR) == currentDate.get(Calendar.YEAR) &&
                    date.get(Calendar.MONTH) == currentDate.get(Calendar.MONTH) &&
                    date.get(Calendar.DAY_OF_MONTH) == currentDate.get(Calendar.DAY_OF_MONTH));
        }).forEach(t -> {
            forTomorrow.put(t.getNeedName(), t);
        });
    }

    public static TreeMap<String, HomeWork> getForTomorrow() {
        return forTomorrow;
    }

    public static void delete(HomeWork homeWork){
        if(needTreeSet.containsKey(homeWork.needName)){
            needTreeSet.remove(homeWork.needName);
        }
        if(forTomorrow.containsKey(homeWork.needName)){
            forTomorrow.remove(homeWork.needName);
        }
    }

    public static void delete(Need homeWork){
        if(needTreeSet.containsKey(homeWork.needName)){
            needTreeSet.remove(homeWork.needName);
        }
        if(forTomorrow.containsKey(homeWork.needName)){
            forTomorrow.remove(homeWork.needName);
        }
    }


    @Override
    public int compareTo(HomeWork o) {
        return 0;
    }
}
