import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class ProgrammableCalculatorTest {

    public static void main(String[] args) {
        runTest();negativeTest();caseTest();letTest();let2Test();printTest();gotoTest();endTest();ifTest();inputTest();
    }

    private static void runTest() {
        String program = """
                10 LET count = 0
                20 PRINT "Hello, World!"
                25 LET count = count + 1
                30 IF count < 10 GOTO 20
                """;

        var calc = new ProgrammableCalculator();
        var stdout = new StringLinePrinter();
        calc.setStdin(new StringLineReader(""));
        calc.setStdout(stdout);
        calc.programCodeReader(new BufferedReader(new StringReader(program)));

        calc.run(10);

        assertListEquals(stdout.lines, List.of("Hello, World!", "Hello, World!", "Hello, World!", "Hello, World!",
                "Hello, World!", "Hello, World!", "Hello, World!", "Hello, World!", "Hello, World!", "Hello, World!"));
    }

    private static void negativeTest() {String program = """
                100000 LET count = -10
                200000 PRINT "Hello, World!"
                300000 LET count = count - -1
                400000 IF count < -5 GOTO 200000
                """;

        var calc = new ProgrammableCalculator();
        var stdout = new StringLinePrinter();
        calc.setStdin(new StringLineReader(""));
        calc.setStdout(stdout);
        calc.programCodeReader(new BufferedReader(new StringReader(program)));

        calc.run(100000);

        assertListEquals(stdout.lines, List.of("Hello, World!", "Hello, World!", "Hello, World!", "Hello, World!", "Hello, World!"));
    }

    private static void caseTest() {String program = """
                110 lEt a = 1
                111 prInt a
                112 eNd
                """;

        var calc = new ProgrammableCalculator();
        var stdout = new StringLinePrinter();
        calc.setStdin(new StringLineReader(""));
        calc.setStdout(stdout);
        calc.programCodeReader(new BufferedReader(new StringReader(program)));

        calc.run(110);

        assertListEquals(stdout.lines, List.of("1"));
    }

    private static void letTest() {String program = """
                190 LET a = 1
                195 LET b = 2
                200 LET a = a + 1
                210 LET a = a * b
                """;

        var calc = new ProgrammableCalculator();
        var stdout = new StringLinePrinter();
        calc.setStdin(new StringLineReader(""));
        calc.setStdout(stdout);
        calc.programCodeReader(new BufferedReader(new StringReader(program)));

        calc.run(190);

        assert stdout.lines.isEmpty();
    }

    private static void let2Test() {String program = """
                1 INPUT a
                22 LET a = a + 1
                333 LET bb = 123 * a
                4444 LET ccc = 7 * 123
                55555 LET dddd = bb - ccc
                666666 PRINT dddd
                """;

        var calc = new ProgrammableCalculator();
        var stdout = new StringLinePrinter();
        calc.setStdin(new StringLineReader("1"));
        calc.setStdout(stdout);
        calc.programCodeReader(new BufferedReader(new StringReader(program)));

        calc.run(1);

        assertListEquals(stdout.lines, List.of("-615"));
    }

    private static void printTest() {String program = """
                10 LET a = 100
                11 PRINT "Czesc a = "
                12 PRINT a""";

        var calc = new ProgrammableCalculator();
        var stdout = new StringLinePrinter();
        calc.setStdin(new StringLineReader(""));
        calc.setStdout(stdout);
        calc.programCodeReader(new BufferedReader(new StringReader(program)));

        calc.run(10);

        assertListEquals(stdout.lines, List.of("Czesc a = ", "100"));
    }

    private static void gotoTest() {String program = """
                5 GOTO 8
                6 PRINT "NIE"
                8 GOTO 30
                10 PRINT "CZESC"
                20 GOTO 5
                30 PRINT "AAAAA"
                """;

        var calc = new ProgrammableCalculator();
        var stdout = new StringLinePrinter();
        calc.setStdin(new StringLineReader(""));
        calc.setStdout(stdout);
        calc.programCodeReader(new BufferedReader(new StringReader(program)));

        calc.run(10);

        assertListEquals(stdout.lines, List.of("CZESC", "AAAAA"));
    }

    private static void endTest() {String program = """
                10 PRINT "A"
                20 END
                30 PRINT "B"
                """;

        var calc = new ProgrammableCalculator();
        var stdout = new StringLinePrinter();
        calc.setStdin(new StringLineReader(""));
        calc.setStdout(stdout);
        calc.programCodeReader(new BufferedReader(new StringReader(program)));

        calc.run(10);

        assertListEquals(stdout.lines, List.of("A"));
    }

    private static void ifTest() {String program = """
                10 LET a = 10
                20 LET b = 20
                30 IF a < b GOTO 50
                40 PRINT "a nie jest < od b"
                45 GOTO 60
                50 PRINT "a < od b"
                60 END
                """;

        var calc = new ProgrammableCalculator();
        var stdout = new StringLinePrinter();
        calc.setStdin(new StringLineReader(""));
        calc.setStdout(stdout);
        calc.programCodeReader(new BufferedReader(new StringReader(program)));

        calc.run(10);

        assertListEquals(stdout.lines, List.of("a < od b"));
    }

    private static void inputTest() {String program = """
                10 INPUT a
                20 INPUT b
                30 IF a < b GOTO 50
                40 PRINT "a nie jest < od b"
                45 GOTO 60
                50 PRINT "a < od b"
                60 END
                """;

        var calc = new ProgrammableCalculator();
        var stdout = new StringLinePrinter();
        calc.setStdin(new StringLineReader("2\n2"));
        calc.setStdout(stdout);
        calc.programCodeReader(new BufferedReader(new StringReader(program)));

        calc.run(10);

        assertListEquals(stdout.lines, List.of("a nie jest < od b"));
    }

    private static void assertListEquals(List<String> actual, List<String> expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("Lists are not equal. Actual: " + actual + ", Expected: " + expected);
        }
    }
}

class StringLineReader implements ProgrammableCalculatorInterface.LineReader {
    List<String> lines;

    StringLineReader(String input) {
        lines = new ArrayList<>(input.lines().toList());
    }

    @Override
    public String readLine() {
        assert !lines.isEmpty();
        return lines.remove(0);
    }
}

class StringLinePrinter implements ProgrammableCalculatorInterface.LinePrinter {
    List<String> lines = new ArrayList<>();

    @Override
    public void printLine(String line) {
        lines.add(line);
    }
}
