import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ServerRunnable implements Runnable{

    //define variables and constructors
    protected Socket clientSocket;

    public ServerRunnable(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    public Socket getSocket() {
        return this.clientSocket;
    }

    
    @Override
    public void run() {

        try {
            while (true){

            //get message from client
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //create avenue to send message to the client
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            //save message gotten from client in the variable
            String val = in.readLine();


                String line[] = val.split(" ");
                int numb = Integer.parseInt(in.readLine());

                //get the unique process id
                //int pid = Integer.parseInt(line[1]);
                System.out.println("This is the pid: " + numb);

                sendTo(SocketServer.processes, numb, val, numb);
                //pid = user typed
                //numb = file input
            }

                } catch (SocketException e ) {
            try {
                clientSocket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public void sendTo(ArrayList<ServerRunnable> temp, int id, String msg, int pid) throws IOException {

        if (check())
        {
            System.exit(0);
        }
        //get the socket
        if (id > 1)
        {
            id = 0;
        }
        else
            id = 1;

        Socket s = temp.get(id).getSocket();

        if (msg.equalsIgnoreCase("logout")) {
            s = temp.get(pid-1).getSocket();
            s.close();
        }

        if (s.isClosed()) {
            System.out.println("Closed");
        }


        else {
            //create avenue to send message to the client
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            //display message gotten from client
            System.out.println("Passed through server");

            //send message to the client
            out.println(">>From Client " + pid + ": " + msg);


        }
    }

    private Boolean check() {
        if (SocketServer.processes.get(0).clientSocket.isClosed() && SocketServer.processes.get(1).clientSocket.isClosed())
            return true;
        else
            return false;
    }

}

