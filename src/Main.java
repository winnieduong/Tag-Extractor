import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class Main extends JFrame {
    private JTextArea textArea;
    private TagExtractor tagExtractor;

    public Main() {
        setTitle("Tag Extractor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton selectFileButton = new JButton("Select Text File");
        JButton selectStopWordsButton = new JButton("Select Stop Words File");
        JButton extractTagsButton = new JButton("Extract Tags");
        JButton saveTagsButton = new JButton("Save Tags");

        buttonPanel.add(selectFileButton);
        buttonPanel.add(selectStopWordsButton);
        buttonPanel.add(extractTagsButton);
        buttonPanel.add(saveTagsButton);
        add(buttonPanel, BorderLayout.SOUTH);

        tagExtractor = new TagExtractor();

        selectFileButton.addActionListener(e -> selectFile("text"));
        selectStopWordsButton.addActionListener(e -> selectFile("stopwords"));
        extractTagsButton.addActionListener(e -> extractTags());
        saveTagsButton.addActionListener(e -> saveTags());
    }

    private void selectFile(String type) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            if (type.equals("text")) {
                tagExtractor.setInputFile(selectedFile);
                textArea.append("Selected text file: " + selectedFile.getName() + "\n");
            } else if (type.equals("stopwords")) {
                tagExtractor.setStopWordsFile(selectedFile);
                textArea.append("Selected stop words file: " + selectedFile.getName() + "\n");
            }
        }
    }

    private void extractTags() {
        if (tagExtractor.getInputFile() == null || tagExtractor.getStopWordsFile() == null) {
            JOptionPane.showMessageDialog(this, "Please select both a text file and a stop words file.");
            return;
        }
        String result = tagExtractor.extractTags();
        textArea.setText(result);
    }

    private void saveTags() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            boolean success = tagExtractor.saveTagsToFile(saveFile);
            if (success) {
                JOptionPane.showMessageDialog(this, "Tags saved successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save tags.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main mainFrame = new Main();
            mainFrame.setVisible(true);
        });
    }
}
