package src.pieces;

import src.MoveList;
import src.Piece;

/**
 * The Chess queen.
 *
 * This class describes the movement and capture behavior of the Chess
 * queen.
 */
public class Regina extends Piece {

    /**
     * Create a new queen on the given side.
     *
     * @param side piece owner
     */
    public Regina(final Side side) {
        super(side, "Regina");
    }

    @Override
    public final MoveList getMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        /* Take advantage of the Alfiere and Torre implementations. */
        list = Torre.getMoves(this, list);
        list = Alfiere.getMoves(this, list);
        return list;
    }
}
