# 🧬 Algoritmo Genético para Otimização de Circuitos Eletrônicos


Aluno: Sérgio Cerqueira Santos  
Disciplina: INF032 - Inteligência Artificial  
Prof.: Marcelo Vera Cruz Diniz

## 📋 Visão Geral

A empresa "Tecnologia Inovadora" está desenvolvendo um dispositivo portátil para comunicação móvel e enfrenta o desafio de otimizar o design do circuito eletrônico. O problema envolve alguns objetivos conflitantes:

- **Maximizar** o número de componentes eletrônicos no circuito
- **Maximizar** a eficiência energética média do circuito  
- **Otimizar** o aproveitamento do orçamento disponível
- **Respeitar** limitações de orçamento (R$ 1.000,00)
- **Respeitar** limitações de espaço da placa-mãe (100 cm²)
- **Garantir** eficiência mínima do circuito (60%)

## 🏗️ Arquitetura do Sistema

### Estrutura de Classes

```
📁 src/
├── 🔧 Main.java                          # Classe principal e interface do usuário
├── 🧬 AlgoritmoGeneticoCircuito.java         # Implementação do algoritmo genético
├── 🔗 CromossomoCircuito.java                         # Representação docromossomo
├── ⚡ Componente.java                            # Modelo de componente eletrônico
└── 🏭 GeradorComponentes.java                  # Gerador de componentes para teste
```

## 🔬 Como Funciona o Algoritmo Genético

### 1. **Representação da Solução (Cromossomo)**

Cada solução é representada por um `CromossomoCircuito` que contém:
- Lista de componentes eletrônicos selecionados;
- Cálculos de custo, eficiência e tamanho totais;
- Valor de fitness que avalia a qualidade da solução;
- Verificação de viabilidade das restrições

```java
// Exemplo de cromossomo (solução)
CromossomoCircuito circuito = [
    Sensor_Temperatura,      // Custo: R$ 8,00  | Eficiência: 95% | Tamanho: 1.5 cm²
    Microcontrolador_ESP32,  // Custo: R$ 25,00 | Eficiência: 75% | Tamanho: 6.0 cm²
    Modulo_WiFi,            // Custo: R$ 32,00 | Eficiência: 70% | Tamanho: 6.5 cm²
    LED_RGB                 // Custo: R$ 12,00 | Eficiência: 85% | Tamanho: 2.0 cm²
];
```

### 2. **Função de Fitness (Avaliação)**

A função de fitness considera diferentes objetivos com pesos balanceados:

#### 📊 Componentes do Fitness (para soluções viáveis):
- **35%** - Número de componentes (maximizar densidade)
- **30%** - Eficiência energética (maximizar performance)
- **15%** - Aproveitamento do orçamento (otimizar recursos financeiros)
- **10%** - Aproveitamento do espaço (otimizar uso da placa)
- **+Bônus** - Densidade de componentes e relação eficiência/custo

#### ⚠️ Penalidades (para soluções inviáveis):
```java
// Penalidades aplicadas quando restrições são violadas
if (custoTotal > ORCAMENTO_MAXIMO) {
    penalidade += (custoTotal - ORCAMENTO_MAXIMO) / ORCAMENTO_MAXIMO * 2000;
}
if (tamanhoTotal > TAMANHO_MAXIMO_PLACA) {
    penalidade += (tamanhoTotal - TAMANHO_MAXIMO_PLACA) / TAMANHO_MAXIMO_PLACA * 2000;
}
if (eficienciaMedia < EFICIENCIA_MINIMA) {
    penalidade += (EFICIENCIA_MINIMA - eficienciaMedia) * 1000;
}
```

### 3. **Exemplo de cálculo do Fitness**

```
Cromossomo: [Sensor_Temp(8.0, 0.95, 1.5), ESP32(25.0, 0.75, 6.0), LED(3.0, 0.85, 1.2)]

Métricas Calculadas:
├── Custo Total = 8.0 + 25.0 + 3.0 = 36.0
├── Eficiência Média = (0.95 + 0.75 + 0.85) / 3 = 0.85
├── Tamanho Total = 1.5 + 6.0 + 1.2 = 8.7
└── Número Componentes = 3

Fatores Normalizados:
├── fatorNumeroComponentes = 3/50 = 0.06
├── fatorEficiencia = 0.85
├── fatorAproveitamentoOrcamento = 36/1000 = 0.036
└── fatorAproveitamentoEspaco = 8.7/100 = 0.087

Bônus Calculados:
├── bonusDensidade = min(3/8.7/5, 0.2) ≈ 0.069
└── bonusEficienciaCusto = min(0.85*3/36, 0.15) ≈ 0.071

Fitness Final = (0.06 * 0.35) + (0.85 * 0.30) + (0.036 * 0.15) + (0.087 * 0.10) + 0.069 + 0.071
              = 0.021 + 0.255 + 0.0054 + 0.0087 + 0.069 + 0.071
              ≈ 0.43 + bônus adicional (se >= 20 componentes e >= 80% eficiência)
```

