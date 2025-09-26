package com.chatapp.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    static ConcurrentHashMap<String, ClientHandler> clientes = new ConcurrentHashMap<>();

    public static void main(String[] args) {
        int porta = 12345;
        final ServerLogger logger = new ServerLogger("server_log.txt");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.logServerShutdown();
        }));

        try (ServerSocket servidor = new ServerSocket(porta)) {
            System.out.println("Servidor iniciado na porta " + porta);

            while (true) {
                Socket socket = servidor.accept();
                ClientHandler handler = new ClientHandler(socket, logger);
                handler.start();
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
        }
    }
}