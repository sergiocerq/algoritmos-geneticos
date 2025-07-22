import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Classe principal para executar a otimização de design de circuitos usando Algoritmos Genéticos
 * 
 * Problema: Empresa "Tecnologia Inovadora" precisa otimizar o design
 * de um circuito para dispositivo portátil, minimizando custo,
 * maximizando eficiência energética e respeitando as limitações do espaço da placa-mãe
 */
public class Main {

  public static void main(String[] args) {
    System.out.println("===================================================================");
    System.out.println("    OTIMIZAÇÃO DE DESIGN DE CIRCUITOS - TECNOLOGIA INOVADORA");
    System.out.println("===================================================================");
    System.out.println("Problema: Desenvolver circuito para dispositivo portátil que:");
    System.out.println("• Minimize o custo total dos componentes");
    System.out.println("• Maximize a eficiência energética");
    System.out.println("• Respeite limitações de espaço da placa-mãe");
    System.out.println("• Mantenha-se dentro do orçamento disponível");
    System.out.println();
    
    exibirParametrosProblema();

    // Gera os componentes disponíveis
    List<Componente> componentesDisponiveis = GeradorComponentes.gerarComponentesExemplo();

    componentesDisponiveis.addAll(GeradorComponentes.gerarComponentesAleatorios(20));

    System.out.println("\n" + "=".repeat(70));

    Properties config = carregarConfiguracao();

    // Configura os parâmetros do algoritmo genético
    int tamanhoPopulacao = Integer.parseInt(config.getProperty("tamanho_populacao", "100"));
    int numeroGeracoes = Integer.parseInt(config.getProperty("numero_geracoes", "200"));
    double taxaMutacao = Double.parseDouble(config.getProperty("taxa_mutacao", "0.15"));
    double taxaCruzamento = Double.parseDouble(config.getProperty("taxa_cruzamento", "0.8"));
    double taxaElitismo = Double.parseDouble(config.getProperty("taxa_elitismo", "0.1"));
    int tamanhoTorneio = Integer.parseInt(config.getProperty("tamanho_torneio", "3"));

    AlgoritmoGeneticoCircuito ag = new AlgoritmoGeneticoCircuito(
        componentesDisponiveis,
        tamanhoPopulacao,
        numeroGeracoes,
        taxaMutacao,
        taxaCruzamento,
        taxaElitismo,
        tamanhoTorneio
    );

    CromossomoCircuito melhorSolucao = ag.executar();

    // Exibe a análise da solução
    exibirAnaliseDetalhada(melhorSolucao, componentesDisponiveis);

    System.out.println("\n" + "=".repeat(70));

    if (args.length > 0 && args[0].equals("--teste-comparativo")) {
      executarTesteComparativo(componentesDisponiveis);
    }
  }

  private static void exibirParametrosProblema() {
    System.out.println("RESTRIÇÕES DO PROBLEMA:");
    System.out.printf("• Orçamento máximo: R$ %.2f%n", CromossomoCircuito.ORCAMENTO_MAXIMO);
    System.out.printf("• Tamanho máximo da placa: %.2f cm²%n", CromossomoCircuito.TAMANHO_MAXIMO_PLACA);
    System.out.printf("• Eficiência mínima exigida: %.1f%%%n", CromossomoCircuito.EFICIENCIA_MINIMA * 100);
    System.out.println();
  }

