import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class ProgrammableCalculator implements ProgrammableCalculatorInterface {
	private BufferedReader myReader;
	private ProgrammableCalculatorInterface.LineReader input;
	private ProgrammableCalculatorInterface.LinePrinter output;
	private Map<Integer, String> programLines;
	private Map<String, Integer> variables;
	private Stack<Integer> subroutinesLineNumbers;
	private int currentLineNumber;
	private int maxLineNumber;

	public ProgrammableCalculator() {
		programLines = new HashMap<Integer, String>();
		variables = new HashMap<String, Integer>();
		subroutinesLineNumbers = new Stack<Integer>();
		maxLineNumber = 0;
		Map<Integer, String> mapka = new HashMap<>(10);
	}


	@Override
	public void programCodeReader(BufferedReader reader) {
	    this.myReader = reader;
	    
	    if (this.myReader != null) {
			try {
				String programLine;
		        while ((programLine = this.myReader.readLine()) != null) {
		        	if (programLine.isBlank() == false) {
		        		addLineToMap(programLine);
		        	}
		        }
		    } catch (IOException e) {
		    
		    }
		}
	}


	@Override
	public void setStdin(ProgrammableCalculatorInterface.LineReader input) {
		this.input = input;
	}


	@Override
	public void setStdout(ProgrammableCalculatorInterface.LinePrinter output) {
		this.output = output;
	}


	@Override
	public void run(int line) {
		currentLineNumber = line;
		String instructionLine;

		while (currentLineNumber <= maxLineNumber) {
			instructionLine = programLines.get(currentLineNumber);
			
			if (instructionLine != null) {
				String instructionName = instructionLine.split(" ")[0].toLowerCase();
				if (instructionName.equals("let")) {
		            executeLet(instructionLine);
		        } else if (instructionName.equals("print")) {
		            executePrint(instructionLine);
		        } else if (instructionName.equals("goto")) {
		            executeGoto(instructionLine);
		        } else if (instructionName.equals("end")) {
		            executeEnd();
		        } else if (instructionName.equals("if")) {
		            executeIf(instructionLine);
		        } else if (instructionName.equals("input")) {
		            executeInput(instructionLine);
		        } else if (instructionName.equals("gosub")) {
		        	executeGosub(instructionLine);
		        } else if (instructionName.equals("return")) {
		        	executeReturn(instructionLine);
		        }

			} else {
				++currentLineNumber;
			}
		}
	}


	private void addLineToMap(String line) {
		String[] array = line.split(" ", 2);
		int lineNumber = Integer.parseInt(array[0]);
		String lineInstructions = array[1];
		maxLineNumber = Math.max(maxLineNumber, lineNumber);
		this.programLines.put(lineNumber, lineInstructions);
	}
	
	
	private void executeReturn(String instructionLine) {
		currentLineNumber = subroutinesLineNumbers.pop();
	}
	
	
	private void executeGosub(String instructionLine) {
		subroutinesLineNumbers.push(++currentLineNumber);
		currentLineNumber = Integer.parseInt(instructionLine.split(" ")[1]);
	}


	private void executeInput(String instructionLine) {
		variables.put(instructionLine.split(" ")[1], Integer.parseInt(input.readLine()));
		++currentLineNumber;
	}

	
	private void executeIf(String instructionLine) {
	    String[] array = instructionLine.split(" ");
	    int a = parseValue(array[1]);
	    String comparison = array[2];
	    int b = parseValue(array[3]);

	    if (comparison.equals("=") && (a == b)) {
	        currentLineNumber = Integer.parseInt(array[5]);
	    } else if (comparison.equals("<") && (a < b)) {
	        currentLineNumber = Integer.parseInt(array[5]);
	    } else if (comparison.equals(">") && (a > b)) {
	        currentLineNumber = Integer.parseInt(array[5]);
	    } else {
	        ++currentLineNumber;
	    }

	}


	private void executeEnd() {
		currentLineNumber = Integer.MAX_VALUE;
	}


	private void executeGoto(String instructionLine) {
		currentLineNumber = Integer.parseInt(instructionLine.split(" ")[1]);
	}


	private void executePrint(String instructionLine) {
	    String printStatement = instructionLine.split(" ", 2)[1];

	    if (printStatement.startsWith("\"")) {
	        output.printLine(printStatement.substring(1, printStatement.length() - 1));
	    } else {
	        output.printLine(String.valueOf(parseValue(printStatement)));
	    }

	    ++currentLineNumber;
	}


	private void executeLet(String instructionLine) {
		String[] array = instructionLine.split(" ");
		
	    String variableName = array[1];
	    int variableValue = calculateValue(array);
	    variables.put(variableName, variableValue);
	    ++currentLineNumber;
	}


	private int calculateValue(String[] array) {
	    if (array.length == 4) {
	        return parseValue(array[3]);
	    } else {
	        int a = parseValue(array[3]);

	        String operator = array[4];

	        int b = parseValue(array[5]);

	        if (a != Integer.MAX_VALUE && b != Integer.MAX_VALUE) {
	        	if (operator.equals("+")) {
	        	    return a + b;
	        	} else if (operator.equals("-")) {
	        	    return a - b;
	        	} else if (operator.equals("*")) {
	        	    return a * b;
	        	} else if (operator.equals("/")) {
	        	    return a / b;
	        	}

	        }
	    }
		return Integer.MAX_VALUE;
	}


	private int parseValue(String operand) {
	    try {
	        return Integer.parseInt(operand);
	    } catch (NumberFormatException e) {
	        return variables.getOrDefault(operand, Integer.MAX_VALUE);
	    }
	}

}


