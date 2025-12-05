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
package io.github.brm.aoc._2025;

import io.github.brm.aoc.AdventOfCodePuzzle;

import java.util.List;

/**
 * Day 4: Printing Department
 *
 * @author benjaminmichael
 * @since 12-4-2025
 */
public class DayFour extends AdventOfCodePuzzle {

   /** A 2D move */
   private record Move(int r, int c) { }

   private static final List<Move> moves = List.of(
           new Move(1, 0), new Move(-1, 0), new Move(0, 1), new Move(0, -1),
           new Move(-1, -1), new Move(-1, 1), new Move(1, 1), new Move(1, -1));

   /** Day four */
   public DayFour() {
      super(2025, 4);
   }

   @Override
   public long solvePartOne() {
      int count = 0;
      char[][] map = readInput(String::toCharArray).toArray(char[][]::new);
      for (int row = 0; row < map.length; row++) {
         for (int col = 0; col < map[row].length; col++) {
            if (map[row][col] == '@' && canMovePaperRoll(map, row, col)) {
               count++;
            }
         }
      }

      return count;
   }

   /** Check if there are less than 4 adjacent rolls */
   private boolean canMovePaperRoll(char[][] map, int r, int c) {
      if (!inBounds(map, r, c)) {
         return false;
      }

      int adjacentRolls = 0;
      for (Move move : moves) {
         int newR = r + move.r();
         int newC = c + move.c();

         if (inBounds(map, newR, newC) && map[newR][newC] == '@') {
            adjacentRolls++;
         }
      }

      return adjacentRolls < 4;
   }

   /** Check if the given row/col index is in bounds for the map */
   private boolean inBounds(char[][] map, int r, int c) {
      return r >= 0 && r < map.length && c >= 0 && c < map[r].length;
   }

   @Override
   public long solvePartTwo() {
      int count = 0;
      char[][] map = readInput(String::toCharArray).toArray(char[][]::new);

      while (true) {
         int prev = count;
         for (int row = 0; row < map.length; row++) {
            for (int col = 0; col < map[row].length; col++) {
               if (map[row][col] == '@' && canMovePaperRoll(map, row, col)) {
                  map[row][col] = '.';
                  count++;
               }
            }
         }

         if (prev == count) {
            break;
         }
      }

      return count;
   }

   /** Solve day fours puzzles */
   public static void main(String[] args) {
      new DayFour().solvePuzzles();
   }
}
