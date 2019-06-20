package TextEditor;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

import static javax.swing.BoxLayout.PAGE_AXIS;

public class StatsFrame extends JFrame {

    JPanel wordCountPanel, charCountPanel, paragCountPanel, fileSizePanel;
    JLabel wordCountLabel, charCountLabel, paragCountLabel, fileSizeLabel;



    public StatsFrame(String fileContents, JFrame root) {
        createUI(root);

        getStats(fileContents);
    }

    private void getStats(String fileContents) {
        Scanner scanner = new Scanner(fileContents);
        int words = 0, chars = 0, parags = 0, fileSize;
        String line = null, prevLine;

        scanner.useDelimiter("[,.;:()\\[\\]{} \t\n'\"]+");
        while (scanner.hasNext()) {
            scanner.next();
            words++;
        }

        chars = fileContents.length();
        scanner = new Scanner(fileContents);
        scanner.useDelimiter("[\n]{2,}"); //a sequence of empty lines counts as one paragraph
        while (scanner.hasNext()) {
            scanner.next();
            parags++;
        }

        wordCountLabel.setText(String.valueOf(words));
        charCountLabel.setText(String.valueOf(chars));
        paragCountLabel.setText(String.valueOf(parags));
        fileSizeLabel.setText(String.valueOf(chars)+"Bytes"); //plus EOF
    }

    private void createUI(Component root) {
        this.setTitle("File statistics");

        this.setLayout(new BoxLayout(this.getContentPane(), PAGE_AXIS));

        wordCountPanel = new JPanel();
        charCountPanel = new JPanel();
        paragCountPanel = new JPanel();
        fileSizePanel = new JPanel();

        wordCountPanel.setBorder(BorderFactory.createTitledBorder("Words"));
        charCountPanel.setBorder(BorderFactory.createTitledBorder("Characters count"));
        paragCountPanel.setBorder(BorderFactory.createTitledBorder("Paragraphs"));
        fileSizePanel.setBorder(BorderFactory.createTitledBorder("File size"));

        this.add(wordCountPanel);
        this.add(charCountPanel);
        this.add(paragCountPanel);
        this.add(fileSizePanel);

        wordCountLabel = new JLabel("123");
        charCountLabel = new JLabel("3425");
        paragCountLabel = new JLabel("2");
        fileSizeLabel = new JLabel("3Kb");

        wordCountPanel.add(wordCountLabel);
        charCountPanel.add(charCountLabel);
        paragCountPanel.add(paragCountLabel);
        fileSizePanel.add(fileSizeLabel);


        this.setLocationRelativeTo(root);
        this.getLocation().translate(10,0);
        this.setSize(200,250);
        this.setResizable(false);
        this.setVisible(true);
    }
}
