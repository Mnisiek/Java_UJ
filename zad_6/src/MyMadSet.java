import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class MyMadSet implements MadSet {
	private List<Point> listOfPoints = new ArrayList<Point>();
	private DistanceMeasure measure;
	private double minAllowed;

	@Override
	public void setDistanceMeasure(DistanceMeasure measure) throws TooCloseException {
		this.measure = measure;
		List<Point> removedPoints = pointsToRemove();
		
		if (removedPoints.size() != 0) {
			for (Point element : removedPoints) {
				listOfPoints.remove(element);
			}
			throw new TooCloseException(removedPoints);
		}
	}


	@Override
	public void setMinDistanceAllowed(double minAllowed) throws TooCloseException {
		this.minAllowed = minAllowed;
		
		List<Point> removedPoints = pointsToRemove();
		
		if (removedPoints.size() != 0) {
			for (Point element : removedPoints) {
				listOfPoints.remove(element);
			}
			throw new TooCloseException(removedPoints);
		}
	}


	@Override
	public void addPoint(Point point) throws TooCloseException {
		List<Point> toClosePoints = new ArrayList<Point>();
		boolean toCloseToAdd = false;
		
		for (Point element : listOfPoints) {
			if (this.measure.distance(element, point) <= this.minAllowed) {
				toClosePoints.add(element);
				toCloseToAdd = true;
			}
		}
		
		if (toCloseToAdd == true) {
			toClosePoints.add(point);
			throw new TooCloseException(toClosePoints);
		} else {
			this.listOfPoints.add(point);
		}
	}


	@Override
	public List<Point> getPoints() {
		return listOfPoints;
	}


	@Override
	public List<Point> getSortedPoints(Point referencePoint) {
		List<Point> sortedListOfPoints = new ArrayList<Point>();
		List<PairOfElements> pointsWithDistances = new ArrayList<PairOfElements>();
		double calculatedDistance;
		
		for (Point element : listOfPoints) {
			calculatedDistance = this.measure.distance(referencePoint, element);
			pointsWithDistances.add(new PairOfElements(element, calculatedDistance));
		}
		
	    pointsWithDistances.sort(Comparator.comparing(PairOfElements::distance));
		
		for (PairOfElements element : pointsWithDistances) {
			sortedListOfPoints.add(element.point);
		}
		return sortedListOfPoints;
	}
	

	public List<Point> pointsToRemove() {
		List<Point> removedPoints = new ArrayList<Point>();
		for (int i = 0; i < listOfPoints.size(); ++i) {
			for (int j = i + 1; j < listOfPoints.size(); ++j) {
				Point a = listOfPoints.get(i);
				Point b = listOfPoints.get(j);
				if (this.measure.distance(a, b) <= this.minAllowed) {
					removedPoints.add(a);
					removedPoints.add(b);
				}
			}
		}
		return removedPoints;
	}
	
	
	public record PairOfElements(Point point, double distance) {
	}

}
