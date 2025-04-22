import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Indiana11 implements Explorer {
	private int underwaterMovesAllowed;
	private PlayerController controller;
	
	private Map<Position, Integer> visitedFields;
	List<Direction> availableMoves;
	private Position currentPosition;
	private int currentUnderwaterMoves;
	private Direction currentDirection;
	/////
	
	

	public Indiana11() {
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
		int fieldLabel;
		int tempFieldLabel;
		while (true) {
			fieldLabel = Integer.MAX_VALUE;
			tempFieldLabel = visitedFields.getOrDefault(Direction.NORTH.step(currentPosition), 0);
			if (tempFieldLabel >= 0 && tempFieldLabel < fieldLabel) {
				currentDirection = Direction.NORTH;
				fieldLabel = tempFieldLabel;
			}
			
			tempFieldLabel = visitedFields.getOrDefault(Direction.WEST.step(currentPosition), 0);
			if (tempFieldLabel >= 0 && tempFieldLabel < fieldLabel) {
				currentDirection = Direction.WEST;
				fieldLabel = tempFieldLabel;
			}
			
			tempFieldLabel = visitedFields.getOrDefault(Direction.SOUTH.step(currentPosition), 0);
			if (tempFieldLabel >= 0 && tempFieldLabel < fieldLabel) {
				currentDirection = Direction.SOUTH;
				fieldLabel = tempFieldLabel;
			}
			
			tempFieldLabel = visitedFields.getOrDefault(Direction.EAST.step(currentPosition), 0);
			if (tempFieldLabel >= 0 && tempFieldLabel < fieldLabel) {
				currentDirection = Direction.EAST;
				fieldLabel = tempFieldLabel;
			}
			
			visitedFields.put(currentPosition, visitedFields.getOrDefault(currentPosition, 0) + 1);
			
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
				}
			} catch (Wall e) {
				visitedFields.put(nextPosition(), -1);
			} catch (Exit e) {
				return;
			}

//			int fieldLabel = visitedFields.getOrDefault(currentPosition, 0);
//			if (fieldLabel < 0) {
//				if (fieldLabel == -1) {
//					// stoję na polu "ściana" (ruch "na zapas"), więc wracam na poprzednie właściwe pole
//					// zmieniam kierunek ruchu z tego poprzedniego pola, żeby nie trafić znowu na ścianę
//					currentPosition = previousPosition();
//					currentDirection = nextDirection();
//				} else if (fieldLabel == -2) {
//					// stoję na polu "ogień", więc zmieniam kierunek na przeciwny, aby wyjść z ognia
//					// w kolejnym ruchu (move)
//					currentDirection = reverseDirection();
//				}
//			} else {
//					try {
//						this.controller.move(currentDirection);
//					} catch (OnFire e) {
//						currentPosition = nextPosition();
//						visitedFields.put(currentPosition, -2);
//					} catch (Flooded e) {
//						if (currentUnderwaterMoves <= underwaterMovesAllowed / 2) {
//							++currentUnderwaterMoves;
//							currentPosition = nextPosition();
//						} else {
//							backFromWater();
//							continue;
//						}
//					} catch (Wall e) {
//						visitedFields.put(nextPosition(), -1);
//						currentPosition = nextPosition();
//					} catch (Exit e) {
//						return;
//					}
//				}
			
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

