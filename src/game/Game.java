package game;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;

public class Game extends GridPane
{
    private int xsize;
    private int ysize;
    private int bombs;
    private int flagsLeft;
    private int cellsToDiscover;
    private boolean gameOver;
    private boolean[][] marked;
    private Controller controller;

    Pair[] directions = {new Pair(0,0), new Pair(0,1), new Pair(0,-1), new Pair(1,0), new Pair(1,1), new Pair(1,-1), new Pair(-1,0), new Pair(-1,1), new Pair(-1,-1)};

    private class Pair
    {
        private int x;
        private int y;

        public Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    public Game(int xsize, int ysize, int bombs,Controller controller)
    {
        this.controller = controller;
        this.restart(xsize,ysize, bombs);
    }

    public void restart()
    {
        this.restart(this.xsize, this.ysize, this.bombs);
    }

    public void restart(int xsize, int ysize, int bombs)
    {
        this.xsize = xsize;
        this.ysize = ysize;
        this.bombs = bombs;
        this.flagsLeft = this.bombs;
        this.gameOver = false;
        this.marked = new boolean[this.xsize][this.ysize];
        this.cellsToDiscover = (this.xsize*this.ysize) - this.bombs;

        this.getChildren().remove(0, this.getChildren().size());

        for (int i = 0; i < ysize; ++i)
        {
            for (int j = 0; j < xsize; ++j)
            {
                Cell c = new Cell(this, j, i);
                this.add(c, j, i);
            }
        }
        setBombs();
        this.controller.updateFlags(this.flagsLeft);
    }

    private void setBombs()
    {
        int i = 0;
        while (i < this.bombs)
        {
            Random r = new Random();

            int x = r.nextInt(this.xsize);
            int y = r.nextInt(this.ysize);

            //System.out.println(x*this.xsize+y);
            Cell c = (Cell) this.getChildren().get(y*this.xsize+x);
            if(c.getValue() == 0)
            {
                c.setValue(-1);
                this.incrementNeighbours(x,y);
                ++i;
            }
        }
    }

    private void incrementNeighbours(int x, int y)
    {
        for(Pair p : directions)
        {
            int nx = x+ p.getX();
            int ny = y+ p.getY();
            if(positionInRange(nx, ny))
            {
                Cell c = (Cell) this.getChildren().get(ny*this.xsize +nx);
                if(!c.isBomb())
                    c.setValue(c.getValue()+1);
            }
        }
    }

    private boolean positionInRange(int x, int y)
    {
        return x >= 0 && y >= 0 && x < this.xsize && y < this.ysize;
    }

    public void discoverNeighbours(int x, int y)
    {
        if (!gameOver)
        {
            for(int i = 0; i < this.xsize; ++i)
            {
                for(int j = 0; j < this.ysize; ++j)
                {
                    marked[i][j] = false;
                }
            }

            Cell c = (Cell) this.getChildren().get(y*this.xsize +x);
            if (c.isBomb())
            {
                c.discover();
                this.gameOver();
            }
            else
            {
                checkCell(x,y);
            }
        }
    }

    private void checkCell(int x,int y)
    {
        marked[x][y] = true;
        Cell c =  (Cell) this.getChildren().get(y*this.xsize +x);

        if (c.getValue() != 0)
        {
            c.discover();
        }
        else
        {
            c.discover();
            for (Pair p : directions)
            {
                int nx = x + p.getX();
                int ny = y + p.getY();
                if (positionInRange(nx,ny) && !marked[nx][ny])
                {
                    checkCell(nx, ny);
                }
            }
        }
        this.checkWin();
    }

    public void gameOver()
    {
        this.gameOver = true;
        for(int i = 0; i < this.xsize; ++i)
        {
            for(int j = 0; j < this.ysize; ++j)
            {
                Cell c = (Cell) this.getChildren().get(j*this.xsize +i);
                if(c.isBomb())
                    c.discover();
            }
        }
    }

    public void checkWin()
    {
        if(this.cellsToDiscover == 0 && !gameOver)
        {
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setHeaderText("You win");
            a.setContentText("NICE");
            a.show();
            gameOver = true;
        }
    }

    public boolean isOver()
    {
        return this.gameOver;
    }

    public void cellDiscovered()
    {
        this.cellsToDiscover--;
    }

    public void flagAdded()
    {
        this.flagsLeft--;
        this.controller.updateFlags(this.flagsLeft);
    }

    public void flagRemoved()
    {
        this.flagsLeft++;
        this.controller.updateFlags(this.flagsLeft);
    }
}
