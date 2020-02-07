package app.gui.profile;

import app.App;
import app.config.SignedUser;
import app.gui.registration.RegistrationController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.io.IOException;

public class ProfileController {

    private static ProfileController instance = new ProfileController();
    public static ProfileController getInstance(){return instance;}


    public void init() throws IOException {
        setScene();
    }

    private void setScene() throws IOException {
        Parent parent = loadFXML("profile");
        App.setRoot(parent);
    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ProfileEditController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

}
