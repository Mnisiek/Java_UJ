import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Indiana implements Explorer {
	private int underwaterMovesAllowed;
	private PlayerController controller;
	
	private Map<Position, Integer> visitedFields;
	private List<Direction> availableMoves;
	private Position currentPosition;
	private Direction currentDirection;
	

	public Indiana() {
		visitedFields = new HashMap<Position, Integer>();
		currentPosition = new Position(5, 3);
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
		boolean inFire = false;
		boolean inWater = false;
		boolean backFromWater = false;
		int currentUnderwaterMoves = 0;
		
		while (true) {
 
			if (!inFire && !backFromWater && !inWater) {
				// sprawdzamy otoczenie i wybieramy najlepszy kierunek
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
				
			} else if (inWater) {
				// w wodzie nie zmieniamy kierunku chyba ze doplynelismy do sciany
				if (visitedFields.getOrDefault(currentDirection.step(currentPosition), 0) < 0) {
					currentDirection = reverseDirection();
					backFromWater= true;			
					if (currentUnderwaterMoves <= 0) 
						backFromWater = false;
					else
						--currentUnderwaterMoves;
				}
				visitedFields.put(currentPosition, visitedFields.getOrDefault(currentPosition, 0) + 1);					
				
			} else if (inFire) {
				// zmienilismy kierunek na przeciwny, wiec najblizszy ruch wycofa nas z ognia
				inFire = false;
				
			} else if (backFromWater) {
				--currentUnderwaterMoves;				
				if (currentUnderwaterMoves <= 0) {
					backFromWater = false;
				}
			}
			
			try {
				this.controller.move(currentDirection);
				
			} catch (OnFire e) {

				visitedFields.put(nextPosition(), -2);
				currentPosition = nextPosition();	
				currentDirection = reverseDirection();
				inFire = true;
			    continue;
			} catch (Flooded e) {

				inWater = true;
				if ((currentUnderwaterMoves < underwaterMovesAllowed / 2) && !backFromWater) {					
					++currentUnderwaterMoves;
					currentPosition = nextPosition();				
				} else if (!backFromWater){				
					currentPosition = nextPosition();
					currentDirection = reverseDirection();
					backFromWater = true;
				} else if (backFromWater) {
					currentPosition = nextPosition();
				}
				continue;
			} catch (Wall e) {

				visitedFields.put(currentDirection.step(currentPosition), -1);
				continue;
			} catch (Exit e) {
				return;

			}
			inWater = false;
			currentUnderwaterMoves = 0;
			currentPosition = nextPosition();
		}
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

}




