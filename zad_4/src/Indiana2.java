import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Indiana2 implements Explorer {
	private int underwaterMovesAllowed;
	private PlayerController controller;
	
	private Map<Position, String> visitedFields;
	List<Direction> availableMoves;
	private Position currentPosition;
	private int currentUnderwaterMoves;
	private Direction currentDirection;
	

	public Indiana2() {
		visitedFields = new HashMap<Position, String>();
		currentPosition = new Position(0, 0);
		currentUnderwaterMoves = 0;
		currentDirection = Direction.NORTH;
		
		availableMoves = new ArrayList<Direction>();
		availableMoves.add(Direction.NORTH);
		availableMoves.add(Direction.WEST);
		availableMoves.add(Direction.SOUTH);
		availableMoves.add(Direction.EAST);
	}


	@Override
	public void underwaterMovesAllowed(int moves) {
		this.underwaterMovesAllowed = moves;
	}


	@Override
	public void setPlayerController(PlayerController controller) {
		this.controller = controller;
	}


	@Override
	public void findExit() {
		while (true) {
			if (visitedFields.get(nextPosition()).equals("Wall")) {
				currentDirection = nextDirection();
			} else if (visitedFields.get(nextPosition()).equals("Onfire")) {
				currentDirection = reverseDirection();
			}

			try {
				this.controller.move(currentDirection);
			} catch (Wall e) {
				visitedFields.put(nextPosition(), "Wall");
				continue;
			} catch (OnFire e) {
				visitedFields.put(nextPosition(), "OnFire");
				currentPosition = nextPosition();
				currentDirection = reverseDirection();
				continue;
			} catch (Flooded e) {
				if (currentUnderwaterMoves <= underwaterMovesAllowed / 2) {
					++currentUnderwaterMoves;
					currentPosition = nextPosition();
				} else {
					backFromWater();
					continue;
				}
			} catch (Exit e) {
				return;
			}
		}
	}
	

	public Direction nextDirection() {
		int index = (availableMoves.indexOf(currentDirection) + 1) % 4;
		currentDirection = availableMoves.get(index);
		return currentDirection;
	}
	

	public Direction reverseDirection() {
		int index = (availableMoves.indexOf(currentDirection) + 2) % 4;
		currentDirection = availableMoves.get(index);
		return currentDirection;
		
	}
	

	public Position nextPosition() {
		return currentDirection.step(currentPosition);
	}
	
	
	public void backFromWater() {
		currentDirection = reverseDirection();
		int i = currentUnderwaterMoves;
		while (i > 0) {
			currentPosition = nextPosition();
		}
		currentUnderwaterMoves = 0;
	}

}


//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//public class Indiana implements Explorer {
//	int underwater_moves_allowed;
//	PlayerController controller;
//
//	@Override
//	public void underwaterMovesAllowed(int moves) {
//		this.underwater_moves_allowed = moves;
//	}
//
//	@Override
//	public void setPlayerController(PlayerController controller) {
//		this.controller = controller;
//	}
//
//	@Override
//	public void findExit() {
//		Map<Position, String> visited_fields = new HashMap<Position, String>();
//		Position current_position = new Position(0, 0);
//		int current_underwater_moves = 0;
//		
//		
//		boolean must_step_back = false;
//		Direction next_direction = Direction.NORTH;
//		boolean must_go_forward = false;
//		
//		List<Direction> available_moves = new ArrayList<Direction>();
//		available_moves.add(Direction.NORTH);
//		available_moves.add(Direction.WEST);
//		available_moves.add(Direction.SOUTH);
//		available_moves.add(Direction.EAST);
//		
//		//sysout
//		while (true) {
//			for (Direction current_direction: available_moves) {
//				
//				if (visited_fields.get(current_direction.step(current_position)).equals("Wall")) {
//					continue;
//				} else if (visited_fields.get(current_direction.step(current_position)).equals("2")) {
//					continue;
//				}
//					
//				if (must_step_back) {
//					try {
//						this.controller.move(next_direction);
//					} catch (OnFire e) {
//						
//					} catch (Flooded e) {
//						// robimy to, co dla wody
//					} catch (Wall e) {
//						
//					} catch (Exit e) {
//
//					}
//					current_position = next_direction.step(current_position);
//					must_step_back = false;
//				}
//				
//				if (must_go_forward) {
//					try {
//						try {
//							this.controller.move(next_direction);
//						} catch (OnFire e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (Flooded e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (Wall e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						} catch (Exit e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					finally {
//						
//					}
//				}
//				try {
//					this.controller.move(current_direction);
//				} catch (Wall e) {
//					// dodajemy pole o znaczniku "Wall", nie zmieniamy obecnej pozycji gracza
//					visited_fields.put(current_direction.step(current_position), "Wall");
//					continue;
//				} catch (OnFire e) {
//					// dodajemy pole o znaczniku "Onfire"
//					visited_fields.put(current_direction.step(current_position), "OnFire");
//					current_position = current_direction.step(current_position);
//					next_direction = reverseDirection(current_direction);
//					must_step_back = true;
//					continue;
//				} catch (Flooded e) {
//					// dodajemy pole o znaczniku "Flooded", aktualizujemy obecna pozycje
//					visited_fields.put(current_direction.step(current_position), "Flooded");
//					if (current_underwater_moves < this.underwater_moves_allowed / 2)  {
//						++current_underwater_moves;
//						current_position = current_direction.step(current_position);
//					} else {
//						current_direction = reverseDirection(current_direction);
//						for (; current_underwater_moves > 0; --current_underwater_moves) {
//							try {
//								this.controller.move(current_direction);
//							} catch (OnFire e1) {
//
//							} catch (Flooded e1) {
//
//							} catch (Wall e1) {
//		
//							} catch (Exit e1) {
//
//							}
//							current_position = current_direction.step(current_position);
//							
//						}
//					}
//					continue;
//				} catch (Exit e) {
//					// znalezlismy wyjscie z labiryntu
//					return;
//				}
//				// dobrze
//				if (visited_fields.getOrDefault(current_position, "0").equals("1")) {
//					visited_fields.put(current_position, "2");
//				} else {
//					visited_fields.put(current_position, "1");
//				}
//				current_position = current_direction.step(current_position);
//				// dobrze
//			}
//		}
//		
//	}
//
//
//	Direction reverseDirection(Direction direction) {
//		if (direction.equals(Direction.NORTH))
//			return Direction.SOUTH;
//		else if (direction.equals(Direction.SOUTH))
//			return Direction.NORTH;
//		else if (direction.equals(Direction.EAST))
//			return Direction.WEST;
//		else if (direction.equals(Direction.WEST))
//			return Direction.EAST;
//		else
//			return Direction.NORTH;
//	}
//}
