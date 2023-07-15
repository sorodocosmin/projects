package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class ServerGame {
    final private int PORT ; // The server's port
    final private ServerSocket socket;
    public volatile static boolean notStopped = true;

    final static private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);

    public ServerGame (int nrPort) throws IOException, InterruptedException {
        this.PORT = nrPort;

        this.socket = new ServerSocket(this.PORT);
        this.socket.setSoTimeout(5000);//waits 5 seconds for a client to connect

        this.connect();
    }

    private void connect() throws InterruptedException {

        while(notStopped){
            //System.out.println("Waiting for client on port " + socket.getLocalPort() + "...");
            try {

                executor.execute(new ClientThread(socket.accept()));

            } catch (SocketTimeoutException e){
                //System.out.println("Socket timed out!");
            } catch (IOException e) {
                System.out.println("I/O error when waiting for connection : " + e);
            }

        }

        try{
            this.socket.close();
            System.out.println("Server stopped");
        }
        catch (IOException e) {
            System.err.println(e);
        }


        if(executor.awaitTermination(15, java.util.concurrent.TimeUnit.SECONDS)) {//waits 5 seconds for all threads to finish (if not, it will throw an exception
            System.out.println("All threads finished");
        }
        else{
            System.out.println("Threads didn't finish in time");
        }
    }

    public static void stop() {
            notStopped = false;
            executor.shutdown();
            System.out.println("Server stopped");
    }


}
