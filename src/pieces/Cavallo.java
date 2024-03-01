package src.pieces;

import src.Move;
import src.MoveList;
import src.Piece;
import src.Position;

/**
 * The Chess knight.
 *
 * This class describes the movement and capture behavior of the Chess
 * knight.
 */
public class Cavallo extends Piece {


    /**
     * Short segment of movement.
     */
    static final int NEAR = 1;

    /**
     * Long segment of movement.
     */
    static final int FAR = 2;

    /**
     * Create a new knight on the given side.
     *
     * @param side piece owner
     */
    public Cavallo(final Side side) {
        super(side, "Cavallo");
    }

    @Override
    public final MoveList getMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        list = getMoves(this, list);
        return list;
    }

    /**
     * Determine knight moves for given situation.
     *
     * This method is here for the purposes of reuse.
     *
     * @param p     the piece being tested
     * @param list  list to be appended to
     * @return      the modified list
     */
    public static MoveList getMoves(final Piece p,
                                    final MoveList list) {
        Position pos = p.getPosition();
        list.addCapture(new Move(pos, new Position(pos,  NEAR,  FAR)));
        list.addCapture(new Move(pos, new Position(pos,  FAR,  NEAR)));
        list.addCapture(new Move(pos, new Position(pos, -FAR,  NEAR)));
        list.addCapture(new Move(pos, new Position(pos, -FAR, -NEAR)));
        list.addCapture(new Move(pos, new Position(pos,  FAR, -NEAR)));
        list.addCapture(new Move(pos, new Position(pos,  NEAR, -FAR)));
        list.addCapture(new Move(pos, new Position(pos, -NEAR, -FAR)));
        list.addCapture(new Move(pos, new Position(pos, -NEAR,  FAR)));
        return list;
    }
}
