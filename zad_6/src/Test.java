
class DM1 implements DistanceMeasure { // norma taksówkowa
    @Override
    public double distance(Point a, Point b) {
        double xDiff = a.x() - b.x();
        double yDiff = a.y() - b.y();
        return Math.abs(xDiff) + Math.abs(yDiff);
    }
}
class DM2 implements DistanceMeasure { // norma Euklidesowa
    @Override
    public double distance(Point a, Point b) {
        double xDiff = a.x() - b.x();
        double yDiff = a.y() - b.y();
        return Math.sqrt(xDiff * xDiff + yDiff * yDiff);
    }
}

public class Test {
    public static void main(String[] args) {
        MyMadSet ms = new MyMadSet();
        try {
            ms.setDistanceMeasure(new DM2()); // teraz jesteśmy w normie euklidesowej
            ms.setMinDistanceAllowed(2);

            // dodajemy punkty, tu nie powinno się nic zepsuć
            ms.addPoint(new Point(4.1, 4.1));
            ms.addPoint(new Point(1,1));
            ms.addPoint(new Point(2, 4));
            ms.addPoint(new Point(8, 8));
            ms.addPoint(new Point(2, 7));

            // teraz po prostu zwracamy listę punktów
            System.out.println("Lista punktów:");
            System.out.println(ms.getPoints());
            System.out.println();

            // teraz zwracamy posortowaną listę punktów co do odległości względem (2,4)
            System.out.println("Posortowane względem (2, 4):");
            System.out.println(ms.getSortedPoints(new Point(2, 4)));
            System.out.println("A powinno być: ");
            System.out.println("[Point[x=2.0, y=4.0], Point[x=4.1, y=4.1], Point[x=2.0, y=7.0], Point[x=1.0, y=1.0], Point[x=8.0, y=8.0]]");
            System.out.println();

            // teraz dodajemy punkt, który jest nielegalny i koliduje z punktem (2, 7)
            try {
                ms.addPoint(new Point(3, 7));
            } catch (TooCloseException e) {
                System.out.println("Lista usuniętych punktów dla (3,7): ");
                System.out.println(e.removePoints());
                System.out.println("A powinno się usunąć: ");
                System.out.println("[Point[x=2.0, y=7.0], Point[x=3.0, y=7.0]]");
                System.out.println();
            }

            // teraz zwiększamy minimalny dystans
            try {
                ms.setMinDistanceAllowed(2.5);
            } catch (TooCloseException e) {
                System.out.println("Lista usuniętych punktów dla zmiany minDistanceAllowed: ");
                System.out.println(e.removePoints());
                System.out.println("A powinno się usunąć: ");
                System.out.println("[Point[x=2.0, y=4.0], Point[x=4.1, y=4.1]]");
                System.out.println();
            }

            // teraz zmieniamy normę, dodajemy punkt, który jest dobry w tej normie,
            // i zmieniamy normę spowrotem do euklidesowej, gdzie ten punkt nie jest ok.
            ms.setDistanceMeasure(new DM1());
            ms.addPoint(new Point(2, 3));
            try {
                ms.setDistanceMeasure(new DM2());
            } catch (TooCloseException e) {
                System.out.println("Lista usuniętych punktów dla zmiany normy: ");
                System.out.println(e.removePoints());
                System.out.println("A powinno się usunąć: ");
                System.out.println("[Point[x=1.0, y=1.0], Point[x=2.0, y=3.0]]");
                System.out.println();
            }

        } catch (TooCloseException e) {
            System.out.println("TO NIE POWINNO SIĘ STAĆ - LISTA USUNIĘTYCH PUNKTÓW: ");
            System.out.println(e.removePoints());
            throw new RuntimeException(e);
        }

    }
}
