package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Despesa {
    private int id;
    private String descricao;
    private double valor;
    private LocalDate data;

    public Despesa(String descricao, double valor, LocalDate data) {
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
    }

    public Despesa(int id, String descricao, double valor, LocalDate data) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public LocalDate getData() {
        return data;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return descricao + " - R$ " + String.format("%.2f", valor) + " - " + data.format(formatter);
    }
}
