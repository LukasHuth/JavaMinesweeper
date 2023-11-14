package de.lukashuth;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

interface JTableGetter {
    JTable get();
}
interface JLabelGetter {
    JLabel get();
}
interface JPanelGetter {
    JPanel get();
}
interface LongGetter {
    long get();
}
public class MainWindow {
    long startTime = System.currentTimeMillis();
    long lastupdate = System.currentTimeMillis();
    JPanel container;
    private JPanel information_panel;
    private JPanel grid_panel;
    private JLabel time_label;
    private JLabel remaining_label;
    private JLabel status_label;
    // private JPanel[] rows = {r01, r02, r03, r04, r05, r06, r07, r08, r09, r10, r11, r12};
    private ArrayList<JPanel> rows = new ArrayList<>();
    private JTable table1;
    public JLabelGetter getRemaining_label = () -> this.remaining_label;
    public JLabelGetter getTime_label = () -> this.time_label;
    public JPanelGetter getInformation_panel = () -> this.information_panel;
    public JPanelGetter getGrid_panel = () -> this.grid_panel;
    public JPanelGetter getContainer = () -> this.container;
    public JTableGetter getTable1 = () -> this.table1;
    public JLabelGetter getStatus_label = () -> this.status_label;
    public LongGetter getStartTime = () -> this.startTime;

    public MainWindow(int size) {
        this.rows = new ArrayList<>();
        grid_panel.setLayout(new GridLayout(size, 1));
        for(int i = 0; i < size; i++) {
            JPanel row = new JPanel();
            rows.add(row);
            this.grid_panel.add(row);
        }
    }

    public ArrayList<JPanel> getRows() {
        return rows;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
