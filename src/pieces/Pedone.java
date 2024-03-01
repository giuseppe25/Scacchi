package src.pieces;

import src.Board;
import src.Move;
import src.MoveList;
import src.Piece;
import src.Position;

/**
 * The Chess pawn.
 *
 * This class describes the movement and capture behavior of the pawn
 * chess piece.
 */
public class Pedone extends Piece {


    /**
     * Create a new pawn on the given side.
     *
     * @param side piece owner
     */
    public Pedone(final Side side) {
        super(side, "Pedone");
    }

    @Override
    public final MoveList getMoves(final boolean check) {
        MoveList list = new MoveList(getBoard(), check);
        Position pos = getPosition();
        Board board = getBoard();

        int dir = direction();
        Position dest = new Position(pos, 0, dir);
        Move first = new Move(pos, dest);
        addUpgrade(first);

        if (list.addMove(first) && !moved()) {
            list.addMove(new Move(pos, new Position(pos, 0, 2 * dir)));
        }

        Move captureLeft = new Move(pos, new Position(pos, -1, dir));
        addUpgrade(captureLeft);
        list.addCaptureOnly(captureLeft);
        Move captureRight = new Move(pos, new Position(pos,  1, dir));
        addUpgrade(captureRight);
        list.addCaptureOnly(captureRight);

        /* check for en passant */
        Move last = board.last();
        if (last != null) {
            Position left = new Position(pos, -1, 0);
            Position right = new Position(pos, 1, 0);
            Position lOrigin = last.getOrigin();
            Position lDest = last.getDest();

            if (left.equals(lDest) &&
                (lOrigin.getX() == lDest.getX()) &&
                (lOrigin.getY() == lDest.getY() + dir * 2) &&
                (board.getPiece(left) instanceof Pedone)) {

                /* en passant to the left */
                Move passant = new Move(pos, new Position(pos, -1, dir));
                passant.setNext(new Move(left, null));
                list.addMove(passant);

            } else if (right.equals(lDest) &&
                       (lOrigin.getX() == lDest.getX()) &&
                       (lOrigin.getY() == lDest.getY() + dir * 2) &&
                       (board.getPiece(right) instanceof Pedone)) {

                /* en passant to the right */
                Move passant = new Move(pos, new Position(pos, 1, dir));
                passant.setNext(new Move(right, null));
                list.addMove(passant);
            }
        }
        return list;
    }

    /**
     * Add the upgrade actions to the given move if needed.
     *
     * @param move the move to be modified
     */
    private void addUpgrade(final Move move) {
        if (move.getDest().getY() != 7) {
            return;
        }
        move.setNext(new Move(move.getDest(), null)); // remove the pawn
        Move upgrade = new Move(null, move.getDest());
        upgrade.setReplacement("Regina");
        upgrade.setReplacementSide(getSide());
        move.getNext().setNext(upgrade);              // add a queen
    }

    /**
     * Determine direction of this pawn's movement.
     *
     * @return direction for this pawn
     */
    private int direction() {
        if (getSide() == Side.WHITE) {
            return 1;
        } else {
            return -1;
        }
    }


}
