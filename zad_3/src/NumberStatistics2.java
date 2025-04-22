import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NumberStatistics2 implements Statistics {
	private int n_col;
	private int n_row;
	private int[][] plane;

	@Override
	public void sideLength(int length) {
		this.n_col = length;
		this.n_row = length;
	}

	@Override
	public void addNumbers(Map<Integer, Set<Position>> numberPositions) {
		plane = new int[n_col][n_row];
		
		for (Map.Entry<Integer, Set<Position>> entry : numberPositions.entrySet()) {
			for (Position position : entry.getValue()) {
				plane[position.col() - n_col][position.row() - n_row] = entry.getKey();
			}
		}
		
		for (int[] row: plane) {
			for (int element: row) {
				System.out.print(element);
			}
			System.out.println();
		}
	}

	@Override
	public Map<Integer, Map<Integer, Integer>> neighbours(Position position, int maxDistanceSquared) {
		int x_index = position.col() - 1;
		int y_index = position.row() - 1;
		int value;
		
		Map<Integer, Map<Integer, Integer>> result = new HashMap<Integer, Map<Integer,Integer>>();
		
		// uwzgledniamy zarowno liczby ujemne jak i dodatnie, co odpowiada przesunieciu w lewo/prawo
		for (int x = -maxDistanceSquared; x <= maxDistanceSquared; ++x) {
			// uwzgledniamy zarowno liczby ujemne jak i dodatnie, co odpowiada przesunieciu w dol/gore
	        for (int y = -maxDistanceSquared; y <= maxDistanceSquared; ++y) {
	        	if (x == 0 && y == 0)
	                continue;
	        	
	            int x_index_period = (x_index + x + this.n_col) % this.n_col;
	            int y_index_period = (y_index + y + this.n_row) % this.n_row;
	            
	            // dystans to suma kwadratow roznic wspolrzednych: nowego polozenia i polozenia punktu, ktory badamy
	            int current_distance = (x_index_period - x_index) * (x_index_period - x_index) + (y_index_period - y_index) * (y_index_period - y_index);
	            
	            if (current_distance <= maxDistanceSquared) {
	                value = this.plane[x_index_period][y_index_period];
	                if (value != 0) {
	                	Map<Integer, Integer> inner_map = result.get(value);
						if (inner_map == null) {
							inner_map = new HashMap<>();
							result.put(value, inner_map);
						}
					inner_map.put(current_distance, inner_map.getOrDefault(current_distance, 0) + 1);

					}
	    		}
	    	}
		}

		return result;
	}
	
//	@Override
//	public Map<Integer, Map<Integer, Integer>> neighbours(Position position, int maxDistanceSquared) {
//	    Map<Integer, Map<Integer, Integer>> distances = new HashMap<>();
//	    int[][] distancesArray = new int[n_col][n_row];
//
//	    for (int x = 0; x < n_col; ++x) {
//	        for (int y = 0; y < n_row; ++y) {
//	            distancesArray[x][y] = Integer.MAX_VALUE;
//	        }
//	    }
//
//	    int x_index = position.col();
//	    int y_index = position.row();
//
//	    for (int value : numberPositions.keySet()) {
//	        Set<Position> positions = numberPositions.get(value);
//	        for (Position neighbourPos : positions) {
//	            int x_distance = Math.min(Math.abs(x_index - neighbourPos.col()), n_col - Math.abs(x_index - neighbourPos.col()));
//	            int y_distance = Math.min(Math.abs(y_index - neighbourPos.row()), n_row - Math.abs(y_index - neighbourPos.row()));
//	            int squaredDistance = x_distance * x_distance + y_distance * y_distance;
//
//	            if (squaredDistance <= maxDistanceSquared && squaredDistance < distancesArray[neighbourPos.col()][neighbourPos.row()]) {
//	                distancesArray[neighbourPos.col()][neighbourPos.row()] = squaredDistance;
//	            }
//	        }
//	    }
//
//	    for (int x = 0; x < n_col; ++x) {
//	        for (int y = 0; y < n_row; ++y) {
//	            int squaredDistance = distancesArray[x][y];
//	            if (squaredDistance > 0 && squaredDistance <= maxDistanceSquared) {
//	                int value = plane[x][y];
//	                if (value != 0) {
//	                    Map<Integer, Integer> inner_map = distances.getOrDefault(value, new HashMap<>());
//	                    inner_map.put(squaredDistance, inner_map.getOrDefault(squaredDistance, 0) + 1);
//	                    distances.put(value, inner_map);
//	                }
//	            }
//	        }
//	    }
//
//	    return distances;
//	}


}


