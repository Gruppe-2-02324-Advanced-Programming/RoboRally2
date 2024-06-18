package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;

/**
 * This class represents the action of a laser field. The action is to shoot a laser in the direction of the player's heading.
 *
 * @author Setare, s232629
 */

public class Laser {
    private GameController gameController;

    public Laser(GameController gameController) {
        this.gameController = gameController;
    }

    public void shootLaser(Player player) {
        Heading heading = player.getHeading();
        Space currentSpace = player.getSpace();

        while (true) {
            Space nextSpace = gameController.getBoard().getNeighbour(currentSpace, heading);
            if (nextSpace == null || hasWall(currentSpace, heading) || hasWall(nextSpace, heading.prev().prev())) {
                break;
            }

            Player targetPlayer = nextSpace.getPlayer();
            if (targetPlayer != null) {
                discardCards(targetPlayer);
                break;
            }

            currentSpace = nextSpace;
        }
    }

    private boolean hasWall(Space space, Heading heading) {
        return space.getWalls().contains(heading);
    }

    private void discardCards(Player player) {
        // Use spam card logic to discard the cards
        for (int i = 0; i < Player.NO_REGISTERS; i++) {
            CommandCardField field = player.getProgramField(i);
            CommandCard card = field.getCard();
            if (card != null) {
                player.getDiscardpile().getCards()[player.getDiscardpile().size()] = card;
                player.getDiscardpile().size();
                field.setCard(null);
            }
        }
    }
}
