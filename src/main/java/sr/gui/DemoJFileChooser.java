package sr.gui;


    import uk.co.caprica.vlcjplayer.view.util.AlertBox;

    import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

    import static sr.utility.Output.debug;


public class DemoJFileChooser extends JPanel
        implements ActionListener {
    JButton go;

    JFileChooser chooser;
    String choosertitle;

    public DemoJFileChooser() {
        go = new JButton("Do it");
        go.addActionListener(this);
        add(go);
    }

    public void actionPerformed(ActionEvent e) {
        chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle(choosertitle);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        //
        // disable the "All files" option.
        //
        chooser.setAcceptAllFileFilterUsed(false);
        //
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            debug("getCurrentDirectory(): "
                    + chooser.getCurrentDirectory());
            debug("getSelectedFile() : "
                    + chooser.getSelectedFile());
        } else {
            System.out.println("No Selection ");
            AlertBox.warningBox("No folder Selected","No folder Selected");
        }
        debug("getCurrentDirectory(): "
                + chooser.getCurrentDirectory());
        debug("getSelectedFile() : "
                + chooser.getSelectedFile());
    }

    public Dimension getPreferredSize() {
        return new Dimension(200, 200);
    }

    public static void main(String s[]) {
        JFrame frame = new JFrame("");
        DemoJFileChooser panel = new DemoJFileChooser();
        frame.addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                }
        );
        frame.getContentPane().add(panel, "Center");
        frame.setSize(panel.getPreferredSize());
        frame.setVisible(true);
    }
}
