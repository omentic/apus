package model;

import model.util.Event;
import model.util.EventLog;

import java.util.ArrayDeque;

public class BrowserState {
    private ArrayDeque<String> tabs;
    private String currentTab;

    public BrowserState(ArrayDeque<String> tabs, String currentTab) {
        this.tabs = tabs;
        this.currentTab = currentTab;
    }

    public ArrayDeque<String> getTabs() {
        return this.tabs;
    }

    public String getCurrentTab() {
        return this.currentTab;
    }

    public void setCurrentTab(String tab) {
        this.currentTab = tab;
    }

    public void addTab(String added) {
        if (!this.tabs.contains(added)) {
            this.tabs.add(added);
        }
        EventLog.getInstance().logEvent(new Event("Added tab " + added + " to tablist"));
    }

    public void removeTab(String removed) {
        this.tabs.remove(removed);
        EventLog.getInstance().logEvent(new Event("Removed tab " + removed + " from tablist"));
    }
}
