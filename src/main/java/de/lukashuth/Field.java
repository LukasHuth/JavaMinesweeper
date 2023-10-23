package de.lukashuth;

public class Field {
    public static int MINES = 0;

    public enum State {
        EMPTY,
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN,
        EIGHT
    }
    private State state;
    private boolean isOpened;
    private boolean isFlagged;
    private boolean isMine;
    public Field(int x,int y) {
        this.positionX = x;
        this.positionY = y;
        final int POSSIBLE_MINES = Main.MINES;
        if(MINES < POSSIBLE_MINES) {
            this.isMine = true;
            MINES++;
        } else {
            this.isMine = false;
        }
        this.state = State.EMPTY;
        this.isOpened = false;
        this.isFlagged = false;
    }
    public boolean isMine() {
        return isMine;
    }
    public boolean isNotMine() {
        return !isMine;
    }
    public void setMine(boolean mine) {
        isMine = mine;
    }
    public State getState() {
        return state;
    }
    public void setState(State state) {
        this.state = state;
    }
    public boolean isOpened() {
        return isOpened;
    }
    public void setOpened(boolean opened) {
        isOpened = opened;
    }
    public boolean isFlagged() {
        return isFlagged;
    }
    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }
    public void open() {
        this.isOpened = true;
    }
    public void flag() {
        this.isFlagged = true;
    }
    public void unflag() {
        this.isFlagged = false;
    }
    public void setNumber(int number) {
        switch (number) {
            case 1 -> this.state = State.ONE;
            case 2 -> this.state = State.TWO;
            case 3 -> this.state = State.THREE;
            case 4 -> this.state = State.FOUR;
            case 5 -> this.state = State.FIVE;
            case 6 -> this.state = State.SIX;
            case 7 -> this.state = State.SEVEN;
            case 8 -> this.state = State.EIGHT;
            default -> this.state = State.EMPTY;
        }
    }
    public boolean isZero() {
        return this.state == State.EMPTY;
    }
    public int getNumber() {
        switch (this.state) {
            case ONE -> {
                return 1;
            }
            case TWO -> {
                return 2;
            }
            case THREE -> {
                return 3;
            }
            case FOUR -> {
                return 4;
            }
            case FIVE -> {
                return 5;
            }
            case SIX -> {
                return 6;
            }
            case SEVEN -> {
                return 7;
            }
            case EIGHT -> {
                return 8;
            }
            default -> {
                return 0;
            }
        }
    }
    private int positionX, positionY;
    public int getPositionX() {
        return positionX;
    }
    public int getPositionY() {
        return positionY;
    }
    public void setPosition(int x, int y) {
        this.positionX = x;
        this.positionY = y;
    }
}
