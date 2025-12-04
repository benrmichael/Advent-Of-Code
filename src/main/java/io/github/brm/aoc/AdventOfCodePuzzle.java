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
package io.github.brm.aoc;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * TODO: Class description
 *
 * @author benjaminmichael
 * @since 0.0.0
 */
public abstract class AdventOfCodePuzzle {

   private final int year;
   private final int day;

   protected AdventOfCodePuzzle(int year, int day) {
      this.year = year;
      this.day = day;
   }

   protected <T> Stream<T> readInput(Function<String, T> mapper) {
      return readInput().stream()
              .filter(Objects::nonNull)
              .filter(Predicate.not(String::isBlank))
              .map(mapper);
   }

   protected List<String> readInput() {
      String inputFileName = String.format("/%d/day%d.txt", year, day);
      URL resource = AdventOfCodePuzzle.class.getResource(inputFileName);
      try {
         assert resource != null;
         Path path = Path.of(resource.toURI());
         return Files.readAllLines(path);
      } catch (Exception exception) {
         throw new RuntimeException(exception);
      }
   }

   public abstract Object solvePartOne();

   public abstract Object solvePartTwo();

   public void solvePuzzles() {
      Object partOneSolution = solvePartOne();
      Object partTwoSolution = solvePartTwo();

      System.out.printf("Answer to day one = \"%s\"\n", partOneSolution);
      System.out.printf("Answer to day two = \"%s\"\n", partTwoSolution);
   }
}
