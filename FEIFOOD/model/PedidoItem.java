package model;

public class PedidoItem {
    private Alimento alimento;
    private int quantidade;
    private double precoUnitario;

    public PedidoItem() {}

    public PedidoItem(Alimento alimento, int quantidade, double precoUnitario) {
        this.alimento = alimento;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    public Alimento getAlimento() {
        return alimento;
    }

    public void setAlimento(Alimento alimento) {
        this.alimento = alimento;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}
