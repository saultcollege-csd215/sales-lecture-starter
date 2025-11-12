package sales.ui;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import sales.controllers.AppController;
import sales.ui.views.ErrorView;


public class SalesApp extends Application implements MainWindow {

    private Stage primaryStage;
    private BorderPane mainLayout;
    private AppController appController;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        this.appController = new AppController(this);
        this.mainLayout = this.appController.setMainLayout();

        primaryStage.setScene(new Scene(mainLayout, 800,600));

        primaryStage.show();

    }

    @Override
    public void stop() {
        appController.stop();
    }

    public void setTitle(String title) {
        var t = "Sales Application";
        if (!title.isEmpty()) {
            t += " - " + title;
        }
        primaryStage.setTitle(t);
    }

    public void setMainScene(Node n) {
        mainLayout.setCenter(n);
    }

    public void showError(Exception e) {
        primaryStage.setTitle("Error");
        mainLayout.setCenter(ErrorView.createScene(e));
    }

}
