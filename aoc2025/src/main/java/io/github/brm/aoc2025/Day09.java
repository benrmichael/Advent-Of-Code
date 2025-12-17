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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import static java.lang.Integer.parseInt;

/**
 * Day 9: Movie Theater
 *
 * @author benjaminmichael
 * @since 12-11-25
 */
public class Day09 extends AdventOfCodePuzzle {

    /** Bounding box formed by all points */
    private final Polygon bounds = new Polygon();
    /** Areas of each square */
    private final TreeSet<Rectangle> rects = new TreeSet<>(Comparator.comparingLong(Day09::area)
            .reversed()
            .thenComparingInt(r -> r.x)
            .thenComparingInt(r -> r.y)
            .thenComparingInt(r -> r.width)
            .thenComparingInt(r -> r.height));

    /** Read input */
    public Day09() {
        List<int[]> redTiles = readInput(Day09::parseLine).toList();
        for (int i = 0; i < redTiles.size(); i++) {
            int[] a = redTiles.get(i);
            bounds.addPoint(a[0], a[1]);
            for (int j = i + 1; j < redTiles.size(); j++) {
                int[] b = redTiles.get(j);
                Point topLeft = new Point(Math.min(a[0], b[0]), Math.min(a[1], b[1]));
                int width =  Math.abs(a[0] - b[0]);
                int height = Math.abs(a[1] - b[1]);
                Rectangle rect = new Rectangle(topLeft, new Dimension(width, height));
                rects.add(rect);
            }
        }
    }

    @Override
    public long solvePartOne() {
        return area(rects.first());
    }

    @Override
    public long solvePartTwo() {
        return rects.stream()
                .filter(bounds::contains)
                .findFirst()
                .map(Day09::area)
                .orElse(-1L);
    }

    /** Area formed by tiles in these two locations */
    private static long area(Rectangle rect) {
        return (long) (rect.width + 1) * (rect.height + 1);
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
}
