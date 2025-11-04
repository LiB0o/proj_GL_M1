# proj_GL_M1

# **Software Technical Documentation Template**

## 1. **Introduction**

   The morpion is a simple two-player game where players take turns to place the different symbol types on an (n * n) grid, where (n*n) is the number of cases. The goal is to align n symbols of the marks vertically, horizontally, or diagonally.
   This project was developed as part of a software engineering master's degree to demonstrate proper architecture, project management, and version control using Git.

## 2. **Objectives**

   Apply engineering software functionally (modularity, abstraction, documentation).
   Demonstrate teamwork through version control GIT and collaborative development.
   Write an algorithm for the game.
   Program the algorithm for the game.
   Write algorithms to extend the initial game.  
   Program the extensions to the initial game.
   Use a graphics library to display the game (currently JavaFX).

## 3. **Product information**

###    **Overview**

   The application allows two players to play morpion on the same device or against an AI (Bot).
   It validates places symbols, detects the winner, and restarts new games

* ###    **Features**

     Two-player mode (example: Player X vs. Player O).

         Optional AI opponent.

         Win and draw detection.

         Simple and intuitive user interface (GUI).

* ###    **System Requirements**

        OS: Windows / macOS / Linux

        Programming Language: java17+

        Dependencies: JavaFX (for GUI version), OpenJDK 21+, Maven

        jUnit5+ (for testing)

## 4. **Installation**

* ###    **Pre-installation Requirements**

   Ensure **java** and **SDK javaFX** is installed.

   install **GIT** if you want to clone the repository

* ### Installation Steps.

   **command to clone the repository:**

        git clone <URL_OF_THE_REPOSITORY>.git
        Example : git clone https://github.com/my-users/my-project.git

This creates a **my-project** folder containing all the code and history of the repository.

* ###    **install dependencies:**

   	  mvn clean install


* ### **Post-installation Configuration**
		
		I advice you to use IntelliIj IDEA 2023+ to run the game.

## 5. **User Guides**

* ###    **Getting Started**

  Go to the file morpion/src/main/java/gl/morpion/MainLauncher.java and start it, choose your game mode from player/player or player/AI (bot). You can also click on “custom” for more features.
  After choosing your game mode, you can start playing by clicking on one of the squares on the grid to place a symbol. The first player to place 5 symbols of the same type in a row vertically, horizontally, or diagonally wins the game.
  You can start a new game at any time by clicking on the back button.

* ### **Navigation**

    Use mouse clicks (GUI) to place symbols.

    The game displays alert messages in the event of a win/draw.

* ### 	**UseCases**

		UC1: Start a new game.

		UC2: place a symbol.

		UC3: Detect and display winner/draw.

		UC4: Restart or exit.

## 6. **Technical Details**

* ###    **Architecture overview**

   The project follows an MVC (Model-View-Controller) architecture:

   		Model: Manages game state and logic.

   		View: Displays the board and alert messages.

   		Controller: Handles user inputs and updates the model.
*   ###  **Data Model**

   	Game class: orchestrates flow, win checking, turn management.

   	GameBoard class: stores the state of the grid, the game logic, the different types of boards, etc...

   	Player class: stores player symbol.
   	
   	Symbol: stores differnet types of symbols.

###    **API Reference**

## **7. Troubleshooting**

| Issue            | Possible Cause  | Solution you can try |
|------------------|----------|----------|
| Game does not start |Missing dependency | Run 'mvn clean install' |
|                  |              |              |
|                  |  |  |
|                  |  |  |
|                  |  |  |
|                  |  |  |

## **8. Sidenotes**

This project is not finished; some features may be changed or added in the future.
Please also note that the infrastructure is not optimal and will be improve in the future.


# **A few images to show the interface 🎨**
![Texte alternatif](/morpion/src/main/resources/gl/morpion/iteration_1/start.PNG)

![Texte alternatif](/morpion/src/main/resources/gl/morpion/iteration_1/two_player_human.PNG)
