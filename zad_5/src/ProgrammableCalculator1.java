
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ProgrammableCalculator1 implements ProgrammableCalculatorInterface {
    private BufferedReader reader;
    private ProgrammableCalculatorInterface.LineReader input;
    private ProgrammableCalculatorInterface.LinePrinter output;
    private Map<String, Integer> variables = new HashMap<String, Integer>();
    private Map<Integer, String> program_lines = new HashMap<Integer, String>();
    private int current_line_number = 0;

    public ProgrammableCalculator1() {
        linesToMap();
    }
    
    private void linesToMap() {
        String line;
        while ((line = input.readLine()) != null) {
		    String[] parts = line.split(" ", 2);
		    int lineNumber = Integer.parseInt(parts[0]);
		    program_lines.put(lineNumber, parts[1]);
		}
    }
    
    @Override
    public void programCodeReader(BufferedReader reader) {
        this.reader = reader;
    }

    @Override
    public void setStdin(LineReader input) {
        this.input = input;
    }

    @Override
    public void setStdout(LinePrinter output) {
        this.output = output;
    }

    @Override
    public void run(int line) {
    	String code_line;
    	int program_lines_size = program_lines.size();
        while (program_lines_size >= current_line_number) {
			code_line = program_lines.get(current_line_number);
			String[] components = code_line.split(" ", 2);
		    int line_number = Integer.parseInt(components[0]);

		    if (line_number == current_line_number) {
		        String instruction = components[1].toLowerCase();

		        if ("let".equals(instruction)) {
		            executeLet(components[1]);
		        } else if ("print".equals(instruction)) {
		            executePrint(components[1]);
		        } else if ("goto".equals(instruction)) {
		            executeGoto(components[1]);
		        } else if ("end".equals(instruction)) {
		            executeEnd();
		        } else if ("if".equals(instruction)) {
		            executeIf(components[1]);
		        } else if ("input".equals(instruction)) {
		            executeInput(components[1]);
		        }
		    }
		}
    }

    private void executeLet(String code) {
        String[] components = code.split("=", 2);
        // dodajemy do mapy
        variables.put(components[0].trim(), evaluateExpression(components[1].trim()));
        current_line_number++;
    }

    private void executePrint(String code) {
        code = code.trim();
        if (code.startsWith("\"")) {
        	// usuwa z tekstu cudzyslow na poczatku i na koncu
            output.printLine(code.substring(1, code.length() - 1));
        } else {
        	String variable_name = code.trim();
            int variable_value = variables.getOrDefault(variable_name, 0);
            output.printLine(Integer.toString(variable_value));
        }
        ++current_line_number;
    }

    private void executeGoto(String code) {
    	// przeskakujemy do lini o tym numerze
        current_line_number = Integer.parseInt(code.trim());
    }

    private void executeEnd() {
    	//  maksymalny numer lini kodu, dla typu int
        current_line_number = Integer.MAX_VALUE;
    }

    private void executeIf(String code) {
        String[] components = code.split(" ", 3);
        String variable_name = components[0].trim();
        String operator = components[1].trim();
        int value = evaluateExpression(components[2].trim());
        
        int variable_value = variables.getOrDefault(variable_name, 0);


        if (operator.equals("=") && variable_value == value) {
            current_line_number++;
        } else if (operator.equals("<") && variable_value < value) {
            current_line_number++;
        } else if (operator.equals(">") && variable_value > value) {
            current_line_number++;
        }
    }

    private void executeInput(String code) {
        String variable_name = code.trim();
        String inputString = input.readLine();
        int input_value = Integer.parseInt(inputString);
		variables.put(variable_name, input_value);
        current_line_number++;
    }

    private int evaluateExpression(String expression) {
        return Integer.parseInt(expression);
    }
}


//import java.io.BufferedReader;
//import java.util.HashMap;
//import java.util.Map;
//
//public class ProgrammableCalculator implements ProgrammableCalculatorInterface {
//	private BufferedReader reader;
//	private ProgrammableCalculatorInterface.LineReader input;
//	private ProgrammableCalculatorInterface.LinePrinter output;
//	private Map<Integer, String> lines = new HashMap<Integer, String>();
//	private Map<String, Integer> variables = new HashMap<String, Integer>();
//	int current_line = 0;
//
//	@Override
//	public void programCodeReader(BufferedReader reader) {
//		this.reader = reader;
//	}
//
//	@Override
//	public void setStdin(ProgrammableCalculatorInterface.LineReader input) {
//		this.input = input;
//	}
//
//	@Override
//	public void setStdout(ProgrammableCalculatorInterface.LinePrinter output) {
//		this.output = output;
//	}
//
//	@Override
//	public void run(int line) {
//		String input_line;
//		while ((input_line = input.readLine()) != null) {
//			
//		}
//	}
//
//}
