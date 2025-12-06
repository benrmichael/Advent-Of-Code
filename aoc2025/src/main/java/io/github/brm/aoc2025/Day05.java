//========================================================================
//
//                       U N C L A S S I F I E D
//
//========================================================================
//  Copyright (c) 2025 Chesapeake Technology International Corp.
//  ALL RIGHTS RESERVED
//  This material may be reproduced by or for the U.S. Government
//  pursuant to the copyright license under the clause at
//  DFARS 252.227-7013 (OCT 1988).
//========================================================================
//  SBIR Data Rights Statement
//  Contract Number: N68335-13-C-0258
//
// Expiration of SBIR Data Rights Period:
//     5 years after completion of final contract modification
//
// The Government's rights to use, modify, reproduce, release, perform,
//  display, or disclose technical data or computer software marked with
//  this legend are restricted during the period shown as provided in
//  paragraph (b)(4) of the Rights in Noncommercial Technical Data and
//  Computer Software--Small Business Innovative Research (SBIR) Program
//  clause contained in the above identified contract. No restrictions
//  apply after the expiration date shown above. Any reproduction of
//  technical data, computer software, or portions thereof marked with
//  this legend must also reproduce the markings.
//========================================================================
package io.github.brm.aoc2025;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static java.lang.Long.parseLong;

/**
 * Day 5: Cafeteria
 *
 * @author Copyright 2025 Chesapeake Technology International Corp.
 * @since 12-5-25
 */
public class Day05 extends AdventOfCodePuzzle {

    private final List<long[]> freshRanges;
    private final List<Long> ingredients = new ArrayList<>();

    /** Day five */
    public Day05() {
        List<long[]> ranges = new ArrayList<>();
        Iterator<String> iter = readInput().iterator();
        while (iter.hasNext()) {
            String line = iter.next();
            if (line.isBlank()) continue;

            if (line.contains("-")) {
                String[] split = line.split("-");
                long start = parseLong(split[0]);
                long end = parseLong(split[1]);
                ranges.add(new long[]{start, end});
            } else {
                ingredients.add(parseLong(line));
            }
        }

        this.freshRanges = combineRanges(ranges);
    }

    /**
     * Combines the provided list of ranges into the smallest set
     * needed to represent them. It does so by combining ranges
     * which overlap with one another into a single range.
     *
     * @param ranges the ranges to combine.
     * @return list of ranges with all overlapping ranges combined.
     */
    private List<long[]> combineRanges(List<long[]> ranges) {
        // Sort by starting value of range, which lets
        // us do a single iteration over the list
        ranges.sort(Comparator.comparingLong(a -> a[0]));

        List<long[]> result = new ArrayList<>();
        result.add(new long[]{ranges.get(0)[0], ranges.get(0)[1]});

        for (int i = 1; i < ranges.size(); i++) {
            long[] prev = result.get(result.size() - 1);
            long[] current = ranges.get(i);

            // If the start of the current range is less
            // than or equal to the end of the last range
            // we know they overlap with one another
            if (current[0] <= prev[1]) {
                prev[1] = Math.max(prev[1], current[1]);
            } else {
                result.add(new long[]{current[0], current[1]});
            }
        }

        return result;
    }

    @Override
    public long solvePartOne() {
        int freshIngredients = 0;
        for (Long ingredient : ingredients) {
            for (long[] freshRange : freshRanges) {
                if (ingredient >= freshRange[0] && ingredient <= freshRange[1]) {
                    freshIngredients++;
                    break;
                }
            }
        }

        return freshIngredients;
    }

    @Override
    public long solvePartTwo() {
        return freshRanges.stream()
                .mapToLong(arr -> arr[1] - arr[0] + 1)
                .sum();
    }

    /** Solve day five */
    public static void main(String[] args) {
        new Day05().solvePuzzles();
    }
}
