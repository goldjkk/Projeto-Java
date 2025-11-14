package model;

public class Bebida extends Alimento implements ImpostoAlcool {
    private double teorAlcoolico;

    @Override
    public String getTipo() {
        return "BEBIDA";
    }

    public double getTeorAlcoolico() {
        return teorAlcoolico;
    }

    public void setTeorAlcoolico(double teorAlcoolico) {
        this.teorAlcoolico = teorAlcoolico;
    }

    @Override
    public double calcularImposto(double precoBase, double teor) {
        return precoBase * (0.10 + 0.002 * teor);
    }
}
