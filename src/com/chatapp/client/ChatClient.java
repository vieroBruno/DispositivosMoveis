package com.chatapp.client;

import com.chatapp.common.FileUtils;
import com.chatapp.common.Message;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    public static void main(String[] args) {
        String servidor = "127.0.0.1";
        int porta = 12345;

        try (Socket socket = new Socket(servidor, porta)) {
            System.out.println("Conectado ao servidor.");

            ObjectOutputStream saida = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream entrada = new ObjectInputStream(socket.getInputStream());

            Scanner teclado = new Scanner(System.in);

            System.out.print("Digite seu nome: ");
            String nome = teclado.nextLine();
            saida.writeObject(nome);
            saida.flush();

            new Thread(() -> {
                try {
                    Object obj;
                    while ((obj = entrada.readObject()) != null) {
                        if (obj instanceof Message msg) {
                            if (msg.getType() == Message.Type.TEXT) {
                                System.out.println(msg.getRemetente() + ": " + msg.getConteudo());
                            } else if (msg.getType() == Message.Type.FILE) {
                                FileUtils.salvarArquivo(msg.getNomeArquivo(), msg.getDadosArquivo());
                                System.out.println("Arquivo recebido de " + msg.getRemetente() + ": " + msg.getNomeArquivo());
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Conexão encerrada.");
                }
            }).start();

            while (true) {
                String comando = teclado.nextLine();

                if (comando.startsWith("/send message")) {
                    String[] partes = comando.split(" ", 4);
                    if (partes.length < 4) {
                        System.out.println("Uso correto: /send message <destinatario> <mensagem>");
                        continue;
                    }
                    Message msg = new Message(Message.Type.TEXT, nome, partes[2]);
                    msg.setConteudo(partes[3]);
                    saida.writeObject(msg);
                    saida.flush();

                } else if (comando.startsWith("/send file")) {
                    String[] partes = comando.split(" ", 4);
                    if (partes.length < 4) {
                        System.out.println("Uso correto: /send file <destinatario> <caminho_arquivo>");
                        continue;
                    }
                    String destinatario = partes[2];
                    String caminho = partes[3];

                    try {
                        byte[] dados = FileUtils.lerArquivo(caminho);
                        String nomeArquivo = new File(caminho).getName();

                        Message msg = new Message(Message.Type.FILE, nome, destinatario);
                        msg.setNomeArquivo(nomeArquivo);
                        msg.setDadosArquivo(dados);

                        saida.writeObject(msg);
                        saida.flush();

                        System.out.println("Arquivo enviado para " + destinatario);

                    } catch (IOException e) {
                        System.err.println("Erro ao ler arquivo: " + caminho);
                    }

                } else if (comando.equalsIgnoreCase("/sair")) {
                    Message msg = new Message(Message.Type.TEXT, nome, "Servidor");
                    msg.setConteudo("/sair");
                    saida.writeObject(msg);
                    saida.flush();
                    break;

                } else if (comando.equalsIgnoreCase("/users")) {
                    Message msg = new Message(Message.Type.TEXT, nome, "Servidor");
                    msg.setConteudo("/users");
                    saida.writeObject(msg);
                    saida.flush();

                } else {
                    System.out.println("Comando inválido.");
                }
            }

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
        }
    }
}
