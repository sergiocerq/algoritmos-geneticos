import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Implementação do Algoritmo Genético para otimização de design de circuitos
 * eletrônicos.
 * Resolve o problema multi-objetivo de minimizar custo, maximizar eficiência
 * energética e respeitar limitações de espaço da placa-mãe
 */
public class AlgoritmoGeneticoCircuito {
    private final List<Componente> componentesDisponiveis;
    private final int tamanhoPopulacao;
    private final int numeroGeracoes;
    private final double taxaMutacao;
    private final double taxaCruzamento;
    private final double taxaElitismo;
    private final int tamanhoTorneio;
    private final Random random;

    public AlgoritmoGeneticoCircuito(List<Componente> componentesDisponiveis,
            int tamanhoPopulacao,
            int numeroGeracoes,
            double taxaMutacao,
            double taxaCruzamento,
            double taxaElitismo,
            int tamanhoTorneio) {
        this.componentesDisponiveis = componentesDisponiveis;
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.numeroGeracoes = numeroGeracoes;
        this.taxaMutacao = taxaMutacao;
        this.taxaCruzamento = taxaCruzamento;
        this.taxaElitismo = taxaElitismo;
        this.tamanhoTorneio = tamanhoTorneio;
        this.random = new Random();
    }

    // Executa o algoritmo genético e retorna a melhor solução encontrada
    public CromossomoCircuito executar() {
        // Gera população inicial
        List<CromossomoCircuito> populacao = gerarPopulacaoInicial();

        System.out.println("=== ALGORITMO GENÉTICO PARA OTIMIZAÇÃO DE CIRCUITOS ===");
        System.out.println("População inicial: " + tamanhoPopulacao);
        System.out.println("Gerações: " + numeroGeracoes);
        System.out.println("Taxa de mutação: " + taxaMutacao);
        System.out.println("Taxa de cruzamento: " + taxaCruzamento);
        System.out.println();

        CromossomoCircuito melhorSolucao = null;

        for (int geracao = 0; geracao < numeroGeracoes; geracao++) {
            // Avalia fitness de toda a população
            for (CromossomoCircuito individuo : populacao) {
                individuo.calcularFitness();
            }

            // Ordena população por fitness (descendente)
            populacao.sort((a, b) -> Double.compare(b.getFitness(), a.getFitness()));

            // Atualiza melhor solução
            if (melhorSolucao == null || populacao.get(0).getFitness() > melhorSolucao.getFitness()) {
                melhorSolucao = populacao.get(0).clone();
            }

            // Critério de parada: se encontrou solução ótima
            if (melhorSolucao.getFitness() > 0.95) {
                System.out.println("Solução ótima encontrada na geração " + geracao);
                break;
            }

            // Gera nova população
            populacao = gerarNovaPopulacao(populacao);
        }

        System.out.println("\n=== MELHOR SOLUÇÃO ENCONTRADA ===");
        System.out.println(melhorSolucao);
        System.out.println("\nComponentes selecionados:");
        for (Componente comp : melhorSolucao.getComponentesSelecionados()) {
            System.out.println("  " + comp);
        }

        return melhorSolucao;
    }

    // Gera população inicial com indivíduos aleatórios
    private List<CromossomoCircuito> gerarPopulacaoInicial() {
        List<CromossomoCircuito> populacao = new ArrayList<>();

        for (int i = 0; i < tamanhoPopulacao; i++) {
            CromossomoCircuito individuo = new CromossomoCircuito();

            // Adiciona componentes aleatórios respeitando algumas restrições básicas
            List<Componente> componentesShuffled = new ArrayList<>(componentesDisponiveis);
            Collections.shuffle(componentesShuffled, random);

            double custoAcumulado = 0;
            double tamanhoAcumulado = 0;

            for (Componente comp : componentesShuffled) {
                // 70% de chance de adicionar um novo componente no circuito
                if (custoAcumulado + comp.getCusto() <= CromossomoCircuito.ORCAMENTO_MAXIMO &&
                        tamanhoAcumulado + comp.getTamanho() <= CromossomoCircuito.TAMANHO_MAXIMO_PLACA &&
                        random.nextDouble() < 0.7) { 

                    individuo.adicionarComponente(comp);
                    custoAcumulado += comp.getCusto();
                    tamanhoAcumulado += comp.getTamanho();
                }
            }

            populacao.add(individuo);
        }

        return populacao;
    }

