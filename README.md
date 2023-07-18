# taco-cloud-modified

Example code from ["Spring in Action"](https://www.manning.com/books/spring-in-action-sixth-edition) by Craig Walls, 6th Edition (Manning 2022), modified to my liking.

This code grows & changes as I go through the book. I will add pages with additional notes when warranted. 

Note that this project uses Gradle as build tool, with the Kotlin DSL, instead of Maven as the book does. I'm using the Intellij IDEA IDE, which may have have various effects on the project structure.

   - 2023-07-16: Finished Chapter 2. Code has been given various changes, including the possibility to have the application propose a Taco, and been organized in a way that makes the most sense, at least t to me. In Chapter 3 ("Working with data"), we will try to connect to an in-memory H2 SQL database.
   - 2023-07-17: Code prepared so that the _relation_ for "Ingredients" can alternatively be based on hardcoded initialization be or read from a database with Spring JDBC. This is basically a question of organization - what is an interface, what isn't. Assuming the "Ingredients" relation is based on database contents, when should it be updated. Evidently at application start, but therafter? In fact, never. Unless one wants to increase the number of ingredients over time. Removing ingredients opens a can of worms, one does not want to have ingredients disappear while a taco-compositing session is underway. Removing ingredients can only be based on a garbage-collection scheme whereby you cannot remove on if there is any taco that uses it.
   - 2023-07-18: Trying to get some basic H2 functionality to work. Better read the manuals. 
