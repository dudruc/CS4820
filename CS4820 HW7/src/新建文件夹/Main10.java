import java.util.*;
import java.io.*;

class Main10 {
	/* To be loaded from StdIn using input(). */
	int height; // height of image
	int width; // width of image
	int blockHeight; // height of a tiling block
	int blockWidth; // width of a tiling block
	int[][] fReward; // reward for putting pixel in foreground
	int[][] bReward; // reward for putting pixel in background
	int[][] pBtwCols; // pBtwCols[i][j] is separation penalty between pixel (i,j), (i,j+1)
						// dimensions: height x (width - 1)
	int[][] pBtwRows; // pBtwRows[i][j] is separation penalty between pixel (i,j), (i+1,j)
						// dimensions: (height-1) x (width)
	int[][] graph;
	/* To be printed to StdOut using output() */
	boolean[][] foreground; // selects the pixels that will go in the foreground

	// Load input from StdIn.
	void input() {
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String[] hw = in.readLine().split("\\s+");
			height = Integer.parseInt(hw[0]);
			width = Integer.parseInt(hw[1]);
			String[] bhw = in.readLine().split("\\s+");
			blockHeight = Integer.parseInt(bhw[0]);
			blockWidth = Integer.parseInt(bhw[1]);
			fReward = new int[height][width];
			bReward = new int[height][width];
			pBtwCols = new int[height][width - 1];
			pBtwRows = new int[height - 1][width];
			// populate fReward
			for (int i = 0; i < height; i++) {
				String[] rewards = in.readLine().split("\\s+");
				for (int j = 0; j < width; j++) {
					fReward[i][j] = Integer.parseInt(rewards[j]);
				}
			}
			// populate bReward
			for (int i = 0; i < height; i++) {
				String[] rewards = in.readLine().split("\\s+");
				for (int j = 0; j < width; j++) {
					bReward[i][j] = Integer.parseInt(rewards[j]);
				}
			}
			// populate pBtwColsA
			for (int i = 0; i < height; i++) {
				String[] penalties = in.readLine().split("\\s+");
				for (int j = 0; j < width - 1; j++) {
					pBtwCols[i][j] = Integer.parseInt(penalties[j]);
				}
			}
			// populate pBtwRows
			for (int i = 0; i < height - 1; i++) {
				String[] penalties = in.readLine().split("\\s+");
				for (int j = 0; j < width; j++) {
					pBtwRows[i][j] = Integer.parseInt(penalties[j]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	String arrToString(int[][] arr) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				builder.append(arr[i][j]);
				if (j < arr[i].length - 1) {
					builder.append(" ");
				}
			}
			builder.append("\n");
		}
		return builder.toString();
	}

	// Print output to StdOut.
	void output() {
		try {
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
			out.write(arrToString(graph));
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Main10() {
		input();
		// YOUR CODE HERE
		int numBlockH = height / blockHeight;
		int numBlockW = width / blockWidth;
		graph = new int[numBlockH * numBlockW + 2][numBlockH * numBlockW + 2];
		for (int BH = 0; BH < numBlockH; BH++) {
			for (int BW = 0; BW < numBlockW; BW++) {
				double blockFSum = 0;
				double blockBSum = 0;
				for (int p = BH * blockHeight; p < BH * blockHeight + blockHeight; p++) {
					for (int q = BW * blockWidth; q < BW * blockWidth + blockWidth; q++) {
						blockFSum += fReward[p][q];
						blockBSum += bReward[p][q];
					}
				}
				graph[numBlockH * numBlockW][BH * numBlockW + BW] = (int) Math
						.floor(blockFSum / (blockHeight * blockWidth));
				graph[BH * numBlockW + BW][numBlockH * numBlockW + 1] = (int) Math
						.floor(blockBSum / (blockHeight * blockWidth));

			}
		}

		for (int BH = 1; BH < numBlockH; BH++) {
			for (int BW = 0; BW < numBlockW; BW++) {
				double pBlkBtwRowsSum = 0;
				for (int j = BW * blockWidth; j < (BW + 1) * blockWidth; j++) {
					pBlkBtwRowsSum += pBtwRows[BH * blockHeight - 1][j];
				}
				graph[BH * numBlockW + BW][(BH - 1) * numBlockW + BW] = (int) Math.floor(pBlkBtwRowsSum / blockWidth);
				graph[(BH - 1) * numBlockW + BW][BH * numBlockW + BW] = (int) Math.floor(pBlkBtwRowsSum / blockWidth);
			}
		}

		for (int BW = 1; BW < numBlockW; BW++) {
			for (int BH = 0; BH < numBlockH; BH++) {
				double pBlkBtwColsSum = 0;
				for (int i = BH * blockHeight; i < (BH + 1) * blockHeight; i++) {
					pBlkBtwColsSum += pBtwCols[i][BW * blockWidth - 1];
				}
				graph[BH * blockWidth + BW][BH * blockWidth + BW - 1] = (int) Math.floor(pBlkBtwColsSum / blockHeight);
				graph[BH * blockWidth + BW - 1][BH * blockWidth + BW] = (int) Math.floor(pBlkBtwColsSum / blockHeight);
			}
		}

//		int[][] rg = ScalingMaxFlow(graph, numBlockH * numBlockW, numBlockH * numBlockW + 1);
//		foreground = new boolean[height][width];
//		int[] parent = new int[graph.length];
//		for (int k = 0; k < numBlockH * numBlockW; k++) {
//			boolean res = BFS(rg, numBlockH * numBlockW, k, parent);
//			for (int i = (k / numBlockW) * blockHeight; i < (k / numBlockW + 1) * blockHeight; i++) {
//				for (int j = (k % numBlockW) * blockWidth; j < ((k % numBlockW) + 1) * blockWidth; j++) {
//					foreground[i][j] = res;
//				}
//			}
//		}

		// YOUR CODE HERE
		output();
	}

	public int[][] ScalingMaxFlow(int[][] graph, int s, int t) {
		int[][] residualGraph = new int[graph.length][graph.length];
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph.length; j++) {
				residualGraph[i][j] = graph[i][j];
			}
		}

		int[] parent = new int[graph.length];

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

		}

		return residualGraph;
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

	public static void main(String[] args) {
		new Main10();
	}
}
