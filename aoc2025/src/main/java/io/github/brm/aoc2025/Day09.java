/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.github.brm.aoc2025;

import java.util.List;
import java.util.TreeSet;

import static java.lang.Integer.parseInt;
import static java.util.Collections.reverseOrder;

/**
 * Day 9: Movie Theater
 *
 * @author benjaminmichael
 * @since 12-11-25
 */
public class Day09 extends AdventOfCodePuzzle {

    /** The red tile locations. arr[0] = x, arr[1] = y */
    private final List<int[]> redTiles;
    /** Areas of each square */
    private final TreeSet<Square> areas = new TreeSet<>(reverseOrder());

    /** Read input */
    public Day09() {
        redTiles = readInput(Day09::parseLine).toList();
        for (int i = 0; i < redTiles.size(); i++) {
            int[] a = redTiles.get(i);
            for (int j = i + 1; j < redTiles.size(); j++) {
                int[] b = redTiles.get(j);
                areas.add(new Square(a, b, area(a, b)));
            }
        }
    }

    @Override
    public long solvePartOne() {
        return areas.first().area();
    }

    @Override
    public long solvePartTwo() {
        return -1L;
    }

    /** Area formed by tiles in these two locations */
    private static long area(int[] a, int[] b) {
        long x = Math.abs(a[0] - b[0]) + 1;
        long y = Math.abs(a[1] - b[1]) + 1;
        return x * y;
    }

    /** Parse input line into int[] */
    private static int[] parseLine(String line) {
        String[] arr = line.split(",");
        int[] nums = new int[2];
        nums[0] = parseInt(arr[0]);
        nums[1] = parseInt(arr[1]);
        return nums;
    }

    /** Solve day 9 puzzles */
    public static void main(String[] args) {
        new Day09().solvePuzzles();
    }

    /** Square! */
    private record Square(int[] a, int[] b, long area) implements Comparable<Square> {
        @Override
        public int compareTo(Square other) {
            return Long.compare(area(), other.area());
        }
    }
}
