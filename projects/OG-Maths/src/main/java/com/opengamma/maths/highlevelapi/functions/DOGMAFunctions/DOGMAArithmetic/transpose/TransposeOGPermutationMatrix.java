/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.maths.highlevelapi.functions.DOGMAFunctions.DOGMAArithmetic.transpose;

import com.opengamma.maths.highlevelapi.datatypes.primitive.OGPermutationMatrix;
import com.opengamma.maths.lowlevelapi.functions.checkers.Catchers;

/**
 * Transpose for {@link OGPermutationMatrix}
 */
public final class TransposeOGPermutationMatrix implements TransposeAbstract<OGPermutationMatrix> {
  private static TransposeOGPermutationMatrix s_instance = new TransposeOGPermutationMatrix();

  public static TransposeOGPermutationMatrix getInstance() {
    return s_instance;
  }

  private TransposeOGPermutationMatrix() {
  }

  @Override
  public OGPermutationMatrix transpose(OGPermutationMatrix array1) {
    Catchers.catchNullFromArgList(array1, 1);
    // transpose of a permutation matrix is just looking up its own indices in itself a(i,j)=a(j,i) but we walk just once with compressed canonical vectors
    // so if you have permutation vector P, range=1:length(P), the range(P) gives the transpose permutation
    int[] data = array1.getData();
    int dim = array1.getNumberOfRows();

    int[] tmp = new int[dim];
    for (int i = 0; i < dim; i++) {
      tmp[data[i]] = i;
    }
    return new OGPermutationMatrix(tmp);
  }
}