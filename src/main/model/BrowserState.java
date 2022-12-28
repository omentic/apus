package model;

import java.util.ArrayDeque;

// This BrowserState class collects the stateful portions of the browser into one modelable class.
public class BrowserState {
    private final ArrayDeque<String> tabs;
    public String currentTab;

    public BrowserState(ArrayDeque<String> tabs, String currentTab) {
        this.tabs = tabs;
        this.currentTab = currentTab;
    }

    public ArrayDeque<String> getTabs() {
        return this.tabs;
    }

    public void addTab(String added) {
        if (!this.tabs.contains(added)) {
            this.tabs.add(added);
        }
    }

    public void removeTab(String removed) {
        this.tabs.remove(removed);
    }
}
