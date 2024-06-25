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
# How to Play

##  Singleplayer

- Navigate the main menu and press new game.

### Select Number of Players

- Choose between 2 to 6 players.

### Choose Game Board

- Select the game board you want to play on.

### Load- & save game
- Whenever you are playing a game you can save the game at any time press file -> save game, and choose a name for your saved game.
- if/when you wanna continue the game, press load game in the main menu -> press load game, choose your preffered saved game - and continue to play.
### Game rules
- If you're in doubt about the rules regarding the game, just press the rules button in the main menu and you will be navigated to the rules of roborally.

### Enjoy the Game!

- You are now ready to play RoboRally. Have fun!


##  Multiplayer
####  Hosting
- In order to play multiplayer, you have to click the start server button in the main menu
- Then you will be prompted with an ip-address, write this down for your friends to see. Then press "okay" (sometimes on mac on some networks it's not the correct one, but do not worry!)
- If you're hosting the game press the multiplayer button
- Select the amount of players in the game
- Select your board (we reccomend DIZZY-HIGHWAY & BURNOUT)
- Then you will be prompted to enter an ip address, if you're hosting the game and server the ip-address is already inserted so just press ok (This is where the mac fix is, this will display your ip-address correctly if you're hosting)
- Press "okay" then you will pick your player number which for the most part is 1 when you're hosting
- Press host game
- Enter your name, and press ok
- Game is now running!
####  Joining
- when you're joining a game, you should've aldready been through the hosting flow. (or someone else on the same wifi as you)
- Press multiplayer, then select the same amount of players the host did
- Then pick the same map as the host
- Enter the ip address of the host, then ok
- Choose your player number. Should not be the same of someone that already have picked that.
- Press join game, then enter the gameid of the host's game (under the execution buttons)
- You have now joined the host's game
## Playing online
- When you're playing online each player should'v joined the game.
- Each player press send your cards to server
- When every player has pushed their card to the server, everyone presses get other players cards
- Then each player presses "finnish programming"
- Rince and repeat until victory







