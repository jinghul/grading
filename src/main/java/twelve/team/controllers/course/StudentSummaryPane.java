package twelve.team.controllers.course;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import twelve.team.Loader;
import twelve.team.Router;
import twelve.team.models.Assignment;
import twelve.team.models.Course;
import twelve.team.models.Section;
import twelve.team.models.Student;
import twelve.team.utils.Animator;

import java.net.URL;
import java.util.ResourceBundle;

public class StudentSummaryPane extends VBox implements Initializable {
    public static final String STUDENT_PANE_FXML = "course/StudentSummaryPage.fxml";

    @FXML
    private AssignmentPane assignment_pane;

    @FXML
    private Button btn_back;

    public StudentSummaryPane() {
        Loader.load(STUDENT_PANE_FXML, this);
        if(Router.getRouter().canGoBack()) {
            btn_back.setVisible(true);
            btn_back.setOnAction(e -> {
                Router.getRouter().goBack();
            });
        } else {
            btn_back.setVisible(false);
        }
    }

    public void load(Course course, Student student) {
        assignment_pane.load(course, student);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Animator.fadeIn(this, null);
    }
}
