import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Indiana1 implements Explorer {
	private int underwater_moves_allowed;
	private PlayerController controller;

	@Override
	public void underwaterMovesAllowed(int moves) {
		this.underwater_moves_allowed = moves;
	}

	@Override
	public void setPlayerController(PlayerController controller) {
		this.controller = controller;
	}

	@Override
	public void findExit() {
		Map<Position, String> visited_fields = new HashMap<Position, String>();
		Position current_position = new Position(0, 0);
		int current_underwater_moves = 0;
		Direction current_direction = Direction.NORTH;
		int index = 0;
		
		List<Direction> available_moves = new ArrayList<Direction>();
		available_moves.add(Direction.NORTH);
		available_moves.add(Direction.WEST);
		available_moves.add(Direction.SOUTH);
		available_moves.add(Direction.EAST);
		
		//sysout
		while (true) {
				
			if (visited_fields.get(current_direction.step(current_position)).equals("Wall")) {
				index = (available_moves.indexOf(current_direction) + 1) % 4;
				current_direction = available_moves.get(index);
			} else if (visited_fields.get(current_direction.step(current_position)).equals("2")) {
				index = (available_moves.indexOf(current_direction) + 1) % 4;
				current_direction = available_moves.get(index);
			} else if (visited_fields.get(current_direction.step(current_position)).equals("Onfire")) {
				index = (available_moves.indexOf(current_direction) + 1) % 4;
				current_direction = available_moves.get(index);
			}
				
			try {
				this.controller.move(current_direction);
			} catch (Wall e) {
				// dodajemy pole o znaczniku "Wall", nie zmieniamy obecnej pozycji gracza
				visited_fields.put(current_direction.step(current_position), "Wall");
				continue;
			} catch (OnFire e) {
				// dodajemy pole o znaczniku "Onfire"
				visited_fields.put(current_direction.step(current_position), "OnFire");
				current_position = current_direction.step(current_position);
				current_direction = reverseDirection(current_direction);
				continue;
			} catch (Flooded e) {
//				 co z tą wodą?
				// dodajemy pole o znaczniku "Flooded", aktualizujemy obecna pozycje
				visited_fields.put(current_direction.step(current_position), "Flooded");
				if (current_underwater_moves < this.underwater_moves_allowed / 2)  {
					++current_underwater_moves;
					current_position = current_direction.step(current_position);
				} else {
					current_direction = reverseDirection(current_direction);
					current_position = current_direction.step(current_position);
						
					}

			// dobrze
			} catch (Exit e) {
				// znalezlismy wyjscie z labiryntu
				return;
			}
			
			if (visited_fields.getOrDefault(current_position, "0").equals("1")) {
				visited_fields.put(current_position, "2");
			} else {
				visited_fields.put(current_position, "1");
			}
			current_position = current_direction.step(current_position);
			// dobrze
		}
	}


	public Direction reverseDirection(Direction direction) {
		if (direction.equals(Direction.NORTH))
			return Direction.SOUTH;
		else if (direction.equals(Direction.SOUTH))
			return Direction.NORTH;
		else if (direction.equals(Direction.EAST))
			return Direction.WEST;
		else if (direction.equals(Direction.WEST))
			return Direction.EAST;
		else
			return Direction.NORTH;
	}
}
