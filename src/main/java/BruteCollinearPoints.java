/**
 * Alexander Artamonov (asartamonov@gmail.com) 2016.
 */

import java.util.Arrays;

/**
 * Finds all line segments containing 4 points.
 */
public class BruteCollinearPoints {
    private final LineSegment[] segments;
    private Point[][] segmentPoints;

    /**
     * Finds all line segments containing 4 or more points
     * Throw a java.lang.NullPointerException either the argument
     * to the constructor is null or if any point in the array is null.
     * Throw a java.lang.IllegalArgumentException if the argument
     * to the constructor contains a repeated point.
     */
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new NullPointerException();
        Point[] refPoints = new Point[points.length];
        System.arraycopy(points, 0, refPoints, 0, points.length);
        Arrays.sort(refPoints);
        for (int i = 0; i < refPoints.length; i++) {
            if (refPoints == null) throw new NullPointerException();
            for (int j = i + 1; j < refPoints.length; j++)
                if (refPoints[i].compareTo(refPoints[j]) == 0)
                    throw new IllegalArgumentException();
        }
        segmentPoints = new Point[refPoints.length][2];
        int breakValue = 4;
        int foundSegments = 0;
        for (int i = 0; i < refPoints.length; i++) {
            Point startPoint = refPoints[i];
            for (int j = i + 1; j < refPoints.length; j++) {
                double referenceSlope = refPoints[i].slopeTo(refPoints[j]);
                int pointsInLine = 2;
                Point endPoint = refPoints[j];
                for (int k = j + 1; k < refPoints.length; k++) {
                    if (refPoints[i].slopeTo(refPoints[k]) == referenceSlope
                            && refPoints[k].compareTo(endPoint) > 0) {
                        endPoint = refPoints[k];
                        ++pointsInLine;
                    }
                }
                if (pointsInLine >= breakValue) {
                    if (foundSegments == segmentPoints.length)
                        segmentPoints = resize(segmentPoints, 1.25);
                    if (!isSegmentPart(startPoint, endPoint)) {
                        segmentPoints[foundSegments][0] = startPoint;
                        segmentPoints[foundSegments][1] = endPoint;
                        ++foundSegments;
                    }
                }
            }
        }
        segments = new LineSegment[foundSegments];
        for (int i = 0; i < foundSegments; i++) {
            segments[i] =
                    new LineSegment(segmentPoints[i][0], segmentPoints[i][1]);
        }
    }

    /**
     * Checks for already added segments (by their points) if found segment
     * is a part of already (previously) found segment.
     *
     * @param start start point of found segment
     * @param end   end point of found segment.
     * @return true if examined segment is a part of already found segment.
     */
    private boolean isSegmentPart(Point start, Point end) {
        for (Point[] segmentPoint : segmentPoints) {
            if (end == segmentPoint[1]
                    && end.slopeTo(start) == end.slopeTo(segmentPoint[0]))
                return true;
        }
        return false;
    }

    /**
     * Receives array, makes a resized copy and returns resized copy to calling point.
     *
     * @param resizeCoeff coefficient of resizing larger 0.
     */
    private Point[][] resize(Point[][] points, double resizeCoeff) {
        if (resizeCoeff <= 0)
            throw new IllegalArgumentException();
        Point[][] newArray = new Point[(int) Math.round(points.length * resizeCoeff)][2];
        System.arraycopy(points, 0, newArray, 0, points.length);
        return newArray;
    }

    /**
     * Returns the number of line segments
     */
    public int numberOfSegments() {
        return segments.length;
    }

    /**
     * Returns the line segments as an array
     */
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, segments.length);
    }
}
