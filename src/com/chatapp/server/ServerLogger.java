package com.chatapp.server;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServerLogger {
    private final String logFileName;

    public ServerLogger(String logFileName) {
        this.logFileName = logFileName;
        logServerStart();
    }

    public void logServerStart() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFileName, true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);
            writer.println("\n" + timestamp + " - SERVIDOR INICIADO");
        } catch (IOException e) {
            System.err.println("Erro ao registrar o início do servidor: " + e.getMessage());
        }
    }

    public void logClientConnection(String username, String ipAddress) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFileName, true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);
            writer.println(timestamp + " - Usuário " + username + " conectado: " + ipAddress);
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo de log: " + e.getMessage());
        }
    }

    public void logClientDisconnection(String username) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFileName, true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);
            writer.println(timestamp + " - Usuário " + username + " desconectado.");
        } catch (IOException e) {
            System.err.println("Erro ao escrever no arquivo de log: " + e.getMessage());
        }
    }

    public void logServerShutdown() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(logFileName, true))) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String timestamp = now.format(formatter);
            writer.println(timestamp + "- SERVIDOR DESLIGADO\n");
        } catch (IOException e) {
            System.err.println("Erro ao registrar o desligamento do servidor: " + e.getMessage());
        }
    }
}