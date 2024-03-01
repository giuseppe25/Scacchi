package src.gui;

import src.*;
import src.data.Scoreboard;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The JFrame that contains all GUI elements.
 */
public class ChessFrame extends JFrame
    implements ComponentListener, GameListener {

    /** The board display. */
    private BoardPanel display;

    /** The current game. */
    private Game game;

    private Info stat;

    Scoreboard scoreboard = Scoreboard.getInstance();

    private JToolBar navigationBar;


    public ChessFrame() {

        super(Chess.getTitle());
        setResizable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());

        // Add StartPanel to the top of the frame
        navigationBar = new JToolBar();
        navigationBar.setFloatable(false);

        JButton resetButton = new JButton("Reset");
        JButton openButton = new JButton("Open");
        JButton saveButton = new JButton("Save");
        resetButton.setFocusPainted(false);
        openButton.setFocusPainted(false);
        saveButton.setFocusPainted(false);


        resetButton.addActionListener(e -> newGame());
        openButton.addActionListener(e ->{
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(ChessFrame.this);

            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                Game loadedGame = game.loadGame(filePath);

                if (loadedGame != null) {

                    game = new Game(loadedGame, this);
                    display.setBoard(loadedGame.getBoard());
                    display.invalidate();
                    setSize(getPreferredSize());


                    game.addGameListener(this);
                    game.addGameListener(display);
                    game.begin();

                }
            }
        } );
        saveButton.addActionListener(e ->{
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showSaveDialog(ChessFrame.this);

            if (result == JFileChooser.APPROVE_OPTION) {
                String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                game.saveGame(filePath);
            }
        } );
        navigationBar.add(resetButton);
        navigationBar.add(openButton);
        navigationBar.add(saveButton);
        add(navigationBar, BorderLayout.NORTH);


        display = new BoardPanel();
        add(display, BorderLayout.CENTER);

        newGame();

        addComponentListener(this);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();

        stat = new Info(game.getBoard());

        JButton mosse = new JButton("Mosse");
        mosse.setFocusPainted(false);
        navigationBar.add(mosse);
        stat.setVisible(true);
        add(stat, BorderLayout.EAST);



        mosse.addActionListener(e -> {
            stat.setVisible(!stat.isVisible());
            if (stat.isVisible()) {
                setSize(new Dimension(getWidth(), getHeight() + stat.getHeight()));
            } else {
                pack();
            }
        });
    }


    public final void newGame() {


            game = new Game(new Board(), this);
            Board board = game.getBoard();
            display.setBoard(board);
            display.invalidate();
            setSize(getPreferredSize());


            game.addGameListener(this);
            game.addGameListener(display);
            game.begin();

    }

    public final void endGame() {
        game.end();
    }


    public final Player getPlayer() {return display;}


    public final Scoreboard getScoreBoard() {return scoreboard;}



    @Override
    public final void componentResized(final ComponentEvent e) {
        Container p = getContentPane();
        Dimension d = null;
        if (p.getWidth()  < (p.getHeight() )) {
            d = new Dimension((p.getHeight()  ),
                    p.getHeight());
        } else if (p.getWidth() > (p.getHeight() )) {
            d = new Dimension(p.getWidth(), p.getWidth() );
        }
        if (d != null) {
            p.setPreferredSize(d);
            pack();
        }
    }

    public final void gameEvent(final GameEvent e) {}

    public void componentHidden(final ComponentEvent e) {}

    public void componentMoved(final ComponentEvent e) {}

    public void componentShown(final ComponentEvent e) {}
}
