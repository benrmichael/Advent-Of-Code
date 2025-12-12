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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Day 11: Reactor
 *
 * @author benjaminmichael
 * @since 12-11-25
 */
public class Day11 extends AdventOfCodePuzzle {

    private final Map<String, Set<String>> devices = new HashMap<>();
    private final Map<String, Map<String, Long>> memo = new HashMap<>();

    /** Set up the input */
    public Day11() {
        Iterator<String> iterator = readInput().iterator();
        while (iterator.hasNext()) {
            String line = iterator.next();

            int colon = line.indexOf(':');
            String name = line.substring(0, colon);
            String rem = line.substring(colon + 1).trim();
            String[] connections = rem.split("\\s+");

            devices.put(name, Set.of(connections));
        }
    }

    @Override
    public long solvePartOne() {
        return pathsBetween(new HashSet<>(), "you", "out");
    }

    /**
     * DFS search for a device to see how many paths from the
     * current device lead to the "last" device.
     *
     * @param visited the devices visited thus far.
     * @param current the device to visit.
     * @param last the last device to find.
     * @return paths from the device to visit to the last.
     */
    private long pathsBetween(Set<String> visited, String current, String last) {
        if (visited.contains(current) || !devices.containsKey(current)) {
            return 0L;
        }

        // Check the memo
        if (memo.containsKey(current) && memo.get(current).containsKey(last)) {
            return memo.get(current).get(last);
        }

        visited.add(current);
        long paths = 0;
        for (String device : devices.get(current)) {
            if (device.equals(last)) {
                paths++;
            } else {
                paths += pathsBetween(visited, device, last);
            }
        }
        visited.remove(current);
        memo.computeIfAbsent(current, key -> new HashMap<>()).put(last, paths);

        return paths;
    }

    @Override
    public long solvePartTwo() {
        long svrToFft = pathsBetween(new HashSet<>(), "svr", "fft");
        long fftToDac = pathsBetween(new HashSet<>(), "fft", "dac");
        long dacToOut = pathsBetween(new HashSet<>(), "dac", "out");
        long branchOne = svrToFft * fftToDac * dacToOut;

        long svrToDac = pathsBetween(new HashSet<>(), "svr", "dac");
        long dacToFft = pathsBetween(new HashSet<>(), "dac", "fft");
        long fftToOut = pathsBetween(new HashSet<>(), "fft", "out");
        long branchTwo = svrToDac * dacToFft * fftToOut;

        return branchOne + branchTwo;
    }

    /** Solve day 11 */
    public static void main(String[] args) {
        new Day11().solvePuzzles();
    }
}
