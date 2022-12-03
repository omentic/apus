package model;

import model.util.Event;
import model.util.EventLog;

import java.util.ArrayDeque;

// This BrowserState function collects the stateful portions of the browser into one modelable class.
public class BrowserState {
    private ArrayDeque<String> tabs;
    private String currentTab;

    // EFFECTS: constructs a new BrowserState
    // MODIFIES: this
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

    // MODIFIES: this
    // EFFECTS: Sets the current tab
    public void setCurrentTab(String tab) {
        this.currentTab = tab;
    }

    // MODIFIES: this
    // EFFECTS: add a new tab
    public void addTab(String added) {
        if (!this.tabs.contains(added)) {
            this.tabs.add(added);
        }
        EventLog.getInstance().logEvent(new Event("Added tab " + added + " to tablist"));
    }

    // MODIFIES: this
    // EFFECTS: removes a tab from the tablist
    public void removeTab(String removed) {
        this.tabs.remove(removed);
        EventLog.getInstance().logEvent(new Event("Removed tab " + removed + " from tablist"));
    }
}
