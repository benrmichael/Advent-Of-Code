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

import io.github.brm.aoc2025.cmn.Cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
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
        return pathsOut(new HashSet<>(), "you");
    }

    /**
     * DFS search for a device to see how many paths from the
     * device lead to the "out" device.
     *
     * @param visited the devices visited thus far.
     * @param device the device to visit.
     * @return paths from the device to visit to "out".
     */
    @Cache
    private long pathsOut(Set<String> visited, String device) {
        if (visited.contains(device) || !devices.containsKey(device)) {
            return 0L;
        }

        visited.add(device);
        long paths = 0;
        for (String d : devices.get(device)) {
            if (d.equals("out")) {
                paths++;
            } else {
                paths += pathsOut(visited, d);
            }
        }
        visited.remove(device);

        return paths;
    }

    @Override
    public long solvePartTwo() {
        return pathsOut2(new HashSet<>(), "svr", new LinkedHashSet<>());
    }

    private long pathsOut2(Set<String> visited, String device, Set<String> path) {
        // Avoid loop
        if (visited.contains(device)) {
            return 0L;
        }

        path.add(device);
        visited.add(device);
        long paths = 0;
        for (String d : devices.get(device)) {
            if (d.equals("out")) {
                if (path.contains("fft") && path.contains("dac")) {
                    paths++;
                }
            } else {
                paths += pathsOut2(visited, d, path);
            }
        }
        path.remove(device);
        visited.remove(device);

        return paths;
    }

    /** Solve day 11 */
    public static void main(String[] args) {
        new Day11().solvePuzzles();
    }
}
