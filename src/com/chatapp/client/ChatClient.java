package com.chatapp.client;

import com.chatapp.common.FileUtils;
import com.chatapp.common.Message;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private static ObjectOutputStream saida;
    private static ObjectInputStream entrada;
    private static Scanner teclado;
    private static String nome;

    public static void main(String[] args) {
        String servidor = "127.0.0.1";
        int porta = 12345;

        try (Socket socket = new Socket(servidor, porta)) {
            System.out.println("Conectado ao servidor.");

            saida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());
            teclado = new Scanner(System.in);

            System.out.print("Digite seu nome: ");
            nome = teclado.nextLine();
            saida.writeObject(nome);
            saida.flush();

            new Thread(() -> {
                try {
                    Object obj;
                    while ((obj = entrada.readObject()) != null) {
                        if (obj instanceof Message msg) {
                            exibirMensagemDoServidor(msg);
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Conexão encerrada.");
                }
            }).start();

            enviarMensagens();

        } catch (IOException e) {
            System.err.println("Erro no usuário: " + e.getMessage());
        }
    }

    private static void exibirMensagemDoServidor(Message msg) {
        System.out.print("\r");

        if (msg.getType() == Message.Type.TEXT) {
            System.out.println(msg.getRemetente() + ": " + msg.getConteudo());
        } else if (msg.getType() == Message.Type.FILE) {
            try {
                FileUtils.salvarArquivo(msg.getNomeArquivo(), msg.getDadosArquivo());
                System.out.println("Arquivo recebido de " + msg.getRemetente() + ": " + msg.getNomeArquivo());
            } catch (IOException e) {
                System.err.println("Erro ao salvar arquivo: " + e.getMessage());
            }
        }

        System.out.print("> ");
    }

    private static void enviarMensagens() {
        System.out.print("> ");

        while (teclado.hasNextLine()) {
            String comando = teclado.nextLine();

            if (comando.startsWith("/send message")) {
                String[] partes = comando.split(" ", 4);
                if (partes.length < 4) {
                    System.out.println("Uso correto: /send message <destinatario> <mensagem>");
                    continue;
                }
                try {
                    Message msg = new Message(Message.Type.TEXT, nome, partes[2]);
                    msg.setConteudo(partes[3]);
                    saida.writeObject(msg);
                    saida.flush();
                } catch (IOException e) {
                    System.err.println("Erro ao enviar mensagem.");
                }

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
                try {
                    Message msg = new Message(Message.Type.TEXT, nome, "Servidor");
                    msg.setConteudo("/sair");
                    saida.writeObject(msg);
                    saida.flush();
                } catch (IOException e) {
                    System.err.println("Erro ao enviar comando de saída.");
                }
                break;

            } else if (comando.equalsIgnoreCase("/users")) {
                try {
                    Message msg = new Message(Message.Type.TEXT, nome, "Servidor");
                    msg.setConteudo("/users");
                    saida.writeObject(msg);
                    saida.flush();
                } catch (IOException e) {
                    System.err.println("Erro ao enviar comando /users.");
                }

            } else {
                System.out.println("Comando inválido.");
            }

            System.out.print("> ");
        }
    }
}