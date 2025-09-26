package com.chatapp.common;

import java.io.Serializable;

public class Message implements Serializable {
    public enum Type {
        TEXT,
        FILE
    }

    private Type type;
    private String remetente;
    private String destinatario;
    private String conteudo;
    private String nomeArquivo;
    private byte[] dadosArquivo;

    public Message(Type type, String remetente, String destinatario) {
        this.type = type;
        this.remetente = remetente;
        this.destinatario = destinatario;
    }

    public Type getType() {
        return type;
    }

    public String getRemetente() {
        return remetente;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public byte[] getDadosArquivo() {
        return dadosArquivo;
    }

    public void setDadosArquivo(byte[] dadosArquivo) {
        this.dadosArquivo = dadosArquivo;
    }
}
