package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class BrowserBar extends JToolBar {
    private final BrowserWindow parent;

    private final JPopupMenu tabMenu;
    private final JToggleButton tabButton;
    private final JTextField uriInput;
//    private final JButton saveTabsButton;
    private final JButton openUriButton;

    public BrowserBar(BrowserWindow parent) {
        this.parent = parent;

        tabMenu = new JPopupMenu("Tabs");

        tabButton = new JToggleButton("Tabs");
        tabButton.addActionListener(toggleTabMenu());
        add(tabButton);

        uriInput = new JTextField();
        add(uriInput);

        openUriButton = new JButton("Go");
        openUriButton.addActionListener(openTab());
        add(openUriButton);

    }

    private ActionListener openTab() {
        return actionEvent -> {
            String uri = uriInput.getText();
            parent.render(uri);
            addTab(uri);
        };
    }

    public void addTab(String tab) {
        JToggleButton tabButton = new JToggleButton(tab);

        tabButton.addActionListener(actionEvent -> {
            int action = JOptionPane.showOptionDialog(null,
                    "Open or close this tab?", "apus",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, new String[]{"Open", "Close"}, "Open");
            if (action == 0) {
                parent.render(tab);
            } else {
                tabMenu.remove(tabButton);
                tabMenu.setVisible(false);
                parent.getBrowserState().removeTab(tab);
            }
        });

        this.tabMenu.add(tabButton);
        parent.getBrowserState().addTab(tab);
    }

    private ActionListener toggleTabMenu() {
        return actionEvent -> {
            if (tabButton.isSelected()) {
                Point location = tabButton.getLocationOnScreen();
                location.translate(0, 30); // fuck this method lol
                tabMenu.setLocation(location);
                tabMenu.setVisible(true);
            } else {
                tabMenu.setVisible(false);
            }
        };
    }
}
