import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Main {

	public static void main(String[] args) {
		
		final int side_length = 9;
		
		Set<Position> positions_set_1 = new HashSet<Position>();
		positions_set_1.add(new Position(4, 3));
		positions_set_1.add(new Position(0, 4));
		positions_set_1.add(new Position(4, 5));
		positions_set_1.add(new Position(2, 6));
		positions_set_1.add(new Position(0, 8));
		
		Set<Position> positions_set_2 = new HashSet<Position>();
		positions_set_2.add(new Position(0, 3));
		positions_set_2.add(new Position(8, 0));
		positions_set_2.add(new Position(3, 3));
		positions_set_2.add(new Position(8, 4));
		
		Set<Position> positions_set_3 = new HashSet<Position>();
		positions_set_3.add(new Position(5, 6));
		positions_set_3.add(new Position(8, 8));
		
		Set<Position> positions_set_4 = new HashSet<Position>();
		positions_set_4.add(new Position(0, 0));
		positions_set_4.add(new Position(5, 4));
		positions_set_4.add(new Position(6, 5));
		
		Map<Integer, Set<Position>> num_pos = new HashMap<Integer, Set<Position>>();
		int val = 1;
		num_pos.put(val++, positions_set_1);
		num_pos.put(val++, positions_set_2);
		num_pos.put(val++, positions_set_3);
		num_pos.put(val++, positions_set_4);
		System.out.println(num_pos);
		
		NumberStatistics num_stats_1 = new NumberStatistics();
		num_stats_1.sideLength(side_length);
		num_stats_1.addNumbers(num_pos);
		
		Map<Integer, Map<Integer, Integer>> result;
		result = num_stats_1.neighbours(new Position(1, 1), 8);
		System.out.println(result);
		
	}
	
}
