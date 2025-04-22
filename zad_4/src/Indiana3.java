import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Indiana3 implements Explorer {
	private int underwaterMovesAllowed;
	private PlayerController controller;
	
	private Map<Position, Integer> visitedFields;
	List<Direction> availableMoves;
	private Position currentPosition;
	private int currentUnderwaterMoves;
	private Direction currentDirection;
	

	public Indiana3() {
		visitedFields = new HashMap<Position, Integer>();
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
//			String fieldLabel = visitedFields.get(nextPosition());
////			String fieldLabel = visitedFields.get(currentPosition);
//			if (fieldLabel != null) {
//				if (fieldLabel.equals("Wall")) {
//					currentDirection = nextDirection();
//				} else if (fieldLabel.equals("OnFire")) {
//					currentPosition = nextPosition();
//					currentDirection = reverseDirection();
//				}
//			}
//
//			try {
//				this.controller.move(currentDirection);
//			} catch (Wall e) {
//				visitedFields.put(nextPosition(), "Wall");
//				continue;
//			} catch (OnFire e) {
//				visitedFields.put(nextPosition(), "OnFire");
//				continue;
//			} catch (Flooded e) {
//				if (currentUnderwaterMoves <= underwaterMovesAllowed / 2) {
//					++currentUnderwaterMoves;
//					currentPosition = nextPosition();
//				} else {
//					backFromWater();
//					continue;
//				}
//			} catch (Exit e) {
//				return;
//			}
			
			int fieldLabel = visitedFields.getOrDefault(currentPosition, 0);
			if (fieldLabel < 0) {
				if (fieldLabel == -1) {
					// stoję na polu "ściana" (ruch "na zapas"), więc wracam na poprzednie właściwe pole
					// zmieniam kierunek ruchu z tego poprzedniego pola, żeby nie trafić znowu na ścianę
					currentPosition = previousPosition();
					currentDirection = nextDirection();
				} else if (fieldLabel == -2) {
					// stoję na polu "ogień", więc zmieniam kierunek na przeciwny, aby wyjść z ognia
					// w kolejnym ruchu (move)
					currentDirection = reverseDirection();
				}
			} else {
					try {
						this.controller.move(currentDirection);
					} catch (OnFire e) {
						visitedFields.put(nextPosition(), -2);
						currentPosition = nextPosition();
					} catch (Flooded e) {
						if (currentUnderwaterMoves <= underwaterMovesAllowed / 2) {
							++currentUnderwaterMoves;
							currentPosition = nextPosition();
						} else {
							backFromWater();
							continue;
						}
					} catch (Wall e) {
						visitedFields.put(nextPosition(), -1);
						currentPosition = nextPosition();
					} catch (Exit e) {
						return;
					}
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
	
	
	public Position previousPosition() {
		currentDirection = reverseDirection();
		Position prevPos = currentDirection.step(currentPosition);
		currentDirection = reverseDirection();
		return prevPos;
	}
	
	
	public void backFromWater() {
		currentDirection = reverseDirection();
		int i = currentUnderwaterMoves;
		while (i > 0) {
			currentPosition = nextPosition();
			--i;
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
//	private int underwaterMovesAllowed;
//	private PlayerController controller;
//	
//	private Map<Position, Integer> visitedFields;
//	List<Direction> availableMoves;
//	private Position currentPosition;
//	private int currentUnderwaterMoves;
//	private Direction currentDirection;
//	
//
//	public Indiana() {
//		visitedFields = new HashMap<Position, Integer>();
//		currentPosition = new Position(3, 7);
//		currentUnderwaterMoves = 0;
//		currentDirection = Direction.NORTH;
//		
//		availableMoves = new ArrayList<Direction>();
//		availableMoves.add(Direction.NORTH);
//		availableMoves.add(Direction.WEST);
//		availableMoves.add(Direction.SOUTH);
//		availableMoves.add(Direction.EAST);
//	}
//
//
//	@Override
//	public void underwaterMovesAllowed(int moves) {
//		this.underwaterMovesAllowed = moves;
//	}
//
//
//	@Override
//	public void setPlayerController(PlayerController controller) {
//		this.controller = controller;
//	}
//
//
//	@Override
//	public void findExit() {
//		int fieldLabel;
//		int tempFieldLabel;
//		boolean inFire = false;
//		boolean backFromWater = false;
//		while (true) {
////			System.out.println(inFire);
////			System.out.println(backFromWater);
////			System.out.println(visitedFields);
////			System.out.println(currentUnderwaterMoves);
//
//			System.out.println("Warunek: " + (!inFire && !backFromWater));
//			if (!inFire && !backFromWater) {
//				fieldLabel = Integer.MAX_VALUE;
//				tempFieldLabel = visitedFields.getOrDefault(Direction.NORTH.step(currentPosition), 0);
//				if (tempFieldLabel >= 0 && tempFieldLabel < fieldLabel) {
//					currentDirection = Direction.NORTH;
//					fieldLabel = tempFieldLabel;
//				}
//				
//				tempFieldLabel = visitedFields.getOrDefault(Direction.WEST.step(currentPosition), 0);
//				if (tempFieldLabel >= 0 && tempFieldLabel < fieldLabel) {
//					currentDirection = Direction.WEST;
//					fieldLabel = tempFieldLabel;
//				}
//				
//				tempFieldLabel = visitedFields.getOrDefault(Direction.SOUTH.step(currentPosition), 0);
//				if (tempFieldLabel >= 0 && tempFieldLabel < fieldLabel) {
//					currentDirection = Direction.SOUTH;
//					fieldLabel = tempFieldLabel;
//				}
//				
//				tempFieldLabel = visitedFields.getOrDefault(Direction.EAST.step(currentPosition), 0);
//				if (tempFieldLabel >= 0 && tempFieldLabel < fieldLabel) {
//					currentDirection = Direction.EAST;
//					fieldLabel = tempFieldLabel;
//				System.out.println("field value: " + fieldLabel);
//				System.out.println("Current direction: " + currentDirection);
//				}
//				
//				visitedFields.put(currentPosition, visitedFields.getOrDefault(currentPosition, 0) + 1);
//			} else if (inFire){
//				inFire = false;
//				
//			} else if (backFromWater) {
//				--currentUnderwaterMoves;
//				
//				if (currentUnderwaterMoves == 0) {
//					backFromWater = false;
//				}
//			}
//			
//			try {
//				this.controller.move(currentDirection);
//			} catch (OnFire e) {
//				visitedFields.put(nextPosition(), -2);
//				currentPosition = nextPosition();
//				
//				currentDirection = reverseDirection();
//				inFire = true;
//			} catch (Flooded e) {
//				if ((currentUnderwaterMoves <= underwaterMovesAllowed / 2) && !backFromWater) {
//					++currentUnderwaterMoves;
//					currentPosition = nextPosition();
//				} else if (!backFromWater){
//					currentPosition = nextPosition();
//					currentDirection = reverseDirection();
//					backFromWater = true;
//				} else if (backFromWater) {
//					currentPosition = nextPosition();
//				}
//				continue;
//			} catch (Wall e) {
//				visitedFields.put(currentDirection.step(currentPosition), -1);
//				continue;
//			} catch (Exit e) {
//				return;
//			}
//			currentUnderwaterMoves = 0;
//			currentPosition = nextPosition();
//			System.out.println("Current position: " + currentPosition);
//		}
//	}
//
//	public Direction nextDirection() {
//		int index = (availableMoves.indexOf(currentDirection) + 1) % 4;
//		currentDirection = availableMoves.get(index);
//		return currentDirection;
//	}
//	
//
//	public Direction reverseDirection() {
//		int index = (availableMoves.indexOf(currentDirection) + 2) % 4;
//		currentDirection = availableMoves.get(index);
//		return currentDirection;
//		
//	}
//	
//
//	public Position nextPosition() {
//		return currentDirection.step(currentPosition);
//	}
//	
//	
//	public Position previousPosition() {
//		currentDirection = reverseDirection();
//		Position prevPos = currentDirection.step(currentPosition);
//		currentDirection = reverseDirection();
//		return prevPos;
//	}
//
//}





