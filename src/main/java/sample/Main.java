package sample;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import sample.Utils.Config;
import sample.Utils.MyMongo;
import sample.Utils.Strings;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.*;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

          // Disabling extensive mongo-logging
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.ERROR);

          // Creating Grid
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Login app");
        sceneTitle.setFont(Font.font(Font.getFontNames().get(2), FontWeight.NORMAL, 20));

        grid.add(sceneTitle, 1, 0, 2, 1);

        Label userNameLabel = new Label("Username:");
        grid.add(userNameLabel, 0, 1);

        final TextField userNameTF = new TextField();
        grid.add(userNameTF, 1, 1);

        Label password = new Label("Password:");
        grid.add(password, 0, 2);

        final PasswordField passwordBox = new PasswordField();
        grid.add(passwordBox, 1, 2);


        Button registerButton = new Button("Register");
        Button signInButton = new Button("Sign in");
        Button signOutButton = new Button("Sign out");
        HBox hbButton = new HBox(15);
        hbButton.setAlignment(Pos.BOTTOM_LEFT);
        hbButton.getChildren().add(registerButton);
        hbButton.setAlignment(Pos.BOTTOM_CENTER);
        hbButton.getChildren().add(signInButton);
        hbButton.setAlignment(Pos.BOTTOM_RIGHT);
        hbButton.getChildren().add(signOutButton);
        grid.add(hbButton, 1, 5);

        // Hide it till successful sign in
        signOutButton.setVisible(false);

        final Text outputText = new Text();
        grid.add(outputText, 1, 6);

        // Scene setup
        Scene scene = new Scene(grid, 550, 420, Color.LIGHTSLATEGRAY);
        stage.setScene(scene);

        stage.setTitle("Simple javafx application login system");
        stage.show();


          // Setting actions
        signInButton.setOnAction(e -> {
            String username = userNameTF.getText();
            String pass = passwordBox.getText();
            if (username.length() < Config.minUsernameLength) {
                outputText.setFill(Color.RED);
                outputText.setText(Strings.usernameTooShort);
                return;
            }
            if (pass.length() < Config.minPassLength) {
                outputText.setFill(Color.RED);
                outputText.setText(Strings.passTooShort);
                return;
            }

            if (MyMongo.checkUserCreds(username, pass)) {
                outputText.setFill(Color.GREEN);
                outputText.setText("Sign in successfully!");
                sceneTitle.setText("Signed in as: "+username);
                passwordBox.setText("");
                userNameTF.setText("");
                registerButton.setVisible(false);
                signInButton.setVisible(false);
                signOutButton.setVisible(true);
            } else {
                outputText.setFill(Color.RED);
                outputText.setText("Please check credentials again");
                passwordBox.setText("");
                registerButton.setVisible(true);
                signInButton.setVisible(true);
                signOutButton.setVisible(false);
            }
        });

        registerButton.setOnAction(e -> {
            String username = userNameTF.getText();
            String pass = passwordBox.getText();
            if (username.length() < Config.minUsernameLength) {
                outputText.setFill(Color.RED);
                outputText.setText(Strings.usernameTooShort);
                return;
            }
            if (pass.length() < Config.minPassLength) {
                outputText.setFill(Color.RED);
                outputText.setText(Strings.passTooShort);
                return;
            }

            if (MyMongo.registerNewUser(new MyUser(username, pass))) {
                outputText.setFill(Color.GREEN);
                outputText.setText("Registered successfully!");
                sceneTitle.setText("Signed in as: "+username);
                passwordBox.setText("");
                userNameTF.setText("");
                registerButton.setVisible(false);
                signInButton.setVisible(false);
                signOutButton.setVisible(true);
            } else {
                outputText.setFill(Color.RED);
                outputText.setText("Error: User is already registered");
                registerButton.setVisible(true);
                signInButton.setVisible(true);
                signOutButton.setVisible(false);
            }
        });

        signOutButton.setOnAction(e -> {
            sceneTitle.setText("Login app");
            outputText.setFill(Color.BLACK);
            outputText.setText("");
            registerButton.setVisible(true);
            signInButton.setVisible(true);
            signOutButton.setVisible(false);
        });

    }


    public static void main(String[] args) {
        launch(args);
    }
}
