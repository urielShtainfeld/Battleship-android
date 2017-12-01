package entities;

public class GameRoles {
    private Timer timer;
    private int leftShips;
    private int comLeftShips;

    public Timer getTimer() {
        return timer;
    }

    public int getLeftShips() {
        return leftShips;
    }

    public void setLeftShips(int leftShips) {
        this.leftShips = leftShips;
    }

    public int getComLeftShips() {
        return comLeftShips;
    }

    public void setComLeftShips(int comLeftShips) {
        this.comLeftShips = comLeftShips;
    }

    public void victory(){}

    public void lose(){}
}