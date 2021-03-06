package twelve.team.controllers.course;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import twelve.team.Loader;
import twelve.team.Router;
import twelve.team.controllers.MainPane;
import twelve.team.models.Assignment;
import twelve.team.models.Course;
import twelve.team.models.Section;
import twelve.team.models.Student;
import twelve.team.models.table.AssignmentModel;
import twelve.team.models.table.StudentModel;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class StudentPane extends AnchorPane implements Initializable, EventHandler<ActionEvent> {
    public static final String STUDENT_FXML_PATH = "course/StudentPane.fxml";

    @FXML
    private AnchorPane root;

    @FXML
    private TextField tf_search;

    @FXML
    private Button btn_addStudent;

    @FXML
    private JFXTreeTableView table_students;

    private ArrayList<Student> students;
    private Section section;
    private Course course;
    private Assignment assignment;

    public StudentPane() {
        Loader.load(STUDENT_FXML_PATH, this);
    }

    public void load(Section section) {
        ArrayList<TreeItem<StudentModel>> studentModels = new ArrayList<>();
        for (Student student : section.getStudents()) {
            TreeItem<StudentModel> studentModel = new TreeItem<>(new StudentModel(student, section, assignment));
            studentModels.add(studentModel);
        }
        loadTable(studentModels);
    }

    public void load(Course course) {
        ArrayList<TreeItem<StudentModel>> studentModels = new ArrayList<>();
        for (Section section : course.getSections()) {
            for (Student student : section.getStudents()) {
                TreeItem<StudentModel> studentModel = new TreeItem<>(new StudentModel(student, section, assignment));
                studentModels.add(studentModel);
            }
        }
        loadTable(studentModels);
    }

    private void reset() {
        this.students =null;
        this.section = null;
        this.course =null;
        this.assignment =null;
        if (table_students.getRoot() != null) {
            table_students.getRoot().getChildren().clear();
        }
    }

    public void load(Course course, Assignment assignment) {
        reset();
        this.assignment = assignment;
        this.course = course;
        load(course);
    }

    public void load(Section section, Assignment assignment) {
        reset();
        this.assignment = assignment;
        this.section = section;
        load(section);
    }

    private void loadTable(ArrayList<TreeItem<StudentModel>> studentModels) {

        ArrayList<JFXTreeTableColumn> columns = new ArrayList<>();

        JFXTreeTableColumn<StudentModel, String> name = new JFXTreeTableColumn<>("Name");
        name.setPrefWidth(200);
        name.setCellValueFactory(param -> param.getValue().getValue().getStudentName());
        columns.add(name);

        JFXTreeTableColumn<StudentModel, String> universityID = new JFXTreeTableColumn<>("UniversityID");
        universityID.setPrefWidth(200);
        universityID.setCellValueFactory(param -> param.getValue().getValue().getUniversityID());
        columns.add(universityID);

        JFXTreeTableColumn<StudentModel, String> degree = new JFXTreeTableColumn<>("Degree");
        degree.setPrefWidth(200);
        degree.setCellValueFactory(param -> param.getValue().getValue().getDegree());
        columns.add(degree);

        JFXTreeTableColumn<StudentModel, String> grade = new JFXTreeTableColumn<>("Grade");
        grade.setPrefWidth(200);
        grade.setCellValueFactory(param -> param.getValue().getValue().getGrade());
        columns.add(grade);

        JFXTreeTableColumn<StudentModel, String> comments = new JFXTreeTableColumn<>("Comment");
        comments.setPrefWidth(200);
        comments.setCellValueFactory(param -> param.getValue().getValue().getComment());
        columns.add(comments);

        System.out.println("ASSIGNMENT NULL ? " + String.valueOf(assignment == null));
        if (assignment != null) {
            table_students.setEditable(true);
            grade.setCellFactory(param -> new TextFieldTreeTableCell<>());
            grade.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
            grade.setOnEditCommit(event -> {
                final StudentModel studentModel = event.getRowValue().getValue();
                double newGrade = 0;
                try {
                    newGrade = Double.valueOf(event.getNewValue());
                    studentModel.setGrade(newGrade);
                } catch (NumberFormatException e) {
                    return;
                }
            });

            comments.setCellFactory(param -> new TextFieldTreeTableCell<>());
            comments.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
            comments.setOnEditCommit(event -> {
                final StudentModel studentModel = event.getRowValue().getValue();
                studentModel.setComment(event.getNewValue());
            });

            JFXTreeTableColumn<StudentModel, String> sections = new JFXTreeTableColumn<>("Section");
            sections.setPrefWidth(200);
            sections.setCellValueFactory(param -> param.getValue().getValue().getSectionCode());
            columns.add(0, sections);
        }

        if (assignment == null) {
            table_students.setRowFactory( tv -> {
                TreeTableRow<StudentModel> row = new TreeTableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 3 && (! row.isEmpty()) ) {
                        StudentModel rowData = row.getItem();
                        StudentSummaryPane studentSummaryPane = new StudentSummaryPane();
                        if (section != null) {
                            studentSummaryPane.load(course, rowData.getStudent());
                        }
                        Router.getRouter().addPane(studentSummaryPane, false);
                    }
                });
                return row ;
            });
        }


        final TreeItem<StudentModel> root = new TreeItem<>(new StudentModel("Name", "ID", "Degree", "Comment", "Grade"));
        root.getChildren().addAll(studentModels);

        table_students.setRoot(root);
        table_students.getColumns().setAll(name, universityID, degree, grade, comments);
        table_students.setShowRoot(false);

        btn_addStudent.setOnAction(this);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @Override
    public void handle(ActionEvent event) {
        HashMap<Section, Student> result;
        result = new StudentEditPane().load(MainPane.teacher.getCurrentCourse());

        if (result == null) {
            return;
        }

        result.keySet().forEach(section -> {
            Student student = result.get(section);
            table_students.getRoot().getChildren().add(new TreeItem<>(new StudentModel(student, section, assignment)));
        });
    }
}
