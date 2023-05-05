# CS346

## Stickies++

## Description
Our goal is to create a notes app similar to sticky notes, but with the workflow and customization capabilities of a standard notes app. We are prioritizing convenience and ease of use, while also delivering a useful notes interface with plenty of features.

The proper order for running everything is:
1) Install and set "config.txt" file to right ip (using instructions below - Installation)
2) Run the web service (using instructions below - Webservice Instructions)
3) Run the application (using instructions below - Installation)

(Our Usage instructions below walks through how to properly test the application)

## Installation
1) Navigate to the Releases/Sprint 4 directory
2) Download the correct zip/tar file for your computer
3) Uncompress the file
4) Navigate to the application-1.0.0/bin directory (application-1.0.0/application.1.0.0/bin for windows)
5) Change the "config.txt" folder to the desired ip (keep it on one line)
6) Now in the same folder, run "application" when you want to run it

Please Note:
- If this doesn't work, it means the ZIP file is not compatible with your device (we were having problems with this)
- Unfortunately you will then have to run from IntelliJ instead. (you must still change the config.txt file to the correct ip!)
- This is for the latest release, sprint 4, under Jeff's instructions to use distZip/distTar method instead of jPackage
    
## Team members
Jamie Levinson: j2levins@uwaterloo.ca

Noah Nefsky: nnefsky@uwaterloo.ca

John Huang: jy22huan@uwaterloo.ca

Aryaman Goel: a39goel@uwaterloo.ca

## Releases:
Note: Release notes also in Releases folder along with installers 

Release 1 (Sprint 1 demo) Notes:
- Class architecture planned out
- Very basic starter UI working
- Model class started (no data stored yet)
- Console installation completed

Release 2 (Sprint 2 demo) Notes:
- UI fleshed out with almost all features
    - rich text support (font, colour, bold, underline, etc)
    - cut, copy, paste
    - bullet lists
- can sort and search for notes in the list view
- communication between model and view classes (e.g.: Notes save when closed)
- deleting, grouping, and saving to file planned out and in development (for next sprint)

Release 3 (Sprint 3 demo) Notes:
- Added group functionality for grouping Stickies
     - 2 tabs, groups and standard, operations work for both
- Fixing bugs from last sprint (Fixing operations inlcuding delete)
     - Listview bug fix
- Implemented persistance layer
     - stickies get saved to JSON, and loaded when application starts
- Worked on adding Spring Boot setup
     - spring project created seperately, but problems persist if we add to multi-project (so not fully implemented yet)


Release 4 (Sprint 4) Notes:
- Users can now open notes from groups for easier access.
- Added a license to the application.
- Enhanced the import files feature for easier importing.
- Created an installer for easier installation.
- Added the ability to save and delete groups.
- Implemented a webservice for syncing notes across devices.
- Added hotkeys for quicker access to frequently used functions.
- Implemented exception handling to improve application stability.
- Other various final touches

## Webservice Instructions:

(in the terminal)
run "cd server" to move to the server directory

To pull Docker run: "docker pull a39goel/cs346"

To run Docker run: "docker run -p 8080:8080 a39goel/cs346"

## Usage/Marking instructions:
### App Layout:
Our app consists of a main list window, displaying all notes. These notes then can be opened in their own 'sticky note' windows. Text customization is located within these windows, and all other features are within the main list window. We have a working persistance and web service layer, which saves the user settings and data between application runs.
1. Create a note by typing into the textfield with the text "Type Name Here", and then either clicking the create button or Ctrl + N
2. The note will open automatically. Once closed it can be opened from the list of notes by either double clicking the created note from the list or selecting it and pressing enter
3. Play around with the opened note using the text editor by entering various text. Various functions such as Bold, Underline, Italics (with the standard keyboard shortcuts), Cut, Copy, Paste, Bulleted lists, Paragraphs, Font, Color, Etc.
4. Create more notes, and sort them using the sort dropdown box. There is a variety of options to sort by.
5. Search for notes by title using the Search bar
6. For each note you create, Click the ... MenuItem button that is shown on each note, here we can delete (Ctrl + D), rename (Ctrl + R), add to groups, and remove from groups. For shortcuts for delete and rename, please select the corresponding note beforehand using either arrow keys or mouse click such that the correct note is highlighted blue. The add and remove to groups will display all valid groups for these operations.
7. Click the groups tab to switch to group view. Click the create groups button to make new groups. Each group can be expanded to show the notes from that group. All functionality from list view is the same here.

### Additional Features
8. You can toggle between dark mode and light mode with the button button titled "dark mode" at the bottom of the main window
9. As well, the import button allows you to import any text or pdf file as a note in our app, and then open and edit as a standard note. Clicking this button opens a file chooser on your device to select the file to import. Try this with our sample text file and sample pdf included in this repository.
    - note that to do this for pdf files, we used external library pdf2Dom. This parses the pdf files and converts to html. This library may sometimes have limitations out of control, and the parsed file may not be completely accurate. 


## License:

Unless explicitly stated otherwise all files in this repository are licensed under the MIT License
