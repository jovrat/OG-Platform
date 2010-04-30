/**
 * Copyright (C) 2009 - 2009 by OpenGamma Inc.
 * 
 * Please see distribution for license.
 */
package com.opengamma.math.minimization;

import org.junit.Test;

public class DownhillSimplexMinimizerTest extends MultidimensionalMinimizerTestCase {

  @Test
  public void test() {
    final MultidimensionalMinimizer MINIMIZER = new DownhillSimplexMinimizer();
    super.testInputs(MINIMIZER);
    super.test(MINIMIZER);
  }

}
