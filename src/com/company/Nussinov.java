package com.company;


import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static java.util.Collections.max;

/**
 * Created by Owner on 9/19/2017.
 */
public class Nussinov {


    // Traceback method that returns the pair of dna bases using their indices
    public static List<Pair> calculate(String seq) {
        int n = seq.length();
        char[] sequence = seq.toCharArray();
        int[][] matrix = new int[n][n];
        fillMatrix(sequence, matrix, n);
        return traceback(matrix, n, sequence);

    }

    private static int[][] fillMatrix(char[] sequence, int[][] matrix, int length) {
        for (int i = 1; i < length; i++) {
            int loopCount = i;
            for (int j = 0; j < length - i; j++) {
                int case1 = matrix[loopCount - 1][j];
                int case2 = matrix[loopCount][j + 1];
                int case3 = score(sequence[loopCount], sequence[j]) + matrix[loopCount - 1][j + 1];
                //Optional; add an if statement to limit the number of base pairings too close
                ArrayList<Integer> temp = new ArrayList<>();
                int case4 = 0;
                for (int k = j + 1; k < loopCount; k++) {
                    // case 4
                    temp.add(matrix[loopCount][k + 1] + matrix[k][j]);
                }
                if (!temp.isEmpty()) {
                    case4 = max(temp);
                }
                matrix[loopCount][j] = Math.max(Math.max(case1, case2), Math.max(case3, case4));
                loopCount += 1;
            }
        }
        return matrix;
    }

    private static List<Pair> traceback(int[][] matrix, int length, char[] seq) {
        Stack<Pair> storage = new Stack<>();
        Pair<Character, Character> firstPair = new Pair(length - 1, 0);
        storage.push(firstPair);
        List record = new ArrayList<>();
        if (score(seq[length - 1], seq[0]) == 1 ) {
            record.add(firstPair);
        }
        while (!storage.isEmpty()) {
            Pair<Integer, Integer> temp = storage.pop();
            int i = temp.getKey();
            int j = temp.getValue();
            if (j >= i) {
                continue;
            } else if (matrix[i][j + 1] == matrix[i][j]) {
                storage.push(new Pair(i, j + 1));
            } else if (matrix[i - 1][j] == matrix[i][j]) {
                storage.push(new Pair(i - 1, j));
            } else if ((matrix[i - 1][j + 1] + score(seq[i], seq[j])) == matrix[i][j]) {
                if (i != j) {
                   record.add(new Pair(i, j));
                }
                storage.push(new Pair(i - 1, j + 1));
            } else {
                for (int k = j + 1; k < i; k++) {
                    if (matrix[i][k + 1] + matrix[k][j] == matrix[i][j]) {
                        storage.push(new Pair(i, k + 1));
                        storage.push(new Pair(k, j));
                        break;
                    }
                }
            }
        }
        return record;
    }

    private static int score(Character base1, Character base2) {
        int score = 0;
        if (base1.equals('U') && base2.equals('A') || base1.equals('A') && base2.equals('U')) {
            score = 1;
        } else if (base1.equals('G') && base2.equals('C') || base1.equals('C') && base2.equals('G')) {
            score = 1;
        } else {
            score = 0;
        }
        return score;

    }

    public static void main(String args[]) {
        String seq = "CGGACCCAGACUUUC";
        int[][] matrix = new int[15][15];

        fillMatrix(seq.toCharArray(), matrix, 15);
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                System.out.print(matrix[j][i]);
            }
        }
        List<Pair> example = traceback(matrix, 15, seq.toCharArray());
        System.out.println(example.isEmpty());
        for (Pair p : example) {
            System.out.println(p.getKey() + "," + p.getValue());
        }
        for (Pair p : calculate(seq)) {
            System.out.println(p.getKey() + "," + p.getValue());
        }

    }
}
