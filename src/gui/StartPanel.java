package src.gui;

import src.data.Scoreboard;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class StartPanel extends JFrame {


    private static JTable scoreboardTable;
    private static JFrame scoreboardFrame;
    private static DefaultTableModel tableModel;
    private static JTextField nameField;

    private static Point mainMenuLocation;
    private static JFrame mainFrame ;



    public StartPanel() {

        mainFrame  = new JFrame("Swing Menu Example");
        mainFrame .setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame .setSize(300, 350);
        mainFrame.setLocationRelativeTo(null);


        // Pannello per i pulsanti al centro
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(4, 1, 10, 10)); // Layout a griglia 4 righe, 1 colonna

        JButton startButton = new JButton("Start");
        JButton loadButton = new JButton("Load");
        JButton scoreboardButton = new JButton("Scoreboard");
        JButton exitButton = new JButton("Exit");

        // Aggiungi azioni ai pulsanti
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ChessFrame();
                mainFrame .dispose();

            }
        });

        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(mainFrame , "Load selected");
            }
        });

        scoreboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainMenuLocation = mainFrame.getLocationOnScreen();
                // Visualizza la finestra della scoreboard
                showScoreboardFrame(mainMenuLocation);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        // Aggiungi pulsanti al pannello
        centerPanel.add(startButton);
        centerPanel.add(loadButton);
        centerPanel.add(scoreboardButton);
        centerPanel.add(exitButton);

        // Imposta la cornice attorno al pannello
        centerPanel.setBorder(BorderFactory.createEmptyBorder(50, 90, 80, 90)); // Margine 10px

        // Aggiungi il pannello al centro del frame
        mainFrame.add(centerPanel, BorderLayout.CENTER);

        mainFrame.setVisible(true);


    }

    private void showScoreboardFrame(Point location) {

        tableModel = new DefaultTableModel(new Object[]{"Player", "Points"}, 0);
        scoreboardTable = new JTable(tableModel);



        // Creazione della finestra della scoreboard con una tabella
        scoreboardFrame = new JFrame("Scoreboard");
        scoreboardFrame.setSize(400, 300);


        // Creazione del pannello per il form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new FlowLayout());

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField(15);
        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back to Menu");

        loadAndShowScoreboard();


        // Aggiungi azione al pulsante di invio
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String playerName = nameField.getText().trim();

                if (!playerName.isEmpty()) {
                    // Aggiungi il giocatore alla scoreboard
                    Scoreboard.getInstance().addToPlayer(playerName,false);

                    // Pulisci il campo del nome dopo l'inserimento
                    nameField.setText("");

                    // Reload and display the updated scoreboard
                    loadAndShowScoreboard();

                    // Chiudi la finestra corrente
                    scoreboardFrame.dispose();
                }


                // Chiudi la finestra corrente
                scoreboardFrame.dispose();
                mainFrame.dispose();


                new ChessFrame();



            }
        });


        // Aggiungi azione al pulsante "Back to Menu"
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Chiudi la finestra della scoreboard
                scoreboardFrame.dispose();
                if (mainMenuLocation != null) {
                    scoreboardFrame.setLocation(mainMenuLocation);


                }

            }
        });

        // Aggiungi componenti al pannello del form
        formPanel.add(nameLabel);
        formPanel.add(nameField);
        formPanel.add(submitButton);
        formPanel.add(backButton);
        scoreboardFrame.setLocation(location);


        // Aggiungi la tabella e il pannello del form alla finestra
        scoreboardFrame.add(new JScrollPane(scoreboardTable), BorderLayout.CENTER);
        scoreboardFrame.add(formPanel, BorderLayout.SOUTH);

        scoreboardFrame.setVisible(true);
    }


    private void loadAndShowScoreboard() {

        Scoreboard.getInstance().loadScoreboard();

        // Clear the existing rows in the table
        tableModel.setRowCount(0);

        // Fetch scores from Scoreboard
        List<Scoreboard.Score> scores = Scoreboard.getInstance().getScores();

        // Update the tableModel with fetched scores
        for (Scoreboard.Score score : scores) {
            Object[] rowData = {score.getPlayerName(), score.getGamesWon()};
            tableModel.addRow(rowData);
        }
    }


}
