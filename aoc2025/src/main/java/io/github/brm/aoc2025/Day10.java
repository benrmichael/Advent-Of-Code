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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.parseInt;
import static java.lang.Math.min;

/**
 * Day 10: Factory
 *
 * @author benjaminmichael
 * @since 12-14-25
 */
public class Day10 extends AdventOfCodePuzzle {

    private final List<Machine> machines;

    /** Setup input */
    public Day10() {
        this.machines = readInput(Machine::fromLine).toList();
    }

    @Override
    public long solvePartOne() {
        return machines.stream()
                .mapToInt(machine -> {
                    boolean[] diagram = new boolean[machine.lightDiagram().size()];
                    return solvePartOne2(machine, 0, 0, diagram);
                })
                .sum();
    }

    /**
     * Recursive function that will check the number of button
     * presses that can be applied with the current button and
     * all next buttons.
     *
     * @param machine the current machine.
     * @param i the current button index.
     * @param n the number of buttons pressed.
     * @param lightDiagram the current light diagram.
     * @return the number of button presses applicable, or the
     * constant {@link Integer#MAX_VALUE} if it cannot be done.
     */
    private int solvePartOne2(Machine machine, int i, int n, boolean[] lightDiagram) {
        if (i >= machine.schematics().size()) {
            return machine.isValidLightDiagram(lightDiagram) ? n : MAX_VALUE;
        }

        List<Integer> buttonToPress = machine.schematics().get(i++);
        int zero = solvePartOne2(machine, i, n, lightDiagram);
        int once = solvePartOne2(machine, i, ++n, updateLightDiagram(buttonToPress, lightDiagram));
        return min(zero, once);
    }

    /** Clones the light diagram and presses the given button */
    private boolean[] updateLightDiagram(List<Integer> button, boolean[] lightDiagram) {
        boolean[] updatedDiagram = lightDiagram.clone();
        for (int b : button) {
            updatedDiagram[b] = !updatedDiagram[b];
        }
        return updatedDiagram;
    }

    @Override
    public long solvePartTwo() {
        long sum = 0;
        for (Machine machine : machines) {
            int[] power = new int[machine.power().size()];
            int needed = solvePartTwo2(machine, 0, 0, power, new HashMap<>());
            sum += needed;
        }

        return sum;
    }

    private int solvePartTwo2(Machine machine, int i, int n, int[] power, Map<Integer, Integer> memo) {
        if (i >= machine.schematics().size()) {
            return machine.isValidPowerLevels(power) ? n : MAX_VALUE;
        }

        int hash = Objects.hash(i, Arrays.hashCode(power));
        if (memo.containsKey(hash)) {
            int val = memo.get(hash);
            return (val == MAX_VALUE) ? val : val + n;
        }

        int x = 0;
        int min = MAX_VALUE;
        List<Integer> b = machine.schematics().get(i++);
        while (canIncreasePower(power, x, b, machine)) {
            int[] increased = increasePower(power, x, b);
            int res = solvePartTwo2(machine, i, n + x, increased, memo);
            min = Math.min(res, min);
            x++;
        }

        memo.put(hash, min);

        return min;
    }

    private boolean canIncreasePower(int[] power, int x, List<Integer> button, Machine machine) {
        for (int b : button) {
            if ((power[b] + x) > machine.power().get(b)) {
                return false;
            }
        }

        return true;
    }

    private int[] increasePower(int[] power, int presses, List<Integer> button) {
        int[] updated = power.clone();
        for (int b : button) {
            updated[b] = power[b] + presses;
        }

        return updated;
    }

    /** Day10 main method */
    public static void main(String[] args) {
        new Day10().solvePuzzles();
    }

    /** Represents a factory machine */
    private record Machine(List<Boolean> lightDiagram, List<List<Integer>> schematics, List<Integer> power) {

        /**
         * Check if the given light diagram matches the light
         * diagram of this factory machine.
         *
         * @param diagram the light diagram to check.
         * @return {@code TRUE} if the given diagram matches
         * this machines diagram.
         */
        public boolean isValidLightDiagram(boolean[] diagram) {
            if (diagram.length != lightDiagram.size()) {
                return false;
            }

            for (int i = 0; i < diagram.length; i++) {
                if (diagram[i] != lightDiagram.get(i)) {
                    return false;
                }
            }

            return true;
        }

        /** Checks if the power level array matches */
        public boolean isValidPowerLevels(int[] power) {
            if (power.length != power().size()) {
                return false;
            }

            for (int i = 0; i < power.length; i++) {
                if (power[i] != this.power().get(i)) {
                    return false;
                }
            }

            return true;
        }

        /**
         * Parse a line from the input file into a factory machine.
         *
         * @param inputLine the input line to parse.
         * @return a new factory machine.
         */
        public static Machine fromLine(String inputLine) {
            List<Integer> power = new ArrayList<>();
            List<Boolean> lightDiagram = new ArrayList<>();
            List<List<Integer>> schematics = new ArrayList<>();

            State state = State.START;
            StringBuilder token = new StringBuilder();
            List<Integer> schematic = new ArrayList<>();
            for (char c : inputLine.toCharArray()) {
                switch (c) {
                    case '[' -> state = State.LIGHT_DIAGRAM;
                    case ']' -> state = State.WIRING_SCHEMATICS;
                    case '{' -> state = State.POWER;
                    case '.' -> lightDiagram.add(false);
                    case '#' -> lightDiagram.add(true);
                    case '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' -> token.append(c);
                    case ')', '}', ',' -> {
                        int value = parseInt(token.toString());
                        token.setLength(0);
                        if (state == State.WIRING_SCHEMATICS)
                            schematic.add(value);
                        else if (state == State.POWER)
                            power.add(value);

                        if (c == ')') {
                            schematics.add(schematic);
                            schematic = new ArrayList<>();
                        }
                    }
                }
            }

            return new Machine(lightDiagram, schematics, power);
        }

        /** State for FSM when parsing input */
        enum State {
            START,
            LIGHT_DIAGRAM,
            WIRING_SCHEMATICS,
            POWER
        }
    }
}