### 4. **Processo Evolutivo**

#### 🔄 Loop Principal do Algoritmo:

```
INÍCIO
↓
1. Gerar População Inicial (100 indivíduos aleatórios)
   └── Para cada indivíduo:
       ├── Embaralhar componentes disponíveis
       ├── Adicionar componentes com 70% de probabilidade
       └── Respeitar restrições básicas de orçamento e espaço
↓
2. Para cada geração (200 iterações):
   ├── 📊 Avaliar fitness de todos os indivíduos
   ├── 📈 Ordenar por fitness (melhor → pior)
   ├── 🏆 Aplicar Elitismo (manter 10% melhores)
   ├── 🎯 Selecionar pais através de Torneio (tamanho 3)
   ├── 🤝 Aplicar Cruzamento Uniforme (80% chance)
   ├── 🎲 Aplicar Mutação Simples (15% chance)
   └── 🔄 Substituir população antiga
↓
3. Critério de Parada:
   ├── Fitness > 0.95 (solução excelente encontrada)
   └── Ou atingir 200 gerações
↓
4. Retornar melhor solução encontrada
```

#### 🎯 Operadores Genéticos Implementados:

**🏆 Seleção por Torneio (Tamanho 3):**
```java
private CromossomoCircuito selecaoTorneio(List<CromossomoCircuito> populacao, int tamanhoTorneio) {
    CromossomoCircuito melhor = null;
    
    // Escolhe 3 indivíduos aleatoriamente
    for (int i = 0; i < tamanhoTorneio; i++) {
        CromossomoCircuito candidato = populacao.get(random.nextInt(populacao.size()));
        // Mantém o melhor (maior fitness)
        if (melhor == null || candidato.getFitness() > melhor.getFitness()) {
            melhor = candidato;
        }
    }
    return melhor;
}
```

**🤝 Cruzamento Uniforme:**
```java
// Implementação
private CromossomoCircuito[] cruzamento(CromossomoCircuito pai1, CromossomoCircuito pai2) {
    // Coleta todos os componentes dos dois pais
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
            // Componente comum: 50% chance para cada filho
            if (random.nextBoolean()) {
                filho1.adicionarComponente(comp);
            } else {
                filho2.adicionarComponente(comp);
            }
        } else if (estaNoPai1) {
            // Componente só do pai1: 70% chance de herdar
            if (random.nextDouble() < 0.7) {
                filho1.adicionarComponente(comp);
            }
        } else {
            // Componente só do pai2: 70% chance de herdar
            if (random.nextDouble() < 0.7) {
                filho2.adicionarComponente(comp);
            }
        }
    }
    return new CromossomoCircuito[] { filho1, filho2 };
}
```

**🎲 Mutação Simples:**
```java
private void mutacao(CromossomoCircuito individuo) {
    if (random.nextBoolean()) {
        // 50% chance: Mutação de ADIÇÃO
        Componente compAdicionar = componentesDisponiveis.get(random.nextInt(componentesDisponiveis.size()));
        if (!individuo.getComponentesSelecionados().contains(compAdicionar)) {
            individuo.adicionarComponente(compAdicionar);
        }
    } else {
        // 50% chance: Mutação de REMOÇÃO
        if (!individuo.getComponentesSelecionados().isEmpty()) {
            List<Componente> componentes = individuo.getComponentesSelecionados();
            Componente compRemover = componentes.get(random.nextInt(componentes.size()));
            individuo.removerComponente(compRemover);
        }
    }
}
```

## 💾 Componentes Eletrônicos Disponíveis

### 📱 Tipos de Componentes:

| Categoria | Exemplos | Características Típicas |
|-----------|----------|------------------------|
| **Microcontroladores** | ARM Cortex-M4, ESP32, Arduino Nano, STM32 | Alto custo (R$ 15-45), Boa eficiência (65-85%) |
| **Sensores** | Temperatura, Acelerômetro, Proximidade, Pressão | Baixo custo (R$ 8-28), Alta eficiência (78-95%) |
| **Conectividade** | WiFi, Bluetooth, 5G, LoRa, Zigbee | Custo médio (R$ 18-85), Eficiência variável (60-90%) |
| **Alimentação** | Reguladores, Conversores DC-DC, Carregadores | Baixo custo (R$ 5-20), Eficiência crítica (75-92%) |
| **Interface** | Displays, LEDs, Botões, Buzzer | Custo variável (R$ 3-24), Funcionalidade específica |
| **Memória** | Flash, EEPROM, RAM | Baixo custo (R$ 3-12), Alta eficiência (88-95%) |
| **Segurança** | Chips criptografia, TPM | Alto custo (R$ 28-35), Boa eficiência (80-85%) |

