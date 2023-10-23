package de.lukashuth;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
    public JPanel r01;
    private JPanel r02;
    private JPanel r03;
    private JPanel r04;
    private JPanel r05;
    private JPanel r06;
    private JPanel r07;
    private JPanel r08;
    private JPanel r09;
    private JPanel r10;
    private JPanel r11;
    private JPanel r12;
    private JLabel status_label;
    private final JPanel[] rows = {r01, r02, r03, r04, r05, r06, r07, r08, r09, r10, r11, r12};
    private JTable table1;
    public JLabelGetter getRemaining_label = () -> this.remaining_label;
    public JLabelGetter getTime_label = () -> this.time_label;
    public JPanelGetter getInformation_panel = () -> this.information_panel;
    public JPanelGetter getGrid_panel = () -> this.grid_panel;
    public JPanelGetter getContainer = () -> this.container;
    public JTableGetter getTable1 = () -> this.table1;
    public JLabelGetter getStatus_label = () -> this.status_label;
    public LongGetter getStartTime = () -> this.startTime;

    public MainWindow() {
    }

    public JPanel[] getRows() {
        return rows;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
