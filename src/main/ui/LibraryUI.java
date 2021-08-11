package ui;

import model.Library;
import model.Novel;
import model.exceptions.StatusNotCreatedException;
import org.json.JSONException;
import persistence.JsonLoader;
import persistence.JsonSaver;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class LibraryUI extends JPanel implements ActionListener {

    private Scanner scan;
    private Library list;

    final JsonLoader loader1 = new JsonLoader(JSON_DIR_1);
    final JsonSaver saver1 = new JsonSaver(JSON_DIR_1);

    final JsonLoader loader2 = new JsonLoader(JSON_DIR_2);
    final JsonSaver saver2 = new JsonSaver(JSON_DIR_2);

    final JsonLoader loader3 = new JsonLoader(JSON_DIR_3);
    final JsonSaver saver3 = new JsonSaver(JSON_DIR_3);

    private static final String JSON_DIR_1 = "./data/library.json";
    private static final String JSON_DIR_2 = "./data/library2.json";
    private static final String JSON_DIR_3 = "./data/library3.json";

    final Color beige = new Color(236, 222, 193);
    private boolean loaded = false;
    private int selected;
    private int page = 1;
    private int buttonId = -1;

    private boolean firstTime = true;

    private JFrame frame;
    private JToggleButton fileButton1;
    private JToggleButton fileButton2;
    private JToggleButton fileButton3;
    private JButton newFile;
    private JButton loadFile;
    private JButton saveFile;
    private JButton addButton;
    private JButton selectButton;
    private JButton leftButton;
    private JButton rightButton;
    private JTextField bookAdd;
    JTextField statusField;
    JButton editS;
    JButton saveS;
    JButton editR;
    JButton saveR;
    JTextField rate;
    JLabel genre;
    JLabel genreNum;
    JComboBox genres;
    JButton addGenre;
    JButton removeGenre;
    JTextField libName;

    Image img = Toolkit.getDefaultToolkit().createImage("C:/Users/yiran/Downloads/books.jpeg");


    GridBagConstraints gbc = new GridBagConstraints();
    GridBagLayout lay = new GridBagLayout();

    private ButtonGroup fileGroup = new ButtonGroup();
    private ButtonGroup novelGroup = new ButtonGroup();

    private Container contentPane;

    private ArrayList<JToggleButton> buttonList = new ArrayList<>();
    private ArrayList<JToggleButton> fillerList = new ArrayList<>();


    //Effects: runs the library application and creates the frames and buttons
    public LibraryUI() {
        frame = new JFrame("Novel Tracker");
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(new Dimension(800, 450));
        frame.setResizable(false);
        init();
        createStartScreen();
        createFileButton();
        frame.setVisible(true);

    }

    private void createStartScreen() {
        gbc.gridheight = 5;
        gbc.gridwidth = 5;
        gbc.weighty = 2;
        gbc.weightx = .5;

        contentPane = frame.getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
        contentPane.add(this);

        this.setLayout(lay);

        JLabel label = new JLabel("Select a File");
        label.setFont(new Font("Ariel", Font.BOLD, 35));
        addObjects(label, this, lay, gbc, 2, 0, 3, 1);

        newFile = new JButton("New");
        newFile.addActionListener(this);
        addObjects(newFile, this, lay, gbc, 2, 5, 1, 1);

        loadFile = new JButton("Load");
        loadFile.addActionListener(this);
        addObjects(loadFile, this, lay, gbc, 4, 5, 1, 1);

        libName = new JTextField("Library Name");
        libName.setPreferredSize(new Dimension(200, 25));
        addObjects(libName, this, lay, gbc, 2, 5, 3, 1);

        frame.setIconImage(img);

        repaint();

    }

    //Effects: Adds an object to the container according to grid bag layouts
    public void addObjects(Component component, Container yourcontainer, GridBagLayout layout, GridBagConstraints gbc,
                           int gridx, int gridy, int gridwidth, int gridheight) {
        this.gbc.gridx = gridx;
        this.gbc.gridy = gridy;

        this.gbc.gridwidth = gridwidth;
        this.gbc.gridheight = gridheight;

        layout.setConstraints(component, gbc);
        yourcontainer.add(component);
    }

    //Modifies: This
    //Effects: Uses one of the actions listed depending on the user input
    private void useBook(Novel novel) {
        this.removeAll();
        revalidate();

        ratingDetailsInit(novel);

        statusDetailsInit(novel);

        genreDetailsInit(novel);

        JLabel field = new JLabel(novel.getName());
        field.setPreferredSize(new Dimension(360, 60));
        field.setFont(new Font("Ariel", Font.BOLD, 40));
        addObjects(field, this, lay, gbc, 0, 0, 3, 1);

        JButton back = new JButton("Back");
        back.setPreferredSize(new Dimension(90, 25));
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gbc.anchor = GridBagConstraints.CENTER;
                firstTime = false;
                page = 1;
                askQuestionPanel();
            }
        });
        addObjects(back, this, lay, gbc, 6, 5, 1, 1);

        this.revalidate();
        this.repaint();
    }

    //Effects: Initializes genre related buttons
    private void genreDetailsInit(Novel novel) {
        genre = new JLabel("Genre: ");
        genre.setFont(new Font("Ariel", Font.BOLD, 20));
        addObjects(genre, this, lay, gbc, 0, 2, 1, 1);

        genreNum = new JLabel(novel.getGenre().toString());
        genreNum.setFont(new Font("Ariel", Font.BOLD, 10));
        gbc.anchor = GridBagConstraints.LINE_START;
        addObjects(genreNum, this, lay, gbc, 1, 2, 1, 1);

        String[] genresList = {"Fantasy", "Sci-Fi", "Romance", "Comedy",
                "Adventure", "Yaoi/Yuri", "LitRPG", "Tragedy"};
        genres = new JComboBox(genresList);
        genres.setPreferredSize(new Dimension(100, 25));
        addObjects(genres, this, lay, gbc, 2, 2, 1, 1);

        genreDetailsImplement(novel);

    }

    //Effects: Helps genre details init to implement buttons
    private void genreDetailsImplement(Novel novel) {
        addGenre = new JButton("Add");
        addGenre.setPreferredSize(new Dimension(65, 25));
        addGenre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                novel.addGenre(genres.getSelectedIndex() + 1);
                genreNum.setText(novel.getGenre().toString());
                repaint();
            }
        });
        addObjects(addGenre, this, lay, gbc, 3, 2, 1, 1);
        removeGenre = new JButton("Remove");
        removeGenre.setPreferredSize(new Dimension(80, 25));
        removeGenre.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeGen(novel, (String) genres.getSelectedItem());
                genreNum.setText(novel.getGenre().toString());
                repaint();
            }
        });
        addObjects(removeGenre, this, lay, gbc, 4, 2, 1, 1);
    }

    //Effects: Initiates the status related buttons
    private void statusDetailsInit(Novel novel) {
        statusField = new JTextField(novel.getStatus());
        gbc.anchor = GridBagConstraints.LINE_START;
        editS = new JButton("Edit");
        editS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusField.setEditable(true);
                repaint();
            }
        });

        saveS = new JButton("save");
        gbc.anchor = GridBagConstraints.LINE_START;

        statusDetailsImplement(novel);
    }

    //Effects: Helps status details init to initialize all the buttons
    private void statusDetailsImplement(Novel novel) {
        statusField.setPreferredSize(new Dimension(70, 20));
        statusField.setEditable(false);
        addObjects(statusField, this, lay, gbc, 1, 3, 1, 1);

        editS.setPreferredSize(new Dimension(60, 25));
        addObjects(editS, this, lay, gbc, 2, 3, 1, 1);

        saveS.setPreferredSize(new Dimension(65, 25));
        saveS.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (statusField.isEditable()) {
                    updateStasis(novel, statusField.getText());
                    statusField.setEditable(false);
                    repaint();
                }
            }
        });
        addObjects(saveS, LibraryUI.this, lay, gbc, 3, 3, 1, 1);

        JLabel status = new JLabel("Status: ");
        gbc.anchor = GridBagConstraints.LINE_START;
        status.setFont(new Font("Ariel", Font.BOLD, 20));
        addObjects(status, this, lay, gbc, 0, 3, 1, 1);
    }

    //Effects: Initializes the rating related buttons and fields
    private void ratingDetailsInit(Novel novel) {
        rate = new JTextField(Double.toString(novel.getRating()));
        rate.setPreferredSize(new Dimension(70, 20));
        rate.setEditable(false);

        editR = new JButton("Edit");
        gbc.anchor = GridBagConstraints.LINE_START;
        editR.setPreferredSize(new Dimension(65, 25));
        editR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rate.setEditable(true);
                revalidate();
                repaint();
            }
        });
        addObjects(editR, this, lay, gbc, 2, 1, 1, 1);

        addObjects(rate, this, lay, gbc, 1, 1, 1, 1);
        rateDetailsImplement(novel);
    }

    //Effects: Helps rating details Init initialize buttons
    private void rateDetailsImplement(Novel novel) {

        saveR = new JButton("save");
        gbc.anchor = GridBagConstraints.LINE_START;
        saveR.setPreferredSize(new Dimension(65, 25));
        saveR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (rate.isEditable()) {
                    setRating(novel, rate.getText());
                    rate.setEditable(false);
                    rate.setText(Double.toString(novel.getRating()));
                    repaint();
                }
            }
        });
        addObjects(saveR, LibraryUI.this, lay, gbc, 3, 1, 1, 1);

        gbc.anchor = GridBagConstraints.LINE_START;

        JLabel rating = new JLabel("Rating: ");
        gbc.anchor = GridBagConstraints.LINE_START;
        rating.setFont(new Font("Ariel", Font.BOLD, 20));
        addObjects(rating, this, lay, gbc, 0, 1, 1, 1);
    }

    //Modifies: novel
    //Effects: creates or updates the status of the chose novel
    public void updateStasis(Novel novel, String s) {
        try {
            novel.updateStatus(s);
            System.out.println("Status has been set to " + novel.getStatus());
        } catch (StatusNotCreatedException e) {
            novel.addStatus(s);
            System.out.println("Status has been created.");
        }
    }


    //Modifies: This
    //Effects: Allows user to pick a rating for selected novel
    public void setRating(Novel novel, String s) {
        try {
            double n = Double.parseDouble(s);
            novel.rateNovel(n);
        } catch (Exception e) {
            System.err.println("Not a number!");
        }

    }

    //Effects: Initializes buttons for the main question screen
    public void questionPanelInit() {
        leftButton = new JButton("Previous");
        leftButton.addActionListener(this);
        addObjects(leftButton, this, lay, gbc, 2, 7, 1, 1);

        rightButton = new JButton("Next");
        rightButton.addActionListener(this);
        addObjects(rightButton, this, lay, gbc, 4, 7, 1, 1);

        addButton = new JButton("Add Novel");
        addButton.setPreferredSize(new Dimension(100, 30));
        addObjects(addButton, this, lay, gbc, 4, 8, 1, 1);

        selectButton = new JButton("Select");
        addObjects(selectButton, this, lay, gbc, 3, 7, 1, 1);

        saveFile = new JButton("Save");
        saveFile.setPreferredSize(new Dimension(100, 30));
        addActionListener();
        addObjects(saveFile, this, lay, gbc, 3, 9, 1, 1);

        bookAdd = new JTextField("Enter Book name here");
        bookAdd.setPreferredSize(new Dimension(400, 25));
        addObjects(bookAdd, this, lay, gbc, 2, 8, 3, 1);
    }

    //Effects: Creates all the buttons and shows 6 books out of the list at a time along with buttons to add
    //select or save
    public void askQuestionPanel() {
        this.removeAll();
        this.revalidate();

        questionPanelInit();

        for (int i = 0; i < list.getLib().size(); i++) {
            if (firstTime) {
                JToggleButton j;
                j = new JToggleButton(list.getLib().get(i).getName() + "    Rating: "
                        + list.getLib().get(i).getRating() + "     Status: " + list.getLib().get(i).getStatus());
                novelGroup.add(j);
                buttonList.add(j);
                buttonList.get(i).setPreferredSize(new Dimension(400, 25));
                j.addActionListener(this);
            }
            if (this.getComponentCount() < 12) {

                buttonList.get(i).setText(list.getLib().get(i).getName() + "    Rating: "
                        + list.getLib().get(i).getRating() + "     Status: " + list.getLib().get(i).getStatus());
                addObjects(buttonList.get(i), this, lay, gbc, 2, i, 3, 1);
            }

        }

        this.repaint();

    }

    //Effects: Implements some actionlistners for buttons
    private void addActionListener() {
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttonId != -1) {
                    useBook(list.getLib().get(buttonId));
                }
            }
        });
        saveFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selected == 1) {
                    saveLibrary(saver1);
                } else if (selected == 2) {
                    saveLibrary(saver2);
                } else {
                    saveLibrary(saver3);
                }
            }
        });
        addButtonListener();
    }

    //Effects: Helps addActionListener implement actionListeners
    public void addButtonListener() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBook(bookAdd.getText());
                JToggleButton j = new JToggleButton(bookAdd.getText() + "   Rating: N/A   Status: N/A");
                novelGroup.add(j);
                buttonList.add(j);
                j.addActionListener(LibraryUI.this);
                if (getComponentCount() < 12) {
                    j.setPreferredSize(new Dimension(400, 25));
                    addObjects(j, LibraryUI.this, lay, gbc, 2, getComponentCount() - 6, 3, 1);
                }
                firstTime = false;
                page = 1;
                askQuestionPanel();
            }
        });
    }

    //Modifies: This
    //Effects Initializes variables
    private void init() {
        scan = new Scanner(System.in);
        for (int i = 0; i < 5; i++) {
            JToggleButton f = new JToggleButton();
            f.setPreferredSize(new Dimension(400, 25));
            fillerList.add(f);
        }


    }

    //Effects: Creates the first save/load file
    private void createFileButton() {
        try {
            fileButton1 = new JToggleButton("File 1: " + loader1.loadNames());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException j) {
            fileButton1 = new JToggleButton("File 1: Empty");
        }
        fileGroup.add(fileButton1);
        fileButton1.setPreferredSize(new Dimension(200, 40));
        addObjects(fileButton1, this, lay, gbc, 2, 1, 3, 1);
        fileButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selected = 1;
                playSound();
            }
        });
        createFileButton2();
        createFileButton3();
    }

    //Effects: Creates the second save/load file
    private void createFileButton2() {
        try {
            fileButton2 = new JToggleButton("File 2: " + loader2.loadNames());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException j) {
            fileButton2 = new JToggleButton("File 2: Empty");
        }
        fileGroup.add(fileButton2);
        fileButton2.setPreferredSize(new Dimension(200, 40));
        addObjects(fileButton2, this, lay, gbc, 2, 3, 3, 1);
        fileButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selected = 2;
                playSound();
            }
        });
    }

    //Effects: Creates the third save/load file
    private void createFileButton3() {
        try {
            fileButton3 = new JToggleButton("File 3: " + loader3.loadNames());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException j) {
            fileButton3 = new JToggleButton("File 3: Empty");
        }
        fileGroup.add(fileButton3);
        fileButton3.setPreferredSize(new Dimension(200, 40));
        addObjects(fileButton3, this, lay, gbc, 2, 4, 3, 1);
        fileButton3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selected = 3;
                playSound();
            }
        });

    }

    //Effects: Allows user to choose between creating a slot or loading a slot
    private void createLoad(String choice, int slot) {
        switch (choice.toUpperCase()) {
            case "C":

                String name = libName.getText();
                list = new Library(name);
                if (slot == 1) {
                    saveLibrary(saver1);
                } else if (slot == 2) {
                    saveLibrary(saver2);
                } else {
                    saveLibrary(saver3);
                }
                break;
            case "L":
                if (slot == 1) {
                    loadLibrary(loader1);
                } else if (slot == 2) {
                    loadLibrary(loader2);
                } else {
                    loadLibrary(loader3);
                }
                break;
        }
    }

    //Modifies: This
    //Effects: Creates a new book of title and adds it to the list
    private void addBook(String title) {
        Novel n = new Novel(title);
        list.addNovel(n);
        System.out.println("Book added successfully!");
    }


    //Effects: Removes specified genre from given novel
    public void removeGen(Novel n, String s) {
        n.removeGenre(s);
    }

    //Effects: Saves the library of given save slot
    public void saveLibrary(JsonSaver slot) {
        try {
            slot.open();
        } catch (FileNotFoundException e) {
            System.err.println("File not found!");
        }
        slot.write(list);
        slot.close();
        System.out.println("Library saved to" + JSON_DIR_1);
    }

    //Effects: Loads the library of given save slot
    public void loadLibrary(JsonLoader slot) {
        try {
            list = slot.read();
            System.out.println("Library has been loaded.");
            askQuestionPanel();
        } catch (IOException e) {
            System.err.println("Unable to read file.");
        } catch (JSONException j) {
            System.err.println("No file to load!");
        }
        loaded = true;
    }

    //Effects: Action event for assortment of buttons
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newFile && selected != 0) {
            createLoad("C", selected);
            askQuestionPanel();
        }
        if (e.getSource() == loadFile && selected != 0) {
            createLoad("L", selected);
        }
        if (e.getSource() == leftButton && page > 1) {
            page--;
            prepListUpdate(true);
        }
        if (e.getSource() == rightButton && (page * 6) < buttonList.size()) {
            page++;
            prepListUpdate(false);
        }
        for (int i = 0; i < buttonList.size(); i++) {
            if (e.getSource() == buttonList.get(i)) {
                buttonId = i;
                playSound();
            }
        }

    }

    private void updateList(boolean left, boolean notLast) {

        if (left | notLast) {
            for (int i = 0; i < 6; i++) {
                buttonList.get(i + ((page - 1) * 6)).setPreferredSize(new Dimension(400, 25));
                buttonList.get(i + ((page - 1) * 6)).setText(list.getLib().get(i + ((page - 1) * 6)).getName()
                        + "    Rating: " + list.getLib().get(i + ((page - 1) * 6)).getRating()
                        + "     Status: " + list.getLib().get(i + ((page - 1) * 6)).getStatus());
                addObjects(buttonList.get(i + ((page - 1) * 6)), this, lay, gbc, 2, i, 3, 1);
            }
        } else {
            for (int i = 0; i < (buttonList.size() - ((page - 1) * 6)); i++) {
                buttonList.get(i + ((page - 1) * 6)).setPreferredSize(new Dimension(400, 25));
                buttonList.get(i + ((page - 1) * 6)).setText(list.getLib().get(i + ((page - 1) * 6)).getName()
                        + "    Rating: " + list.getLib().get(i + ((page - 1) * 6)).getRating() + "     Status: "
                        + list.getLib().get(i + ((page - 1) * 6)).getStatus());
                addObjects(buttonList.get(i + ((page - 1) * 6)), this, lay, gbc, 2, i, 3, 1);
            }
            for (int i = 0; i < (6 - (buttonList.size() - ((page - 1) * 6))); i++) {
                addObjects(fillerList.get(i), this, lay, gbc, 2,
                        i + (buttonList.size() - ((page - 1) * 6)), 3, 1);
            }
        }
        this.revalidate();
        this.repaint();
    }

    //Effects: Removes neccesary components and prepares variables for updateList
    public void prepListUpdate(boolean left) {
        boolean notLast = false;
        if (buttonList.size() - ((page - 1) * 6) > 6) {
            notLast = true;
        }
        for (int i = 0; i < 6; i++) {
            this.remove(this.getComponentCount() - 1);
        }
        updateList(left, notLast);
    }

    //Effects: Paints the background image
    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }

    //Effects: Plays button Sound
    private void playSound() {
        try {
            AudioInputStream sound = AudioSystem.getAudioInputStream(new File(
                    "C:/Users/yiran/Downloads/button-16.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(sound);
            clip.start();
        } catch (Exception e) {
            System.err.println("Sound error");
        }
    }

}
