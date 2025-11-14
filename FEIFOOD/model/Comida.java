package model;

public class Comida extends Alimento {
    private boolean vegetariano;

    @Override
    public String getTipo() {
        return "COMIDA";
    }

    public boolean isVegetariano() {
        return vegetariano;
    }

    public void setVegetariano(boolean vegetariano) {
        this.vegetariano = vegetariano;
    }
}
