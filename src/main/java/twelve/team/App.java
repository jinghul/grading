/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twelve.team;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Jinghu
 */
public class App extends Application {
    @Override
    public void start(Stage stage) {
        Loader.init(stage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Initialize database connection before starting app.
        Database.init();

        // Start ui flow.
        launch(args);
    }
    
}