  // Mostra a melhor solução
  private static void exibirAnaliseDetalhada(CromossomoCircuito solucao, List<Componente> todosComponentes) {
    System.out.println("\n" + "=".repeat(70));
    System.out.println("                    ANÁLISE DETALHADA DA SOLUÇÃO");
    System.out.println("=".repeat(70));

    System.out.printf("Fitness final: %.4f%n", solucao.getFitness());
    System.out.printf("Solução viável: %s%n", solucao.isViavel() ? "SIM" : "NÃO");
    System.out.println();

    System.out.println("MÉTRICAS PRINCIPAIS:");
    System.out.printf("• Custo total: R$ %.2f (%.1f%% do orçamento)%n",
        solucao.calcularCustoTotal(),
        (solucao.calcularCustoTotal() / CromossomoCircuito.ORCAMENTO_MAXIMO) * 100);

    System.out.printf("• Eficiência energética média: %.1f%%%n",
        solucao.calcularEficienciaMedia() * 100);

    System.out.printf("• Espaço utilizado: %.2f cm² (%.1f%% da placa)%n",
        solucao.calcularTamanhoTotal(),
        (solucao.calcularTamanhoTotal() / CromossomoCircuito.TAMANHO_MAXIMO_PLACA) * 100);

    System.out.printf("• Número de componentes: %d de %d disponíveis%n",
        solucao.getNumeroComponentes(), todosComponentes.size());

    System.out.println("\nDISTRIBUIÇÃO POR CATEGORIA:");
    Map<String, Integer> categorias = new HashMap<>();
    Map<String, Double> custosPorCategoria = new HashMap<>();

    for (Componente comp : solucao.getComponentesSelecionados()) {
      String categoria = comp.getTipo().split("_")[0];
      categorias.put(categoria, categorias.getOrDefault(categoria, 0) + 1);
      custosPorCategoria.put(categoria,
          custosPorCategoria.getOrDefault(categoria, 0.0) + comp.getCusto());
    }

    for (Map.Entry<String, Integer> entry : categorias.entrySet()) {
      System.out.printf("• %s: %d componentes (R$ %.2f)%n",
          entry.getKey(), entry.getValue(),
          custosPorCategoria.get(entry.getKey()));
    }
  }

  private static void executarTesteComparativo(List<Componente> componentes) {
    System.out.println("\n" + "=".repeat(70));
    System.out.println("                    TESTE COMPARATIVO");
    System.out.println("=".repeat(70));

    Object[][] configuracoes = {
        { 30, 50, 5, 60 }, 
        { 75, 150, 25, 95 },
        { 250, 800, 8, 50 }, 
        { 40, 1000, 35, 30 },
        { 500, 100, 3, 99 },
        { 120, 250, 40, 85 },
        { 25, 2000, 12, 75 },
        { 300, 50, 50, 20 },
        { 80, 400, 2, 95 },
        { 15, 100, 60, 40 },
        { 180, 350, 18, 65 },
        { 600, 200, 1, 90 },
        { 10, 50, 80, 10 },
        { 1000, 1000, 15, 80 },
        { 45, 75, 45, 55 }, 
        { 64, 128, 16, 72 },
        { 100, 300, 33, 66 },
        { 150, 500, 20, 80 },
        { 35, 600, 28, 45 },
        { 400, 150, 6, 94 },
        { 90, 900, 22, 78 }, 
        { 7, 2500, 90, 50 }, 
        { 750, 25, 4, 85 }, 
        { 55, 555, 55, 55 },
        { 20, 80, 75, 25 },
        { 333, 333, 30, 70 },
        { 125, 625, 12, 88 }
    };

    for (int i = 0; i < configuracoes.length; i++) {
      int pop = (int) configuracoes[i][0];
      int ger = (int) configuracoes[i][1];
      double mut = (double) configuracoes[i][2];
      double cruz = (double) configuracoes[i][3];
      double elit = (double) configuracoes[i][4];
      int torneio = (int) configuracoes[i][5];

      System.out.printf("\nConfiguracao %d: Pop=%d, Ger=%d, Mut=%.2f, Cruz=%.2f%n",
          i + 1, pop, ger, mut, cruz, elit );

      AlgoritmoGeneticoCircuito ag = new AlgoritmoGeneticoCircuito      (componentes, pop, ger, mut, cruz, elit, torneio);
      long inicio = System.currentTimeMillis();
      CromossomoCircuito resultado = ag.executar();
      long fim = System.currentTimeMillis();

      System.out.printf("Resultado: Fitness=%.4f, Tempo=%.2fs, Viável=%s%n",
          resultado.getFitness(), (fim - inicio) / 1000.0,
          resultado.isViavel() ? "Sim" : "Não");
    }
  }

  // Carrega as configurações default do arquivo de configurações
  private static Properties carregarConfiguracao() {
    Properties config = new Properties();

    try {
      config.load(new FileInputStream("config.properties"));
      System.out.println("Configurações carregadas do arquivo config.properties");
    } catch (IOException e) {
      System.out.println("Erro ao ler config.properties, usando valores padrão");

      config.setProperty("tamanho_populacao", "100");
      config.setProperty("numero_geracoes", "200");
      config.setProperty("taxa_mutacao", "0.15");
      config.setProperty("taxa_cruzamento", "0.8");
      config.setProperty("taxa_elitismo", "0.1");
      config.setProperty("tamanho_torneio", "3");
    }

    return config;
  }
}
