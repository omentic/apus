package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class BrowserBar extends JToolBar {
    private BrowserWindow parent;

    private JPopupMenu tabMenu;
    private JToggleButton tabButton;
    private JTextField uriInput;
//    private JButton saveTabsButton;
    private JButton openUriButton;

    public BrowserBar(BrowserWindow parent) {
        this.parent = parent;

        var test = new JMenuItem("Ipsum");
        tabMenu = new JPopupMenu("Tabs");
        tabMenu.add(new JMenuItem("Hello"));
        tabMenu.add(new JMenuItem("World"));
        tabMenu.add(new JMenuItem("Lorem"));
        tabMenu.add(test);
        tabMenu.remove(test);

        tabButton = new JToggleButton("Tabs");
        tabButton.addActionListener(toggleTabMenu());
        add(tabButton);

        uriInput = new JTextField();
        add(uriInput);

        openUriButton = new JButton("Go");
        openUriButton.addActionListener(openTab());
        add(openUriButton);

    }

    // EFFECTS: opens the content of the text field in the current tab
    private ActionListener openTab() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String uri = uriInput.getText();
                parent.render(uri);
                addTab(uri);
                System.out.println(uri);
                System.out.println("should run");
            }
        };
    }

    // EFFECTS: adds a new tab pointing to URI in the background
    public void addTab(String tab) {
        JToggleButton tabButton = new JToggleButton(tab);

        tabButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                int action = JOptionPane.showOptionDialog(null,
                        "Open or close this tab?", "apus",
                        JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                        null, new String[]{"Open", "Close"}, "Open");
                if (action == 0) {
                    parent.render(tab);
                } else {
                    tabMenu.remove(tabButton);
                    tabMenu.setVisible(false);
                    parent.removeTab(tab);
                }
            }
        });

        this.tabMenu.add(tabButton);
        parent.addTab(tab);
    }

    // MODIFIES: this
    // EFFECTS: toggles the tab menu
    private ActionListener toggleTabMenu() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (tabButton.isSelected()) {
                    Point location = tabButton.getLocationOnScreen();
                    location.translate(0, 30); // fuck this method lol
                    tabMenu.setLocation(location);
                    tabMenu.setVisible(true);
                } else {
                    tabMenu.setVisible(false);
                }
            }
        };
    }
}
