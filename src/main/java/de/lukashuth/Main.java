package de.lukashuth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Stream;

public class Main {
    // private int ROWS;
    // static int COLUMNS = 15;
    static int MINES = 20;
    private static int found_mines = 0;
    static boolean game_over = false;
    private static Thread timeThread;
    private static Field[][] grid;
    public static void main(String[] args) {
        int size = 12;
        if(args.length > 0) {
            if(!args[0].matches("\\d+")) {
                System.out.println("Size must be a number");
                System.exit(1);
            }
            if(Integer.parseInt(args[0]) < 5 || Integer.parseInt(args[0]) > 20) {
                System.out.println("Size must be at least 5 and at most 20");
                System.exit(2);
            }
            size = Integer.parseInt(args[0]);
        }
        MINES = size + size/2;
        grid = new Field[size][size];
        MainWindow mainWindow = new MainWindow(size);
        JFrame frame = new JFrame("Minesweeper");
        frame.setResizable(false);
        setupFrame(frame, mainWindow);
    }
    private static void setupFrame(JFrame frame, MainWindow mainWindow) {
        timeThread = new TimeThread(mainWindow);
        JPanel container = mainWindow.container;
        mainWindow.getRemaining_label.get().setText((MINES-found_mines)+"");
        initializeGrid(mainWindow);
        timeThread.start();
        frame.setContentPane(container);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // mainWindow.getTime_label.get().setText("00:00");
        frame.pack();
        frame.setVisible(true);
    }
    private static void resetGrid(int size) {
        fillGrid(size);
        shuffleGrid(size);
        generateNumbersForGrid();
        // printGrid();
    }
    private static void fillGrid(int size) {
        Field.MINES = 0;
        for(int x = 0; x < size; x++) {
            for(int y = 0; y < size; y++) {
                Field f = new Field(x, y);
                grid[x][y] = f;
            }
        }
    }
    private static void printGrid() {
        for(final Field[] row : grid) {
            for(final Field f : row) {
                System.out.print(f.isMine() ? "X" : f.getNumber());
            }
            System.out.println();
        }
    }
    private static void shuffleGrid(int size) {
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        for(int i = 0; i < 1000; i++) {
            final int x_1 = r.nextInt(size), y_1 = r.nextInt(size);
            final int x_2 = r.nextInt(size), y_2 = r.nextInt(size);
            final Field f_1 = grid[x_1][y_1];
            final Field f_2 = grid[x_2][y_2];
            f_1.setPosition(x_2, y_2);
            f_2.setPosition(x_1, y_1);
            grid[x_1][y_1] = f_2;
            grid[x_2][y_2] = f_1;
        }
    }
    private static void generateNumbersForGrid() {
        for(Field f : Stream.of(grid).flatMap(Stream::of).filter(Field::isMine).toList()) {
            for(Field neighbor : getNeighbors(grid.length,f).stream().filter(Field::isNotMine).toList()) {
                neighbor.setNumber(neighbor.getNumber()+1);
            }
        }
    }
    private static void initializeGrid(MainWindow mainWindow) {
        resetGrid(mainWindow.getRows().size());
        insertStatusClickHandler(mainWindow);
        insertFields(mainWindow);
    }
    private static void resetFields(MainWindow mainWindow) {
        for(final JPanel row : mainWindow.getRows()) {
            for(int i = 0; i < mainWindow.getRows().size(); i++) {
                final JPanel panel = (JPanel) row.getComponents()[i];
                panel.setBackground(Color.WHITE);
                ((JLabel)panel.getComponents()[0]).setText(" ");
            }
        }
    }

