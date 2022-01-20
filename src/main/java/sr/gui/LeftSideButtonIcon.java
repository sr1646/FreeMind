package sr.gui;

import javax.swing.*;
import java.awt.*;

public class LeftSideButtonIcon {
    public LeftSideButtonIcon()
    {
        JFrame frame = new JFrame("Button");
        JPanel panel = new JPanel();
        JButton button = new JButton("Add Folder");
        button.setPreferredSize(new Dimension(200, 30));
        button.setIcon(newIcon("addFavouriteFolder"));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        panel.add(button);
        frame.add(panel);
        frame.setVisible(true);
    }
    private Icon newIcon(String name) {
        return new ImageIcon(getClass().getResource("/icons/buttons/" + name + ".png"));
    }
    public static void main(String[] args)
    {
        new LeftSideButtonIcon();
    }
}
