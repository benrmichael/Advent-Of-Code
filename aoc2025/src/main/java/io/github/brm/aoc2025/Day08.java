//========================================================================
//
//                       U N C L A S S I F I E D
//
//========================================================================
//  Copyright (c) 2025 Chesapeake Technology International Corp.
//  ALL RIGHTS RESERVED
//  This material may be reproduced by or for the U.S. Government
//  pursuant to the copyright license under the clause at
//  DFARS 252.227-7013 (OCT 1988).
//========================================================================
//  SBIR Data Rights Statement
//  Contract Number: N68335-13-C-0258
//
// Expiration of SBIR Data Rights Period:
//     5 years after completion of final contract modification
//
// The Government's rights to use, modify, reproduce, release, perform,
//  display, or disclose technical data or computer software marked with
//  this legend are restricted during the period shown as provided in
//  paragraph (b)(4) of the Rights in Noncommercial Technical Data and
//  Computer Software--Small Business Innovative Research (SBIR) Program
//  clause contained in the above identified contract. No restrictions
//  apply after the expiration date shown above. Any reproduction of
//  technical data, computer software, or portions thereof marked with
//  this legend must also reproduce the markings.
//========================================================================
package io.github.brm.aoc2025;

import java.util.*;

import static java.lang.Integer.parseInt;

/**
 * Day 8: Playground
 *
 * @author Copyright 2025 Chesapeake Technology International Corp.
 * @since 12-9-25
 */
public class Day08 extends AdventOfCodePuzzle {

    private final List<JunctionBox> junctionBoxes;
    private final List<Connection> pairs;

    /** Max connections to make for part one */
    private static final int P1_MAX = 1000;

    public Day08() {
        this.junctionBoxes = readInput(",")
                .map(JunctionBox::fromLine)
                .toList();

        // Precompute all pair distances and sort
        int n = junctionBoxes.size();
        this.pairs = new ArrayList<>(n * n);

        for (int i = 0; i < n; i++) {
            JunctionBox a = junctionBoxes.get(i);
            for (int j = i + 1; j < n; j++) {
                JunctionBox b = junctionBoxes.get(j);
                double dist = a.distanceFrom(b);
                pairs.add(new Connection(i, j, dist));
            }
        }

        Collections.sort(pairs);
    }

    @Override
    public long solvePartOne() {
        int n = junctionBoxes.size();
        DisjointSetUnion dsu = new DisjointSetUnion(n);

        for (int i = 0; i < Math.min(P1_MAX, pairs.size()); i++) {
            Connection p = pairs.get(i);
            dsu.union(p.i(), p.j());
        }

        Map<Integer, Integer> circuitSizes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int root = dsu.find(i);
            circuitSizes.put(root, dsu.size(root));
        }

        return circuitSizes.values().stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .mapToLong(i -> i)
                .reduce(1, (a, b) -> a * b);
    }

    @Override
    public long solvePartTwo() {
        int n = junctionBoxes.size();
        DisjointSetUnion dsu = new DisjointSetUnion(n);

        Connection last = null;
        for (int idx = P1_MAX; idx < pairs.size(); idx++) {
            Connection p = pairs.get(idx);

            // Check if they are not already connected
            if (dsu.union(p.i(), p.j())) {
                last = p;

                // Check if all nodes connected
                if (dsu.size(0) == n) {
                    break;
                }
            }
        }

        // Something went wrong if this fails...
        assert last != null;

        JunctionBox a = junctionBoxes.get(last.i());
        JunctionBox b = junctionBoxes.get(last.j());
        return (long) a.x * b.x;
    }

    /** Solve day 8 */
    public static void main(String[] args) {
        new Day08().solvePuzzles();
    }

    /** Simple junction box record */
    private record JunctionBox(int x, int y, int z) {

        /**
         * Find the straight line distance from this box
         * to the other box.
         *
         * @param o the other box.
         * @return straight line distance.
         */
        public double distanceFrom(JunctionBox o) {
            long dx = o.x - x;
            long dy = o.y - y;
            long dz = o.z - z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }

        /**
         * Util method to map an input line to a new junction
         * box record.
         *
         * @param line the line to parse.
         * @return a new junction box.
         */
        public static JunctionBox fromLine(String[] line) {
            return new JunctionBox(parseInt(line[0]), parseInt(line[1]), parseInt(line[2]));
        }
    }

    /**
     * A hypothetical connection between two junction boxes.
     *
     * @param i the index of the first junction box in the
     *          {@link #junctionBoxes} list.
     * @param j the index of the second junction box in
     *          the {@link #junctionBoxes} list.
     * @param distance distance between the two boxes.
     */
    private record Connection(int i, int j, double distance) implements Comparable<Connection> {
        @Override
        public int compareTo(Connection o) {
            return Double.compare(distance, o.distance);
        }
    }

    /**
     * Disjoin union set used to efficiently merge disjoint
     * circuits of junction boxes to avoid a costly traversal
     * when checking connections.
     *
     * <p> To be transparent, my original solution to this
     * problem was not so efficient so I looked up how to
     * deal with having disjoint sets and ran into this article
     * <a href="https://www.geeksforgeeks.org/dsa/introduction-to-disjoint-set-data-structure-or-union-find-algorithm/">which is what this class is based on</a>
     */
    private static class DisjointSetUnion {
        private final int[] parent;
        private final int[] size;

        public DisjointSetUnion(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }

            return parent[x];
        }

        public boolean union(int a, int b) {
            int pa = find(a);
            int pb = find(b);
            if (pa == pb) {
                return false;
            }

            if (size[pa] < size[pb]) {
                int t = pa;
                pa = pb;
                pb = t;
            }

            parent[pb] = pa;
            size[pa] += size[pb];
            return true;
        }

        public int size(int x) {
            return size[find(x)];
        }
    }
}
