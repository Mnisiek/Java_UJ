
public class CalculatorTest {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();

        // Test 1: Ustawianie akumulatora i pobieranie jego wartości
        calculator.setAccumulator(10);
        System.out.println("Aktualna wartość akumulatora: " + calculator.getAccumulator()); // Powinno być 10

        // Test 2: Dodawanie i odejmowanie od akumulatora
        calculator.addToAccumulator(5);
        System.out.println("Po dodaniu 5: " + calculator.getAccumulator()); // Powinno być 15

        calculator.subtractFromAccumulator(3);
        System.out.println("Po odjęciu 3: " + calculator.getAccumulator()); // Powinno być 12

        // Test 3: Operacje na pamięci
        calculator.accumulatorToMemory(0);
        calculator.setAccumulator(0); // Resetowanie akumulatora

        calculator.addMemoryToAccumulator(0);
        System.out.println("Wartość z pamięci po dodaniu: " + calculator.getAccumulator()); // Powinno być 12

        calculator.subtractMemoryFromAccumulator(0);
        System.out.println("Wartość z pamięci po odjęciu: " + calculator.getAccumulator()); // Powinno być 0

        // Test 4: Operacje na stosie
        calculator.pushAccumulatorOnStack();
        calculator.setAccumulator(99); // Resetowanie akumulatora

        calculator.pullAccumulatorFromStack();
        System.out.println("Wartość po zdjęciu ze stosu: " + calculator.getAccumulator()); // Powinno być 0

        // Test 5: Resetowanie kalkulatora
        calculator.setAccumulator(42);
        calculator.reset();
        System.out.println("Po resecie, wartość akumulatora: " + calculator.getAccumulator()); // Powinno być 0
    }
}

