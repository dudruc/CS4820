import java.util.*;
import java.io.*;

class Main9 {

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String line = br.readLine();
		String[] strs = line.trim().split(" ");
		int n = Integer.parseInt(strs[0]);
		int m = Integer.parseInt(strs[1]);

		int[][] graph = new int[n + 1][n + 1];
		for (int i = 0; i < m; i++) {
			line = br.readLine();
			strs = line.trim().split(" ");
			int x = Integer.parseInt(strs[0]);
			int y = Integer.parseInt(strs[1]);
			int c = Integer.parseInt(strs[2]);
			graph[x][y] = c;
		}

		br.close();

		Main9 ff = new Main9();

		int f = ff.ScalingMaxFlow(graph, 4, 5);

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		bw.write(String.valueOf(f));
		bw.newLine();
		bw.close();

	}
	public int ScalingMaxFlow(int[][] graph, int s, int t) {
		int[][] residualGraph = new int[graph.length][graph.length];
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph.length; j++) {
				residualGraph[i][j] = graph[i][j];
			}
		}

		int[] parent = new int[graph.length];
		int f = 0;
		while (BFS(residualGraph, s, t, parent)) {
			int bottleneck = Integer.MAX_VALUE;
			int v = t;
			while (v != s) {
				int u = parent[v];
				bottleneck = Math.min(bottleneck, residualGraph[u][v]);
				v = u;

			}

			v = t;
			while (v != s) {
				int u = parent[v];
				residualGraph[u][v] -= bottleneck;
				residualGraph[v][u] += bottleneck;
				v = u;
			}
			f += bottleneck;
		}

		return f;
	}

	public boolean BFS(int[][] residualGraph, int s, int t, int[] parent) {
		boolean[] visited = new boolean[residualGraph.length];
		Queue<Integer> q = new LinkedList<Integer>();
		q.add(s);
		visited[s] = true;
		parent[s] = -1;

		while (!q.isEmpty()) {
			int u = q.remove();

			for (int v = 0; v < residualGraph.length; v++) {
				if (visited[v] == false && residualGraph[u][v] > 0) {
					q.add(v);
					parent[v] = u;
					visited[v] = true;
				}
			}
		}
		return visited[t];
	}
}
