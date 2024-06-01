package ru.mikhail.gui;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

class Polyakov extends JPanel {
    private MediaView mediaView;
    private final JFXPanel jfxPanel;

    private static final Logger LOGGER = Logger.getLogger(Polyakov.class.getName());

    public Polyakov() {
        super();

        // Создание JFXPanel
        jfxPanel = new JFXPanel();
        this.setLayout(new BorderLayout());
        this.add(jfxPanel, BorderLayout.CENTER);
        Platform.runLater(this::createScene);

        // Слушатель мышки для перезапуска видео
        jfxPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                restartVideo();
            }
        });
    }

    private void createScene() {
        try {
            // Путь к видеофайлу
            URL videoUrl = getClass().getClassLoader().getResource("Polyakov.exe.mp4");
            if (videoUrl == null) {
                throw new RuntimeException("Video file not found");
            }

            Media media = new Media(videoUrl.toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaView = new MediaView(mediaPlayer);
            mediaView.setFitWidth(1700); // Установка размеров видео
            mediaView.setFitHeight(900);

            // Создание сцены
            Scene scene = new Scene(new javafx.scene.Group(mediaView));

            // Сцена в JFXPanel
            jfxPanel.setScene(scene);

            // Воспроизведение видео, если плеер готов
            mediaPlayer.setOnReady(() -> {
                if (mediaPlayer.getStatus() != MediaPlayer.Status.READY && mediaPlayer.getStatus() != MediaPlayer.Status.PAUSED) {
                    mediaPlayer.stop(); // Остановить воспроизведение, если не готов
                    mediaPlayer.seek(mediaPlayer.getStartTime()); // Сбросить плеер в начальное состояние
                }
                mediaPlayer.play();
            });

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to create the media player scene", e);
        }
    }

    // Метод для перезапуска видео
    private void restartVideo() {
        Platform.runLater(() -> {
            MediaPlayer mediaPlayer = mediaView.getMediaPlayer();
            if (mediaPlayer != null) {
                mediaPlayer.seek(mediaPlayer.getStartTime());
                mediaPlayer.play();
            }
        });
    }
}
