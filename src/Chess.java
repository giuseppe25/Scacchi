package src;

import src.gui.ChessFrame;
import src.gui.StartPanel;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main class for the Chess game application.
 */
public final class Chess {

    /** This class's Logger. */
    private static final Logger LOG =
        Logger.getLogger("src.Chess");

    /** The program's running title, prefix only. */
    private static final String TITLE_PREFIX = "Chess";

    /**
     * Hidden constructor.
     */
    private Chess() {}

    /**
     * The main method of the Chess game application.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) {
        try {
            String lnf = UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(lnf);
        } catch (IllegalAccessException e) {
            LOG.warning("Failed to access 'Look and Feel'");
        } catch (InstantiationException e) {
            LOG.warning("Failed to instantiate 'Look and Feel'");
        } catch (ClassNotFoundException e) {
            LOG.warning("Failed to find 'Look and Feel'");
        } catch (UnsupportedLookAndFeelException e) {
            LOG.warning("Failed to set 'Look and Feel'");
        }
       new StartPanel();

    }

    /**
     * Returns the full title for the program, including version number.
     *
     * @return the title of the program
     */
    public static String getTitle() {
        String version = "";
        try {
            InputStream s = Chess.class.getResourceAsStream("/version.txt");
            Reader isr = new InputStreamReader(s, StandardCharsets.UTF_8);
            BufferedReader in = new BufferedReader(isr);
            version = in.readLine();
            in.close();
        } catch (Exception e) {
            LOG.warning("failed to read version info");
            version = "";
        }
        return TITLE_PREFIX + " " + version;
    }
}
