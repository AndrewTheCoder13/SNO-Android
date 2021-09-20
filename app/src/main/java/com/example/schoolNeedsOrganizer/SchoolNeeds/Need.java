package com.example.schoolNeedsOrganizer.SchoolNeeds;

import java.util.Calendar;
import java.util.TreeMap;

public abstract class Need{
    protected String needName;
    protected String needDescription;
    protected Calendar implementationDate;
    protected boolean isImportant;

    public boolean isEnded() {
        return isEnded;
    }

    public void setEnded(boolean ended) {
        isEnded = ended;
    }

    protected boolean isEnded;
    protected static TreeMap<String,Need> needTreeSet;

    protected String status;

    public Need() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNeedName() {
        return needName;
    }

    public void setNeedName(String needName) {
        this.needName = needName;
    }

    public String getNeedDescription() {
        return needDescription;
    }

    public void setNeedDescription(String needDescription) {
        this.needDescription = needDescription;
    }

    public Calendar getImplementationDate() {
        return implementationDate;
    }

    public void setImplementationDate(Calendar implementationDate) {
        this.implementationDate = implementationDate;
    }

    public boolean isImportant() {
        return isImportant;
    }

    public void setImportant(boolean important) {
        isImportant = important;
    }

    public static void setNeedTreeSet(Need need, String name) {
        Need.needTreeSet.put(name, need);
    }

    public static TreeMap<String,Need> getNeedTreeSet() {
        return needTreeSet;
    }



}
