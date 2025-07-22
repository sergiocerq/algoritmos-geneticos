import java.util.*;

/**
 * Classe para gerar componentes eletrônicos de exemplo
 */
public class GeradorComponentes {
  private final static Random random = new Random(42);

  // Gera uma lista de componentes eletrônicos diversos para testar
  public static List<Componente> gerarComponentesExemplo() {
    List<Componente> componentes = new ArrayList<>();
    int id = 1;

    // Microcontroladores
    componentes.add(new Componente(id++, "Microcontrolador_ARM_Cortex_M4", 45.0, 0.85, 8.5));
    componentes.add(new Componente(id++, "Microcontrolador_ESP32", 25.0, 0.75, 6.0));
    componentes.add(new Componente(id++, "Microcontrolador_Arduino_Nano", 15.0, 0.65, 4.2));
    componentes.add(new Componente(id++, "Microcontrolador_STM32", 35.0, 0.80, 7.1));

    // Sensores
    componentes.add(new Componente(id++, "Sensor_Acelerometro", 12.0, 0.90, 2.3));
    componentes.add(new Componente(id++, "Sensor_Giroscopio", 18.0, 0.88, 3.1));
    componentes.add(new Componente(id++, "Sensor_Temperatura", 8.0, 0.95, 1.5));
    componentes.add(new Componente(id++, "Sensor_Proximidade", 22.0, 0.82, 4.0));
    componentes.add(new Componente(id++, "Sensor_Pressao", 28.0, 0.78, 3.8));
    componentes.add(new Componente(id++, "Sensor_Luminosidade", 10.0, 0.92, 2.1));

    // Módulos de Conectividade
    componentes.add(new Componente(id++, "Modulo_WiFi_802.11ac", 32.0, 0.70, 6.5));
    componentes.add(new Componente(id++, "Modulo_Bluetooth_5.0", 18.0, 0.85, 3.2));
    componentes.add(new Componente(id++, "Modulo_5G", 85.0, 0.60, 12.0));
    componentes.add(new Componente(id++, "Modulo_LoRa", 25.0, 0.88, 4.7));
    componentes.add(new Componente(id++, "Modulo_NFC", 15.0, 0.90, 2.8));

    // Componentes de Alimentação
    componentes.add(new Componente(id++, "Regulador_3V3_LDO", 5.0, 0.75, 2.0));
    componentes.add(new Componente(id++, "Regulador_5V_Switching", 12.0, 0.92, 4.5));
    componentes.add(new Componente(id++, "Conversor_DC_DC", 20.0, 0.88, 6.2));
    componentes.add(new Componente(id++, "Carregador_Bateria_Li_Ion", 18.0, 0.85, 5.1));

    // Componentes de Interface
    componentes.add(new Componente(id++, "Display_OLED_128x64", 24.0, 0.70, 8.0));
    componentes.add(new Componente(id++, "Display_LCD_16x2", 12.0, 0.60, 6.5));
    componentes.add(new Componente(id++, "LED_RGB", 3.0, 0.85, 1.2));
    componentes.add(new Componente(id++, "Buzzer_Piezo", 4.0, 0.80, 2.5));

    // Memória
    componentes.add(new Componente(id++, "Flash_Memory_16MB", 8.0, 0.90, 2.8));
    componentes.add(new Componente(id++, "EEPROM_64KB", 3.0, 0.95, 1.5));
    componentes.add(new Componente(id++, "RAM_SRAM_256KB", 12.0, 0.88, 3.2));

    // Componentes Passivos Críticos
    componentes.add(new Componente(id++, "Crystal_32MHz", 2.0, 0.98, 1.0));
    componentes.add(new Componente(id++, "Capacitor_Supercap", 15.0, 0.85, 4.0));
    componentes.add(new Componente(id++, "Indutor_Power", 6.0, 0.90, 2.5));

    // Componentes de Segurança
    componentes.add(new Componente(id++, "Chip_Criptografia", 35.0, 0.80, 5.5));
    componentes.add(new Componente(id++, "TPM_Security", 28.0, 0.85, 4.2));

    return componentes;
  }

  // Gera os componentes de maneira aleatória
  public static List<Componente> gerarComponentesAleatorios(int quantidade) {
    List<Componente> componentes = new ArrayList<>();
    String[] tipos = { "Sensor", "Modulo", "Regulador", "Interface", "Memoria", "Processador" };

    for (int i = 0; i < quantidade; i++) {
      String tipo = tipos[random.nextInt(tipos.length)] + "_" + (i + 100);
      double custo = 5.0 + random.nextDouble() * 90.0;
      double eficiencia = 0.5 + random.nextDouble() * 0.5;
      double tamanho = 1.0 + random.nextDouble() * 15.0;

      componentes.add(new Componente(1000 + i, tipo, custo, eficiencia, tamanho));
    }

    return componentes;
  }

  // Exibe informações sobre todos os componentes disponíveis
  public static void exibirComponentesDisponiveis(List<Componente> componentes) {
    System.out.println("=== COMPONENTES DISPONÍVEIS ===");

    Map<String, List<Componente>> componentesPorTipo = new HashMap<>();

    for (Componente comp : componentes) {
      String tipo = comp.getTipo().split("_")[0];
      componentesPorTipo.computeIfAbsent(tipo, k -> new ArrayList<>()).add(comp);
    }

    for (Map.Entry<String, List<Componente>> entry : componentesPorTipo.entrySet()) {
      System.out.println("\n" + entry.getKey() + ":");
      for (Componente comp : entry.getValue()) {
        System.out.println("  " + comp);
      }
    }

    System.out.println("\nTotal de componentes: " + componentes.size());

    double custoMedio = componentes.stream().mapToDouble(Componente::getCusto).average().orElse(0);
    double eficienciaMedia = componentes.stream().mapToDouble(Componente::getEficienciaEnergetica).average().orElse(0);
    double tamanhoMedio = componentes.stream().mapToDouble(Componente::getTamanho).average().orElse(0);

    System.out.printf("Estatísticas: Custo médio=%.2f, Eficiência média=%.3f, Tamanho médio=%.2f%n",
        custoMedio, eficienciaMedia, tamanhoMedio);
  }
}
