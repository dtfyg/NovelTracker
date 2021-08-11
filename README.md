# My Personal Project

## *Novel Tracker*

As an Avid reader of online webnovels, light novels and novels in general, 
 I want to create this application mainly as a way to track my feelings and progress
through such novels. Because of the way webnovels/light novels usually update
weekly/daily, dropping them temporarily is quite a common occurance for me.
 This application will have a variety of functions that will help to keep
track of these novels including:

- A description of why I dropped the novel including a brief summary of the plot
- A rating system 
- A genre and rating filter function that will allow you to select genres for each novel
- A search function 
- Ranking functions that will sort the list of novels by last read or rating
- An individual page for every novel that you can enter for specfic details

Aside from myself I think that other webnovel/lightnovel readers will find this
program to be of use in terms of tracking as the system on NovelUpdates,
a commonly used site for tracking novels, makes it a bit clunky and difficult to 
keep track of certain aspects of novels.

## User Stories ##

- As a user, I want to be able to add new novels to the library
- As a user, I want to be able to view the list of novels
- As a user, I want to be able to add a rating to a novel
- As a user, I want to be able to change a rating for a novel
- As a user, I want to be able to add multiple genres to a novel
- As a user, I want to be able to remove a genre from a novel 


### Phase 2 Stories ###
- As a user, I want to be able to save my libraries of books and data to multiple files
- As a user, I want to be able to load my library that I have saved from multiple files.

### Phase 3 Stories ###
- As a user, I want to have a GUI with all the implemented functions

### Phase 4 Stories ###
- Library class in model has been modified to be robust and has been tested

### Phase 4: Task 3 ###
If I had more time to work on this project, the main thing I would do is to refactor the LibraryUI class. 
This class is the one class the manages all the GUI related aspects at the moment, and I would like to split it 
up into several classes to manage the functionality. Because I have multiple "screens" that the application can
enter, I would create classes dedicated to the functionality of that one screen.
One example would be to refactor the beginning load/create new screen into 
its own class that focuses on creating the layout, buttons, and functionality of the methods for it. This way,
the UI section would be a lot more organized and easy to read.