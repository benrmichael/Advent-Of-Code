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

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Abstract class that can be extended by an advent of code
 * class used to solve the puzzles for any given day.
 *
 * @author benjaminmichael
 * @since 0.0.0
 */
public abstract class AdventOfCodePuzzle {

   private final int day;

   protected AdventOfCodePuzzle() {
       String name = getClass().getSimpleName();
       this.day = Integer.parseInt(name.substring(3));
   }

    /**
     * Read the input file and map each line using the given
     * mapper to the expected object type.
     *
     * @param mapper mapper to use
     * @return stream of type {@code T}
     * @param <T> the object type.
     */
   protected <T> Stream<T> readInput(Function<String, T> mapper) {
      return readInput()
              .filter(Objects::nonNull)
              .filter(Predicate.not(String::isBlank))
              .map(mapper);
   }

    /**
     * Read the input file, split each line of the input file
     * using the given {@code splitter}, then map each split
     * string to the type {@code T} with the mapper.
     *
     * @param splitter splitter used to split each input line.
     * @param mapper mapper to map to expected type.
     * @return stream of {@code T}
     * @param <T> the mapped object type
     */
    protected <T> Stream<T> readInput(String splitter, Function<String, T> mapper) {
       return readInput()
               .flatMap(line -> Arrays.stream(line.split(splitter)))
               .map(mapper);
    }

    /**
     * Read the input file for this day and return it as a
     * stream of the lines contained.
     *
     * @return stream of lines in the input file.
     */
    protected Stream<String> readInput() {
      String inputFileName = String.format("/day%d.txt", day);
      URL resource = AdventOfCodePuzzle.class.getResource(inputFileName);
      try {
         assert resource != null;
         Path path = Path.of(resource.toURI());
         return Files.lines(path);
      } catch (Exception exception) {
         throw new RuntimeException(exception);
      }
   }

   /** Solve part one */
   public abstract long solvePartOne();

   /** Solve part two */
   public abstract long solvePartTwo();

   /** Calls the methods to solve each puzzle and prints the answers */
   public void solvePuzzles() {
      long partOneSolution = solvePartOne();
      long partTwoSolution = solvePartTwo();

      System.out.printf("Answer to day one = \"%d\"\n", partOneSolution);
      System.out.printf("Answer to day two = \"%d\"\n", partTwoSolution);
   }
}
