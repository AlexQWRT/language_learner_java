import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;
import java.util.Scanner;
import java.util.Vector;

public class Main {

    // settings

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int screenWidth = (int) screenSize.getWidth();
    private int screenHeight = (int) screenSize.getHeight();
    private Font font = new Font("Arial", Font.BOLD, 16);
    private int k = 0;

    // left part

    private JFrame mainFrame;
    private JButton buttonAddNew;
    private JButton buttonEditSelectedOne;
    private JButton buttonDeleteSelected;
    private JButton buttonSelectAll;
    private JButton buttonRefreshList;
    private JButton buttonQuiz;

    // right part

    private JSplitPane splitPane;
    private JScrollPane scrollPane;
    private JList<String> wordsListDisplaying;
    private JList<String> translationListDisplaying;
    private DefaultListModel<String> listModelWords;
    private DefaultListModel<String> listModelTranslation;
    public static Vector<String[]> wordsList;

    // read words from file

    public void readFile() {
        wordsList = new Vector<String[]>();
        try {
            Scanner file = new Scanner(new File("words.txt"));
            int count = Integer.parseInt(file.nextLine());
            for (int i = 0; i < count; i++) {
                String word = file.nextLine();
                wordsList.add(new String[] { word, "" });
                listModelWords.add(i, word);
            }
            for (int i = 0; i < count; i++) {
                String translation = file.nextLine();
                wordsList.elementAt(i)[1] = translation;
                listModelTranslation.add(i, translation);
            }
            splitPane.validate();
            scrollPane.validate();
            file.close();
        } catch (FileNotFoundException ex) {
            JDialog d = new JDialog();
            d.setTitle("Important!");
            d.setLayout(new GridLayout(2, 1));
            d.add(new JLabel("Error! File not found.", JLabel.CENTER));
            d.add(new JLabel("Was created a new file.", JLabel.CENTER));
            d.setBounds(screenWidth / 2 - 100, screenHeight / 2 - 50, 200, 100);
            d.setResizable(false);
            d.setVisible(true);
            try {
                Formatter createNew = new Formatter(new File("words.txt"));
                createNew.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (NumberFormatException ex) {
            JDialog d = new JDialog();
            d.setTitle("Important!");
            d.setLayout(new GridLayout());
            d.add(new JLabel("File was crashed!", JLabel.CENTER));
            d.setBounds(screenWidth / 2 - 100, screenHeight / 2 - 50, 200, 100);
            d.setResizable(false);
            d.setVisible(true);
        }
    }

    // write words in file

    public void writeFile() {
        try {
            Formatter file = new Formatter(new File("words.txt"));
            file.format("%d\n", wordsList.size());
            for (int i = 0; i < wordsList.size(); i++)
                file.format("%s\n", wordsList.elementAt(i)[0]);
            for (int i = 0; i < wordsList.size(); i++)
                file.format("%s\n", wordsList.elementAt(i)[1]);
            file.close();
        } catch (FileNotFoundException ex) {
            JDialog d = new JDialog();
            d.setTitle("Important!");
            d.setLayout(new GridLayout(2, 1));
            d.add(new JLabel("Error! File not found.", JLabel.CENTER));
            d.setBounds(screenWidth / 2 - 100, screenHeight / 2 - 50, 200, 100);
            d.setResizable(false);
            d.setVisible(true);
        }
    }

    // buildin GUI

    public Main() {

        // settings

        int FRAME_HEIGHT = 720, FRAME_WIDTH = 1280;

        // main frame

        mainFrame = new JFrame("Language learner");
        mainFrame.setBounds(screenWidth / 2 - FRAME_WIDTH / 2, screenHeight / 2 - FRAME_HEIGHT / 2, FRAME_WIDTH,
                FRAME_HEIGHT);
        mainFrame.setLayout(new GridBagLayout());
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // main frame constraints

        GridBagConstraints positionSettings = new GridBagConstraints();
        positionSettings.fill = GridBagConstraints.BOTH;
        positionSettings.anchor = GridBagConstraints.CENTER;
        int paddingsx = mainFrame.getHeight() / 72, paddingsy = mainFrame.getWidth() / 128;
        positionSettings.insets = new Insets(paddingsx, paddingsy, paddingsx, paddingsy);
        positionSettings.weightx = 1;
        positionSettings.weighty = 1;

        // button add

        buttonAddNew = new JButton("Add a new word");
        buttonAddNew.setFont(font);
        positionSettings.gridx = 0;
        positionSettings.gridy = 0;
        buttonAddNew.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog win = new JDialog();
                win.setTitle("Adding");
                JPanel p1 = new JPanel(new GridLayout(1, 2, 10, 10));
                JPanel p2 = new JPanel(new GridLayout(1, 2, 10, 10));
                win.setLayout(new GridLayout(3, 1, 10, 10));
                win.setBounds(screenWidth / 2 + 5 - FRAME_WIDTH / 2, screenHeight / 2 + FRAME_HEIGHT / 16,
                        FRAME_WIDTH - 900, FRAME_HEIGHT - 500);
                JLabel wordDescription = new JLabel("Enter the word", JLabel.CENTER);
                wordDescription.setFont(font);
                p1.add(wordDescription);
                JLabel translationDescription = new JLabel("Enter the translation", JLabel.CENTER);
                translationDescription.setFont(font);
                p1.add(translationDescription);
                JTextField word = new JTextField();
                word.setHorizontalAlignment(JTextField.CENTER);
                word.setFont(font);
                p2.add(word);
                JTextField translation = new JTextField();
                translation.setHorizontalAlignment(JTextField.CENTER);
                translation.setFont(font);
                p2.add(translation);
                JButton addNewWordButton = new JButton("Add new word");
                addNewWordButton.setEnabled(false);
                word.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent de) {
                        if (word.getText().isEmpty() || translation.getText().isEmpty())
                            addNewWordButton.setEnabled(false);
                        else
                            addNewWordButton.setEnabled(true);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent de) {
                        if (word.getText().isEmpty() || translation.getText().isEmpty())
                            addNewWordButton.setEnabled(false);
                        else
                            addNewWordButton.setEnabled(true);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent de) {
                        if (word.getText().isEmpty() || translation.getText().isEmpty())
                            addNewWordButton.setEnabled(false);
                        else
                            addNewWordButton.setEnabled(true);
                    }
                });
                translation.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent de) {
                        if (word.getText().isEmpty() || translation.getText().isEmpty())
                            addNewWordButton.setEnabled(false);
                        else
                            addNewWordButton.setEnabled(true);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent de) {
                        if (word.getText().isEmpty() || translation.getText().isEmpty())
                            addNewWordButton.setEnabled(false);
                        else
                            addNewWordButton.setEnabled(true);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent de) {
                        if (word.getText().isEmpty() || translation.getText().isEmpty())
                            addNewWordButton.setEnabled(false);
                        else
                            addNewWordButton.setEnabled(true);
                    }
                });
                addNewWordButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        wordsList.add(new String[] { word.getText(), translation.getText() });
                        listModelWords.clear();
                        listModelTranslation.clear();
                        word.setText("");
                        translation.setText("");
                        writeFile();
                        readFile();
                        buttonQuiz.setEnabled(true);
                    }

                });

                addNewWordButton.setFont(font);
                win.add(p1);
                win.add(p2);
                win.add(addNewWordButton);
                win.setVisible(true);

            }
        });
        mainFrame.add(buttonAddNew, positionSettings);
        listModelWords = new DefaultListModel<String>();
        listModelTranslation = new DefaultListModel<String>();
        wordsListDisplaying = new JList<String>(listModelWords);
        wordsListDisplaying.setFont(font);
        wordsListDisplaying.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        wordsListDisplaying.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                translationListDisplaying.setSelectedIndices(wordsListDisplaying.getSelectedIndices());
                if (wordsListDisplaying.getSelectedIndices().length == 1) {
                    buttonEditSelectedOne.setEnabled(true);
                } else {
                    buttonEditSelectedOne.setEnabled(false);
                }
                if (wordsListDisplaying.getSelectedIndices().length > 0) {
                    buttonDeleteSelected.setEnabled(true);
                } else {
                    buttonDeleteSelected.setEnabled(false);
                }
            }
        });
        translationListDisplaying = new JList<String>(listModelTranslation);
        translationListDisplaying.setFont(font);
        translationListDisplaying.setEnabled(false);
        translationListDisplaying.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, wordsListDisplaying, translationListDisplaying);
        splitPane.setDividerLocation(FRAME_WIDTH / 3);
        scrollPane = new JScrollPane(splitPane);
        positionSettings.ipadx = FRAME_WIDTH / 2;
        positionSettings.gridheight = 12;
        positionSettings.gridx = 1;
        positionSettings.gridy = 0;
        mainFrame.add(scrollPane, positionSettings);

        buttonEditSelectedOne = new JButton("Edit selected one");
        buttonEditSelectedOne.setEnabled(false);
        buttonEditSelectedOne.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JDialog win = new JDialog();
                win.setTitle("Editing");
                JPanel p1 = new JPanel(new GridLayout(1, 2, 10, 10));
                JPanel p2 = new JPanel(new GridLayout(1, 2, 10, 10));
                win.setLayout(new GridLayout(3, 1, 10, 10));
                win.setBounds(screenWidth / 2 + 5 - FRAME_WIDTH / 2, screenHeight / 2 + FRAME_HEIGHT / 16,
                        FRAME_WIDTH - 900, FRAME_HEIGHT - 500);
                JLabel wordDescription = new JLabel("Enter the word", JLabel.CENTER);
                wordDescription.setFont(font);
                p1.add(wordDescription);
                JLabel translationDescription = new JLabel("Enter the translation", JLabel.CENTER);
                translationDescription.setFont(font);
                p1.add(translationDescription);
                JTextField word = new JTextField();
                word.setHorizontalAlignment(JTextField.CENTER);
                word.setText(wordsListDisplaying.getSelectedValue());
                word.setFont(font);
                p2.add(word);
                JTextField translation = new JTextField();
                translation.setHorizontalAlignment(JTextField.CENTER);
                translation.setText(translationListDisplaying.getSelectedValue());
                translation.setFont(font);
                p2.add(translation);
                JButton addNewWordButton = new JButton("Apply editing");

                word.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent de) {
                        if (word.getText().isEmpty() || translation.getText().isEmpty())
                            addNewWordButton.setEnabled(false);
                        else
                            addNewWordButton.setEnabled(true);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent de) {
                        if (word.getText().isEmpty() || translation.getText().isEmpty())
                            addNewWordButton.setEnabled(false);
                        else
                            addNewWordButton.setEnabled(true);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent de) {
                        if (word.getText().isEmpty() || translation.getText().isEmpty())
                            addNewWordButton.setEnabled(false);
                        else
                            addNewWordButton.setEnabled(true);
                    }
                });
                translation.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent de) {
                        if (word.getText().isEmpty() || translation.getText().isEmpty())
                            addNewWordButton.setEnabled(false);
                        else
                            addNewWordButton.setEnabled(true);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent de) {
                        if (word.getText().isEmpty() || translation.getText().isEmpty())
                            addNewWordButton.setEnabled(false);
                        else
                            addNewWordButton.setEnabled(true);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent de) {
                        if (word.getText().isEmpty() || translation.getText().isEmpty())
                            addNewWordButton.setEnabled(false);
                        else
                            addNewWordButton.setEnabled(true);
                    }
                });
                addNewWordButton.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int selected = wordsListDisplaying.getSelectedIndex();
                        wordsList.elementAt(selected)[0] = word.getText();
                        wordsList.elementAt(selected)[1] = translation.getText();
                        listModelWords.clear();
                        listModelTranslation.clear();
                        writeFile();
                        readFile();
                    }

                });

                addNewWordButton.setFont(font);
                win.add(p1);
                win.add(p2);
                win.add(addNewWordButton);
                win.setVisible(true);

            }
        });
        buttonEditSelectedOne.setFont(font);
        positionSettings.ipadx = GridBagConstraints.NONE;
        positionSettings.gridheight = 1;
        positionSettings.gridx = 0;
        positionSettings.gridy = 1;
        mainFrame.add(buttonEditSelectedOne, positionSettings);

        buttonDeleteSelected = new JButton("Delete selected");
        buttonDeleteSelected.setEnabled(false);
        buttonDeleteSelected.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                while (wordsListDisplaying.getSelectedIndex() != -1) {
                    int selected = wordsListDisplaying.getSelectedIndex();
                    listModelWords.removeElementAt(selected);
                    listModelTranslation.removeElementAt(selected);
                    wordsList.remove(selected);
                }
                if (wordsList.isEmpty()) {
                    buttonQuiz.setEnabled(false);
                }
                writeFile();
            }
        });
        buttonDeleteSelected.setFont(font);
        positionSettings.gridy = 2;
        mainFrame.add(buttonDeleteSelected, positionSettings);

        buttonSelectAll = new JButton("Select all");
        buttonSelectAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] indices = new int[wordsList.size()];
                for (int i = 0; i < wordsList.size(); i++) {
                    indices[i] = i;
                }
                wordsListDisplaying.setSelectedIndices(indices);
            }
        });
        buttonSelectAll.setFont(font);
        positionSettings.gridy = 3;
        mainFrame.add(buttonSelectAll, positionSettings);

        buttonRefreshList = new JButton("Refresh list");
        buttonRefreshList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listModelWords.clear();
                listModelTranslation.clear();
                readFile();
                if (wordsList.isEmpty()) {
                    buttonQuiz.setEnabled(false);
                } else {
                    buttonQuiz.setEnabled(true);
                }
            }
        });
        buttonRefreshList.setFont(font);
        positionSettings.gridy = 4;
        mainFrame.add(buttonRefreshList, positionSettings);

        positionSettings.gridy = 5;
        mainFrame.add(new JLabel(), positionSettings);
        positionSettings.gridy = 6;
        mainFrame.add(new JLabel(), positionSettings);
        positionSettings.gridy = 7;
        mainFrame.add(new JLabel(), positionSettings);
        positionSettings.gridy = 8;
        mainFrame.add(new JLabel(), positionSettings);
        positionSettings.gridy = 9;
        mainFrame.add(new JLabel(), positionSettings);
        positionSettings.gridy = 10;
        mainFrame.add(new JLabel(), positionSettings);

        buttonQuiz = new JButton("Start quiz!");
        buttonQuiz.setEnabled(false);
        buttonQuiz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.setVisible(false);
                JFrame win = new JFrame();
                win.setLayout(new GridLayout(3, 1, 0, 60));
                win.setBounds(screenWidth / 2 - FRAME_WIDTH / 2, screenHeight / 2 - FRAME_HEIGHT / 2, FRAME_WIDTH / 2,
                        FRAME_HEIGHT / 2);
                int[] array = new int[wordsList.size()];
                for (int i = 0; i < array.length; i++) {
                    int temp;
                    do {
                        boolean wasFound = false;
                        temp = (int) (Math.random() * array.length);
                        for (int j = 0; j < i; j++) {
                            if (temp == array[j]) {
                                wasFound = true;
                            }
                        }
                        if (!wasFound) {
                            break;
                        }
                    } while (true);
                    array[i] = temp;
                }
                k = 0;
                JPanel p1 = new JPanel(new GridLayout(1, 3, 30, 0));
                JPanel p2 = new JPanel(new GridLayout(1, 3, 30, 0));

                JLabel word = new JLabel(wordsList.elementAt(array[k])[0], JLabel.CENTER);
                word.setFont(font);
                win.add(word);
                JTextField translation = new JTextField();
                // translation.setAlignmentX(JTextField.CENTER_ALIGNMENT);
                translation.setHorizontalAlignment(JTextField.CENTER);
                translation.setFont(font);
                p2.add(new JLabel());
                p2.add(translation);
                p2.add(new JLabel());
                win.add(p2);
                JButton show = new JButton("Show translation");
                show.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        translation.setText(wordsList.elementAt(array[k])[1]);
                    }
                });
                show.setFont(font);
                p1.add(show);
                p1.add(new JLabel());
                JButton next = new JButton("Next");
                next.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        k++;
                        if (k == wordsList.size()) {
                            k = 0;
                        }
                        word.setText(wordsList.elementAt(array[k])[0]);
                    }
                });
                next.setFont(font);
                p1.add(next);
                win.add(p1);
                win.setVisible(true);
                win.addWindowListener(new WindowListener() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        mainFrame.setVisible(true);
                        win.setVisible(false);
                    }

                    @Override
                    public void windowDeactivated(WindowEvent e) {
                    }

                    @Override
                    public void windowActivated(WindowEvent e) {
                    }

                    @Override
                    public void windowDeiconified(WindowEvent e) {
                    }

                    @Override
                    public void windowIconified(WindowEvent e) {
                    }

                    @Override
                    public void windowClosed(WindowEvent e) {
                    }

                    @Override
                    public void windowOpened(WindowEvent e) {
                    }
                });
            }
        });
        buttonQuiz.setFont(font);
        positionSettings.gridy = 11;
        mainFrame.add(buttonQuiz, positionSettings);

        mainFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
}