### ⚡ Exemplos de Componentes:

```java
// Componentes de alta eficiência e baixo custo
new Componente(7, "Sensor_Temperatura", 8.0, 0.95, 1.5);
new Componente(10, "Sensor_Luminosidade", 10.0, 0.92, 2.1);

// Microcontroladores versáteis
new Componente(2, "Microcontrolador_ESP32", 25.0, 0.75, 6.0);
new Componente(1, "Microcontrolador_ARM_Cortex_M4", 45.0, 0.85, 8.5);

// Módulos conectividade
new Componente(11, "Modulo_WiFi_802.11ac", 32.0, 0.70, 6.5);
new Componente(13, "Modulo_5G", 85.0, 0.60, 12.0);

// Componentes básicos
new Componente(21, "LED_RGB", 3.0, 0.85, 1.2);
new Componente(25, "Crystal_32MHz", 2.0, 0.98, 1.0);
```

### 📊 Parâmetros Configuráveis Atuais:

```java
// Configurações implementadas no Main.java
int tamanhoPopulacao = 100;      // Indivíduos na população
int numeroGeracoes = 200;        // Gerações máximas
double taxaMutacao = 0.15;       // Taxa de mutação (15%)
double taxaCruzamento = 0.8;     // Taxa de cruzamento (80%)

// Restrições do Problema (CromossomoCircuito.java)
double ORCAMENTO_MAXIMO = 1000.0;        // R$ 1.000,00
double TAMANHO_MAXIMO_PLACA = 100.0;     // 100 cm²
double EFICIENCIA_MINIMA = 0.6;          // 60%
```

### 🎯 Exemplo de Saída da Aplicação:

```
===================================================================
    OTIMIZAÇÃO DE DESIGN DE CIRCUITOS - TECNOLOGIA INOVADORA
===================================================================
Problema: Desenvolver circuito para dispositivo portátil que:
• Minimize o custo total dos componentes
• Maximize a eficiência energética
• Respeite limitações de espaço da placa-mãe
• Mantenha-se dentro do orçamento disponível

RESTRIÇÕES DO PROBLEMA:
• Orçamento máximo: R$ 1000.00
• Tamanho máximo da placa: 100.00 cm²
• Eficiência mínima exigida: 60.0%

=== ALGORITMO GENÉTICO PARA OTIMIZAÇÃO DE CIRCUITOS ===
População inicial: 100
Gerações: 200
Taxa de mutação: 0.15
Taxa de cruzamento: 0.8

=== MELHOR SOLUÇÃO ENCONTRADA ===
Circuito[Componentes=18, Custo=623.45, Eficiência=0.782, Tamanho=67.3, Fitness=0.645, Viável=true]

======================================================================
                    ANÁLISE DETALHADA DA SOLUÇÃO
======================================================================
Fitness final: 0.6450
Solução viável: SIM ✓

MÉTRICAS PRINCIPAIS:
• Custo total: R$ 623.45 (62.3% do orçamento)
• Eficiência energética média: 78.2%
• Espaço utilizado: 67.30 cm² (67.3% da placa)
• Número de componentes: 18 de 40 disponíveis

DISTRIBUIÇÃO POR CATEGORIA:
• Sensor: 6 componentes (R$ 124.00)
• Microcontrolador: 2 componentes (R$ 70.00)
• Modulo: 4 componentes (R$ 178.00)
• Regulador: 3 componentes (R$ 45.00)
• Display: 1 componentes (R$ 24.00)
• LED: 1 componentes (R$ 3.00)
• Memoria: 1 componentes (R$ 12.00)
```

### 📊 Interpretação da resposta:

| Métrica | Exemplo | Interpretação |
|---------|---------|---------------|
| **Fitness** | 0.6450 | Boa solução (0.6+), Excelente (0.8+), Ótima (0.9+) |
| **Viabilidade** | SIM ✓ | Atende todas as restrições do problema |
| **Aproveitamento Orçamento** | 62.3% | Bom uso do orçamento, mas ainda há margem |
| **Eficiência Energética** | 78.2% | Boa solução, acima do mínimo (60%) |
| **Aproveitamento Espaço** | 67.3% | Boa utilização do espaço físico da placa |
| **Densidade Componentes** | 18/40 | 45% dos componentes selecionados |


## Conclusão

Esta implementação demonstra como os algoritmos genéticos podem ser aplicados em problemas de engenharia complexos, oferecendo uma solução prática para o desafio de otimização de design de circuitos da empresa "Tecnologia Inovadora".
