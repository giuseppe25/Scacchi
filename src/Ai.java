package src;

import java.util.Iterator;
import java.util.Random;
import java.util.logging.Logger;

public class Ai implements Player {


    private static final int DEFENSE_PERCENTAGE = 70;
    private static final int ATTACK_PERCENTAGE = 70;

    private Piece.Side currentSide;

    private final Game game;

    public Ai(Game game) {
        this.game = game;
    }

    @Override
    public Move takeTurn(final Board board, final Piece.Side side) {
        Random random = new Random();

        // true = attacco, false = difesa
        boolean strategy = random.nextBoolean();
        game.setStatus("Strategy: " + (strategy ? "attack" : "defense"));

        currentSide = side;

        MoveList availableMoves = board.allMoves(currentSide, true);


        if (!availableMoves.isEmpty()) {
            if (strategy) {
                return attackMove(availableMoves);
            } else {
                return defenseMove(availableMoves);
            }
        }
        return null;
    }

    private Move defenseMove(MoveList availableMoves) {
        Random random = new Random();

        // 1. Cattura un pezzo dell'avversario se possibile
        for (Move move : availableMoves) {
            if ( !game.getBoard().isFree(move.getDest()) ) {
                return move;
            }
        }

        // 2. Altrimenti, muove un pezzo che non può essere catturato (70%)
        // o muove casualmente (30%)
        if (random.nextInt(100) < DEFENSE_PERCENTAGE) {
            MoveList nonCaptureMoves = getNonCaptureMoves(availableMoves);
            return getRandomMove(nonCaptureMoves);
        } else {
            return getRandomMove(availableMoves);
        }
    }


    private Move attackMove(MoveList availableMoves) {
        Random random = new Random();

        // 1. Cattura un pezzo dell'avversario se possibile
        for (Move move : availableMoves) {
            if ( !game.getBoard().isFree(move.getDest()) ) {
                return move;
            }
        }

        // 2. Altrimenti, muove il pezzo che avvicina maggiormente al re (70%)
        // o alla regina (30%)
        if (random.nextInt(100) < ATTACK_PERCENTAGE) {
            return getKingProximityMove(availableMoves);
        } else {
            return getQueenProximityMove(availableMoves);
        }
    }




    private Move getKingProximityMove(MoveList moves) {
        Position enemyKing = game.getBoard().findKing(currentSide.opposite());

        if (enemyKing == null) {
            return null;
        }

        int minDistance = Integer.MAX_VALUE;
        Move selectedMove = null;

        for (Move move : moves) {
            int distance = calculateDistance(move.getDest(), enemyKing);

            if (distance < minDistance) {
                minDistance = distance;
                selectedMove = move;
            }
        }

        return selectedMove;
    }

    private Move getQueenProximityMove(MoveList moves) {
        Position enemyQueen = game.getBoard().findQueen(currentSide.opposite());

        if (enemyQueen == null) {
            return null;
        }

        int minDistance = Integer.MAX_VALUE;
        Move selectedMove = null;

        for (Move move : moves) {

            int distance = calculateDistance(move.getDest(), enemyQueen);

            if (distance < minDistance) {
                minDistance = distance;
                selectedMove = move;
            }
        }

        return selectedMove;
    }


    private static int calculateDistance(Position pos1, Position pos2) {
        int deltaX = Math.abs(pos1.getX() - pos2.getX());
        int deltaY = Math.abs(pos1.getY() - pos2.getY());
        return Math.max(deltaX, deltaY);
    }


    private MoveList getNonCaptureMoves(MoveList moves) {
        MoveList nonCaptureMoves = new MoveList(game.getBoard());
        for (Move move : moves) {
            if ( game.getBoard().isFree(move.getDest()) ) {
                nonCaptureMoves.add(move);
            }
        }
        return nonCaptureMoves;
    }

    private Move getRandomMove(MoveList moves) {
        Random random = new Random();

        Iterator<Move> moveIterator = moves.iterator();

        int randomIndex = random.nextInt(moves.size());

        for (int i = 0; i < randomIndex; i++) {
            moveIterator.next();
        }
        return moveIterator.next();
    }
}

