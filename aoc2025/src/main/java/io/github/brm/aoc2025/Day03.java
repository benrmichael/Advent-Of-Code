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

/**
 * Day 3: Lobby
 *
 * @author benjaminmichael
 * @since 12-3-25
 */
public class Day3 extends AdventOfCodePuzzle {

    /** A battery in the bank */
    private record Battery(char jolts, int position) { }

    @Override
    public long solvePartOne() {
        return readInput()
                .mapToLong(bank -> findLargestJoltage(bank, 2))
                .sum();
    }

    @Override
    public long solvePartTwo() {
        return readInput()
                .mapToLong(bank -> findLargestJoltage(bank, 12))
                .sum();
    }

    private long findLargestJoltage(String bankString, int numBatteries) {
        int len = bankString.length();
        char[] bank = bankString.toCharArray();
        Battery[] batteries = new Battery[numBatteries];

        for (int i = 0; i < len; i++) {
            char jolts = bank[i];
            for (int j = 0; j < numBatteries; j++) {
                if (i + (numBatteries - j - 1) >= len) {
                    continue;
                }

                if (batteries[j] == null || (jolts > batteries[j].jolts && i > batteries[j].position)) {
                    for (int k = j, n = i; k < numBatteries; k++, n++) {
                        batteries[k] = new Battery(bank[n], n);
                    }
                    break;
                }
            }
        }

        long totalJolatage = 0;
        for (Battery battery : batteries) {
            int jolts = battery.jolts - '0';
            totalJolatage = totalJolatage * 10 + jolts;
        }

        return totalJolatage;
    }

    /** Solve day three */
    public static void main(String[] args) {
        new Day3().solvePuzzles();
    }
}
