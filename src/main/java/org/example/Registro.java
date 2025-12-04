package org.example;

import java.util.List;

public class Registro {

    String dominio;
    String tipo;
    String valor;


    public Registro() {}

    public Registro(String dominio, String tipo, String valor) {
        this.dominio = dominio;
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return dominio + tipo + valor;
    }
}
