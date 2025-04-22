import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NumberStatistics1 implements Statistics {
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
				plane[position.col()][position.row()] = entry.getKey();
			}
		}
	}

	@Override
	public Map<Integer, Map<Integer, Integer>> neighbours(Position position, int maxDistanceSquared) {
		int x_index = position.col();
		int y_index = position.row();
		int value;
		
		Map<Integer, Map<Integer, Integer>> result = new HashMap<Integer, Map<Integer,Integer>>();
		
		for (int x = -maxDistanceSquared; x <= maxDistanceSquared; ++x) {
	        for (int y = -maxDistanceSquared; y <= maxDistanceSquared; ++y) {
	        	int current_distance = x * x + y * y;
	        	
	            int x_index_periodic = (x_index + x + this.n_col) % this.n_col;
	            int y_index_periodic = (y_index + y + this.n_row) % this.n_row;
	            
	            if (current_distance <= maxDistanceSquared) {
	                value = this.plane[x_index_periodic][y_index_periodic];
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

}
