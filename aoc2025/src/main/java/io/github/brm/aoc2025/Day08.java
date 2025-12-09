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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static java.lang.Integer.parseInt;

/**
 * Day 8: Playground
 *
 * @author Copyright 2025 Chesapeake Technology International Corp.
 * @since 12-9-25
 */
public class Day08 extends AdventOfCodePuzzle {

    /** All the junction boxes */
    private final List<Node> junctionBoxes;

    /** Set up the input */
    public Day08() {
        this.junctionBoxes = readInput(",")
                .map(arr -> new Node(parseInt(arr[0]), parseInt(arr[1]), parseInt(arr[2])))
                .toList();
    }

    @Override
    public long solvePartOne() {
        int maxConnections = 1000;
        PriorityQueue<Connection> pq = new PriorityQueue<>(maxConnections, Collections.reverseOrder());
        for (int i = 0; i < junctionBoxes.size(); i++) {
            Node first = junctionBoxes.get(i);
            for (int j = i + 1; j < junctionBoxes.size(); j++) {
                Node second = junctionBoxes.get(j);
                Connection connection = new Connection(first, second);
                if (pq.size() < maxConnections) {
                    pq.offer(connection);
                    first.addConnection(second);
                } else if (pq.peek().distance() > connection.distance()) {
                    Connection old = pq.poll();
                    old.one().removeConnection(old.two());
                    pq.offer(connection);
                    first.addConnection(second);
                }
            }
        }

        Set<Node> seenNodes = new HashSet<>();
        SortedSet<Node> circuits = new TreeSet<>(Comparator.comparingInt(Node::connections).reversed());
        for (Connection connection : pq) {
            Node firstNode = connection.one();
            if (!seenNodes.contains(firstNode)) {
                circuits.add(firstNode);
                seenNodes.addAll(firstNode.getNodesInCircuit());
            }
        }

        return circuits.stream()
                .limit(3)
                .mapToInt(Node::connections)
                .reduce(1, (acc, i) -> acc * i);
    }

    @Override
    public long solvePartTwo() {
        return 0;
    }

    /** Solver for day 8 */
    public static void main(String[] args) {
        new Day08().solvePuzzles();
    }

    private static class Node {
        public int x;
        public int y;
        public int z;
        public final Set<Node> connections = new HashSet<>();

        public Node(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public int connections() {
            return findEdges(new HashSet<>()).size() + 1;
        }

        public void addConnection(Node node) {
            connections.add(node);
            node.connections.add(this);
        }

        public void removeConnection(Node node) {
            connections.remove(node);
            node.connections.remove(this);
        }

        public Set<Node> getNodesInCircuit() {
            return findEdges(new HashSet<>());
        }

        private Set<Node> findEdges(Set<Node> visited) {
            if (visited.contains(this)) {
                return Collections.emptySet();
            }

            visited.add(this);
            Set<Node> edges = new HashSet<>();
            for (Node node : connections) {
                if (!visited.contains(node)) {
                    edges.add(node);
                    edges.addAll(node.findEdges(visited));
                }
            }

            return edges;
        }

        public double distanceFrom(Node other) {
            long _x = other.x - this.x;
            long _y = other.y - this.y;
            long _z = other.z - this.z;
            return Math.sqrt(_x * _x + _y * _y + _z * _z);
        }

        @Override
        public String toString() {
            return "Node[x=" +  x
                    + ", y=" + y
                    + ", z=" + z + "]";
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        @Override
        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }

            return other instanceof Node node
                    && this.hashCode() == node.hashCode()
                    && Objects.equals(this.x, node.x)
                    && Objects.equals(this.y, node.y)
                    && Objects.equals(this.z, node.z);
        }
    }

    private record Connection(
            Node one,
            Node two,
            double distance
    ) implements Comparable<Connection> {

        public Connection(Node one, Node two) {
            this(one, two, one.distanceFrom(two));
        }

        @Override
        public int compareTo(Connection o) {
            return Double.compare(this.distance, o.distance());
        }
    }
}
