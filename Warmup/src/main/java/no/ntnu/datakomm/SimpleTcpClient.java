package no.ntnu.datakomm;

import java.io.*;
import java.net.Socket;

/**
 * A Simple TCP client, used as a warm-up exercise for assignment A4.
 */
public class SimpleTcpClient {
    // Remote host where the server will be running
    private static final String HOST = "datakomm.work";
    // TCP port
    private static final int PORT = 1301;

    private Socket socket;
    private BufferedReader br;
    private OutputStream out;

    /**
     * Run the TCP Client.
     *
     * @param args Command line arguments. Not used.
     */
    public static void main(String[] args) {
        SimpleTcpClient client = new SimpleTcpClient();
        try {
            client.run();
        } catch (InterruptedException e) {
            log("Client interrupted");
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Run the TCP Client application. The logic is already implemented, no need to change anything in this method.
     * You can experiment, of course.
     *
     * @throws InterruptedException The method sleeps to simulate long client-server conversation.
     *                              This exception is thrown if the execution is interrupted halfway.
     */
    public void run() throws InterruptedException {
        log("Simple TCP client started");

        if (connectToServer(HOST, PORT)) {
            log("Connection to the server established");
            int a = (int) (1 + Math.random() * 10);
            int b = (int) (1 + Math.random() * 10);
            String request = a + "+" + b;
            if (sendRequestToServer(request)) {
                log("Sent " + request + " to server");
                String response = readResponseFromServer();
                if (response != null) {
                    log("Server responded with: " + response);
                    int secondsToSleep = 2 + (int)(Math.random() * 5);
                    log("Sleeping " + secondsToSleep + " seconds to allow simulate long client-server connection...");
                    Thread.sleep(secondsToSleep * 1000);
                    request = "bla+bla";
                    if (sendRequestToServer(request)) {
                        log("Sent " + request + " to server");
                        response = readResponseFromServer();
                        if (response != null) {
                            log("Server responded with: " + response);
                            if (sendRequestToServer("game over") && closeConnection()) {
                                log("Game over, connection closed");
                                // When the connection is closed, try to send one more message. It should fail.
                                if (!sendRequestToServer("2+2")) {
                                    log("Sending another message after closing the connection failed as expected");
                                } else {
                                    log("ERROR: sending a message after closing the connection did not fail!");
                                }
                            } else {
                                log("ERROR: Failed to stop conversation");
                            }
                        } else {
                            log("ERROR: Failed to receive server's response!");
                        }
                    } else {
                        log("ERROR: Failed to send invalid message to server!");
                    }
                } else {
                    log("ERROR: Failed to receive server's response!");
                }
            } else {
                log("ERROR: Failed to send valid message to server!");
            }
        } else {
            log("ERROR: Failed to connect to the server");
        }

        log("Simple TCP client finished");
    }

    /**
     * Close the TCP connection to the remote server.
     *
     * @return True on success, false otherwise
     */
    private boolean closeConnection() {
        boolean disconnected = false;
        if (socket != null) {
            try {
                socket.close();
                disconnected = true;

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return disconnected;
    }

    /**
     * Try to establish TCP connection to the server (the three-way handshake).
     *
     * @param host The remote host to connect to. Can be domain (localhost, ntnu.no, etc), or IP address
     * @param port TCP port to use
     * @return True when connection established, false otherwise
     */
    private boolean connectToServer(String host, int port) {
        // TODO - implement this method
        // Remember to catch all possible exceptions that the Socket class can throw.

        try {
            socket = new Socket(host, port);
            InputStream in = socket.getInputStream();
            br = new BufferedReader(new InputStreamReader(in));
            out = socket.getOutputStream();
            //returns true to confirm socket connection
            return true;

        } catch (IOException e) {
            System.out.println("Socket error: " + e.getMessage());
        }

        //returns false to confirm socket failed connection

        return false;
    }

    /**
     * Send a request message to the server (newline will be added automatically)
     *
     * @param request The request message to send. Do NOT include the newline in the message!
     * @return True when message successfully sent, false on error.
     */
    private boolean sendRequestToServer(String request) {
        // TODO - implement this method
        // Hint: What can go wrong? Several things:
        // * Connection closed by remote host (server shutdown)
        // * Internet connection lost, timeout in transmission
        // * Connection not opened.
        // * What is the request is null or empty?
        try {
            String commandToSend = request + "\n";
            out.write(commandToSend.getBytes());
            out.flush();

            return true;

        } catch (IOException e) {
            System.out.println("Socket error: " + e.getMessage());
        }

        return false;
    }

    /**
     * Wait for one response from the remote server.
     *
     * @return The response received from the server, null on error. The newline character is stripped away
     * (not included in the returned value).
     */
    private String readResponseFromServer() {
        // TODO - implement this method
        // Similarly to other methods, exception can happen while trying to read the input stream of the TCP Socket
        try {

            String response;

                response = br.readLine();
                if(response != null){

                   return response;
                }



        } catch (IOException e) {
            System.out.println("Socket error: " + e.getMessage());
        }

        return null;
    }

    /**
     * Log a message to the system console.
     *
     * @param message The message to be logged (printed).
     */
    private static void log(String message) {
        String threadId = "THREAD #" + Thread.currentThread().getId() + ": ";
        System.out.println(threadId + message);
    }
}
