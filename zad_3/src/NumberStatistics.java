import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class NumberStatistics implements Statistics {
	private int side_length;
	private Map<Integer, Set<Position>> num_positions;
	
	
	@Override
	public void sideLength(int length) {
		this.side_length = length;
	}

	@Override
	public void addNumbers(Map<Integer, Set<Position>> numberPositions) {
		this.num_positions = numberPositions;
	}

	@Override
	public Map<Integer, Map<Integer, Integer>> neighbours(Position position, int maxDistanceSquared) {
		int pos_col = position.col();
		int pos_row = position.row();
		Map<Integer, Map<Integer, Integer>> result = new HashMap<Integer, Map<Integer, Integer>>();
		
		for (Map.Entry<Integer, Set<Position>> entry : this.num_positions.entrySet()) {
	        int value = entry.getKey();
	        // utworzenie wewnetrznej mapy dla kazdej nowej liczby
	        Map<Integer, Integer> inner_map = new HashMap<>();

	        for (Position curr_pos : entry.getValue()) {
	        	// obliczamy najkrotsza odleglosc wzdluz osi y z uwzglednieniem geometrii torusa
				int y_distance = Math.min(this.side_length - Math.abs(pos_row - curr_pos.row()), Math.abs(pos_row - curr_pos.row()));
	        	
	        	// obliczamy najkrotsza odleglosc wzdluz osi x z uwzglednieniem geometrii torusa
				int x_distance = Math.min(this.side_length - Math.abs(pos_col - curr_pos.col()), Math.abs(pos_col - curr_pos.col()));
				int distance_squared = y_distance * y_distance + x_distance * x_distance;

	            if (distance_squared <= maxDistanceSquared) {
	            	if (inner_map.get(distance_squared) == null)
	            		inner_map.put(distance_squared, 1);
	            	else
	            		inner_map.put(distance_squared, inner_map.get(distance_squared) + 1);
	            }
	        }
	        
	        result.put(value, inner_map);
	    }
		
	    return result;
	    
	}
}