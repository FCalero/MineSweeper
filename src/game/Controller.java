package game;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;



public class Controller
{
    @FXML
    AnchorPane gamePane;

    @FXML
    Label flagsLeft;

    @FXML
    MenuButton difficultySelector;

    @FXML
    AnchorPane mainPane;

    Game game;

    @FXML
    public void initialize()
    {

        game = new Game(8, 8, 10,this);
        gamePane.getChildren().add(game);
    }

    @FXML
    public void restart()
    {
        game.restart();
    }

    @FXML
    public void selectHard()
    {
        game.restart(32,16,99);
        Main.resizeWindow(1608,880);
    }

    @FXML
    public void selectNormal()
    {
        Main.resizeWindow(808,880);
        game.restart(16,16, 40);
    }

    @FXML
    public void selectEasy()
    {
        game.restart(8,8,10);
        Main.resizeWindow(408,480);
    }

    public void updateFlags(int flagsLeft)
    {
        this.flagsLeft.setText(Integer.toString(flagsLeft));
    }


}
