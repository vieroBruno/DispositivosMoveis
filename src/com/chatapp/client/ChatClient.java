package com.chatapp.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Cliente do chat.
 * Envia comandos e recebe mensagens do servidor.
 */
public class ChatClient {
    public static void main(String[] args) {
        String servidor = "127.0.0.1";
        int porta = 12345;

        try (Socket socket = new Socket(servidor, porta)) {
            System.out.println("Conectado ao servidor.");

            Scanner teclado = new Scanner(System.in);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter saida = new PrintWriter(socket.getOutputStream(), true);

            // Solicita nome do cliente
            System.out.print("Digite seu nome: ");
            String nome = teclado.nextLine();
            saida.println(nome);

            // Thread para ouvir mensagens do servidor
            new Thread(() -> {
                try {
                    String resposta;
                    while ((resposta = entrada.readLine()) != null) {
                        System.out.println(resposta);
                    }
                } catch (IOException e) {
                    System.err.println("Conexão encerrada.");
                }
            }).start();

            // Envia mensagens digitadas no console
            while (true) {
                String mensagem = teclado.nextLine();
                saida.println(mensagem);
                if (mensagem.equalsIgnoreCase("/sair")) {
                    break;
                }
            }

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
    }
}
