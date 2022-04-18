package com.noahhusby.ticketing;

import com.noahhusby.lib.application.config.Configuration;
import com.noahhusby.lib.application.config.exception.ClassNotConfigException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class TicketingLauncher extends Application {

    @Getter
    private final Logger logger = LoggerFactory.getLogger("Ticketing");

    @Override
    public void start(Stage stage) throws IOException {
        logger.info("Starting Ticketing ...");

        // Load config
        try {
            logger.info("Loading configuration ...");
            Configuration configuration = Configuration.of(TicketingConfig.class);
            configuration.sync(TicketingConfig.class);
            logger.info("Successfully loaded configuration ...");
        } catch (ClassNotConfigException ignored) {
            // I wrote the config library. This exception won't be thrown, so it can be ignored (:
        }

        FXMLLoader fxmlLoader = new FXMLLoader(TicketingLauncher.class.getResource("ticketing.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}