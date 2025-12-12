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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Aspect used with the {@link Cache} annotation.
 *
 * @author benjaminmichael
 * @since 0.0.0
 */
@Aspect
public class CacheAspect {

   /** Map of method call to object value */
   private final Map<String, Object> cache = new ConcurrentHashMap<>();

   @Around("@annotation(io.github.brm.aoc2025.cmn.Cache)")
   public Object cacheMethod(ProceedingJoinPoint pjp) throws Throwable {
      String cacheKey = generateCacheKey(pjp);
      if (cache.containsKey(cacheKey)) {
         return cache.get(cacheKey);
      }

      Object result = pjp.proceed();
      cache.put(cacheKey, result);
      return result;
   }

   /**
    * Returns a cache key for a method where the key is a
    * concatenation of the target class name, method name,
    * and arguments.
    *
    * @param pjp the method join point.
    * @return a cache key.
    */
   private String generateCacheKey(ProceedingJoinPoint pjp) {
      String className = pjp.getTarget().getClass().getSimpleName();
      String methodName = pjp.getSignature().getName();
      String args = Arrays.toString(pjp.getArgs());
      return className + "." + methodName + "(" + args + ")";
   }
}
