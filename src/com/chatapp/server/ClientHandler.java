package com.chatapp.server;

import com.chatapp.common.Message;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientHandler extends Thread {
    private Socket socket;
    private String nome;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;
    private ServerLogger logger;

    public ClientHandler(Socket socket, ServerLogger logger) {
        this.socket = socket;
        this.logger = logger;
    }

    @Override
    public void run() {
        try {
            saida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());

            nome = (String) entrada.readObject();
            ChatServer.clientes.put(nome, this);

            String ip = socket.getInetAddress().getHostAddress();
            System.out.println("Usuário conectado: " + nome + " - IP: " + ip);
            logger.logClientConnection(nome, ip);

            Object obj;
            while ((obj = entrada.readObject()) != null) {
                if (obj instanceof Message msg) {
                    if (msg.getType() == Message.Type.TEXT) {
                        processarMensagemTexto(msg);
                    } else if (msg.getType() == Message.Type.FILE) {
                        processarArquivo(msg);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Conexão encerrada com usuário: " + nome);
        } finally {
            encerrarConexao();
        }
    }

    private void processarMensagemTexto(Message msg) {
        if (msg.getConteudo().equalsIgnoreCase("/sair")) {
            return;
        } else if (msg.getConteudo().equalsIgnoreCase("/users")) {
            listarUsuarios();
        } else {
            ClientHandler destino = ChatServer.clientes.get(msg.getDestinatario());
            if (destino != null) {
                destino.enviarMensagem(msg);
            } else {
                Message resposta = new Message(Message.Type.TEXT, "Servidor", nome);
                resposta.setConteudo("Usuário não encontrado: " + msg.getDestinatario());
                enviarMensagem(resposta);
            }
        }
    }

    private void processarArquivo(Message msg) {
        ClientHandler destino = ChatServer.clientes.get(msg.getDestinatario());
        if (destino != null) {
            destino.enviarMensagem(msg);
        } else {
            Message resposta = new Message(Message.Type.TEXT, "Servidor", nome);
            resposta.setConteudo("Usuário não encontrado: " + msg.getDestinatario());
            enviarMensagem(resposta);
        }
    }

    public void enviarMensagem(Message msg) {
        try {
            saida.writeObject(msg);
            saida.flush();
        } catch (IOException e) {
            System.err.println("Erro ao enviar mensagem para " + nome);
        }
    }

    private void listarUsuarios() {
        StringBuilder lista = new StringBuilder("Usuários conectados: ");
        for (String user : ChatServer.clientes.keySet()) {
            lista.append(user).append(" ");
        }

        Message resposta = new Message(Message.Type.TEXT, "Servidor", nome);
        resposta.setConteudo(lista.toString());
        enviarMensagem(resposta);
    }

    private void encerrarConexao() {
        try {
            if (nome != null) {
                ChatServer.clientes.remove(nome);
                logger.logClientDisconnection(nome);
            }
            socket.close();
        } catch (IOException e) {
            System.err.println("Erro ao encerrar conexão: " + e.getMessage());
        }
    }
}