    // Gera uma nova população através de seleções, cruzamentos e mutações
    private List<CromossomoCircuito> gerarNovaPopulacao(List<CromossomoCircuito> populacaoAtual) {
        List<CromossomoCircuito> novaPopulacao = new ArrayList<>();

        // Elitismo: mantém os 10% melhores
        int numElite = (int) (tamanhoPopulacao * taxaElitismo);
        for (int i = 0; i < numElite; i++) {
            novaPopulacao.add(populacaoAtual.get(i).clone());
        }

        // Gera o restante da população
        while (novaPopulacao.size() < tamanhoPopulacao) {
            // Seleção por torneio
            CromossomoCircuito pai1 = selecaoTorneio(populacaoAtual, tamanhoTorneio);
            CromossomoCircuito pai2 = selecaoTorneio(populacaoAtual, tamanhoTorneio);

            // Cruzamento
            if (random.nextDouble() < taxaCruzamento) {
                CromossomoCircuito[] filhos = cruzamento(pai1, pai2);

                // Mutação
                if (random.nextDouble() < taxaMutacao) {
                    mutacao(filhos[0]);
                }
                if (random.nextDouble() < taxaMutacao) {
                    mutacao(filhos[1]);
                }

                novaPopulacao.add(filhos[0]);
                if (novaPopulacao.size() < tamanhoPopulacao) {
                    novaPopulacao.add(filhos[1]);
                }
            } else {
                novaPopulacao.add(pai1.clone());
                if (novaPopulacao.size() < tamanhoPopulacao) {
                    novaPopulacao.add(pai2.clone());
                }
            }
        }

        return novaPopulacao;
    }

    // Seleção por torneio
    private CromossomoCircuito selecaoTorneio(List<CromossomoCircuito> populacao, int tamanhoTorneio) {
        CromossomoCircuito melhor = null;

        for (int i = 0; i < tamanhoTorneio; i++) {
            CromossomoCircuito candidato = populacao.get(random.nextInt(populacao.size()));
            if (melhor == null || candidato.getFitness() > melhor.getFitness()) {
                melhor = candidato;
            }
        }

        return melhor;
    }

    // Cruzamento uniforme: cada componente tem 50% de chance de vir de cada pai
    private CromossomoCircuito[] cruzamento(CromossomoCircuito pai1, CromossomoCircuito pai2) {
        Set<Componente> componentesPai1 = new HashSet<>(pai1.getComponentesSelecionados());
        Set<Componente> componentesPai2 = new HashSet<>(pai2.getComponentesSelecionados());

        Set<Componente> todosComponentes = new HashSet<>();
        todosComponentes.addAll(componentesPai1);
        todosComponentes.addAll(componentesPai2);

        CromossomoCircuito filho1 = new CromossomoCircuito();
        CromossomoCircuito filho2 = new CromossomoCircuito();

        for (Componente comp : todosComponentes) {
            boolean estaNoPai1 = componentesPai1.contains(comp);
            boolean estaNoPai2 = componentesPai2.contains(comp);

            if (estaNoPai1 && estaNoPai2) {
                // Componente está nos dois pais
                if (random.nextBoolean()) {
                    filho1.adicionarComponente(comp);
                } else {
                    filho2.adicionarComponente(comp);
                }
            } else if (estaNoPai1) {
                // Componente só está no pai 1
                if (random.nextDouble() < 0.7) {
                    filho1.adicionarComponente(comp);
                }
            } else {
                // Componente só está no pai 2
                if (random.nextDouble() < 0.7) {
                    filho2.adicionarComponente(comp);
                }
            }
        }

        return new CromossomoCircuito[] { filho1, filho2 };
    }

    // Mutação: adiciona/remove componentes aleatoriamente
    private void mutacao(CromossomoCircuito individuo) {
        if (random.nextBoolean()) {
            // Mutação de adição
            Componente compAdicionar = componentesDisponiveis.get(random.nextInt(componentesDisponiveis.size()));
            if (!individuo.getComponentesSelecionados().contains(compAdicionar)) {
                individuo.adicionarComponente(compAdicionar);
            }
        } else {
            // Mutação de remoção
            if (!individuo.getComponentesSelecionados().isEmpty()) {
                List<Componente> componentes = individuo.getComponentesSelecionados();
                Componente compRemover = componentes.get(random.nextInt(componentes.size()));
                individuo.removerComponente(compRemover);
            }
        }
    }
}
