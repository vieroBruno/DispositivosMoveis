package com.chatapp.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileUtils {
    public static byte[] lerArquivo(String caminho) throws IOException {
        return Files.readAllBytes(new File(caminho).toPath());
    }

    public static void salvarArquivo(String nomeArquivo, byte[] dados) throws IOException {
        Files.write(new File(nomeArquivo).toPath(), dados);
    }
}
