package Module4.Part3HW;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random; //zb64 2/22/24

public class Server {
    int port = 3001;
    // connected clients
    private List<ServerThread> clients = new ArrayList<ServerThread>();

    private void start(int port) {
        this.port = port;
        // server listening
        try (ServerSocket serverSocket = new ServerSocket(port);) {
            Socket incoming_client = null;
            System.out.println("Server is listening on port " + port);
            do {
                System.out.println("waiting for next client");
                if (incoming_client != null) {
                    System.out.println("Client connected");
                    ServerThread sClient = new ServerThread(incoming_client, this);
                    
                    clients.add(sClient);
                    sClient.start();
                    incoming_client = null;
                    
                }
            } while ((incoming_client = serverSocket.accept()) != null);
        } catch (IOException e) {
            System.err.println("Error accepting connection");
            e.printStackTrace();
        } finally {
            System.out.println("closing server socket");
        }
    }
    protected synchronized void disconnect(ServerThread client) {
		long id = client.getId();
        client.disconnect();
		broadcast("Disconnected", id);
	}
    
    protected synchronized void broadcast(String message, long id) {
        if(processCommand(message, id)){

            return;
        }
        // let's temporarily use the thread id as the client identifier to
        // show in all client's chat. This isn't good practice since it's subject to
        // change as clients connect/disconnect
        message = String.format("User[%d]: %s", id, message);
        // end temp identifier
        // loop over clients and send out the message
        Iterator<ServerThread> it = clients.iterator();
        while (it.hasNext()) {
            ServerThread client = it.next();
            boolean wasSuccessful = client.send(message);
            if (!wasSuccessful) {
                System.out.println(String.format("Removing disconnected client[%s] from list", client.getId()));
                it.remove();
                broadcast("Disconnected", id);
            }
        }
    }

    private boolean processCommand(String message, long clientId){
        System.out.println("Checking command: " + message);
        if(message.equalsIgnoreCase("flip")){
            System.out.println("User has flipped.");
            message = "User has flipped coin.";
            Random random = new Random();
            int results = random.nextInt(2);
            if (results == 0) {
                message = "User[" + clientId + "] flipped a coin and got heads.";
            } else {
                message = clientId + " flipped a coin and got tails.";
            }
            String finalMessage = String.format("User[%d]: %s", clientId, message);;
            Iterator<ServerThread> it = clients.iterator();
            while (it.hasNext()) {
                ServerThread client = it.next();
                client.send(finalMessage);
            }
        } //zb64 2/24/24
        else if (message.startsWith("roll")){
            System.out.println("User has rolled.");
            message = "User has rolled a dice.";
            Random random = new Random();
            int product = random.nextInt(6) + 1;
            if (product == 1) {
                message = "User[" + clientId + "] rolled a dice and got " + product;
            } else if (product == 2) {
                message = clientId + " rolled a dice and got " + product;
            } else if (product == 3) {
                message = clientId + " rolled a dice and got " + product;
            } else if (product == 4) {
                message = clientId + " rolled a dice and got " + product;
            } else if (product == 5) {
                message = clientId + " rolled a dice and got " + product;
            } else if (product == 6) {
                message = clientId + " rolled a dice and got " + product;
            } else {
                message = clientId + " rolled a dice and got an invalid result";
            }
            String finalMessage = String.format("User[%d]: %s", clientId, message);;
            Iterator<ServerThread> it = clients.iterator();
            while (it.hasNext()) {
                ServerThread client = it.next();
                client.send(finalMessage);
            }
        }//zb64 2/24/24
        else if(message.equalsIgnoreCase("disconnect")){
            Iterator<ServerThread> it = clients.iterator();
            while (it.hasNext()) {
                ServerThread client = it.next();
                if(client.getId() == clientId){
                    it.remove();
                    disconnect(client);
                    
                    break;
                }
            }
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("Starting Server");
        Server server = new Server();
        int port = 3000;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            // can ignore, will either be index out of bounds or type mismatch
            // will default to the defined value prior to the try/catch
        }
        server.start(port);
        System.out.println("Server Stopped");
    }
}