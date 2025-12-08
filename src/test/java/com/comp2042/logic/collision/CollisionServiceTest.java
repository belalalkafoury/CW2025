package com.comp2042.logic.collision;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CollisionServiceTest {

    private CollisionService collisionService;

    @BeforeEach
    void setUp() {
        collisionService = new CollisionService();
    }

    @Test
    void testIntersectsWithEmptyBoard() {
        int[][] board = new int[10][20];
        int[][] shape = new int[][]{
            {1, 1},
            {1, 1}
        };
        
        assertFalse(collisionService.intersects(board, shape, 0, 0), 
            "Shape should not intersect with empty board");
    }

    @Test
    void testIntersectsWithFilledCell() {
        int[][] board = new int[10][20];
        board[5][5] = 1;
        int[][] shape = new int[][]{
            {1}
        };
        
        assertTrue(collisionService.intersects(board, shape, 5, 5), 
            "Shape should intersect with filled cell");
    }

    @Test
    void testIntersectsOutOfBoundsLeft() {
        int[][] board = new int[10][20];
        int[][] shape = new int[][]{
            {1}
        };
        
        assertTrue(collisionService.intersects(board, shape, -1, 5), 
            "Shape should intersect when out of bounds to the left");
    }

    @Test
    void testIntersectsOutOfBoundsRight() {
        int[][] board = new int[10][20];
        int[][] shape = new int[][]{
            {1}
        };
        
        assertTrue(collisionService.intersects(board, shape, 20, 5), 
            "Shape should intersect when out of bounds to the right");
    }

    @Test
    void testIntersectsOutOfBoundsTop() {
        int[][] board = new int[10][20];
        int[][] shape = new int[][]{
            {1}
        };
        
        assertTrue(collisionService.intersects(board, shape, 5, -1), 
            "Shape should intersect when out of bounds at top");
    }

    @Test
    void testIntersectsOutOfBoundsBottom() {
        int[][] board = new int[10][20];
        int[][] shape = new int[][]{
            {1}
        };
        
        assertTrue(collisionService.intersects(board, shape, 5, 10), 
            "Shape should intersect when out of bounds at bottom");
    }

    @Test
    void testCanPlaceAtValidPosition() {
        int[][] board = new int[10][20];
        int[][] shape = new int[][]{
            {1, 1},
            {1, 1}
        };
        
        assertTrue(collisionService.canPlaceAt(board, shape, 0, 0), 
            "Should be able to place shape at valid position");
    }

    @Test
    void testCanPlaceAtInvalidPosition() {
        int[][] board = new int[10][20];
        board[5][5] = 1;
        int[][] shape = new int[][]{
            {1}
        };
        
        assertFalse(collisionService.canPlaceAt(board, shape, 5, 5), 
            "Should not be able to place shape on filled cell");
    }

    @Test
    void testCanPlaceAtOutOfBounds() {
        int[][] board = new int[10][20];
        int[][] shape = new int[][]{
            {1}
        };
        
        assertFalse(collisionService.canPlaceAt(board, shape, -1, 5), 
            "Should not be able to place shape out of bounds");
    }

    @Test
    void testIntersectsWithPartialShape() {
        int[][] board = new int[10][20];
        board[5][6] = 1;
        int[][] shape = new int[][]{
            {1, 1, 1}
        };
        
        assertTrue(collisionService.intersects(board, shape, 5, 5), 
            "Should detect intersection when part of shape overlaps");
    }

    @Test
    void testCanPlaceAtWithZeroCells() {
        int[][] board = new int[10][20];
        int[][] shape = new int[][]{
            {0, 0},
            {0, 0}
        };
        
        assertTrue(collisionService.canPlaceAt(board, shape, 0, 0), 
            "Shape with all zeros should be placeable anywhere");
    }

    @Test
    void testIntersectsIgnoresZeroCells() {
        int[][] board = new int[10][20];
        board[5][5] = 1;
        int[][] shape = new int[][]{
            {0, 1},
            {1, 0}
        };
        
        assertFalse(collisionService.intersects(board, shape, 4, 4), 
            "Should not intersect when non-zero cells don't overlap with filled board cells");
        
        assertTrue(collisionService.intersects(board, shape, 4, 5), 
            "Should intersect when non-zero cell overlaps with filled board cell");
    }
}

