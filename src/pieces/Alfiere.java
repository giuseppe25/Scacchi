package src.pieces;

import src.Move;
import src.MoveList;
import src.Piece;
import src.Position;

/**
 * The Chess bishop.
 *
 * This class describes the movement and capture behavior of the Chess
 * bishop.
 */
public class Alfiere extends Piece {


    /**
     * Create bishop with given side.
     *
     * @param side piece side
     */
    public Alfiere(final Side side) {
        super(side, "Alfiere");
    }

    @Override
    public final MoveList getMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        list = getMoves(this, list);
        return list;
    }

    /**
     * Determine bishop moves for given situation.
     *
     * This method is here for the purposes of reuse.
     *
     * @param p     the piece being tested
     * @param list  list to be appended to
     * @return      the modified list
     */
    public static final MoveList getMoves(final Piece p,
                                          final MoveList list) {
        /* Scan each direction and stop looking when we run into something. */
        Position home = p.getPosition();
        int x = home.getX();
        int y = home.getY();
        while (x >= 0 && y >= 0) {
            x--;
            y--;
            Position pos = new Position(x, y);
            if (!list.addCapture(new Move(home, pos))) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        x = home.getX();
        y = home.getY();
        while (x < 8 &&
               y < 8) {

            x++;
            y++;
            Position pos = new Position(x, y);
            if (!list.addCapture(new Move(home, pos))) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        x = home.getX();
        y = home.getY();
        while (x >= 0 && y < 8) {
            x--;
            y++;
            Position pos = new Position(x, y);
            if (!list.addCapture(new Move(home, pos))) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        x = home.getX();
        y = home.getY();
        while (x < 8 && y >= 0) {
            x++;
            y--;
            Position pos = new Position(x, y);
            if (!list.addCapture(new Move(home, pos))) {
                break;
            }
            if (!p.getBoard().isFree(pos)) {
                break;
            }
        }
        return list;
    }
}
