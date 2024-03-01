package src;

import src.gui.ChessFrame;

import java.io.Serializable;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Logger;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

/**
 * Guida una partita a scacchi, dati i giocatori e un tabellone.
 *
 * Questo, a sua volta, informerà i giocatori del loro turno e li aspetterà
 *per rispondere con una mossa.
 */
public class Game implements Runnable, Serializable {

    /** This class's Logger. */
    private static final Logger LOG =
        Logger.getLogger("src.Game");

    /** The board being used for this game. */
    private final Board board;

    /** Parent to this dialog. */
    private final transient ChessFrame parent;

    /** The player playing white. */
    private final transient Player white;

    /** The player playing black. */
    private final transient Player black;

    /** Whose turn it is right now. */
    private Piece.Side turn;

    /** Current status message. */
    private String status = "";

    /** Set to true when the board is in a completed state. */
    private volatile Boolean done = false;

    /** When the game is done, this is the winner. */
    private Piece.Side winner;

    private String humanPlayerName;

    /** List of event listeners. */
    private final transient Collection<GameListener> listeners =
        new CopyOnWriteArraySet<GameListener>();


    /**
     * Create a new game with the given board and players.
     *
     * @param gameBoard   the game board
     */
    public Game(final Board gameBoard, ChessFrame owner) {
        parent = owner;
        board = gameBoard;
        white = parent.getPlayer();
        black = new Ai(this);
    }

    public Game(Game load, ChessFrame owner) {
        parent = owner;
        board = load.getBoard();
        white = parent.getPlayer();
        black = new Ai(this);
    }


    public void saveGame(String filePath) {
        try (FileOutputStream fileOut = new FileOutputStream(filePath);
             ObjectOutputStream objectOut = new ObjectOutputStream(fileOut)) {

            objectOut.writeObject(this);
            setStatus("Il gioco è stato salvato con successo.");

        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    public Game loadGame(String filePath) {
        Game loadedGame = null;
        try (FileInputStream fileIn = new FileInputStream(filePath);
             ObjectInputStream objectIn = new ObjectInputStream(fileIn)) {

            loadedGame = (Game) objectIn.readObject();
            setStatus("Il gioco è stato caricato con successo.");



        } catch (Exception e) {
            e.printStackTrace();
        }

        return loadedGame;
    }



    /**
     * End the running game.
     */
    public final void end() {
        parent.getScoreBoard().addToPlayer(humanPlayerName, true);
        listeners.clear();
        winner = null;
        done = true;
    }

    /**
     * Begin the game.
     */
    public final void begin() {
        done = false;
        turn = Piece.Side.BLACK;
        callGameListeners(GameEvent.TURN);
        new Thread(this).start();
    }

    @Override
    public final void run() {
        while (!done) {
            /* Determine who's turn it is. */
            Player player;
            if (turn == Piece.Side.WHITE) {
                turn = Piece.Side.BLACK;
                setStatus("Black's turn.");
                player = black;
            } else {
                turn = Piece.Side.WHITE;
                setStatus("White's turn.");
                player = white;
            }

            /* Fetch the move from the player. */
            Move move = player.takeTurn(getBoard(), turn);
            board.move(move);

            if (done) {
                /* Game may have ended abruptly during the player's
                 * potentially lengthy turn. */
                return;
            }

            /* Check for the end of the game. */
            Piece.Side opp = Piece.opposite(turn);
            if (board.checkmate(opp)) {
                done = true;
                if (opp == Piece.Side.BLACK) {
                    setStatus("White wins!");
                    winner = Piece.Side.WHITE;
                } else {
                    setStatus("Black wins!");
                    winner = Piece.Side.BLACK;
                }
                callGameListeners(GameEvent.GAME_END);
                return;
            } else if (board.stalemate(opp)) {
                done = true;
                setStatus("Stalemate!");
                winner = null;
                callGameListeners(GameEvent.GAME_END);
                return;
            }
            callGameListeners(GameEvent.TURN);
        }
    }

    /**
     * Get the board that belongs to this game.
     *
     * @return the game's board
     */
    public final Board getBoard() {
        return board.copy();
    }

    /**
     * Add a new event listener.
     *
     * @param listener the new event listener
     */
    public final void addGameListener(final GameListener listener) {
        listeners.add(listener);
    }

    /**
     * Call all of the game event listeners.
     *
     * @param type the type of event that occured
     */
    private void callGameListeners(final int type) {
        for (GameListener listener : listeners) {
            listener.gameEvent(new GameEvent(this, type));
        }
    }


    /**
     * Ask if the game is complete.
     *
     * @return true if the game is complete
     */
    public final boolean isDone() {
        return done;
    }

    /**
     * Return the winner of this game.
     *
     * @return the winner for this game
     */
    public final Piece.Side getWinner() {
        return winner;
    }

    /**
     * Set the Game's current status message.
     *
     * @param message  new status message
     */
    public final void setStatus(final String message) {
        LOG.info( message);
        if (message == null) {
            throw new IllegalArgumentException();
        }
        status = message;
        callGameListeners(GameEvent.STATUS);
    }

    /**
     * Get the Game's current status message.
     *
     * @return the current status message
     */
    public final String getStatus() {
        return status;
    }

    public final void setHumanPlayerName(String name) {
        humanPlayerName = name;
    }
}

