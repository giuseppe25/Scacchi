package src.pieces;

import src.Piece;

/**
 * Creates pieces based on their name strings.
 */
public final class PieceFactory {

    /** Hidden constructor. */
    private PieceFactory() {
    }

    /**
     * Create a new piece by name.
     *
     * @param name name of the piece
     * @param side side for the new piece
     * @return the new piece
     */
    public static Piece create(final String name, final Piece.Side side) {
        if ("Regina".equals(name)) {
            return new Regina(side);
        } else {
            /* Maybe throw an exception here? */
            return null;
        }
    }
}
