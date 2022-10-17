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
- As a user, I want to be able to add multiple Nodes to an HTML file.
- As a user, I want to be able to open and render an arbitrary HTML file.
