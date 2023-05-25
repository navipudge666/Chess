package Network;

import Chess.Move;
import Chess.Point;
import Chess.Square;

import java.io.*;
import java.net.Socket;

public class NetWorker
{
    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public NetWorker(String host, int port) throws IOException
    {
        this.socket = new Socket(host, port);
        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void close() throws IOException
    {
        socket.close();
    }

    public void sendMove(String username, Move move) throws IOException
    {
        Point from = move.getFrom();
        Point to = move.getTo();
        String message = "move " + username + " " +
                from.getRow() + " " + from.getColumn() + " " +to.getRow() + " " + to.getColumn();
        if (move.isGameIsFinished())
            message = "move " + username + " -1 -1 -1 -1";
        System.out.println("Sent this move: " + message);
        writer.write(message + "\r\n");
        writer.flush();
    }

    public Move getMove() throws IOException
    {
        String message = reader.readLine();
        System.out.println("Got this move: " + message);
        String[] answer = message.split(" ");
        if (!answer[0].equals("move"))
            return null;
        int r1 = Integer.parseInt(answer[2]);
        int c1 = Integer.parseInt(answer[3]);
        int r2 = Integer.parseInt(answer[4]);
        int c2 = Integer.parseInt(answer[5]);
        Point from = new Point(r1, c1);
        Point to = new Point(r2, c2);
        return new Move(from, to);
    }

    public void setName(String username) throws Exception
    {
        String message = "setName " + username;
        writer.write(message + "\r\n");
        writer.flush();

        String answer = reader.readLine();
        System.out.println(answer);
        if (!answer.equals("ok"))
            throw new Exception("Error occurred while setting nickname");
    }

    public void connect(String hostUsername, String playerUsername) throws Exception
    {
        String message = "joinGame " + hostUsername + " " + playerUsername;
        writer.write(message + "\r\n");
        writer.flush();

        String answer = reader.readLine();
        System.out.println("Connection answer: " + answer);
        if (!answer.equals("ok"))
            throw new Exception("Can't connect to " + hostUsername);
    }

    public void createGame(String hostName) throws Exception
    {
        String message = "createGame " + hostName;
        writer.write(message + "\r\n");
        writer.flush();

        String answer = reader.readLine();
        if (!answer.equals("ok"))
            throw new Exception("Can't create game");
    }

    public String[] getGames() throws Exception
    {
        String message = "getGames";
        writer.write(message + "\r\n");
        writer.flush();

        return reader.readLine().split(" ");
    }

    public String waitForJoining() throws Exception
    {
        String[] answer = reader.readLine().split(" ");
        if (!answer[0].equals("joined"))
            throw new Exception("Wrong format");
        return answer[1];
    }

    public void sendGameResult(String winnerName, String loserName) throws Exception
    {
        String message = "finished " + winnerName + " " + loserName;
        writer.write(message + "\r\n");
        writer.flush();

        String answer = reader.readLine();
        if (!answer.equals("ok"))
            throw new Exception("Error occurred while sending game result");
    }

    public String[] getStats() throws Exception
    {
        String message = "getStats";
        writer.write(message + "\r\n");
        writer.flush();

        return reader.readLine().split(" ");
    }
}
