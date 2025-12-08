package com.comp2042.controller;

import com.comp2042.logic.bricks.Brick;
import com.comp2042.logic.bricks.BrickFactory;
import com.comp2042.util.BrickColorMapper;
import javafx.animation.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.effect.Glow;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainMenuAnimationController {
    private static final int BRICK_SIZE = 32;
    private static final int NUM_PIECES = 10;
    private static final double MIN_SPEED = 0.8;
    private static final double MAX_SPEED = 1.8;
    private static final double MIN_HORIZONTAL_DRIFT = -0.15;
    private static final double MAX_HORIZONTAL_DRIFT = 0.15;
    private static final double OPACITY = 0.8;
    private static final double COLLISION_BUFFER = 2.0;
    
    private final Pane container;
    private final BrickFactory brickFactory;
    private final Random random;
    private final List<AnimatedPiece> pieces;
    private Timeline animationLoop;
    
    public MainMenuAnimationController(Pane container) {
        this.container = container;
        this.brickFactory = new BrickFactory();
        this.random = new Random();
        this.pieces = new ArrayList<>();
    }
    
    public void start() {
        createInitialPieces();
        
        animationLoop = new Timeline(new KeyFrame(
            Duration.millis(8), // ~120 FPS for smoother animation
            e -> updateAnimations()
        ));
        animationLoop.setCycleCount(Animation.INDEFINITE);
        animationLoop.play();
    }
    
    public void stop() {
        if (animationLoop != null) {
            animationLoop.stop();
        }
        container.getChildren().clear();
        pieces.clear();
    }
    
    private void createInitialPieces() {
        for (int i = 0; i < NUM_PIECES; i++) {
            createNewPiece();
        }
    }
    
    private void createNewPiece() {
        boolean createSingleBlock = random.nextDouble() < 0.25;
        GridPane gridPane;
        
        if (createSingleBlock) {
            gridPane = createSingleBlock();
        } else {
            Brick brick = brickFactory.createRandomBrick();
            List<int[][]> shapes = brick.getShapeMatrix();
            int randomRotation = random.nextInt(shapes.size());
            int[][] shape = shapes.get(randomRotation);
            gridPane = createBrickGridPane(shape);
        }
        
        double containerWidth = container.getWidth() > 0 ? container.getWidth() : 700;
        double containerHeight = container.getHeight() > 0 ? container.getHeight() : 600;
        
        double startX = random.nextDouble() * (containerWidth - 150);
        double startY = -150 - random.nextDouble() * 200;
        
        double verticalSpeed = MIN_SPEED + random.nextDouble() * (MAX_SPEED - MIN_SPEED);
        double horizontalDrift = MIN_HORIZONTAL_DRIFT + random.nextDouble() * (MAX_HORIZONTAL_DRIFT - MIN_HORIZONTAL_DRIFT);
        double rotationSpeed = (random.nextBoolean() ? 1 : -1) * (0.05 + random.nextDouble() * 0.15);
        
        gridPane.setLayoutX(startX);
        gridPane.setLayoutY(startY);
        gridPane.setOpacity(OPACITY);
        gridPane.setMouseTransparent(true);
        gridPane.setRotate(random.nextDouble() * 360);
        
        Glow glow = new Glow(0.35);
        gridPane.setEffect(glow);
        
        container.getChildren().add(gridPane);
        
        AnimatedPiece piece = new AnimatedPiece(gridPane, verticalSpeed, horizontalDrift, rotationSpeed);
        pieces.add(piece);
    }
    
    private GridPane createSingleBlock() {
        GridPane gridPane = new GridPane();
        Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
        
        int[] colors = {1, 2, 3, 4, 5, 6, 7};
        int randomColor = colors[random.nextInt(colors.length)];
        
        rectangle.setFill(BrickColorMapper.getFillColor(randomColor));
        rectangle.setArcHeight(9);
        rectangle.setArcWidth(9);
        
        gridPane.add(rectangle, 0, 0);
        return gridPane;
    }
    
    private GridPane createBrickGridPane(int[][] shape) {
        GridPane gridPane = new GridPane();
        gridPane.setVgap(1);
        gridPane.setHgap(1);
        
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[i].length; j++) {
                if (shape[i][j] != 0) {
                    Rectangle rectangle = new Rectangle(BRICK_SIZE, BRICK_SIZE);
                    rectangle.setFill(BrickColorMapper.getFillColor(shape[i][j]));
                    rectangle.setArcHeight(9);
                    rectangle.setArcWidth(9);
                    gridPane.add(rectangle, j, i);
                }
            }
        }
        
        return gridPane;
    }
    
    private void updateAnimations() {
        double containerWidth = container.getWidth() > 0 ? container.getWidth() : 700;
        double containerHeight = container.getHeight() > 0 ? container.getHeight() : 600;
        List<AnimatedPiece> toRemove = new ArrayList<>();
        
        for (AnimatedPiece piece : pieces) {
            GridPane gridPane = piece.getGridPane();
            double currentY = gridPane.getLayoutY();
            double currentX = gridPane.getLayoutX();
            
            double newY = currentY + piece.getVerticalSpeed();
            double newX = currentX + piece.getHorizontalDrift();
            double newRotate = gridPane.getRotate() + piece.getRotationSpeed();
            
            gridPane.setLayoutX(newX);
            gridPane.setLayoutY(newY);
            gridPane.setRotate(newRotate);
            
            checkAndResolveCollisions(piece, pieces);
            
            if (newY > containerHeight + 200 || newX < -200 || newX > containerWidth + 200) {
                toRemove.add(piece);
                container.getChildren().remove(gridPane);
            }
        }
        
        pieces.removeAll(toRemove);
        
        while (pieces.size() < NUM_PIECES) {
            createNewPiece();
        }
    }
    
    private void checkAndResolveCollisions(AnimatedPiece piece, List<AnimatedPiece> allPieces) {
        GridPane gridPane = piece.getGridPane();
        double pieceCenterX = gridPane.getLayoutX() + getPieceWidth(gridPane) / 2;
        double pieceCenterY = gridPane.getLayoutY() + getPieceHeight(gridPane) / 2;
        
        for (AnimatedPiece otherPiece : allPieces) {
            if (otherPiece == piece) {
                continue;
            }
            
            GridPane otherGrid = otherPiece.getGridPane();
            double otherCenterX = otherGrid.getLayoutX() + getPieceWidth(otherGrid) / 2;
            double otherCenterY = otherGrid.getLayoutY() + getPieceHeight(otherGrid) / 2;
            
            double distanceX = pieceCenterX - otherCenterX;
            double distanceY = pieceCenterY - otherCenterY;
            double distance = Math.sqrt(distanceX * distanceX + distanceY * distanceY);
            
            double minDistance = (getPieceWidth(gridPane) + getPieceWidth(otherGrid)) / 2 + COLLISION_BUFFER;
            
            if (distance < minDistance && distance > 0) {
                double pushForce = 0.15;
                double pushX = (distanceX / distance) * pushForce;
                double pushY = (distanceY / distance) * pushForce;
                
                piece.setHorizontalDrift(piece.getHorizontalDrift() + pushX);
                piece.setVerticalSpeed(piece.getVerticalSpeed() + pushY);
                
                otherPiece.setHorizontalDrift(otherPiece.getHorizontalDrift() - pushX);
                otherPiece.setVerticalSpeed(otherPiece.getVerticalSpeed() - pushY);
                
                double currentX = gridPane.getLayoutX();
                double currentY = gridPane.getLayoutY();
                gridPane.setLayoutX(currentX + pushX * 5);
                gridPane.setLayoutY(currentY + pushY * 5);
                
                double otherX = otherGrid.getLayoutX();
                double otherY = otherGrid.getLayoutY();
                otherGrid.setLayoutX(otherX - pushX * 5);
                otherGrid.setLayoutY(otherY - pushY * 5);
            }
        }
    }
    
    private double getPieceWidth(GridPane gridPane) {
        return gridPane.getBoundsInLocal().getWidth();
    }
    
    private double getPieceHeight(GridPane gridPane) {
        return gridPane.getBoundsInLocal().getHeight();
    }
    
    private static class AnimatedPiece {
        private final GridPane gridPane;
        private double verticalSpeed;
        private double horizontalDrift;
        private double rotationSpeed;
        
        public AnimatedPiece(GridPane gridPane, double verticalSpeed, double horizontalDrift, double rotationSpeed) {
            this.gridPane = gridPane;
            this.verticalSpeed = verticalSpeed;
            this.horizontalDrift = horizontalDrift;
            this.rotationSpeed = rotationSpeed;
        }
        
        public GridPane getGridPane() {
            return gridPane;
        }
        
        public double getVerticalSpeed() {
            return verticalSpeed;
        }
        
        public void setVerticalSpeed(double verticalSpeed) {
            this.verticalSpeed = verticalSpeed;
        }
        
        public double getHorizontalDrift() {
            return horizontalDrift;
        }
        
        public void setHorizontalDrift(double horizontalDrift) {
            this.horizontalDrift = horizontalDrift;
        }
        
        public double getRotationSpeed() {
            return rotationSpeed;
        }
        
        public void setRotationSpeed(double rotationSpeed) {
            this.rotationSpeed = rotationSpeed;
        }
    }
}

