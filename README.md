"Degrees of Separation" Calculator for Wikipedia Pages
===

This small project defines determines the shortest path from one wikipedia page
to another and then prints it out. A "path" is defined as a series of link 
clicks that take you from page to page, beginning at a page with a given title
and ending at a page with another given title.

Note that this branch of the Degree Checker uses a static variable to hold
the target of the search. This may afford greater performance than the master
branch, which uses a non-static factory to specify the target.
My own very cursory tests suggest that non-static overhead results in about a 4%
decrease in performance, though the margin of error is very large.

The implementation is currently non-deterministic. Pages are stored in HashSets
and the WikiPage class doesn't have a defined hashCode() function. This means 
that, while the path length will be constant, the specific path given may vary
from run to run.

The project was inspired by the wikipedia based game "Six Degrees of Adolf
Hitler", which was itself inspired by the parlor game "Six Degrees of Kevin
Bacon". I hope that you have as much fun with it as I did.
