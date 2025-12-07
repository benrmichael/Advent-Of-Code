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

import io.github.brm.aoc2025.cmn.Point2D;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

/**
 * Day 7: Laboratories
 *
 * @author benjaminmichael
 * @since 12-7-25
 */
public class Day07 extends AdventOfCodePuzzle {

   /** Where the beam starts */
   private final Point2D start;
   /** The manifold */
   private final char[][] manifold;

   /** Setup map and starting point */
   public Day07() {
      char[][] manifold = readInput(String::toCharArray).toArray(char[][]::new);
      int[] start = new int[] {0, 0};
      for (int i = 0; i < manifold[0].length; i++) {
         if (manifold[0][i] == 'S') {
            start[1] = i;
            break;
         }
      }

      this.manifold = manifold;
      this.start = new Point2D(start[0], start[1]);
   }

   @Override
   public long solvePartOne() {
      Queue<Point2D> queue = new ArrayDeque<>();
      Set<Point2D> inQueue = new HashSet<>();

      int splits = 0;
      queue.add(start);
      while (!queue.isEmpty()) {
         Point2D pos = queue.poll();
         inQueue.remove(pos);

         // The beam will escape the manifold
         if (pos.x() + 1 >= manifold.length) continue;

         // No splitter below the beam
         if (manifold[pos.x() + 1][pos.y()] != '^') {
            addToQueue(queue, inQueue, pos.transform(1, 0), manifold);
            continue;
         }

         // Now we handle a split
         int oldQSize = queue.size();
         addToQueue(queue, inQueue, pos.transform(1, -1), manifold);
         addToQueue(queue, inQueue, pos.transform(1, 1), manifold);

         // Check if we split
         if (queue.size() != oldQSize) {
            splits++;
         }
      }

      return splits;
   }

   /**
    * Adds to queue if the given point is:
    * <pre>
    * 1. Not out of bounds of the manifold
    * 2. Not already in the queue
    * 3. Is not a splitter point
    * </pre>
    * */
   private void addToQueue(Queue<Point2D> q, Set<Point2D> inQ, Point2D pos, char[][] manifold) {
      // Bounds checking
      if (pos.x() < 0
              || pos.x() >= manifold.length
              || pos.y() < 0
              || pos.y() >= manifold[pos.x()].length
              || inQ.contains(pos)
              || manifold[pos.x()][pos.y()] == '^'
      ) {
         return;
      }

      q.add(pos);
      inQ.add(pos);
   }

   @Override
   public long solvePartTwo() {
      // DFS with a cache to avoid duplicate work
      return solvePartTwo2(start, manifold, new HashMap<>());
   }

   /**
    * DFS search of the manifold to find all timelines
    *
    * @param point the current point to search
    * @param manifold the manifold
    * @param cache the cache of points visited
    * @return number of timelines from the given point that
    * exit the manifold
    */
   public long solvePartTwo2(Point2D point, char[][] manifold, Map<Point2D, Long> cache) {
      // Check the cache first
      if (cache.containsKey(point)) {
         return cache.get(point);
      }

      // Out of bounds on the left or right of the manifold
      if (point.y() < 0 || point.y() >= manifold[point.x()].length) {
         cache.putIfAbsent(point, 0L);
         return 0;
      }

      // Exited the manifold
      if (point.x() + 1 >= manifold.length) {
         cache.put(point, 1L);
         return 1;
      }

      // No splitter in the way, continue down
      if (manifold[point.x() + 1][point.y()] != '^') {
         Point2D newPoint = point.transform(1, 0);
         long timelines = solvePartTwo2(newPoint, manifold, cache);
         cache.putIfAbsent(newPoint, timelines);
         return timelines;
      }

      long left = solvePartTwo2(point.transform(1, -1), manifold, cache);
      long right = solvePartTwo2(point.transform(1, 1), manifold, cache);
      long timelines = left + right;
      cache.putIfAbsent(point, timelines);
      return timelines;
   }

   /** Solve day 7 */
   public static void main(String[] args) {
      new Day07().solvePuzzles();
   }
}
