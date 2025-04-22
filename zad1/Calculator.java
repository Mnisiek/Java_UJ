
public class Calculator extends CalculatorOperations {
	
	private int[] memory;
	private int[] stack;
	private int stack_index = 0;
	private int accumulator = 0;
	
	// konstruktor bezargumentowy
	public Calculator() {
		memory = new int[MEMORY_SIZE]; // domyslnie zainicjalizowana zerami
		stack = new int[STACK_SIZE]; // domyslnie zainicjalizowana zerami
		stack_index = 0;
		accumulator = 0;
	}
	
	@Override
	public void setAccumulator(int value) {
		accumulator = value;
	}

	@Override
	public int getAccumulator() {
		return accumulator;
	}

	@Override
	public int getMemory(int index) {
		return memory[index];
	}

	@Override
	public void accumulatorToMemory(int index) {
		memory[index] = accumulator;
	}

	@Override
	public void addToAccumulator(int value) {
		accumulator += value;
	}

	@Override
	public void subtractFromAccumulator(int value) {
		accumulator -= value;
	}

	@Override
	public void addMemoryToAccumulator(int index) {
		accumulator += memory[index];
	}

	@Override
	public void subtractMemoryFromAccumulator(int index) {
		accumulator -= memory[index];
	}

	@Override
	public void reset() {
		int i;
		for (i = 0; i < MEMORY_SIZE; i++)
			memory[i] = 0;
		
		for (i = 0; i < STACK_SIZE; i++)
			stack[i] = 0;
		
		accumulator = 0;
		stack_index = 0;
	}

	@Override
	public void exchangeMemoryWithAccumulator(int index) {
		int temp;
		temp = memory[index];
		memory[index] = accumulator;
		accumulator = temp;
	}

	@Override
	public void pushAccumulatorOnStack() {
		stack[stack_index] = accumulator;
		stack_index++;
	}

	@Override
	public void pullAccumulatorFromStack() {
		stack_index--;
		accumulator = stack[stack_index];
	}
	
}
