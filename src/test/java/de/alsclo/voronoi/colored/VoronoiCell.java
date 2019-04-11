
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.alsclo.voronoi.graph.Edge;
import de.alsclo.voronoi.graph.Point;

public class VoronoiCell implements Shape {
	private GeneralPath path = new GeneralPath();

	private ArrayList<Edge> edges;

	private List<Point> points = new ArrayList<Point>();

	private Point center;
	
	public ArrayList<Edge> getEdges() {
		return edges;
	}

	public List<Point> getPoints() {
		return points;
	}

	public Point getCenter() {
		return center;
	}

	public VoronoiCell(Point center, ArrayList<Edge> edges) {

		this.center = center;
		
		for (Edge edge : edges) {
			Point a = edge.getA().getLocation();
			Point b = edge.getB().getLocation();

			if (!points.contains(a)) {
				points.add(a);
			}
			if (!points.contains(b)) {
				points.add(b);
			}
		}

		this.edges = edges;

		if (points.size()==0) {
			return;
		}

		points = sortVertices(points);

		if (points.size() > 0) {
			path.moveTo(points.get(0).x, points.get(0).y);
			for (int i = 1; i < points.size(); i++) {
				Point p = points.get(i);
				path.lineTo(p.x, p.y);
			}
			path.closePath();
		}

	}

	private Point findCentroid(List<Point> points) {
		
	    int x = 0;
	    int y = 0;
	    for (Point p : points) {
	        x += p.x;
	        y += p.y;
	    }
	    int cx = x / points.size();
	    int cy = y / points.size();
	    return new Point(cx, cy);
	}

	private List<Point> sortVertices(List<Point> points) {
	    Point center = findCentroid(points);
	    Collections.sort(points, (a, b) -> {
	        double a1 = (Math.toDegrees(Math.atan2(a.x - center.x, a.y - center.y)) + 360) % 360;
	        double a2 = (Math.toDegrees(Math.atan2(b.x - center.x, b.y - center.y)) + 360) % 360;
	        return (int) (a1 - a2);
	    });
	    return points;
	}

	@Override
	public java.awt.Rectangle getBounds() {
		return path.getBounds();
	}

	@Override
	public Rectangle2D getBounds2D() {
		return path.getBounds2D();
	}

	@Override
	public boolean contains(double x, double y) {
		return path.contains(x, y);
	}

	@Override
	public boolean contains(Point2D p) {
		return path.contains(p);
	}

	@Override
	public boolean intersects(double x, double y, double w, double h) {
		return path.intersects(x, y, w, h);
	}

	@Override
	public boolean intersects(Rectangle2D r) {
		return path.intersects(r);
	}

	@Override
	public boolean contains(double x, double y, double w, double h) {
		return path.contains(x, y, w, h);
	}

	@Override
	public boolean contains(Rectangle2D r) {
		return path.contains(r);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at) {
		return path.getPathIterator(at);
	}

	@Override
	public PathIterator getPathIterator(AffineTransform at, double flatness) {
		return path.getPathIterator(at, flatness);
	}
}