# What

This is the worst database ever

You can pass the file name for the database from the command line, otherwise it will just create a file called `thing.db`.

The `Db` class is responsible for reading and writting. It has several methods:

 * `open` - opens an existing database file
 * `get(key)` - gets the value for the given key
 * `set(key, value)` - sets the value for the given key

The `App` class has an example of how to use it. It creates a `Db` object, writes three values(two strings and a date) and displays the contents. Then it re-reads the data from file and again shows its content and finally it displays the result for one of the keys. 

# How to build it

Clone repo

## IDE 

 * Open project in your favourite IDE
 * Click build
 * Click run

## Command-line 

```
javac *.java
java App
```

# Dependencies

 * JDK