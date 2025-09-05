package com.chatapp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    // Armazena os clientes conectados (nome -> ClientHandler)
    static ConcurrentHashMap<String, ClientHandler> clientes = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        int porta = 12345;

        try (ServerSocket servidor = new ServerSocket(porta)) {
            System.out.println("Servidor iniciado na porta " + porta);

            while (true) {
                Socket socket = servidor.accept();
                ClientHandler handler = new ClientHandler(socket);
                handler.start();
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }
}
