# apus: a miniature web browser

apus (from _Gigantochloa apus_: the most common [bamboo](https://github.com/j-james/bamboo) species on the island of Java) is a basic web browser.
It was written for a certain introductory computer science class at the University of British Columbia.

This is a basic web browser implemented purely in Java and Java Swing (implementations of HTML and HTTP are from-scratch).
It can fetch, render, and display HTML webpages locally or from the internet, and apply standard layout rules and basic styling.
In the future, I hope to extend apus to handle a number of different formats and protocols, such as Markdown, EPUB, Gemini, and implement a larger and larger subset of proper HTTP/HTML/CSS support.

This project was of interest to me because I enjoy writing parsers and had been meaning to play around with text and image rendering.
It certainly could have been thrown together with an HTTP library and Swing's native HTML rendering component, but I enjoyed entirely disregarding them and instead reimplementing them poorly. 
Scope creep? What's that?

Hopefully nobody outside of the class this was written for will use it, because it is a standards-failing web browser written in Java Swing.

## code design

The HTML parsing is from-scratch and implemented as an LL(1) parser - so theoretically should be quite efficient even on large documents. It does not check that closing tags correspond to their opening tags, instead keeping track of a global nesting level, and so is not terribly robust against "wild" HTML.

This codebase is probably not very helpful to anyone in a certain CPSC course, as it was ported to Java 19 and violently eschews design patterns. It makes great use of public fields, record types, Optionals, and all those things that are better done in literally any other language.
It also uses Java 12's switch _expression_ and Java 17's expansion of this into _pattern matching_, which now provide alternatives to the massive footgun that was switch-statement-fallthrough and the just pretty gross instanceof-then-cast pattern.

The codebase was designed to be fault-tolerant and should not throw exceptions. If you manage to trigger one, please let me know.

## credits

This project makes use of the Javatuples library ([javatuples.org](https://www.javatuples.org/)).
Many thanks to the author, Daniel Fernández.
