import java.util.Scanner;

abstract class Veiculo {
    private String marca;
    private String modelo;
    private double preco;

    public Veiculo(String marca, String modelo, double preco) {
        this.marca = marca;
        this.modelo = modelo;
        this.preco = preco;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public double getPreco() {
        return preco;
    }
    public abstract double calcularPrecoDeVenda();
}
interface VeiculoComercial {
    double calcularImposto();
}
class Carro extends Veiculo {
    private int ano;

    public Carro(String marca, String modelo, double preco, int ano) {
        super(marca, modelo, preco);
        this.ano = ano;
    }

    public int getAno() {
        return ano;
    }

    @Override
    public double calcularPrecoDeVenda() {
        double depreciacao = ano * 100;
        double precoFinal = getPreco() - depreciacao;

        if (precoFinal >= 0) {
            return precoFinal;
        } else {
            return 0;
        }
    }
}
class Moto extends Veiculo {
    private boolean possuiPartidaEletrica;

    public Moto(String marca, String modelo, double preco, boolean possuiPartidaEletrica) {
        super(marca, modelo, preco);
        this.possuiPartidaEletrica = possuiPartidaEletrica;
    }

    public boolean isPossuiPartidaEletrica(String entrada) {
        entrada = entrada.toLowerCase(); // Converte a entrada para letras minúsculas para ser case-insensitive

        if (entrada.equals("1") || entrada.equals("sim") || entrada.equals("ss")) {
            return true;
        } else if (entrada.equals("0") || entrada.equals("nao") || entrada.equals("nn")) {
            return false;
        } else {
            // Valor de entrada não reconhecido, retorna false por padrão
            return false;
        }
    }

    @Override
    public double calcularPrecoDeVenda() {
        double preco = getPreco();
        if (possuiPartidaEletrica) {
            preco += 200; // Adiciona R$200 ao preço se tiver partida elétrica
        }
        return preco;
    }
}
class Caminhao extends Veiculo implements VeiculoComercial {
    private int capacidadeCarga;

    public Caminhao(String marca, String modelo, double preco, int capacidadeCarga) {
        super(marca, modelo, preco);
        this.capacidadeCarga = capacidadeCarga;
    }

    public int getCapacidadeCarga() {
        return capacidadeCarga;
    }

    @Override
    public double calcularImposto() {
        return getPreco() * 0.10; // imposto caminhao | 10% do preço
    }

    @Override
    public double calcularPrecoDeVenda() {
        return getPreco() - (capacidadeCarga * 10); // Preço de revenda caminhão, diminui 10 a cada kg de carga
    }
}
class Concessionaria {
    private String nome;
    private Veiculo[] veiculos;
    private int numVeiculos;

    public Concessionaria(String nome, int capacidadeMaxima) {
        this.nome = nome;
        veiculos = new Veiculo[capacidadeMaxima];
        numVeiculos = 0;
    }

    public void adicionarVeiculo(Veiculo veiculo) {
        if (numVeiculos < veiculos.length) {
            veiculos[numVeiculos] = veiculo;
            numVeiculos++;
        } else {
            System.out.println("Capacidade máxima atingida. Não é possível adicionar mais veículos.");
        }
    }

    public void listarVeiculos() {
        for (int i = 0; i < numVeiculos; i++) {
            Veiculo veiculo = veiculos[i];
            System.out.println("Marca: " + veiculo.getMarca());
            System.out.println("Modelo: " + veiculo.getModelo());
            System.out.println("Preço de Venda: R$" + veiculo.calcularPrecoDeVenda());
            if (veiculo instanceof VeiculoComercial) {
                VeiculoComercial veiculoComercial = (VeiculoComercial) veiculo;
                System.out.println("Imposto: R$" + veiculoComercial.calcularImposto());
            }
            System.out.println("---------------");
        }
    }
}

//===================INTERFACE===========================
interface ConcessionariaUI {
    void exibirMenu();
    void adicionarVeiculo();
    void listarVeiculos();
}

public class Main implements ConcessionariaUI {
    private Concessionaria concessionaria;
    private Scanner scanner;
    public Main() {
        concessionaria = new Concessionaria("Minha Concessionária", 3);
        scanner = new Scanner(System.in);
    }
    public static void main(String[] args) {
        Main programa = new Main();
        programa.executar();
    }
    public void executar() {
        boolean sair = false;

        while (!sair) {
            exibirMenu();
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    adicionarVeiculo();
                    break;
                case 2:
                    listarVeiculos();
                    break;
                case 3:
                    sair = true;
                    System.out.println("Obrigado por usar a Concessionária!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }

        scanner.close();
    }
    @Override
    public void exibirMenu() {
        System.out.println("== Menu da Concessionária ==");
        System.out.println("1. Adicionar Veículo");
        System.out.println("2. Listar Veículos");
        System.out.println("3. Sair");
        System.out.print("Escolha uma opção: ");
    }
    @Override
    public void adicionarVeiculo() {
        System.out.print("Digite a marca do veículo: ");
        String marca = scanner.nextLine();
        System.out.print("Digite o modelo do veículo: ");
        String modelo = scanner.nextLine();
        System.out.print("Digite o preço do veículo: ");
        double preco = scanner.nextDouble();
        scanner.nextLine();

        System.out.println("Selecione o tipo de veículo:");
        System.out.println("1. Carro");
        System.out.println("2. Moto");
        System.out.println("3. Caminhão");
        int tipo = scanner.nextInt();
        scanner.nextLine();

        switch (tipo) {
            case 1:
                System.out.print("Digite o ano do carro: ");
                int anoCarro = scanner.nextInt();
                scanner.nextLine();
                Carro carro = new Carro(marca, modelo, preco, anoCarro);
                concessionaria.adicionarVeiculo(carro);
                System.out.println("Carro adicionado com sucesso!");
                break;
            case 2:
                System.out.print("A moto possui partida elétrica? (true/false): ");
                boolean partidaEletrica = scanner.nextBoolean();
                scanner.nextLine();
                Moto moto = new Moto(marca, modelo, preco, partidaEletrica);
                concessionaria.adicionarVeiculo(moto);
                System.out.println("Moto adicionada com sucesso!");
                break;
            case 3:
                System.out.print("Digite a capacidade de carga do caminhão: ");
                int capacidadeCarga = scanner.nextInt();
                scanner.nextLine();
                Caminhao caminhao = new Caminhao(marca, modelo, preco, capacidadeCarga);
                concessionaria.adicionarVeiculo(caminhao);
                System.out.println("Caminhão adicionado com sucesso!");
                break;
            default:
                System.out.println("Opção inválida. O veículo não foi adicionado.");
        }
    }

    @Override
    public void listarVeiculos() {
        System.out.println("== Veículos na Concessionária ==");
        concessionaria.listarVeiculos();
    }
}
