package ui;

import model.Library;
import model.Novel;
import model.exceptions.StatusNotCreatedException;
import net.miginfocom.swing.MigLayout;
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
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class LibraryUI extends JPanel implements ActionListener {

    private Library list;

    private final JsonLoader loader1 = new JsonLoader(JSON_DIR_1);
    private final JsonSaver saver1 = new JsonSaver(JSON_DIR_1);

    private final JsonLoader loader2 = new JsonLoader(JSON_DIR_2);
    private final JsonSaver saver2 = new JsonSaver(JSON_DIR_2);

    private final JsonLoader loader3 = new JsonLoader(JSON_DIR_3);
    private final JsonSaver saver3 = new JsonSaver(JSON_DIR_3);

    private static final String JSON_DIR_1 = "./data/library.json";
    private static final String JSON_DIR_2 = "./data/library2.json";
    private static final String JSON_DIR_3 = "./data/library3.json";


    private int selected;
    private int page = 1;
    private int buttonId = -1;

    private boolean firstTime = true;

    final JFrame frame;
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
    private JButton search;
    private JTextField statusField;
    private JButton editS;
    private JButton saveS;
    private JButton editR;
    private JButton saveR;
    private JTextField rate;
    private JLabel genre;
    private JLabel genreNum;
    private JComboBox genres;
    private boolean pressed = false;
    JButton addGenre;
    JButton removeGenre;
    JTextField libName;
    JLabel minRating;
    JTextField minRatingField;
    MusicThread thread = new MusicThread();
    Thread thready = new Thread(thread);

    private boolean searching = false;
    private String searchEntry;
    private JButton resetButton;
    boolean firstT = true;

    private Image img = Toolkit.getDefaultToolkit().createImage("./data/books.jpeg");
    private Image img2 = Toolkit.getDefaultToolkit().getImage("./data/backgroundImage.png");
    private Image play = Toolkit.getDefaultToolkit().getImage("./data/playButton.png");

    private Clip clipC;

    protected ButtonGroup fileGroup = new ButtonGroup();
    protected ButtonGroup novelGroup = new ButtonGroup();

    private MigLayout layout = new MigLayout("align 50% 25%, gap 100 30");

    private ArrayList<JToggleButton> buttonList = new ArrayList<>();
    private ArrayList<JToggleButton> fillerList = new ArrayList<>();

    private ArrayList<JToggleButton> searchResult = new ArrayList<>();
    private ArrayList<Novel> searchResults = new ArrayList<>();

    private ArrayList<JToggleButton> filteredButton = new ArrayList<>();
    private ArrayList<Novel> filterResults = new ArrayList<>();
    private ArrayList<String> filterTopics = new ArrayList<>();
    private ArrayList<JToggleButton> searchFilter = new ArrayList<>();
    private ArrayList<Novel> searchFilterN = new ArrayList<>();

    private JButton filter;
    private boolean filtering;
    private boolean meetReqs;

    //Effects: runs the library application and creates the frames and buttons
    public LibraryUI() {
        frame = new JFrame("Novel Tracker");
        init();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800, 450));
        frame.pack();
        frame.setResizable(false);
        createStartScreen();
        frame.setVisible(true);

    }

    private void createStartScreen() {
        this.setLayout(layout);
        frame.add(this);

        JLabel label = new JLabel("Select a File");
        label.setFont(new Font("Ariel", Font.BOLD, 35));
        this.add(label, "span, align center");

        createFileButton();

        newFile = new JButton("New");
        newFile.addActionListener(this);
        this.add(newFile, "align center");

        libName = new JTextField("Library Name");
        libName.setPreferredSize(new Dimension(200, 25));
        this.add(libName);

        loadFile = new JButton("Load");
        loadFile.addActionListener(this);
        this.add(loadFile);


        frame.setIconImage(img);


    }


    //Modifies: This
    //Effects: Uses one of the actions listed depending on the user input
    private void useBook(Novel novel) {
        this.removeAll();
        layout = new MigLayout("wrap 4, align 50% 50%, gap 80 40, insets 5 50 5 50");
        this.setLayout(layout);

        JLabel field = new JLabel(novel.getName());
        field.setPreferredSize(new Dimension(360, 60));
        field.setFont(new Font("Ariel", Font.BOLD, 40));
        this.add(field, "span, align left");

        ratingDetailsInit(novel);

        genreDetailsInit(novel);

        statusDetailsInit(novel);

        backButtonInit();

        this.revalidate();
        this.repaint();
    }

    public void backButtonInit() {
        JButton back = new JButton("Back");
        back.setPreferredSize(new Dimension(90, 25));
        back.addActionListener(e -> {
            searching = false;
            resetLists();
            filtering = false;
            askQuestionPanel();
        });
        this.add(back, "align right");
    }

    //Effects: Initializes genre related buttons
    private void genreDetailsInit(Novel novel) {
        genre = new JLabel("Genre: ");
        genre.setFont(new Font("Ariel", Font.BOLD, 20));

        genreNum = new JLabel(novel.getGenre().toString());
        genreNum.setFont(new Font("Ariel", Font.BOLD, 10));


        String[] genresList = {"Fantasy", "Sci-Fi", "Romance", "Comedy",
                "Adventure", "Yaoi/Yuri", "LitRPG", "Tragedy"};
        genres = new JComboBox(genresList);
        genres.setPreferredSize(new Dimension(100, 25));

        genreDetailsImplement(novel);

    }

    //Effects: Helps genre details init to implement buttons
    private void genreDetailsImplement(Novel novel) {
        addGenre = new JButton("Add");
        addGenre.setPreferredSize(new Dimension(65, 25));
        addGenre.addActionListener(e -> {
            novel.addGenre(genres.getSelectedIndex() + 1);
            genreNum.setText(novel.getGenre().toString());
            repaint();
        });
        removeGenre = new JButton("Remove");
        removeGenre.setPreferredSize(new Dimension(80, 25));
        removeGenre.addActionListener(e -> {
            removeGen(novel, (String) genres.getSelectedItem());
            genreNum.setText(novel.getGenre().toString());
            repaint();
        });
        this.add(genre);
        this.add(genreNum);
        this.add(genres);
        this.add(addGenre, "split 2");
        this.add(removeGenre);
    }

    //Effects: Initiates the status related buttons
    private void statusDetailsInit(Novel novel) {
        statusField = new JTextField(novel.getStatus());
        editS = new JButton("Edit");
        editS.addActionListener(e -> {
            statusField.setEditable(true);
            repaint();

        });

        saveS = new JButton("save");

        statusDetailsImplement(novel);
    }

    //Effects: Helps status details init to initialize all the buttons
    private void statusDetailsImplement(Novel novel) {
        statusField.setPreferredSize(new Dimension(70, 20));
        statusField.setEditable(false);

        editS.setPreferredSize(new Dimension(60, 25));

        saveS.setPreferredSize(new Dimension(65, 25));
        saveS.addActionListener(e -> {
            if (statusField.isEditable()) {
                updateStasis(novel, statusField.getText());
                statusField.setEditable(false);
                repaint();
            }
        });

        JLabel status = new JLabel("Status: ");
        status.setFont(new Font("Ariel", Font.BOLD, 20));
        this.add(status);
        this.add(statusField);
        this.add(editS);
        this.add(saveS);
    }

    //Effects: Initializes the rating related buttons and fields
    private void ratingDetailsInit(Novel novel) {
        rate = new JTextField(Double.toString(novel.getRating()));
        rate.setPreferredSize(new Dimension(70, 20));
        rate.setEditable(false);

        editR = new JButton("Edit");
        editR.setPreferredSize(new Dimension(65, 25));
        editR.addActionListener(e -> {
            rate.setEditable(true);
            revalidate();
            repaint();

        });
        rateDetailsImplement(novel);
    }

    //Effects: Helps rating details Init initialize buttons
    private void rateDetailsImplement(Novel novel) {

        saveR = new JButton("save");
        saveR.setPreferredSize(new Dimension(65, 25));
        saveR.addActionListener(e -> {
            if (rate.isEditable()) {
                setRating(novel, rate.getText());
                rate.setEditable(false);
                rate.setText(Double.toString(novel.getRating()));
                repaint();
            }

        });
        JLabel rating = new JLabel("Rating: ");
        rating.setFont(new Font("Ariel", Font.BOLD, 20));
        this.add(rating);
        this.add(rate);
        this.add(editR);
        this.add(saveR);
    }

    //Modifies: novel
    //Effects: creates or updates the status of the chose novel
    public void updateStasis(Novel novel, String s) {
        try {
            novel.updateStatus(s);
        } catch (StatusNotCreatedException e) {
            novel.addStatus(s);
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
        this.add(leftButton, "align center");


        selectButton = new JButton("Select");
        this.add(selectButton, "align center");

        createRightButton();

        addButton = new JButton("Add Novel");
        addButton.setPreferredSize(new Dimension(100, 30));
        this.add(addButton, "align center");

        bookAdd = new JTextField("Enter Book name here");
        bookAdd.setPreferredSize(new Dimension(400, 25));
        this.add(bookAdd, "align center");

        createSearchButton();

        createResetButton();

        saveFile = new JButton("Save");
        saveFile.setPreferredSize(new Dimension(100, 30));
        addActionListener();
        this.add(saveFile, "align center");

        createFilterButton();
        celesteButton();
    }

    public void createRightButton() {
        rightButton = new JButton("Next");
        rightButton.addActionListener(e -> {
            if ((page * 8) < buttonList.size() && !filtering && !searching) {
                page++;
                prepListUpdate(false);
            } else if ((page * 8) < searchFilter.size() && filtering && searching) {
                page++;
                prepListUpdate(false);
            } else if ((page * 8) < filteredButton.size() && filtering) {
                page++;
                prepListUpdate(false);
            } else if ((page * 8) < searchResult.size() && searching) {
                page++;
                prepListUpdate(false);
            }

        });
        this.add(rightButton, "align center");
    }

    public void resetLists() {
        page = 1;
        searchResults = new ArrayList<>();
        searchResult = new ArrayList<>();
        firstTime = false;
        searchFilter = new ArrayList<>();
        searchFilterN = new ArrayList<>();
        filterTopics = new ArrayList<>();
        filteredButton = new ArrayList<>();
        filterResults = new ArrayList<>();
    }

    public void createFilterButton() {
        filter = new JButton("Filter");
        filter.setPreferredSize(new Dimension(100, 30));
        filter.addActionListener(e -> {
            resetLists();
            filterScreen();

        });
        this.add(filter, "split 2");
    }

    public void filterScreenInit() {
        removeAll();

        layout = new MigLayout("align 50% 50%, gap 100 15");
        this.setLayout(layout);

        JToggleButton fantasy = new JToggleButton("Fantasy");
        JToggleButton scifi = new JToggleButton("Sci-Fi");
        JToggleButton romance = new JToggleButton("Romance");
        JToggleButton comedy = new JToggleButton("Comedy");

        initButton(fantasy, false);
        initButton(scifi, true);
        initButton(romance, false);
        initButton(comedy, true);

        filterScreenInitHelper();

        minRating = new JLabel("Min Rating");
        this.add(minRating, "split 2");
        minRatingField = new JTextField("-1");
        minRatingField.setPreferredSize(new Dimension(100, 25));
        this.add(minRatingField);

    }

    public void filterScreenInitHelper() {
        JToggleButton adventure = new JToggleButton("Adventure");
        JToggleButton yaoi = new JToggleButton("Yaoi/Yuri");
        JToggleButton litRpg = new JToggleButton("LitRPG");
        JToggleButton tragedy = new JToggleButton("Tragedy");

        initButton(adventure, false);
        initButton(yaoi, true);
        initButton(litRpg, false);
        initButton(tragedy, true);
    }

    public void filterScreen() {
        filterScreenInit();
        JButton filterB = new JButton("Filter");
        filterB.setPreferredSize(new Dimension(150, 30));
        filterB.addActionListener(e -> {
            filterButtonEffects();
        });
        add(filterB);
        repaint();
        revalidate();
    }

    public void filterButtonEffects() {
        filtering = true;
        for (int i = 0; i < list.getLib().size(); i++) {
            meetReqs = true;
            for (String s : filterTopics) {
                if (!list.getLib().get(i).getGenre().contains(s)) {
                    meetReqs = false;
                }
            }
            if (!(list.getLib().get(i).getRating() >= Double.valueOf(minRatingField.getText()))) {
                meetReqs = false;
            }

            if (meetReqs) {
                filteredButton.add(buttonList.get(i));
                filterResults.add(list.getLib().get(i));
            }
        }
        searching = false;
        page = 1;
        searchResults = new ArrayList<>();
        searchResult = new ArrayList<>();
        firstTime = false;
        askQuestionPanel();
    }

    //Effects: Puts the buttons that have been filtered in place
    public void filterBooks() {
        for (int i = 0; i < filteredButton.size(); i++) {
            if (this.getComponentCount() < 8) {
                filteredButton.get(i).setText(filterResults.get(i).getName() + "    Rating: "
                        + filterResults.get(i).getRating() + "     Status: " + filterResults.get(i).getStatus());
                this.add(filteredButton.get(i), "span, align center");
            }
        }
    }

    //Effects: Template for initiating the genre buttons
    public void initButton(JToggleButton but, Boolean wrap) {

        but.setPreferredSize(new Dimension(200, 35));
        but.addActionListener(e -> {
            if (!filterTopics.contains(but.getText())) {
                filterTopics.add(but.getText());
            } else {
                filterTopics.remove(but.getText());
            }

        });
        if (wrap) {
            this.add(but, "wrap");
        } else {
            this.add(but);
        }

    }

    //Effects: Creates the search button and initiates it
    public void createSearchButton() {
        search = new JButton("Search");
        search.setPreferredSize(new Dimension(100, 30));
        search.addActionListener(e -> {
            searching = true;
            searchEntry = bookAdd.getText();
            page = 1;
            searchResults = new ArrayList<>();
            searchResult = new ArrayList<>();
            searchFilter = new ArrayList<>();
            searchFilterN = new ArrayList<>();
            firstTime = false;
            askQuestionPanel();
        });
        this.add(search);
    }

    //Effects: Searches a Novel list for novels that contains given string
    public void searchBook(String entry, ArrayList<JToggleButton> emptyA, ArrayList<Novel> emptyN,
                           ArrayList<JToggleButton> poolB, ArrayList<Novel> list) {

        for (int i = 0; i < list.toArray().length; i++) {
            if (list.get(i).getName().equalsIgnoreCase(entry)
                    | list.get(i).getName().toLowerCase()
                    .contains(entry.toLowerCase())) {
                emptyA.add(poolB.get(i));
                emptyN.add(list.get(i));
            }
        }
        for (int i = 0; i < emptyA.size(); i++) {
            if (this.getComponentCount() < 8) {
                emptyA.get(i).setText(emptyN.get(i).getName() + "    Rating: "
                        + emptyN.get(i).getRating() + "     Status: " + emptyN.get(i).getStatus());
                this.add(emptyA.get(i), "span, align center");
            }
        }
    }

    //Effects: Creates the reset button that resets search and filter
    public void createResetButton() {
        resetButton = new JButton("Reset");
        resetButton.setPreferredSize(new Dimension(100, 30));
        resetButton.addActionListener(e -> {
            searching = false;
            resetLists();
            filtering = false;
            askQuestionPanel();
        });
        this.add(resetButton);
    }

    //Effects: Creates all the buttons and shows 8 books out of the list at a time along with buttons to add
    //select or save
    public void askQuestionPanel() {
        this.removeAll();
        questionPanelLayoutReset();

        for (int i = 0; i < list.getLib().size(); i++) {
            if (firstTime) {
                JToggleButton j;
                j = new JToggleButton(list.getLib().get(i).getName() + "    Rating: "
                        + list.getLib().get(i).getRating() + "     Status: " + list.getLib().get(i).getStatus());
                novelGroup.add(j);
                buttonList.add(j);
                buttonList.get(i).setPreferredSize(new Dimension(425, 25));
                j.addActionListener(this);
            }
            if (this.getComponentCount() < 8 && !searching && !filtering) {
                buttonList.get(i).setText(list.getLib().get(i).getName() + "    Rating: "
                        + list.getLib().get(i).getRating() + "     Status: " + list.getLib().get(i).getStatus());
                this.add(buttonList.get(i), "span, align center");
            }
        }
        filterSearchDir();
        fillEmpty();
        questionPanelInit();
        this.repaint();
    }

    public void questionPanelLayoutReset() {
        this.layout = new MigLayout("align 50% 25%, wrap 3, insets 5 75 40 75",
                "[]15[]15[]", "[]5[]5[]5[]5[]5[]5[]5[]10[]20[]20[]");
        this.setLayout(this.layout);
        this.revalidate();
    }

    //Effects: Directory for search or filter or both
    public void filterSearchDir() {
        if (filtering && searching) {
            searchBook(searchEntry, searchFilter, searchFilterN, filteredButton, filterResults);
        } else if (searching) {
            searchBook(searchEntry, searchResult, searchResults, buttonList, list.getLib());
        } else if (filtering) {
            filterBooks();
        }
    }

    //Effects: Fills the extra space from askQuestionPanel with empty buttons
    private void fillEmpty() {
        for (int i = 0; i < 8; i++) {
            if (this.getComponentCount() < 8) {
                this.add(fillerList.get(i), "span, align center");
            }
        }
    }

    //Effects: Implements some actionlistners for buttons
    private void addActionListener() {
        selectButton.addActionListener(e -> {
            if (buttonId != -1) {
                useBook(list.getLib().get(buttonId));
            }

        });
        saveFile.addActionListener(e -> {
            if (selected == 1) {
                saveLibrary(saver1);
            } else if (selected == 2) {
                saveLibrary(saver2);
            } else {
                saveLibrary(saver3);
            }

        });
        addButtonListener();
    }

    //Effects: Helps addActionListener implement actionListeners
    public void addButtonListener() {
        addButton.addActionListener(e -> {
            addBook(bookAdd.getText());
            JToggleButton j = new JToggleButton(bookAdd.getText() + "   Rating: N/A   Status: N/A");
            novelGroup.add(j);
            buttonList.add(j);
            j.addActionListener(LibraryUI.this);
            j.setPreferredSize(new Dimension(425, 25));
            searching = false;
            resetLists();
            filtering = false;
            askQuestionPanel();

        });
    }

    //Modifies: This
    //Effects Initializes variables
    private void init() {
        for (int i = 0; i < 8; i++) {
            JToggleButton f = new JToggleButton("Empty");
            f.setPreferredSize(new Dimension(425, 25));
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
        this.add(fileButton1, "span, align center");
        fileButton1.addActionListener(e -> {
            selected = 1;
            playSound();
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
        this.add(fileButton2, "span, align center");
        fileButton2.addActionListener(e -> {
            selected = 2;
            playSound();
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
        this.add(fileButton3, "span, align center");
        fileButton3.addActionListener(e -> {
            selected = 3;
            playSound();
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
        for (int i = 0; i < buttonList.size(); i++) {
            if (e.getSource() == buttonList.get(i)) {
                buttonId = i;
                playSound();
            }
        }

    }

    //Effects: updates the page to the next or previous page
    private void updateList(boolean left, boolean notLast, ArrayList<JToggleButton> poolB, ArrayList<Novel> poolN) {

        if (left | notLast) {
            for (int i = 0; i < 8; i++) {
                poolB.get(i + ((page - 1) * 8)).setPreferredSize(new Dimension(425, 25));
                poolB.get(i + ((page - 1) * 8)).setText(poolN.get(i + ((page - 1) * 8)).getName()
                        + "    Rating: " + poolN.get(i + ((page - 1) * 8)).getRating()
                        + "     Status: " + poolN.get(i + ((page - 1) * 8)).getStatus());
                this.add(poolB.get(i + ((page - 1) * 8)), "span, align center");
            }
        } else {
            for (int i = 0; i < (poolB.size() - ((page - 1) * 8)); i++) {
                poolB.get(i + ((page - 1) * 8)).setPreferredSize(new Dimension(425, 25));
                poolB.get(i + ((page - 1) * 8)).setText(poolN.get(i + ((page - 1) * 8)).getName()
                        + "    Rating: " + poolN.get(i + ((page - 1) * 8)).getRating() + "     Status: "
                        + poolN.get(i + ((page - 1) * 8)).getStatus());
                this.add(poolB.get(i + ((page - 1) * 8)), "span, align center");
            }
            for (int i = 0; i < (8 - (poolB.size() - ((page - 1) * 8))); i++) {
                this.add(fillerList.get(i), "span, align center");
            }
        }
        questionPanelInit();
        this.revalidate();
        this.repaint();
    }

    //Effects: Removes necessary components and prepares variables for updateList
    public void prepListUpdate(boolean left) {
        boolean notLast = false;
        if (buttonList.size() - ((page - 1) * 8) > 8) {
            notLast = true;
        }
        this.removeAll();
        if (!searching && !filtering) {
            updateList(left, notLast, buttonList, list.getLib());
        } else if (searching && filtering) {
            updateList(left, notLast, searchFilter, searchFilterN);
        } else if (searching) {
            updateList(left, notLast, searchResult, searchResults);
        } else {
            updateList(left, notLast, filteredButton, filterResults);
        }
    }

    //Effects: Paints the background image
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img2, 0, 0, this);
    }

    //Effects: Plays button Sound
    private void playSound() {
        try {
            AudioInputStream sound = AudioSystem.getAudioInputStream(new File("./data/buttonClick.wav"));
            Clip clip = AudioSystem.getClip();
            clip.open(sound);
            clip.start();
        } catch (Exception e) {
            System.err.println("Sound error");
        }
    }

    public void celesteButton() {

        JToggleButton celeste = new JToggleButton("Music");
        if (pressed) {
            celeste.setSelected(true);
        }
        celeste.setPreferredSize(new Dimension(100, 20));
        celeste.addActionListener(e -> {
            playSoundtrack(pressed);

            if (pressed) {
                pressed = false;
            } else {
                pressed = true;
            }
        });
        add(celeste);
    }

    private void playSoundtrack(boolean stop) {

        if (!stop && firstT) {
            thready.start();
            firstT = false;
        }


        if (stop) {
            thready.suspend();
            thread.stopClip();
        } else {
            thready.resume();
        }
    }

}
