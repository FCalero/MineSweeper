package game;

import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;

public class Cell extends Button
{
    //-1 -> Bomb, else -> number of bombs next to it
    int value;
    Status status;
    int xPos;
    int yPos;
    Game game;

    public Cell(Game game, int xPos, int yPos)
    {
        this.value = 0;
        this.game = game;
        this.xPos = xPos;
        this.yPos = yPos;
        this.status = Status.NORMAL;

        this.setPrefHeight(50.0);
        this.setPrefWidth(50.0);

        this.setStyle("-fx-background-radius: 0");
        this.setStyle("-fx-focus-color: transparent;");
        this.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2; ");

        this.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                MouseButton button = event.getButton();

                if(button == MouseButton.PRIMARY && status != Status.FLAG)
                {
                    game.discoverNeighbours(xPos, yPos);
                }
                else if(button == MouseButton.SECONDARY && !game.isOver())
                {
                    switch (status)
                    {
                        case NORMAL: status=Status.FLAG; setImage("flag"); game.flagAdded(); break;
                        case FLAG: status=Status.INTERROGATION; setImage("unexplored"); game.flagRemoved(); break;
                        case INTERROGATION: status=Status.NORMAL;  setGraphic(null); break;
                    }
                }
            }
        });
    }

    public void discover()
    {
        if(this.status != Status.FLAG && this.status != Status.DISCOVERED)
        {
            this.setText("");
            Image image = new Image(getClass().getResourceAsStream("../images/" + value + ".png"));
            this.setGraphic(new ImageView(image));
            this.setDisable(true);
            this.setStyle("-fx-opacity: 0.7;");
            this.game.cellDiscovered();
            this.status = Status.DISCOVERED;
        }
    }

    public boolean isBomb()
    {
        return this.value == -1;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value) { this.value = value; }

    private void setImage(String value)
    {
        Image image = new Image(getClass().getResourceAsStream("../images/"+value+".png"));
        this.setGraphic(new ImageView(image));
    }
}
