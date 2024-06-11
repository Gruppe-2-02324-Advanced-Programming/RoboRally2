# RoboRally

Welcome to the RoboRally Game! This guide will help you install and run the game on your machine.

## Prerequisites

- Java 21
- Git
- IntelliJ IDEA (recommended, but any IDE will work)

## Installation

1. **Clone the Repository**

   Clone the master branch of the repository using your preferred method:

   ```bash
   git clone https://github.com/your-username/roborally.git

## Open the Project

Open the project in your preferred IDE. We recommend using IntelliJ IDEA for the best experience.

## Configure the Project

If configurations are not already set up, follow these steps:

### Set Java 21 as the SDK

1. Go to `File` > `Project Structure` > `Project`.
2. Set the Project SDK to Java 21.

### Set up the Module

1. Go to `File` > `Project Structure` > `Modules`.
2. Select `RoboRally` as the module.

### Set the Main Class

1. Go to `Run` > `Edit Configurations`.
2. Click the `+` button and select `Application`.
3. Name the configuration `RoboRally`.
4. Set the `Main class` to `StartRoboRally`.
5. Apply the changes and close the window.

## Run the Game

Launch the game by running the `StartRoboRally` class:

```java
public class StartRoboRally {
    public static void main(String[] args) {
        RoboRally.main(args);
    }

}
```
### How to Play

### Start a New Game

- Go to `File` and select `New Game`.

### Select Number of Players

- Choose between 2 to 6 players.

### Choose Game Board

- Select the game board you want to play on.

### Enjoy the Game!

- You are now ready to play RoboRally. Have fun!

### Multiplayer (wip)

