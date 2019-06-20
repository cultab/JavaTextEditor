package TextEditor;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_CANCEL_OPTION;

public class MainFrame extends JFrame{

    private JTextArea editTextArea;
    private JMenuBar menuBar;
    private JMenu menuFile, menuEdit, menuView, menuExit;
    private JMenuItem newFileMenuItem, openMenuItem, saveMenuItem,
            copyMenuItem, clearMenuItem, statsMenuItem, exitMenuItem;

    private JPanel panel;
    private JButton newFileBtn, openBtn, saveBtn, copyBtn, clearBtn, statsBtn, exitBtn;

    private JTextField filepath;
    private JLabel unsavedNotif;

    private boolean fileSaved = true;
    private boolean fileExists = false;
    private StatsFrame statsDialog;

    public MainFrame() {
        createUI();
        addListeners();

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setSize(800,800);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void addListeners() {

        ActionListener actionListener;

        //New file
        actionListener = actionEvent -> {
            filepath.setText("New file");
            editTextArea.setText(null);
            setFileSavedStatus(true);
            fileExists = false;
        };

        newFileMenuItem.addActionListener(actionListener);
        newFileBtn.addActionListener(actionListener);

        //Open file
        actionListener = actionEvent -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                File file;
                file = fileChooser.getSelectedFile();

                filepath.setText(file.getAbsolutePath());

                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new FileReader(file));
                    String line;
                    editTextArea.setText(null);
                    while ((line = reader.readLine()) != null) {
                        editTextArea.append(line);
                        editTextArea.append("\n");
                    }
                    setFileSavedStatus(true);
                    fileExists = true;
                } catch (FileNotFoundException e) {
                    JOptionPane.showMessageDialog(MainFrame.this, "This file does not exist!", "Error:", JOptionPane.ERROR_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(MainFrame.this, e.getMessage(), "Error:", JOptionPane.ERROR_MESSAGE);
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            JOptionPane.showMessageDialog(MainFrame.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        };

        openMenuItem.addActionListener(actionListener);
        openBtn.addActionListener(actionListener);

        //Save file
        actionListener = actionEvent -> {
            saveFile(false);
        };

        saveMenuItem.addActionListener(actionListener);
        saveBtn.addActionListener(actionListener);

        //Save as
        actionListener = actionEvent -> {
            saveFile(true);
        };

        copyMenuItem.addActionListener(actionListener);
        copyBtn.addActionListener(actionListener);

        //Clear text
        actionListener = actionEvent -> {
            editTextArea.setText(null);
            setFileSavedStatus(false);
        };

        clearMenuItem.addActionListener(actionListener);
        clearBtn.addActionListener(actionListener);

        //Statistics
        actionListener = (ActionEvent actionEvent) -> {
            statsDialog = new StatsFrame(editTextArea.getText(), MainFrame.this);
        };
        statsMenuItem.addActionListener(actionListener);
        statsBtn.addActionListener(actionListener);

        //Exit
        actionListener = (ActionEvent actionEvent) -> {
            exit();
        };

        exitMenuItem.addActionListener(actionListener);
        exitBtn.addActionListener(actionListener);

        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {

            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                exit();
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {

            }

            @Override
            public void windowIconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowActivated(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {

            }
        });

        DocumentListener documentListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent documentEvent) {
                setFileSavedStatus(false);
            }

            @Override
            public void removeUpdate(DocumentEvent documentEvent) {
                setFileSavedStatus(false);
            }

            @Override
            public void changedUpdate(DocumentEvent documentEvent) {
                setFileSavedStatus(false);
            }
        };

        editTextArea.getDocument().addDocumentListener(documentListener);
    }

    private void setFileSavedStatus(boolean b) {
        fileSaved = b;
        if (b) {
            unsavedNotif.setText(null);
        } else {
            unsavedNotif.setText("Unsaved changes! ");
        }
    }

    private void exit() {
        if (fileSaved) {
            Object[] options = {"Yes", "No", "Cancel"};
            int res = JOptionPane.showOptionDialog(MainFrame.this, "Are you sure you want to exit?", "Exit?", YES_NO_CANCEL_OPTION, WARNING_MESSAGE, null, options, options[2]);
            if (res == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else {
            Object[] options = {"Save", "Don't Save", "Cancel"};
            int res = JOptionPane.showOptionDialog(MainFrame.this, "Save before exiting?", "Unsaved file!", YES_NO_CANCEL_OPTION, WARNING_MESSAGE, null, options, options[2]);
            if (res == JOptionPane.YES_OPTION) {
                saveFile(false);
                System.exit(0);
            } else if (res == JOptionPane.NO_OPTION) {
                //Discard file and exit
                System.exit(0);
            }
        }
    }


    private void saveFile(boolean forceSaveAs)  {
        BufferedWriter writer = null;
        try {
            if (!fileExists | forceSaveAs) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    File file;
                    file = fileChooser.getSelectedFile();

                    filepath.setText(file.getAbsolutePath());

                    writer = new BufferedWriter(new FileWriter(file));
                    editTextArea.write(writer);
                    setFileSavedStatus(true);
                }
            } else {
                try {
                    writer = new BufferedWriter(new FileWriter(filepath.getText()));
                    editTextArea.write(writer);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(MainFrame.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                setFileSavedStatus(true);
            }
            if (writer != null)
                writer.close();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(MainFrame.this, "File not found!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(MainFrame.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void createUI() {
        editTextArea = new JTextArea();

        //Menubar:

        menuBar = new JMenuBar();

        menuFile = new JMenu("File");
        menuEdit = new JMenu("Edit");
        menuView = new JMenu("View");
        menuExit = new JMenu("Exit");

        newFileMenuItem = new JMenuItem("New File");
        openMenuItem = new JMenuItem("Open File");
        saveMenuItem = new JMenuItem("Save");
        copyMenuItem = new JMenuItem("Copy");
        clearMenuItem = new JMenuItem("Clear");
        statsMenuItem = new JMenuItem("Statistics");
        exitMenuItem = new JMenuItem("Exit");


        menuBar.add(menuFile);
        menuBar.add(menuEdit);
        menuBar.add(menuView);
        menuBar.add(menuExit);
        menuBar.add(Box.createHorizontalGlue());
        unsavedNotif = new JLabel("");
        menuBar.add(unsavedNotif);

        menuFile.add(newFileMenuItem);
        menuFile.add(openMenuItem);
        menuFile.addSeparator();
        menuFile.add(saveMenuItem);
        menuFile.add(copyMenuItem);

        menuEdit.add(clearMenuItem);

        menuView.add(statsMenuItem);

        menuExit.add(exitMenuItem);

        //Buttons:

        panel = new JPanel();

        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        newFileBtn = new JButton("New");
        openBtn = new JButton("Open");
        saveBtn = new JButton("Save");
        copyBtn = new JButton("Copy");
        clearBtn = new JButton("Clear");
        statsBtn = new JButton("Stats");
        exitBtn = new JButton("Exit");

        panel.add(newFileBtn);
        panel.add(openBtn);
        panel.add(saveBtn);
        panel.add(copyBtn);
        panel.add(clearBtn);
        panel.add(statsBtn);
        panel.add(exitBtn);

        filepath = new JTextField("New file");
        filepath.setEditable(false);

        this.setTitle("Java Text Editor");
        this.setLayout(new BorderLayout()); //by default for JFrames but ok
        this.setJMenuBar(menuBar);
        this.add(editTextArea, BorderLayout.CENTER);
        this.add(panel, BorderLayout.SOUTH);
        this.add(filepath, BorderLayout.NORTH);
    }


}
