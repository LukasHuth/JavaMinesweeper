# Minesweeper
## Java implementation of the classic game Minesweeper in Java Swing with Java 17
## How to run
### Clone the repository
```bash
git clone git@github.com:LukasHuth/JavaMinesweeper.git
```
### Compile the project
```bash
cd JavaMinesweeper
javac -d out src/*.java
```
### Run the project
```bash
java -cp out Minesweeper
```
## How to play
- Left click to reveal a field
- Right click to flag a field
- Click a revealed field, that has all mines uncoveded around it, to reveal all fields around a field
- To restart the game, click the text "You won!" or "You lost!" at the top of the window    