import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa um cromossomo (configuração de circuito)
 * Cada cromossomo contém um conjunto de componentes que formam o circuito
 */
public class CromossomoCircuito {
    private List<Componente> componentesSelecionados;
    private double fitness;
    private boolean fitnessCalculado;

    // Restrições do problema
    public static double ORCAMENTO_MAXIMO = 1000.0;
    public static double TAMANHO_MAXIMO_PLACA = 100.0;
    public static double EFICIENCIA_MINIMA = 0.6;

    public CromossomoCircuito() {
        this.componentesSelecionados = new ArrayList<>();
        this.fitness = 0.0;
        this.fitnessCalculado = false;
    }

    public CromossomoCircuito(List<Componente> componentes) {
        this.componentesSelecionados = new ArrayList<>(componentes);
        this.fitness = 0.0;
        this.fitnessCalculado = false;
    }

    /**
     * Calcula o fitness do cromossomo baseado em:
     * - Custo total (minimizar)
     * - Eficiência energética média (maximizar)
     * - Respeitando as restrições de espaço e orçamento
     */
    public double calcularFitness() {
        if (fitnessCalculado) {
            return fitness;
        }

        double custoTotal = calcularCustoTotal();
        double eficienciaMedia = calcularEficienciaMedia();
        double tamanhoTotal = calcularTamanhoTotal();
        int numeroComponentes = getNumeroComponentes();

        // Penalidades para violações de restrições
        double penalidade = 0;

        if (custoTotal > ORCAMENTO_MAXIMO) {
            penalidade += (custoTotal - ORCAMENTO_MAXIMO) / ORCAMENTO_MAXIMO * 2000;
        }

        if (tamanhoTotal > TAMANHO_MAXIMO_PLACA) {
            penalidade += (tamanhoTotal - TAMANHO_MAXIMO_PLACA) / TAMANHO_MAXIMO_PLACA * 2000;
        }

        if (eficienciaMedia < EFICIENCIA_MINIMA) {
            penalidade += (EFICIENCIA_MINIMA - eficienciaMedia) * 1000;
        }

        if (penalidade > 0) {
            fitness = -penalidade;
            fitnessCalculado = true;
            return fitness;
        }

        double fatorNumeroComponentes = (double) numeroComponentes / 50.0;
        double fatorEficiencia = eficienciaMedia;
        double fatorAproveitamentoOrcamento = custoTotal / ORCAMENTO_MAXIMO;
        double fatorAproveitamentoEspaco = tamanhoTotal / TAMANHO_MAXIMO_PLACA;

        // Bônus para densidade de componentes (componentes por unidade de espaço)
        double densidadeComponentes = numeroComponentes / Math.max(tamanhoTotal, 1.0);
        double bonusDensidade = Math.min(densidadeComponentes / 5.0, 0.2);

        // Bônus para a relação eficiência/custo
        double eficienciaPorCusto = numeroComponentes > 0 ? (eficienciaMedia * numeroComponentes) / custoTotal : 0;
        double bonusEficienciaCusto = Math.min(eficienciaPorCusto, 0.15);

        // Fitness ponderado - prioriza número de componentes e eficiência
        fitness = ((fatorNumeroComponentes * 0.35) +
                (fatorEficiencia * 0.30) +
                (fatorAproveitamentoOrcamento * 0.15) +
                (fatorAproveitamentoEspaco * 0.10) +
                bonusDensidade +
                bonusEficienciaCusto);

        if (numeroComponentes >= 20 && eficienciaMedia >= 0.8) {
            fitness += 0.2; // +20% de bônus para soluções densas e eficientes
        }

        fitnessCalculado = true;
        return fitness;
    }

    // Calcula o custo total dos componentes selecionados
    public double calcularCustoTotal() {
        return componentesSelecionados.stream()
                .mapToDouble(Componente::getCusto)
                .sum();
    }

    // Calcula a eficiência média dos componentes selecionados
    public double calcularEficienciaMedia() {
        if (componentesSelecionados.isEmpty())
            return 0;
        return componentesSelecionados.stream()
                .mapToDouble(Componente::getEficienciaEnergetica)
                .average()
                .orElse(0.0);
    }

    // Calcula o tamanho total dos componentes selecionados
    public double calcularTamanhoTotal() {
        return componentesSelecionados.stream()
                .mapToDouble(Componente::getTamanho)
                .sum();
    }

    // Verifica se a solução é viável de acordo com as restrições do problema (orçamento, tamanho da placa e eficiência mínima)
    public boolean isViavel() {
        return calcularCustoTotal() <= ORCAMENTO_MAXIMO &&
                calcularTamanhoTotal() <= TAMANHO_MAXIMO_PLACA &&
                calcularEficienciaMedia() >= EFICIENCIA_MINIMA;
    }

    public List<Componente> getComponentesSelecionados() {
        return new ArrayList<>(componentesSelecionados);
    }

    // Define os componentes selecionados e recalcula o fitness
    public void setComponentesSelecionados(List<Componente> componentes) {
        this.componentesSelecionados = new ArrayList<>(componentes);
        this.fitnessCalculado = false;
    }

    public double getFitness() {
        if (!fitnessCalculado) {
            calcularFitness();
        }
        return fitness;
    }

    public void adicionarComponente(Componente componente) {
        componentesSelecionados.add(componente);
        fitnessCalculado = false;
    }

    public void removerComponente(Componente componente) {
        componentesSelecionados.remove(componente);
        fitnessCalculado = false;
    }

    public int getNumeroComponentes() {
        return componentesSelecionados.size();
    }

    @Override
    public String toString() {
        return String.format(
                "Circuito[Componentes=%d, Custo=%.2f, Eficiência=%.3f, Tamanho=%.2f, Fitness=%.3f, Viável=%s]",
                getNumeroComponentes(),
                calcularCustoTotal(),
                calcularEficienciaMedia(),
                calcularTamanhoTotal(),
                getFitness(),
                isViavel());
    }

    // Cria uma cópia profunda do cromossomo
    @Override
    public CromossomoCircuito clone() {
        return new CromossomoCircuito(this.componentesSelecionados);
    }
}
