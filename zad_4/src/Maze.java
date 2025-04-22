import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Arrays;

public class Maze extends JFrame {
    private static final int DEFAULT_WIDTH = 16, DEFAULT_HEIGHT = 16;
    private static final int DEFAULT_COL = 1, DEFAULT_ROW = 1;
    private static final String LAST_MAZE = ".last.maze";
    private static final int DEFAULT_WATER_MOVES = 5;
    private static final int TILE_SIZE = 50;
    private static final Color WATER = new Color(0x1e90ff), FIRE = new Color(0xff0000),
            WALL = new Color(0x4d4d4d), EXIT = new Color(0x6bff00), EMPTY = new Color(0xffffff);


    private int width, height;
    private Tile[][] maze;
    private Position lastPosition;
    private boolean mousePressed;

    public Maze() {
        try {
            loadLayout(new File(LAST_MAZE));
        } catch (IOException | ClassNotFoundException e) {
            changeSize();
        }

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setTitle("Maze");

        var buttonsPanel = getButtonsPanel();
        add(buttonsPanel, BorderLayout.SOUTH);

        var mazePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics graphics) {
                drawMaze(graphics, buttonsPanel.getHeight());
            }
        };

        var dimension = new Dimension(width * TILE_SIZE, height * TILE_SIZE);
        mazePanel.setPreferredSize(dimension);
        add(mazePanel);

        mazePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                updateTitle(event, buttonsPanel.getHeight());
                mousePressed = true;
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                mousePressed = false;
                lastPosition = null;
            }
        });

        mazePanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent event) {
                if (!mousePressed) return;
                updateTitle(event, buttonsPanel.getHeight());
            }
        });

        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() != KeyEvent.VK_ESCAPE && e.getKeyCode() != KeyEvent.VK_ENTER) return;
                dispatchEvent(new WindowEvent(Maze.this, WindowEvent.WINDOW_CLOSING));
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                dispose();
                onExit();
            }
        });
    }

    private JPanel getButtonsPanel() {
        var saveButton = new JButton("Save");
        saveButton.addActionListener(e -> saveLayout());

        var loadButton = new JButton("Load");
        loadButton.addActionListener(e -> loadLayout());

        var clearButton = new JButton("Clear");
        clearButton.addActionListener(e -> fillWithWalls());

        var sizeButton = new JButton("Change size");
        sizeButton.addActionListener(e -> changeSize());

        var buttonsPanel = new JPanel();
        buttonsPanel.add(saveButton);
        buttonsPanel.add(loadButton);
        buttonsPanel.add(clearButton);
        buttonsPanel.add(sizeButton);
        return buttonsPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var drawer = new Maze();
            drawer.pack();
            drawer.setVisible(true);
        });
    }

    private void onExit() {
        try {
            saveLayout(new File(LAST_MAZE));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        int col, row, waterMoves;

        try {
            col = Integer.parseInt(JOptionPane.showInputDialog("Enter start column:", DEFAULT_COL));
            row = Integer.parseInt(JOptionPane.showInputDialog("Enter start row:", DEFAULT_ROW));
            waterMoves = Integer.parseInt(JOptionPane.showInputDialog("Enter maze under water moves:", DEFAULT_WATER_MOVES));
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(null, exception);
            return;
        }

        var controller = new TestPlayerController(maze, new Position(col, row), waterMoves);

        var indiana = new Indiana();
        indiana.setPlayerController(controller);
        indiana.underwaterMovesAllowed(waterMoves);

        var start = System.currentTimeMillis();
        indiana.findExit();
        var end = System.currentTimeMillis();

        if (!controller.foundExit()) {
            throw new RuntimeException("Exit not found");
        }

        System.out.println("Solving maze took " + (end - start) + "ms");
    }

    private void changeSize() {
        try {
            width = Integer.parseInt(JOptionPane.showInputDialog("Enter maze width:", DEFAULT_WIDTH));
            height = Integer.parseInt(JOptionPane.showInputDialog("Enter maze height:", DEFAULT_HEIGHT));
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(null, exception);
            return;
        }

        maze = new Tile[width][height];
        fillWithWalls();
    }

    private void fillWithWalls() {
        for (var row : maze) {
            Arrays.fill(row, Tile.WALL);
        }

        repaint();
    }

    private void updateTitle(MouseEvent event, int panelHeight) {
        var tileSize = Math.min(getWidth() / width, getHeight() / height - 5);
        int xOffset = (getWidth() - width * tileSize) / 2;
        int yOffset = (getHeight() - height * tileSize - getInsets().top - panelHeight) / 2;

        int col = (event.getX() - xOffset) / tileSize;
        int row = (event.getY() - yOffset) / tileSize;
        if (col < 0 || col >= width || row < 0 || row >= height) return;

        var currentPosition = new Position(col, row);
        if (currentPosition.equals(lastPosition)) return;

        maze[col][row] = maze[col][row].next();
        lastPosition = currentPosition;

        repaint();
    }

    private void saveLayout() {
        var chooser = new JFileChooser();
        var filter = new FileNameExtensionFilter("maze files", "maze");
        chooser.setFileFilter(filter);

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            var file = chooser.getSelectedFile();

            if (!filter.accept(file)) {
                var name = file.getName() + "." + filter.getExtensions()[0];
                file = new File(file.getParent(), name);
            }

            try {
                saveLayout(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void saveLayout(File file) throws IOException {
        try (var stream = new ObjectOutputStream(new FileOutputStream(file))) {
            stream.writeObject(maze);
        }
    }

    private void loadLayout() {
        var chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("maze files", "maze"));

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                loadLayout(chooser.getSelectedFile());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void loadLayout(File file) throws IOException, ClassNotFoundException {
        try (var stream = new ObjectInputStream(new FileInputStream(file))) {
            maze = (Tile[][]) stream.readObject();

            height = maze[0].length;
            width = maze.length;
        }

        repaint();
    }

    public void drawMaze(Graphics graphics, int panelHeight) {
        var tileSize = Math.min(getWidth() / width, getHeight() / height - 5);
        int xOffset = (getWidth() - width * tileSize) / 2;
        int yOffset = (getHeight() - height * tileSize - getInsets().top - panelHeight) / 2;

        for (int x = 0; x < width; x++) {
            int col = xOffset + x * tileSize;

            for (int y = 0; y < height; y++) {
                int row = yOffset + y * tileSize;

                var color = switch (maze[x][y]) {
                    case WATER -> WATER;
                    case FIRE -> FIRE;
                    case WALL -> WALL;
                    case EXIT -> EXIT;
                    case EMPTY -> EMPTY;
                };

                graphics.setColor(color);
                graphics.fillRect(col, row, tileSize, tileSize);

                graphics.setColor(Color.BLACK);
                graphics.drawRect(col, row, tileSize, tileSize);

                if (x == 0 || y == 0) {
                    var metrics = graphics.getFontMetrics();
                    var num = String.valueOf((x != 0 ? x : y) + 1);

                    int xx = col + tileSize / 2 - metrics.stringWidth(num) / 2;
                    int yy = row + TILE_SIZE / 2  + metrics.getAscent() / 2;

                    graphics.setColor(Color.BLACK);
                    graphics.drawString(num, xx, yy);
                }
            }
        }
    }
}
