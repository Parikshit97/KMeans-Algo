package Apriori;

import java.util.ArrayList;
import java.util.Scanner;

public class KMeans {

	public static Scanner scn = new Scanner(System.in);

	public static void main(String[] args) {

		Point points[] = new Point[9];

		for (int i = 0; i < points.length; i++) {
			points[i] = new Point();
			points[i].x = scn.nextDouble();
			points[i].y = scn.nextDouble();
			System.out.println(points[i]);
		}

		System.out.println("Enter number of clusters:");
		int k = scn.nextInt();

		Clusters clusters[] = new Clusters[k];

		for (int i = 0; i < k; i++) {
			clusters[i] = new Clusters();
		}

		Point initialCentroids[] = new Point[k];
		for (int i = 0; i < initialCentroids.length; i++) {
			initialCentroids[i] = new Point();
			initialCentroids[i].x = scn.nextDouble();
			initialCentroids[i].y = scn.nextDouble();
			System.out.println(initialCentroids[i]);
		}
		int flag = 0;
		double distance[] = new double[k];
		do {
			for (int i = 0; i < points.length; i++) {
				for (int j = 0; j < initialCentroids.length; j++) {
					distance[j] = points[i].distance(initialCentroids[j]);
				}

				int sidx = -1;
				double min = Double.MAX_VALUE;
				for (int j = 0; j < distance.length; j++) {
					if (distance[j] < min) {
						min = distance[j];
						sidx = j;
					}
				}

				clusters[sidx].clusterArrayList.add(points[i]);
			}

			for (int i = 0; i < clusters.length; i++) {
				System.out.println("Cluster " + i + "=>" + clusters[i].clusterArrayList);
			}

			System.out.println("************************************************************************");

			for (int i = 0; i < clusters.length; i++) {
				if (!clusters[i].getMean().are_equal(initialCentroids[i])) {
					initialCentroids[i] = clusters[i].getMean();
				} else {
					flag++;
				}
			}
			
			for (int i = 0; i < k; i++) {
				clusters[i] = new Clusters();
			}

		} while (flag != k);

		for (int i = 0; i < initialCentroids.length; i++) {
			System.out.println(initialCentroids[i]);
		}
	}

	public static class Clusters {
		ArrayList<Point> clusterArrayList = new ArrayList<>();

		public Point getMean() {
			Point mean = new Point(0, 0);

			for (int i = 0; i < this.clusterArrayList.size(); i++) {
				mean.x += this.clusterArrayList.get(i).x;
				mean.y += this.clusterArrayList.get(i).y;
			}

			mean.x /= this.clusterArrayList.size();
			mean.y /= this.clusterArrayList.size();

			return mean;
		}

	}

}