    private static void insertFields(MainWindow mainWindow) {
        int row_count = 0;
        for(final JPanel row : mainWindow.getRows()) {
            row.setLayout(new GridLayout(1, mainWindow.getRows().size()));
            for(int i = 0; i < mainWindow.getRows().size(); i++) {
                final JPanel panel = new JPanel();
                panel.add(new JLabel(" "));
                final int x = row_count;
                final int y = i;
                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        if(game_over) return;
                        Field f = grid[x][y];
                        final JPanel panel = (JPanel) e.getSource();
                        handleMouseClickOnPanel(e, f, panel, mainWindow, x, y);
                    }
                });
                panel.setPreferredSize(new Dimension(50, 50));
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                row.add(panel);
            }
            row_count++;
        }
    }

    private static void handleMouseClickOnPanel(MouseEvent e, Field f, JPanel panel, MainWindow mainWindow, int x, int y) {
        if(e.getButton() == MouseEvent.BUTTON3) {
            handleRightClick(f, panel, mainWindow);
            return;
        }
        if(f.isFlagged()) return;
        if(f.isMine()) {
            handleClickOnMine(mainWindow);
            return;
        } else if(!f.isOpened()) {
            handleNotOpenedField(panel, mainWindow, x, y);
        } else {
            handleOpenedField(f, mainWindow);
        }
        winIfAllAreFilled(mainWindow);
    }

    private static void handleOpenedField(Field f, MainWindow mainWindow) {
        ArrayList<Field> neighbors = getNeighbors(mainWindow.getRows().size(), f);
        if(neighbors.stream().filter(Field::isFlagged).count() == f.getNumber()) {
            for(Field neighbor : neighbors) {
                if(!neighbor.isOpened() && !neighbor.isFlagged()) {
                    neighbor.open();
                    if(neighbor.isMine()) {
                        mainWindow.getStatus_label.get().setText("Game Over");
                        game_over = true;
                    }
                    if(neighbor.getNumber() == 0)
                        colorNeighboringZeros(neighbor.getPositionX(), neighbor.getPositionY(), mainWindow);
                    else
                        colorField(neighbor, mainWindow);
                }
            }
        }
    }

    private static void handleNotOpenedField(JPanel panel, MainWindow mainWindow, int x, int y) {
        grid[x][y].open();
        // System.out.println(grid[x][y].getNumber());
        if(grid[x][y].getNumber() == 0) {
            panel.setBackground(Color.LIGHT_GRAY);
            colorNeighboringZeros(x, y, mainWindow);
        } else {
            ((JLabel) panel.getComponents()[0]).setText(grid[x][y].getNumber()+"");
            panel.setBackground(Color.GRAY);
        }
    }

    private static void handleClickOnMine(MainWindow mainWindow) {
        mainWindow.getStatus_label.get().setText("Game Over");
        game_over = true;
        System.out.println();
        for(Field f1 : Stream.of(grid).flatMap(Stream::of).filter(Field::isMine).toList()) {
            mainWindow.getRows().get(f1.getPositionX()).getComponents()[f1.getPositionY()].setBackground(Color.RED);
        }
    }

    private static void handleRightClick(Field f, JPanel panel, MainWindow mainWindow) {
        if(f.isFlagged()) {
            f.unflag();
            found_mines--;
            ((JLabel) panel.getComponents()[0]).setText(" ");
            panel.setBackground(Color.WHITE);
        } else if(!f.isOpened()) {
            ((JLabel) panel.getComponents()[0]).setText("F");
            panel.setBackground(Color.RED);
            f.flag();
            found_mines++;
        }
        mainWindow.getRemaining_label.get().setText((MINES-found_mines)+"");
        winIfAllAreFilled(mainWindow);
    }

    private static boolean winIfAllAreFilled(MainWindow mainWindow) {
        boolean all_revealed = true;
        if(found_mines == MINES && !game_over) {
            for(Field[] row : grid) {
                for(Field f1 : row) {
                    if(!f1.isOpened() && !f1.isMine()) {
                        all_revealed = false;
                        break;
                    }
                }
            }
            if(all_revealed) {
                mainWindow.getStatus_label.get().setText("You Won");
                game_over = true;
            }
        }
        return !all_revealed;
    }

    private static void insertStatusClickHandler(MainWindow mainWindow) {
        mainWindow.getStatus_label.get().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                game_over = false;
                mainWindow.getStatus_label.get().setText("Playing");
                mainWindow.getRemaining_label.get().setText(MINES+"");
                found_mines = 0;
                mainWindow.startTime = 0;
                timeThread = new TimeThread(mainWindow);
                timeThread.start();
                resetGrid(mainWindow.getRows().size());
                resetFields(mainWindow);
            }
        });
    }

    private static void colorNeighboringZeros(int x, int y, MainWindow mainWindow) {
        final ArrayList<Field> visited = new ArrayList<>();
        visited.add(grid[x][y]);
        getNeighborsWithZero(mainWindow.getRows().size(), grid[x][y], visited);
        for(Field neighbor : visited) {
            neighbor.open();
            colorField(neighbor, mainWindow);
        }
    }

    private static void colorField(Field neighbor, MainWindow mainWindow) {
        if(neighbor.isMine())
            mainWindow.getRows().get(neighbor.getPositionX()).getComponents()[neighbor.getPositionY()].setBackground(Color.RED);
        else if(neighbor.getNumber() == 0)
            mainWindow.getRows().get(neighbor.getPositionX()).getComponents()[neighbor.getPositionY()].setBackground(Color.LIGHT_GRAY);
        else {
            mainWindow.getRows().get(neighbor.getPositionX()).getComponents()[neighbor.getPositionY()].setBackground(Color.GRAY);
            JPanel neighbor_panel = (JPanel) mainWindow.getRows().get(neighbor.getPositionX()).getComponents()[neighbor.getPositionY()];
            JLabel neighbor_label = (JLabel)neighbor_panel.getComponents()[0];
            neighbor_label.setText(grid[neighbor.getPositionX()][neighbor.getPositionY()].getNumber()+"");
        }
    }

    private static void getNeighborsWithZero(int size, Field f, ArrayList<Field> visited) {
        for(Field neighbor : getNeighbors(size, f)) {
            if(!visited.contains(neighbor)) {
                visited.add(neighbor);
                if(neighbor.isZero()) getNeighborsWithZero(size, neighbor, visited);
            }
        }
    }
    private static ArrayList<Field> getNeighbors(int size, Field f) {
        ArrayList<Field> neighbors = new ArrayList<>();
        final int x = f.getPositionX();
        final int y = f.getPositionY();
        if(x > 0) {
            neighbors.add(grid[x-1][y]);
            if(y > 0) {
                neighbors.add(grid[x-1][y-1]);
            }
            if(y < size-1) {
                neighbors.add(grid[x-1][y+1]);
            }
        }
        if(y > 0) {
            neighbors.add(grid[x][y-1]);
            if(x < size-1) {
                neighbors.add(grid[x+1][y-1]);
            }
        }
        if(x < size-1) {
            neighbors.add(grid[x+1][y]);
            if(y < size-1) {
                neighbors.add(grid[x+1][y+1]);
            }
        }
        if(y < size-1) {
            neighbors.add(grid[x][y+1]);
        }
        return neighbors;
    }
}
