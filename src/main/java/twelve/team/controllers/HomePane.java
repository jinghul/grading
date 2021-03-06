package twelve.team.controllers;

import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import twelve.team.Loader;
import twelve.team.models.Teacher;
import twelve.team.utils.Animator;

import java.net.URL;
import java.util.ResourceBundle;

//9b59b6
public class HomePane extends VBox implements Initializable {
    public static final String HOME_FXML_FILE = "HomePane.fxml";

    Teacher teacher; // Router.getRouter().getMain().getTeacher();

    public HomePane() {
        Loader.load(HOME_FXML_FILE, this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Animator.fadeIn(this, null);
    }
}
