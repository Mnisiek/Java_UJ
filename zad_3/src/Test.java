//
//public class Test {
//	public static void main(String[] args) {
//		int[][] inputArray = {
//                {1, 0, 0, 0, 0},
//                {0, 2, 0, 0, 0},
//                {0, 0, 3, 0, 0},
//                {0, 0, 0, 4, 0},
//                {0, 0, 0, 0, 5}
//        };
//
//        int[][] duplicatedArray = duplicateArray(inputArray);
//
//        // Wyświetlenie zawartości zduplikowanej tablicy
//        for (int[] row : duplicatedArray) {
//            for (int num : row) {
//                System.out.print(num + " ");
//            }
//            System.out.println();
//        }
//    }
//
//    public static int[][] duplicateArray(int[][] inputArray) {
//        int rows = inputArray.length;
//        int cols = inputArray[0].length;
//        int[][] duplicatedArray = new int[rows * 3][cols * 3];
//
//        // Powielenie oryginalnej tablicy w odpowiednich miejscach
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                int value = inputArray[i][j];
//                
//                // Wypełnianie powielonych tablic w odpowiednich miejscach
//                for (int k = 0; k < 3; k++) {
//                    for (int l = 0; l < 3; l++) {
//                        duplicatedArray[i * 3 + k][j * 3 + l] = value;
//                    }
//                }
//            }
//        }
//
//        return duplicatedArray;
//    }
//}
