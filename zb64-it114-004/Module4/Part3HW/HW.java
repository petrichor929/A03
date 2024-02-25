package Module4.Part3HW;

import java.util.Random;

public class HW {
       private String processCommand(String message, String client) {
        if (message.equalsIgnoreCase("/flip")) {
            return coinToss(client);
        } else {
            return "Unknown command: " + message;
        }
    }

    private String coinToss(String client) {
        Random random = new Random();
        int results = random.nextInt(2);

        String message;
        if (results == 0) {
            message = client + " flipped a coin and got heads.";
        } else {
            message = client + " flipped a coin and got tails.";
        }
        return message;
    }

    private String processCommand(String message, String client) {
        if (message.equalsIgnoreCase("/roll")) {
            return rollDice(client);
        } else {
            return "Unknown command: " + message;
        }
    

    private String rollDice(String message, String client) {
        Random random = new Random();
        int product = random.nextInt(6);

        String message;
        if (product == 1) {
            message = client + " rolled a dice and got " + product;
        } else if (product == 2) {
            message = client + " rolled a dice and got " + product;
        } else if (product == 3) {
            message = client + " rolled a dice and got " + product;
        } else if (product == 4) {
            message = client + " rolled a dice and got " + product;
        } else if (product == 5) {
            message = client + " rolled a dice and got " + product;
        } else if (product == 6) {
            message = client + " rolled a dice and got " + product;
        } else {
            message = client + " rolled a dice and got an invalid result";
        }
    }
}
