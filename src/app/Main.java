package app;

import app.server.ChatServer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    
private static boolean serverStarted = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        
        // Jalankan ChatServer di background thread
        if (!serverStarted) {
            serverStarted = true;
            new Thread(() -> {
                try {
                    ChatServer.startServer(12345); // panggil method server
                } catch (Exception e) {
                e.printStackTrace();
                }
            }).start();
        }

        

        // Load FXML LoginView
        Parent root = FXMLLoader.load(getClass().getResource("/app/fxml/LoginView.fxml"));
        primaryStage.setTitle("Aplikasi Bimbingan Skripsi");

        // Fullscreen mode
        primaryStage.setMaximized(true);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
