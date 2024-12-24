import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    // Inner Tile class
    private class Tile {
        int x, y;
        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    int tileSize = 25;
    int boardWidth, boardHeight;
    Tile snakeHead, food;
    ArrayList<Tile> snakeBody = new ArrayList<>();
    Random random = new Random();
    Timer gameLoop;
    int velocityX = 0, velocityY = 0;
    boolean gameOver = false;

    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        snakeHead = new Tile(5, 5);
        food = new Tile(10, 10);
        placeFood();

        gameLoop = new Timer(100, this);
        gameLoop.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    void draw(Graphics g) {
        // Draw snake head
        g.setColor(Color.green);
        g.fillRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize);

        // Draw snake body
        for (Tile snakePart : snakeBody) {
            g.fillRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize);
        }

        // Draw food
        g.setColor(Color.red);
        g.fillRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize);

        // Game over message
        if (gameOver) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Game Over! Score: " + snakeBody.size(), boardWidth / 2 - 50, boardHeight / 2);
        }
    }

    void placeFood() {
        food.x = random.nextInt(boardWidth / tileSize);
        food.y = random.nextInt(boardHeight / tileSize);
    }

    void move() {
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
        }

        for (int i = snakeBody.size() - 1; i > 0; i--) {
            Tile prev = snakeBody.get(i - 1);
            Tile current = snakeBody.get(i);
            current.x = prev.x;
            current.y = prev.y;
        }
        if (!snakeBody.isEmpty()) {
            Tile first = snakeBody.get(0);
            first.x = snakeHead.x;
            first.y = snakeHead.y;
        }

        snakeHead.x += velocityX;
        snakeHead.y += velocityY;

        for (Tile part : snakeBody) {
            if (collision(snakeHead, part)) {
                gameOver = true;
            }
        }
    }

    boolean collision(Tile t1, Tile t2) {
        return t1.x == t2.x && t1.y == t2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            move();
            repaint();
        } else {
            gameLoop.stop();
        }

        if (snakeHead.x < 0 || snakeHead.x >= boardWidth / tileSize ||
            snakeHead.y < 0 || snakeHead.y >= boardHeight / tileSize) {
            gameOver = true;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}

