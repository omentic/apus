# apus

## a local web browser

apus (from _Gigantochloa apus_: the most common bamboo on the island of Java) is a local web browser.
It will parse and display HTML, Markdown, epub, and potentially other XML-based file formats.
It will not connect to the internet, and instead only parse local files given to it on the command line or eventually uploaded in a user interface.
Hopefully nobody outside of this class will use it because it will be a standards-failing web browser written in Java Swing.

This project is of interest to me because I enjoy writing parsers and have been meaning to play around with text and image rendering.
I've heard that Java Swing has a native HTML rendering component. I hope to entirely disregard this, and instead reimplement it poorly.

## user stories

- As a user, I want to be able to construct a structural representation of an HTML file.
- As a user, I want to be able to construct a structural representation of a CSS file.
- As a user, I want to be able to add multiple tabs to a list of open tabs.
- As a user, I want to be able to view a rendering of an arbitrary HTML file.

- As a user, I want to be given the option to save my currently open tabs to disk when quitting.
- As a user, I want to be given the option to restore my previous tabs upon relaunching the application.

## instructions for grader

- You can generate the first required event of adding multiple Xs to a Y by creating a new tab by entering a path into the browser bar and pressing "Go". Observe that the tab is added to the tablist.
- You can generate the second required event of adding multiple Xs to a Y by creating a new tab by opening the tab menu, selecting a tab, and closing it. Observe that the tab is removed from the tablist.
- The panel in which all of the Xs added to a Y are displayed is the tablist.
- You can locate my visual component by observing the main browser window, which may render arbitrary paths and open tabs.
- You can save the state of my application by attempting to close it with tabs open. You will be asked if you would like to save your tabs.
- You can load the state of my application by attempting to open it after saving tabs. You will be asked if you would like to restore your tabs.

## Phase IV: Task 2

```
Fri Dec 02 20:53:00 PST 2022
Added tab /home/apropos/Projects/website/j-james/index.html to tablist
Fri Dec 02 20:53:01 PST 2022
Added tab /home/apropos/Projects/website/j-james/index.html to tablist
Fri Dec 02 20:53:07 PST 2022
Removed tab /home/apropos/Projects/website/j-james/index.html from tablist
```

## Phase IV: Task 3

I think that the design structure shown by the UML diagram is overall pretty much fine, especially for a student project.
There are definitely some problems with it / things I'd do differently, however:
- The BrowserBar and BrowserCanvas have a mutual dependency with BrowserWindow: this doesn't make too much sense and was done because I needed to access BrowserWindow methods from within them. If I did it again, I would either nest the classes or use the Singleton design principle.
- The CssParser is not connected to anything else, as it went unused. I would like to use that class in the future - but I never fully finished implementing the HTML layout, so it didn't make sense to apply CSS. (The JsonUtils class is also unconnected, but that's just because it consists of only static methods.)

## credits

This project makes extensive use of the Javatuples library ([javatuples.org](https://www.javatuples.org/)).
Many thanks to the author, Daniel Fernández.
