package de.lukashuth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    private static final int ROWS = 12;
    private static final int COLUMNS = 12;
    public static final int MINES = 20;
    private static int found_mines = 0;
    private static final Field[][] grid = new Field[ROWS][COLUMNS];
    public static void main(String[] args) {
        JFrame frame = new JFrame("Sudoku");
        // frame.setResizable(false);
        MainWindow mainWindow = new MainWindow();
        setupFrame(frame, mainWindow);
    }
    private static void setupFrame(JFrame frame, MainWindow mainWindow) {
        TimeThread timeThread = new TimeThread(mainWindow);
        JPanel container = mainWindow.container;
        initializeGrid(mainWindow);
        frame.setContentPane(container);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.getTime_label.get().setText("00:00");
        frame.pack();
        frame.setVisible(true);
        timeThread.run();
    }
    private static void resetGrid() {
        Field.MINES = 0;
        for(int x = 0; x < ROWS; x++) {
            for(int y = 0; y < COLUMNS; y++) {
                Field f = new Field(x, y);
                grid[x][y] = f;
            }
        }
    }
    private static void printGrid() {
        for(Field[] row : grid) {
            for(Field f : row) {
                System.out.print(f.isMine() ? "X" : f.getNumber());
            }
            System.out.println();
        }
    }
    private static void shuffleGrid() {
        Random r = new Random();
        r.setSeed(System.currentTimeMillis());
        for(int i = 0; i < 1000; i++) {
            int x_1 = r.nextInt(ROWS), y_1 = r.nextInt(COLUMNS);
            int x_2 = r.nextInt(ROWS), y_2 = r.nextInt(COLUMNS);
            Field f_1 = grid[x_1][y_1];
            Field f_2 = grid[x_2][y_2];
            f_1.setPosition(x_2, y_2);
            f_2.setPosition(x_1, y_1);
            grid[x_1][y_1] = f_2;
            grid[x_2][y_2] = f_1;
        }
    }
    private static void generateNumbersForGrid() {
        for(Field[] row : grid) {
            for(Field f : row) {
                if(f.isMine()) {
                    ArrayList<Field> neighbors = getNeighbors(f);
                    for(Field neighbor : neighbors) {
                        neighbor.setNumber(neighbor.getNumber()+1);
                    }
                }
            }
        }
    }
    private static void initializeGrid(MainWindow mainWindow) {
        resetGrid();
        shuffleGrid();
        generateNumbersForGrid();
        printGrid();
        int row_count = 0;
        for(JPanel row : mainWindow.getRows()) {
            row.setLayout(new GridLayout(1, ROWS));
            for(int i = 0; i < ROWS; i++) {
                JPanel panel = new JPanel();
                panel.add(new JLabel(" "));
                final int x = row_count;
                final int y = i;
                panel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        super.mouseClicked(e);
                        Field f = grid[x][y];
                        // System.out.println("x: "+x+" y: "+y + "f_x: "+f.getPositionX()+" f_y: "+f.getPositionY()+" isMine: "+f.isMine()+" isOpened: "+f.isOpened()+" isFlagged: "+f.isFlagged()+" number: "+f.getNumber()+" state: "+f.getState()+"\n");
                        JPanel panel = (JPanel) e.getSource();
                        if(e.getButton() == MouseEvent.BUTTON3) {
                            if(f.isFlagged()) {
                                f.unflag();
                                found_mines--;
                                ((JLabel)panel.getComponents()[0]).setText(" ");
                                panel.setBackground(Color.WHITE);
                            } else if(!f.isOpened()) {
                                ((JLabel)panel.getComponents()[0]).setText("F");
                                panel.setBackground(Color.RED);
                                f.flag();
                                found_mines++;
                                if(found_mines == MINES) {
                                    System.out.println("You won!");
                                }
                            }
                            mainWindow.getRemaining_label.get().setText((MINES-found_mines)+"");
                            return;
                        }
                        if(f.isMine()) {
                            System.out.println("Game Over");
                            for(Field[] row : grid) {
                                for(Field f1 : row) {
                                    if(f1.isMine()) {
                                        mainWindow.getRows()[f1.getPositionX()].getComponents()[f1.getPositionY()].setBackground(Color.RED);
                                    }
                                }
                            }
                        } else if(!f.isOpened()) {
                            grid[x][y].open();
                            // System.out.println(grid[x][y].getNumber());
                            if(grid[x][y].getNumber() == 0) {
                                panel.setBackground(Color.LIGHT_GRAY);
                                ArrayList<Field> visited = new ArrayList<>();
                                visited.add(grid[x][y]);
                                getNeighborsWithZero(grid[x][y], visited);
                                for(Field neighbor : visited) {
                                    neighbor.open();
                                    if(neighbor.getNumber() == 0)
                                        mainWindow.getRows()[neighbor.getPositionX()].getComponents()[neighbor.getPositionY()].setBackground(Color.LIGHT_GRAY);
                                    else {
                                        mainWindow.getRows()[neighbor.getPositionX()].getComponents()[neighbor.getPositionY()].setBackground(Color.GRAY);
                                        JPanel neighbor_panel = (JPanel)mainWindow.getRows()[neighbor.getPositionX()].getComponents()[neighbor.getPositionY()];
                                        JLabel neighbor_label = (JLabel)neighbor_panel.getComponents()[0];
                                        neighbor_label.setText(grid[neighbor.getPositionX()][neighbor.getPositionY()].getNumber()+"");
                                    }
                                }
                            } else {
                                ((JLabel)panel.getComponents()[0]).setText(grid[x][y].getNumber()+"");
                                panel.setBackground(Color.GRAY);
                            }
                        } else {
                            // TODO: Open neighbors if all mines are flagged
                        }
                    }
                });
                panel.setPreferredSize(new Dimension(50, 50));
                panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                row.add(panel);
            }
            row_count++;
        }
    }
    private static void getNeighborsWithZero(Field f, ArrayList<Field> visited) {
        ArrayList<Field> neighbors = getNeighbors(f);
        for(Field neighbor : neighbors) {
            if(!visited.contains(neighbor)) {
                visited.add(neighbor);
                if(neighbor.getNumber() == 0)
                    getNeighborsWithZero(neighbor, visited);
            }
        }
    }
    private static ArrayList<Field> getNeighbors(Field f) {
        ArrayList<Field> neighbors = new ArrayList<>();
        int x = f.getPositionX();
        int y = f.getPositionY();
        if(x > 0) {
            neighbors.add(grid[x-1][y]);
            if(y > 0) {
                neighbors.add(grid[x-1][y-1]);
            }
            if(y < COLUMNS-1) {
                neighbors.add(grid[x-1][y+1]);
            }
        }
        if(y > 0) {
            neighbors.add(grid[x][y-1]);
            if(x < ROWS-1) {
                neighbors.add(grid[x+1][y-1]);
            }
        }
        if(x < ROWS-1) {
            neighbors.add(grid[x+1][y]);
            if(y < COLUMNS-1) {
                neighbors.add(grid[x+1][y+1]);
            }
        }
        if(y < COLUMNS-1) {
            neighbors.add(grid[x][y+1]);
        }
        return neighbors;
    }
}
