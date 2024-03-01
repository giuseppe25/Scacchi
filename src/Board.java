package src;

import src.pieces.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * Board data structure.
 *
 * After the initial setup, the board <i>must</i> only be modified
 * through move transaction. This allows undo() and copy(), which many
 * other things depends on, to work properly.
 */
public class Board implements Serializable {

    @Serial
    private static final long serialVersionUID = 244162996302362607L;


    /** The internal board array. */
    private Piece[][] board;


    /** Moves taken in this game so far. */
    private final MoveList moves = new MoveList(this);

    /**
     * The standard chess board.
     */
    public Board() {
        clear();
        for (int x = 0; x < 8; x++) {
            setPiece(x, 1, new Pedone(Piece.Side.WHITE));
            setPiece(x, 6, new Pedone(Piece.Side.BLACK));
        }
        setPiece(0, 0, new Torre(Piece.Side.WHITE));
        setPiece(7, 0, new Torre(Piece.Side.WHITE));
        setPiece(0, 7, new Torre(Piece.Side.BLACK));
        setPiece(7, 7, new Torre(Piece.Side.BLACK));
        setPiece(1, 0, new Cavallo(Piece.Side.WHITE));
        setPiece(6, 0, new Cavallo(Piece.Side.WHITE));
        setPiece(1, 7, new Cavallo(Piece.Side.BLACK));
        setPiece(6, 7, new Cavallo(Piece.Side.BLACK));
        setPiece(2, 0, new Alfiere(Piece.Side.WHITE));
        setPiece(5, 0, new Alfiere(Piece.Side.WHITE));
        setPiece(2, 7, new Alfiere(Piece.Side.BLACK));
        setPiece(5, 7, new Alfiere(Piece.Side.BLACK));
        setPiece(3, 0, new Regina(Piece.Side.WHITE));
        setPiece(3, 7, new Regina(Piece.Side.BLACK));
        setPiece(4, 0, new Re(Piece.Side.WHITE));
        setPiece(4, 7, new Re(Piece.Side.BLACK));
    }


    public final Boolean checkmate(final Piece.Side side) {
        return check(side) && (moveCount(side) == 0);
    }


    public final Boolean stalemate(final Piece.Side side) {
        return (!check(side)) && (moveCount(side) == 0);
    }


