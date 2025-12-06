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


import static java.lang.Integer.parseInt;

/**
 * Day 1: Secret Entrance
 *
 * @author benjaminmichael
 * @since 12-3-25
 */
public class Day1 extends AdventOfCodePuzzle {
    private static final int DIAL_SIZE = 100;

    /** The dial, starts at 50 */
    private static class Dial {
        public int value = 50;
        public int hits = 0;

        public Dial merge(Dial other) {
            this.hits += other.hits;
            return this;
        }

        public int hits() {
            return hits;
        }
    }

    public Day1() {
        super(2025, 1);
    }

    private static int mapLineToTurn(String line) {
        line = line.trim();
        int sign = (line.charAt(0) == 'L') ? -1 : 1;
        return sign * parseInt(line.substring(1));
    }

    @Override
    public long solvePartOne() {
        return readInput(Day1::mapLineToTurn).reduce(
                new Dial(),
                (dial, turn) -> {
                    dial.value = Math.floorMod(dial.value + turn, DIAL_SIZE);
                    if (dial.value == 0) {
                        dial.hits++;
                    }
                    return dial;
                    },
                Dial::merge
        ).hits();
    }

    @Override
    public long solvePartTwo() {
        return readInput(Day1::mapLineToTurn).reduce(
                new Dial(),
                (dial, turn) -> {
                    dial.hits += (Math.abs(turn) / 100);
                    int remainder = (Math.abs(turn) > 100) ? turn % 100 : turn;
                    int newValue = Math.floorMod(dial.value + remainder, DIAL_SIZE);
                    if (newValue == 0 || ((remainder < 0) ? newValue > dial.value : newValue < dial.value) && dial.value != 0) {
                        dial.hits++;
                    }

                    dial.value = newValue;
                    return dial;
                },
                Dial::merge
        ).hits();
    }

    /** Solve day one */
    public static void main(String[] args) {
        new Day1().solvePuzzles();
    }
}
