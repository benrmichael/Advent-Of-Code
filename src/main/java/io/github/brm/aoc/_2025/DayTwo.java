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
import static java.lang.Long.parseLong;

/**
 * Day 2: Gift Shop
 *
 * @author benjaminmichael
 * @since 12-3-25
 */
public class DayTwo extends AdventOfCodePuzzle {

    /** Day Two */
    public DayTwo() {
        super(2025, 2);
    }

    @Override
    public Object solvePartOne() {
        String line = readInput().get(0);
        String[] ids = line.split(",");

        long sum = 0;
        for (String id : ids) {
            String[] range = id.split("-");
            long lower = parseLong(range[0]);
            long upper = parseLong(range[1]);

            for (long i = lower; i <= upper; i++) {
                String idString = Long.toString(i);
                if (isInvalidId(idString)) {
                    sum += i;
                }
            }
        }

        return sum;
    }

    private boolean isInvalidId(String id) {
        if (id.length() % 2 != 0) {
            return false;
        }

        int middle = id.length() / 2;
        return id.substring(0, middle).equals(id.substring(middle));
    }

    private boolean hasRepeatedSequence(String source) {
        for (int i = 1; i < source.length(); i++) {
            if (source.length() % i == 0) {
                String str = source.substring(0, i);
                if (str.repeat(source.length() / i).equals(source)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public Object solvePartTwo() {
        String line = readInput().get(0);
        String[] ids = line.split(",");

        long sum = 0;
        for (String id : ids) {
            String[] range = id.split("-");
            long lower = parseLong(range[0]);
            long upper = parseLong(range[1]);

            for (long i = lower; i <= upper; i++) {
                String idString = Long.toString(i);
                if (hasRepeatedSequence(idString)) {
                    sum += i;
                }
            }
        }

        return sum;
    }

    /** Solve day two */
    public static void main(String[] args) {
        new DayTwo().solvePuzzles();
    }
}
