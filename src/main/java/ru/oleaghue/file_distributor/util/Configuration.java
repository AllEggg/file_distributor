package ru.oleaghue.file_distributor.util;

import java.util.Map;

public class Configuration {
    Map<String, String> settingsMap;
    String baseDir;
    String newDir;
    String sleepTimeParam;

    public Configuration(Map<String, String> settingsMap) {
        this.settingsMap = settingsMap;
        this.baseDir = settingsMap.get("BaseDirectory");
        this.newDir = settingsMap.get("DirectoryToDistribute");
        this.sleepTimeParam = settingsMap.get("SleepTime");
    }

    public Map<String, String> getSettingsMap() {
        return settingsMap;
    }

    public void setSettingsMap(Map<String, String> settingsMap) {
        this.settingsMap = settingsMap;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getNewDir() {
        return newDir;
    }

    public void setNewDir(String newDir) {
        this.newDir = newDir;
    }

    public String getSleepTimeParam() {
        return sleepTimeParam;
    }

    public void setSleepTimeParam(String sleepTimeParam) {
        this.sleepTimeParam = sleepTimeParam;
    }
}
