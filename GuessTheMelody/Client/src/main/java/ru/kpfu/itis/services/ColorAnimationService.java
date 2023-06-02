package ru.kpfu.itis.services;

import javafx.animation.Animation;
import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class ColorAnimationService extends Service<Void> {
    private int duration;
    private Pane pane;
    private Color fromColor;
    private Color toColor;

    public ColorAnimationService(Pane pane, Color fromColor, Color toColor, int duration) {
        this.pane = pane;
        this.fromColor = fromColor;
        this.toColor = toColor;
        this.duration = duration;
    }

    @Override
    protected Task<Void> createTask() {
        return new Task<Void>() {
            @Override
            protected Void call(){
                Rectangle rect = new Rectangle();
                rect.setFill(fromColor);

                FillTransition fillTransition = new FillTransition(Duration.millis(duration * 1000), rect, fromColor, toColor);

                fillTransition.setInterpolator(new Interpolator() {
                    @Override
                    protected double curve(double t) {
                        pane.setBackground(new Background(new BackgroundFill(rect.getFill(), CornerRadii.EMPTY, Insets.EMPTY)));
                        return t;
                    }
                });
                fillTransition.setCycleCount(Animation.INDEFINITE);
                fillTransition.setAutoReverse(true);
                fillTransition.play();
                return null;
            }
        };
    }
}

