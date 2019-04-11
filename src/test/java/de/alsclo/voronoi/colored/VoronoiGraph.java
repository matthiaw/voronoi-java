import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JFrame;

import de.alsclo.voronoi.graph.Edge;
import de.alsclo.voronoi.graph.Point;

public class VoronoiGraph extends JFrame {

	private static final double OFFSET = 0.0;

	private static final int size = 1024;

	private static final double POINT_SIZE = 20.0;
	private final VoronoiExtended diagram;

	public VoronoiGraph(VoronoiExtended diagram) {
		this.diagram = diagram;
	}

	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		ArrayList<VoronoiCell> cells = this.diagram.getCells();
		for (VoronoiCell cell : cells) {
			Random randomGenerator = new Random();
			int red = randomGenerator.nextInt(256);
			int green = randomGenerator.nextInt(256);
			int blue = randomGenerator.nextInt(256);

			Color randomColour = new Color(red,green,blue);
			g2.setStroke(new BasicStroke());
			g2.setPaint(randomColour);
			g2.fill(cell);
			
			List<Edge> edges = cell.getEdges();
			for (Edge edge : edges) {
				g2.setPaint(Color.LIGHT_GRAY);
				g2.drawLine((int) edge.getSite1().x, (int) edge.getSite1().y + (int) OFFSET, (int) edge.getSite2().x , (int) edge.getSite2().y + (int) OFFSET);
			}
			
		}
		

		for (Point site : diagram.getGraph().getSitePoints()) {
			g2.setStroke(new BasicStroke());
			g2.setPaint(Color.BLACK);

			g2.fillOval((int) Math.round(site.x - POINT_SIZE / 2),
					(int) Math.round(site.y - POINT_SIZE / 2) + (int) OFFSET, (int) POINT_SIZE, (int) POINT_SIZE);
			// g2.drawString(String.format("%d,%d", (int)site.x, (int)site.y), (int) site.x,
			// size - (int)site.y + 32);
		}

		g2.setStroke(new BasicStroke());
		g2.setPaint(Color.BLACK);

		diagram.getGraph().edgeStream().filter(e -> e.getA() != null && e.getB() != null).forEach(e -> {
			Point a = e.getA().getLocation();
			Point b = e.getB().getLocation();

			g2.drawLine((int) a.x, (int) a.y + (int) OFFSET, (int) b.x, (int) b.y + (int) OFFSET);
		});
	}

	public static void main(String[] args) {
		Random r = new Random(9235563856L);
		Stream<Point> gen = Stream.generate(() -> new Point(r.nextDouble() * size, r.nextDouble() * size));
		VoronoiExtended diagram = new VoronoiExtended(gen.limit(20).collect(Collectors.toList())).relax().relax();
		VoronoiGraph frame = new VoronoiGraph(diagram);
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		frame.setSize(size, size + (int) OFFSET);
		frame.setVisible(true);
	}
}
