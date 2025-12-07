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
package io.github.brm.aoc2025.cmn;

/**
 * Two directional point tuple
 *
 * @author benjaminmichael
 * @since 12-7-25
 */
public record Point2D(int x, int y) {

    /**
     * Return a transform of this point, adding the given
     * values for {@code x} and {@code y} to the point's
     * current value.
     *
     * @param x the x transform.
     * @param y the y transform.
     * @return the transformed point.
     */
    public Point2D transform(int x, int y) {
        return new Point2D(x() + x, y() + y);
    }
}
