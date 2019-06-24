import java.util.*;

class Edge implements Comparable<Edge> {
	public int id; // number
	public int x; // first vertex
	public int y; // second vertex
	public int c; // cost

	@Override
	public int compareTo(Edge e) {
		if (this.c < e.c)
			return -1;
		else if (this.c > e.c)
			return 1;
		else {
			if (this.id < e.id)
				return -1;
			else
				return 1;
		}
	}
}

class Vertex {
	Vector<Edge> neighbors;

	public Vertex() {
		neighbors = new Vector<Edge>();
	}
}

class DisjointSet {
	int[] Component;
	int[] Size;

	void MakeUnionFind(int n) {
		Component = new int[n + 1];
		Size = new int[n + 1];
		for (int i = 1; i < n + 1; i++) {
			Component[i] = i;
			Size[i] = 1;
		}
	}

	int Find(int i) {
		if (Component[i] == i)
			return i;
		return Find(Component[i]);
	}

	void Union(int a, int b) {
		int A = Find(a);
		int B = Find(b);
		if (Size[A] < Size[B]) {
			Component[A] = B;
			Size[B] += Size[A];
		} else if (Size[A] >= Size[B]) {
			Component[B] = A;
			Size[A] += Size[B];
		}

	}
}

class Main {
	public static void main(String[] args) {
		/* To read the input */
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int m = sc.nextInt();
		int p = sc.nextInt();

		Vector<Edge> edges = new Vector<Edge>();
		PriorityQueue<Edge> pq = new PriorityQueue<Edge>();
		Vertex[] graph = new Vertex[n + 1];
		Vector<Edge> mst = new Vector<Edge>();

		for (int j = 1; j <= n; j++) {
			graph[j] = new Vertex();
		}

		for (int i = 1; i <= m; ++i) {
			Edge e = new Edge();
			e.id = i;
			e.x = sc.nextInt();
			e.y = sc.nextInt();
			e.c = sc.nextInt();

			graph[e.x].neighbors.add(e);
			graph[e.y].neighbors.add(e);

			edges.addElement(e);
		}

		if (p == 0) {
			for(Edge dg : edges)
				pq.add(dg);
			DisjointSet ds = new DisjointSet();
			ds.MakeUnionFind(n);

			while (mst.size() < n - 1) {
				Edge e = pq.poll();
				int A = ds.Find(e.x);
				int B = ds.Find(e.y);

				if (A != B) {
					mst.add(e);
					ds.Union(e.x, e.y);
				}

			}

		} else if (p == 1) {
			int seed = 1;
			boolean[] visited = new boolean[graph.length];
			visited[seed] = true;

			for (Edge edge : graph[seed].neighbors) {
				pq.add(edge);
			}

			while (!pq.isEmpty()) {
				Edge e = pq.poll();
				if (!(visited[e.x] && visited[e.y])) {
					visited[e.x] = true;
					for (Edge neighbor : graph[e.y].neighbors) {
						if (!visited[e.y])
							pq.add(neighbor);
					}
					visited[e.y] = true;
					mst.add(e);
				}
			}
		}

		/*
		 * To output the first N-1 edges; note this is probably not actually a spanning
		 * tree
		 */
		for (int i = 0; i < n - 1; ++i) {
			System.out.println(mst.get(i).id);
		}

	}
}