    /**
     * Determine if board is in a state of check.
     *
     * @param side side to check for check
     * @return     true if board is in a state of check
     */
    public final Boolean check(final Piece.Side side) {
        Piece.Side attacker;
        if (side == Piece.Side.WHITE) {
            attacker = Piece.Side.BLACK;
        } else {
            attacker = Piece.Side.WHITE;
        }
        Position kingPos = findKing(side);
        if (kingPos == null) {
            /* no king on board, but can happen in AI evaluation */
            return false;
        }
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece p = getPiece(new Position(x, y));
                if ((p != null) &&
                        (p.getSide() == attacker) &&
                        p.getMoves(false).containsDest(kingPos)) {

                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Find the king belonging to the given side.
     *
     * @param side whose king
     * @return     the king's board position
     */
    public final Position findKing(final Piece.Side side) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Position pos = new Position(x, y);
                Piece p = getPiece(pos);
                if (p instanceof Re &&
                    p.getSide() == side) {

                    return pos;
                }
            }
        }
        return null;
    }

    /**
     * Find the Regina belonging to the given side.
     *
     * @param side whose king
     * @return     the king's board position
     */
    public final Position findQueen(final Piece.Side side) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Position pos = new Position(x, y);
                Piece p = getPiece(pos);
                if (p instanceof Regina &&
                        p.getSide() == side) {

                    return pos;
                }
            }
        }
        return null;
    }

    /**
     * Number of moves available to this player.
     *
     * @param side side to be tested
     * @return     number of moves right now
     */
    public final int moveCount(final Piece.Side side) {
        int count = 0;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece p = getPiece(new Position(x, y));
                if ((p != null) && (p.getSide() == side)) {
                    count += p.getMoves(true).size();
                }
            }
        }
        return count;
    }

    /**
     * Create a new Piece array, effectively clearing the board.
     */
    public final void clear() {
        board = new Piece[8][8];
    }

    /**
     * Put the given Piece at the given position on the board.
     *
     * @param x horizontal part of the position
     * @param y vertical part of the position
     * @param p the piece object to be placed
     */
    public final void setPiece(final int x, final int y, final Piece p) {
        setPiece(new Position(x, y), p);
    }

    /**
     * Put the given Piece at the given Position on the board.
     *
     * @param pos the position on the board
     * @param p   the piece object to be placed
     */
    public final void setPiece(final Position pos, final Piece p) {
        board[pos.getX()][pos.getY()] = p;
        if (p != null) {
            p.setPosition(pos);
            p.setBoard(this);
        }
    }
    /**
     * Copy this board.
     *
     * @return deep copy of the board.
     */
    public final Board copy() {
        Board fresh = new Board();
        for (Move move : moves) {
            fresh.move(new Move(move));
        }
        return fresh;
    }
    /**
     * Get the Piece at the given Position.
     *
     * @param pos the position on the board
     * @return    the Piece at the position
     */
    public final Piece getPiece(final Position pos) {
        return board[pos.getX()][pos.getY()];
    }

    /**
     * Perform the given move action.
     *
     * @param move the move
     */
    public final void move(final Move move) {
        moves.add(move);
        execMove(move);
    }

    /**
     * Actually execute the move.
     *
     * @param move the move
     */
    private void execMove(final Move move) {
        if (move == null) {
            return;
        }
        Position a = move.getOrigin();
        Position b = move.getDest();
        if (a != null && b != null) {
            move.setCaptured(getPiece(b));
            setPiece(b, getPiece(a));
            setPiece(a, null);
            getPiece(b).setPosition(b);
            getPiece(b).incMoved();
        } else if (a != null && b == null) {
            move.setCaptured(getPiece(a));
            setPiece(a, null);
        } else {
            setPiece(b, PieceFactory.create(move.getReplacement(),
                                            move.getReplacementSide()));
        }
        execMove(move.getNext());
    }

    /**
     * Undo the last move.
     */
    public final void undo() {
        execUndo(moves.pop());
    }

    /**
     * Actually perform the undo action.
     *
     * @param move the move
     */
    private void execUndo(final Move move) {
        if (move == null) {
            return;
        }
        execUndo(move.getNext()); // undo in reverse
        Position a = move.getOrigin();
        Position b = move.getDest();
        if (a != null && b != null) {
            setPiece(a, getPiece(b));
            setPiece(b, move.getCaptured());
            getPiece(a).setPosition(a);
            getPiece(a).decMoved();
        } else if (a != null && b == null) {
            setPiece(a, move.getCaptured());
        } else {
            setPiece(b, null);
        }
    }

    /**
     * Return the last move made.
     *
     * @return the previous move
     */
    public final Move last() {
        return moves.peek();
    }

    /**
     * Return true if position has no piece on it.
     *
     * @param pos position to be tested
     * @return    emptiness of position
     */
    public final Boolean isEmpty(final Position pos) {
        return getPiece(pos) == null;
    }

    /**
     * Return true if position has no piece of same side on it.
     *
     * @param pos  position to be tested
     * @param side side of the piece wanting to move
     * @return    emptiness of position
     */
    public final Boolean isEmpty(final Position pos, final Piece.Side side) {
        Piece p = getPiece(pos);
        if (p == null) {
            return true;
        }
        return p.getSide() != side;
    }

    /**
     * Return true if position is on the board.
     *
     * @param pos position to be tested
     * @return    validity of position
     */
    public final Boolean inRange(final Position pos) {
        return (pos.getX() >= 0) && (pos.getY() >= 0) &&
               (pos.getX() < 8) && (pos.getY() < 8);
    }

    /**
     * Return true if position is in range <i>and</i> empty.
     *
     * @param pos position to be tested
     * @return    validity of position
     */
    public final Boolean isFree(final Position pos) {
        return inRange(pos) && isEmpty(pos);
    }

    /**
     * Return true if position is in range and empty of given side.
     *
     * @param pos  position to be tested
     * @param side side of the piece wanting to move
     * @return     validity of position
     */
    public final Boolean isFree(final Position pos, final Piece.Side side) {
        return inRange(pos) && isEmpty(pos, side);
    }

    /**
     * Generate a list of all moves for the given side.
     *
     * @param side  side to get moves for
     * @param check check for check
     * @return      list of all moves
     */
    public final MoveList allMoves(final Piece.Side side, final boolean check) {
        MoveList list = new MoveList(this, false);
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Piece p = board[x][y];
                if (p != null && p.getSide() == side) {
                    list.addAll(p.getMoves(check));
                }
            }
        }
        return list;
    }

    /**
     * Return the number of moves taken on this board.
     *
     * @return number of moves taken on this board
     */
    public final int moveCount() {
        return moves.size();
    }
}
