/**
 * Classe que representa um componente eletrônico do circuito
 */
public class Componente {
    private String tipo;
    private double custo;
    private double eficienciaEnergetica;
    private double tamanho;
    private int id;

    public Componente(int id, String tipo, double custo, double eficienciaEnergetica, double tamanho) {
        this.id = id;
        this.tipo = tipo;
        this.custo = custo;
        this.eficienciaEnergetica = eficienciaEnergetica;
        this.tamanho = tamanho;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public double getCusto() {
        return custo;
    }

    public double getEficienciaEnergetica() {
        return eficienciaEnergetica;
    }

    public double getTamanho() {
        return tamanho;
    }

    @Override
    public String toString() {
        return String.format("Componente[ID=%d, Tipo=%s, Custo=%.2f, Eficiência=%.2f, Tamanho=%.2f]",
                id, tipo, custo, eficienciaEnergetica, tamanho);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Componente that = (Componente) obj;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
