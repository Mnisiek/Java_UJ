import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainTester {

    public static void main(String[] args) {
        SwMikolaj swMikolaj = new SwMikolaj();
        List<String> classList = new ArrayList<>();
        classList.add("ExampleClass1");
        classList.add("ExampleClass2");
        classList.add("ExampleClass3");
        Map<String, Integer> resultsMap = swMikolaj.inwentaryzacja(classList);
        System.out.println(resultsMap);
    }
}
