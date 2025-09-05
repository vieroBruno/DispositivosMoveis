package com.chatapp.server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Classe que gerencia a comunicação com um cliente específico.
 */
public class ClientHandler extends Thread {
    private Socket socket;
    private String nome;
    private PrintWriter saida;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            Scanner entrada = new Scanner(socket.getInputStream());
            saida = new PrintWriter(socket.getOutputStream(), true);

            if (entrada.hasNextLine()) {
                nome = entrada.nextLine();
                ChatServer.clientes.put(nome, this);
                System.out.println("Cliente conectado: " + nome);
            }

            String mensagem;
            while (entrada.hasNextLine()) {
                mensagem = entrada.nextLine();

                if (mensagem.equalsIgnoreCase("/sair")) {
                    break;
                } else if (mensagem.equalsIgnoreCase("/users")) {
                    listarUsuarios();
                } else if (mensagem.startsWith("/send message")) {
                    enviarMensagem(mensagem);
                } else {
                    saida.println("Comando inválido!");
                }
            }

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        } finally {
            encerrarConexao();
        }
    }

    private void listarUsuarios() {
        StringBuilder lista = new StringBuilder("Usuários conectados: ");
        for (String user : ChatServer.clientes.keySet()) {
            lista.append(user).append(" ");
        }
        saida.println(lista.toString());
    }

    private void enviarMensagem(String comando) {
        String[] partes = comando.split(" ", 4);
        if (partes.length < 4) {
            saida.println("Uso correto: /send message <destinatario> <mensagem>");
            return;
        }

        String destinatario = partes[2];
        String conteudo = partes[3];

        ClientHandler clienteDestino = ChatServer.clientes.get(destinatario);
        if (clienteDestino != null) {
            clienteDestino.receberMensagem(nome + ": " + conteudo);
        } else {
            saida.println("Usuário não encontrado: " + destinatario);
        }
    }

    public void receberMensagem(String mensagem) {
        saida.println(mensagem);
    }

    private void encerrarConexao() {
        try {
            if (nome != null) {
                ChatServer.clientes.remove(nome);
                System.out.println("Cliente desconectado: " + nome);
            }
            socket.close();
        } catch (IOException e) {
            System.err.println("Erro ao encerrar conexão: " + e.getMessage());
        }
    }
}
