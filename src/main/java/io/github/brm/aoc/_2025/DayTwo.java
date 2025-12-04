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

import java.util.Objects;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.lang.Long.parseLong;

/**
 * Day 2: Gift Shop
 *
 * @author benjaminmichael
 * @since 12-3-25
 */
public class DayTwo extends AdventOfCodePuzzle {

    /** Range of IDs */
    private record IdRange(long start, long end) {

        /** Factory method */
        public static IdRange from(String input) {
            String[] range = input.split("-");
            long start = parseLong(range[0]);
            long end = parseLong(range[1]);
            return new IdRange(start, end);
        }

        /** Stream over the ID range */
        public Stream<String> stream() {
            return LongStream.rangeClosed(start, end)
                    .boxed()
                    .map(Objects::toString);
        }
    }

    /** Day Two */
    public DayTwo() {
        super(2025, 2);
    }

    @Override
    public long solvePartOne() {
        return readInput(",", IdRange::from)
                .flatMap(IdRange::stream)
                .filter(id -> id.length() % 2 == 0)
                .filter(id -> {
                    int middle = id.length() / 2;
                    return id.substring(0, middle).equals(id.substring(middle));
                })
                .mapToLong(Long::parseLong)
                .sum();
    }

    @Override
    public long solvePartTwo() {
        return readInput(",", IdRange::from)
                .flatMap(IdRange::stream)
                .filter(this::hasRepeatedSequence)
                .mapToLong(Long::parseLong)
                .sum();
    }

    /** Check for part two */
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

    /** Solve day two */
    public static void main(String[] args) {
        new DayTwo().solvePuzzles();
    }
}
