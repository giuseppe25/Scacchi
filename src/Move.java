package src;

import java.io.Serial;
import java.io.Serializable;

/**
 * Rappresenta una singola mossa su una scacchiera.
 *
 * Questa è in realtà una lista concatenata in grado di descrivere diversi movimenti
 * azioni simultanee che compongono un singolo turno (arrocco, per esempio).
 *
 * Se la posizione di destinazione è nulla significa rimuovere il pezzo inserito
 * nella posizione di origine.
 */
public final class Move implements Serializable {

    @Serial
    private static final long serialVersionUID = 244767996302362607L;
    /** Originating position. */
    private final Position origin;

    /** Destination position. */
    private final Position destination;

    /** Linked list entry for next part of this move. */
    private Move next;

    /** Piece that was capture: used for undoing a move.  */
    private Piece captured;

    /** New piece to make. */
    private String replacement;

    /** Side of the new piece to make. */
    private Piece.Side replacementSide;

    /**
     * Create a new move to move a piece from one position to another.
     *
     * @param orig origin position
     * @param dest destination position
     */
    public Move(final Position orig, final Position dest) {
        destination = dest;
        origin = orig;
        next = null;
    }

    /**
     * Create a Move from an existing Move.
     *
     * @param move move to copy
     */
    public Move(final Move move) {
        this(move.getOrigin(), move.getDest());
        captured = move.getCaptured();
        replacement = move.getReplacement();
        replacementSide = move.getReplacementSide();
        if (move.getNext() != null) {
            next = new Move(move.getNext());
        }
    }

    /**
     * Set the next movement action for this move.
     *
     * @param move the next move
     */
    public void setNext(final Move move) {
        next = move;
    }

    /**
     * Get the next movement action for this move.
     *
     * @return the next move
     */
    public Move getNext() {
        return next;
    }

    /**
     * Get the origin position.
     *
     * @return origin position
     */
    public Position getOrigin() {
        return origin;
    }

    /**
     * Get the destination position.
     *
     * @return destination position
     */
    public Position getDest() {
        return destination;
    }

    /**
     * Set the piece that was captured by this move.
     *
     * @param p piece that was captured
     */
    public void setCaptured(final Piece p) {
        captured = p;
    }

    /**
     * Set the piece that was captured by this move.
     *
     * @return piece that was captured
     */
    public Piece getCaptured() {
        return captured;
    }

    /**
     * Set the piece that will be created.
     *
     * @param pieceName piece to be created
     */
    public void setReplacement(final String pieceName) {
        replacement = pieceName;
    }

    /**
     * Get the name of the piece that will be created.
     *
     * @return piece to be created
     */
    public String getReplacement() {
        return replacement;
    }

    /**
     * Set the side of piece that will be created.
     *
     * @param side piece to be created
     */
    public void setReplacementSide(final Piece.Side side) {
        replacementSide = side;
    }

    /**
     * Get the side of the piece that will be created.
     *
     * @return side of piece to be created
     */
    public Piece.Side getReplacementSide() {
        return replacementSide;
    }

    @Override
    public String toString() {
        return "" + origin + destination;
    }
}

