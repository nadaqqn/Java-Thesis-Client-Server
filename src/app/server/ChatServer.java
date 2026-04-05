package app.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.sql.*;

import app.database.DBConnect;

public class ChatServer {
    private static Set<PrintWriter> clientWriters = new HashSet<>();

    public static void startServer(int port) throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("ChatServer running on port " + port + "...");

        while (true) {
            Socket socket = serverSocket.accept();
            System.out.println("New client connected!");
            new Thread(new ClientHandler(socket)).start();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                DataInputStream dataIn = new DataInputStream(socket.getInputStream());

                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                while (true) {
                    int type = dataIn.readByte(); // 0=text, 1=file
                    if (type == 0) { // text
                        String message = dataIn.readUTF();
                        System.out.println("Text received: " + message);
                        broadcast(message);
                    } else if (type == 1) { // file
                        String fileName = dataIn.readUTF();
                        long length = dataIn.readLong();
                        byte[] fileData = new byte[(int) length];
                        dataIn.readFully(fileData);

                        if (saveFileToDB(fileName, fileData)) {
                            broadcast("[FILE]" + fileName);
                            System.out.println("File received and saved: " + fileName);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try { socket.close(); } catch (IOException e) {}
                synchronized (clientWriters) { clientWriters.remove(out); }
            }
        }

        private boolean saveFileToDB(String fileName, byte[] fileData) {
            String sql = "INSERT INTO files (file_name, file_data) VALUES (?, ?)";
            try (Connection conn = DBConnect.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {

                ps.setString(1, fileName);
                ps.setBytes(2, fileData);
                ps.executeUpdate();
                return true;

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        

        private void broadcast(String message) {
            synchronized (clientWriters) {
                for (PrintWriter writer : clientWriters) {
                    writer.println(message);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        startServer(5001);
    }
}
