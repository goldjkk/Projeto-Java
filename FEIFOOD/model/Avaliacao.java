package model;

import java.sql.Timestamp;

public class Avaliacao {

    private int id;
    private int pedidoId;
    private int estrelas; // Avaliação de 0 a 5
    private String comentario;
    private Timestamp criadoEm;

    // Construtor
    public Avaliacao() {}

    public Avaliacao(int pedidoId, int estrelas, String comentario, Timestamp criadoEm) {
        this.pedidoId = pedidoId;
        this.estrelas = estrelas;
        this.comentario = comentario;
        this.criadoEm = criadoEm;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public int getEstrelas() {
        return estrelas;
    }

    public void setEstrelas(int estrelas) {
        this.estrelas = estrelas;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Timestamp getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Timestamp criadoEm) {
        this.criadoEm = criadoEm;
    }

    // Método para mostrar as informações da avaliação
    @Override
    public String toString() {
        return "Avaliacao{" +
                "id=" + id +
                ", pedidoId=" + pedidoId +
                ", estrelas=" + estrelas +
                ", comentario='" + comentario + '\'' +
                ", criadoEm=" + criadoEm +
                '}';
    }
}
