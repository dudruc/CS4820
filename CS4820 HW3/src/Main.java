import java.util.*;
import java.io.*;

class Main {
	public static void main(String[] args) throws IOException {
		/* To read the input */
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		String line = br.readLine();
		String[] strs = line.trim().split(" ");
		int m = Integer.parseInt(strs[0]);
		int n = Integer.parseInt(strs[1]);

		String X = br.readLine();

		String Y = br.readLine();

		int delta = Integer.parseInt(br.readLine());

		int[][] penalty = new int[26][26];
		for (int i = 0; i < 26; i++) {
			for (int j = 0; j < 26; j++) {
				line = br.readLine();
				strs = line.trim().split(" ");
				penalty[i][j] = Integer.parseInt(strs[2]);
			}
		}

		br.close();

		int[][] A = new int[m + 1][n + 1];
		HashMap<Integer, Integer> match = new HashMap<Integer, Integer>();

		for (int i = 0; i <= m; i++)
			A[i][0] = i * delta;
		for (int j = 0; j <= n; j++)
			A[0][j] = j * delta;

		for (int j = 1; j <= n; j++) {
			for (int i = 1; i <= m; i++) {
				int case1 = penalty[X.charAt(i - 1) - 'a'][Y.charAt(j - 1) - 'a'] + A[i - 1][j - 1];
				int case2 = delta + A[i - 1][j];
				int case3 = delta + A[i][j - 1];

				if (case1 < case2 && case1 < case3) {
					A[i][j] = case1;
				} else
					A[i][j] = Math.min(case2, case3);
			}
		}
		int k = m;
		int l = n;
		while (k > 0 && l > 0) {
			if (A[k][l] == penalty[X.charAt(k - 1) - 'a'][Y.charAt(l - 1) - 'a'] + A[k - 1][l - 1]) {
				match.put(k, l);
				k--;
				l--;
			} else if (A[k][l] == delta + A[k - 1][l])
				k--;
			else if (A[k][l] == delta + A[k][l - 1])
				l--;

		}
		/* The output */
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(System.out));
		bw.write(String.valueOf(A[m][n]));
		bw.newLine();
		bw.write(String.valueOf(match.size()));
		bw.newLine();
		for (int i : match.keySet()) {
			bw.write(String.valueOf(i) + " " + String.valueOf(match.get(i)));
			bw.newLine();
		}
		bw.close();
		
	}
	
}
