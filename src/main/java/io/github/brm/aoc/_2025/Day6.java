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
import org.w3c.dom.css.Counter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Day 6: Trash Compactor
 *
 * @author benjaminmichael
 * @since 12-6-25
 */
public class Day6 extends AdventOfCodePuzzle {

    /** Line of numbers from input */
    private final List<String> numberLines;
    /** Operations to apply */
    private final List<Operation> operations;

    /** Day 6 */
    public Day6() {
        super(2025, 6);

        List<String> numberLines = new ArrayList<>();
        List<Operation> operations = new ArrayList<>();
        Iterator<String> iter = readInput().iterator();
        while (iter.hasNext()) {
            String line = iter.next().strip();
            if (!iter.hasNext()) {
                operations.addAll(Arrays.stream(line.split("\\s+"))
                        .map(Operation::from)
                        .toList());
            } else {
                numberLines.add(line);
            }
        }

        this.operations = operations;
        this.numberLines = numberLines;
    }

    @Override
    public long solvePartOne() {
        // Identities for each problem
        long[] identities = operations.stream()
                .mapToLong(Operation::identity)
                .toArray();

        long[] results = numberLines.stream()
                .map(line -> line.split("\\s+"))
                .map(arr -> Arrays.stream(arr).mapToLong(Long::parseLong).toArray())
                .reduce(
                        identities,
                        (acc, nums) -> {
                            for (int i = 0; i < nums.length; i++) {
                                acc[i] = operations.get(i).apply(acc[i], nums[i]);
                            }
                            return acc;
                        });

        return Arrays.stream(results).sum();
    }

    @Override
    public long solvePartTwo() {
        int opIndex = 0;
        long runningTotal = 0;
        Map<Integer, String> map = new HashMap<>();
        for (int i = 0; i < numberLines.get(0).length(); i++) {
            boolean added = false;
            for (String line : numberLines) {
                char c = line.charAt(i);
                if (Character.isDigit(c)) {
                    added = true;
                    map.put(i, map.getOrDefault(i, "") + c);
                }
            }

            if (!added && !map.isEmpty()) {
                Operation op = operations.get(opIndex++);
                runningTotal += map.values().stream()
                        .map(Long::parseLong)
                        .reduce(op.identity(), op::apply);
                map.clear();
            }
        }

        if (!map.isEmpty()) {
            Operation op = operations.get(opIndex);
            runningTotal += map.values().stream()
                    .map(Long::parseLong)
                    .reduce(op.identity(), op::apply);
        }

        return runningTotal;
    }

    /** Solve day six */
    public static void main(String[] args) {
        new Day6().solvePuzzles();
    }

    /** Math operation */
    private sealed interface Operation {
        long identity();
        long apply(long one, long two);

        static Operation from(String str) {
            return str.equals("*")
                    ? new Multiply()
                    : new Add();
        }

        final class Multiply implements Operation {
            @Override
            public long identity() {
                return 1;
            }

            @Override
            public long apply(long one, long two) {
                return one * two;
            }
        }

        final class Add implements Operation {
            @Override
            public long identity() {
                return 0;
            }

            @Override
            public long apply(long one, long two) {
                return one + two;
            }
        }
    }
}
