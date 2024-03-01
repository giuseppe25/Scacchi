package src.gui;

import src.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.util.Iterator;
import java.util.Observable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
public class Info extends JPanel {
    private final MoveList moveList;

    public Info(Board board) {
        this.moveList = board.allMoves(Piece.Side.WHITE, false);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        String[] columnNames = {"Move", "Origin", "Destination"};

        Object[][] data = createTableData();

        JTable table = new JTable(data, columnNames);

        // Aggiungi la tabella a uno JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);
    }

    private Object[][] createTableData() {
        int moveCount = moveList.size();
        Object[][] data = new Object[moveCount][3];

        Iterator<Move> moveIterator = moveList.iterator();

        int rowIndex = 0;
        while (moveIterator.hasNext()) {
            Move move = moveIterator.next();

            data[rowIndex][0] = (rowIndex + 1);
            data[rowIndex][1] = move.getOrigin();
            data[rowIndex][2] = move.getDest();

            rowIndex++;
        }

        return data;
    }
    public void componentShown(ComponentEvent e) {
        // Imposta la larghezza preferita delle colonne dopo che la tabella è stata visualizzata
        JTable table = (JTable)((JScrollPane)getComponent(0)).getViewport().getView();
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);  // Larghezza della colonna "Move"
        columnModel.getColumn(1).setPreferredWidth(80);  // Larghezza della colonna "Origin"
        columnModel.getColumn(2).setPreferredWidth(80);  // Larghezza della colonna "Destination"
    }
}

