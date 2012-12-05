// Autogenerated, do not edit! //CSIGNORE
/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.maths.dogma;
import com.opengamma.maths.commonapi.numbers.ComplexType;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGArray;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGComplexScalar;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGComplexDiagonalMatrix;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGComplexSparseMatrix;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGComplexMatrix;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGRealScalar;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGDiagonalMatrix;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGSparseMatrix;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGMatrix;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGIndexMatrix;
import com.opengamma.maths.highlevelapi.datatypes.primitive.OGPermutationMatrix;
import com.opengamma.maths.lowlevelapi.functions.checkers.Catchers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Suppression against unused code, typically imports, this is due to autogeneration and it being easier to include all at little extra cost.
 */
@SuppressWarnings("unused")
/**
 * Provides the DOGMA Language
 */
public class DOGMA {
/**
 * DOGMA Function: Copy( arg0 )
 * <p>
 * Short Description:
 * Returns a clean copy of the argument.
 * <p>
 * Full Description:
 * Returns a clean deep copy of the argument.
 * <p>
 * Example(s):
 * <p>
 * No examples given.
 * <p>
 * @param arg0 The array to be copied.
 * <p>
 * @return Returns a clean deep copy of the argument
 */
  public static OGArray<? extends Number> copy(OGArray<? extends Number> arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMACopy.copy(arg0);
  }

/**
 * DOGMA Function: Copy( arg0 )
 * <p>
 * Short Description:
 * <p>
 * Returns a clean copy of the argument.
 * <p>
 * Full Description:
 * <p>
 * Returns a clean deep copy of the argument.
 * <p>
 * Example(s):
 * <p>
 * No examples given.
 * <p>
 * @param arg0 The array to be copied.
 * <p>
 * @return Returns a clean deep copy of the argument
 */
  public static Number copy(Number arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMACopy.copy(arg0);
  }

/**
 * DOGMA Function: Ctranspose( arg0 )
 * <p>
 * Short Description:
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * Docs Missing - No description given
 * <p>
 * Example(s):
 * <p>
 * Docs Missing - No description given
 * <p>
 * @param arg0 Docs Missing - No description given
 * <p>
 * @return Docs Missing - No description given
 */
  public static OGArray<? extends Number> ctranspose(OGArray<? extends Number> arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMACtranspose.ctranspose(arg0);
  }

/**
 * DOGMA Function: Ctranspose( arg0 )
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example(s):
 * <p>
 * Docs Missing - No description given
 * <p>
 * @param arg0 Docs Missing - No description given
 * <p>
 * @return Docs Missing - No description given
 */
  public static Number ctranspose(Number arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMACtranspose.ctranspose(arg0);
  }

  public static void disp(OGComplexMatrix arg0) {
    com.opengamma.maths.dogma.autogen.DOGMADisp.disp(arg0);
  };


/**
 * DOGMA Function: Dot
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> dot(OGArray<? extends Number> arg0, OGArray<? extends Number> arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMADot.dot(arg0, arg1);
  }


/**
 * DOGMA Function: Dot
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> dot(Number arg0, OGArray<? extends Number> arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMADot.dot(arg0, arg1);
  }


/**
 * DOGMA Function: Dot
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> dot(OGArray<? extends Number> arg0, Number arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMADot.dot(arg0, arg1);
  }


/**
 * DOGMA Function: Dot
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static Number dot(Number arg0, Number arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMADot.dot(arg0, arg1);
  }

/**
 * DOGMA Function: Erf( arg0 )
 * <p>
 * Short Description:
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * Docs Missing - No description given
 * <p>
 * Example(s):
 * <p>
 * Docs Missing - No description given
 * <p>
 * @param arg0 Docs Missing - No description given
 * <p>
 * @return Docs Missing - No description given
 */
  public static OGArray<? extends Number> erf(OGArray<? extends Number> arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMAErf.erf(arg0);
  }

/**
 * DOGMA Function: Erf( arg0 )
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example(s):
 * <p>
 * Docs Missing - No description given
 * <p>
 * @param arg0 Docs Missing - No description given
 * <p>
 * @return Docs Missing - No description given
 */
  public static Number erf(Number arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMAErf.erf(arg0);
  }

/**
 * DOGMA Function: Erfc( arg0 )
 * <p>
 * Short Description:
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * Docs Missing - No description given
 * <p>
 * Example(s):
 * <p>
 * Docs Missing - No description given
 * <p>
 * @param arg0 Docs Missing - No description given
 * <p>
 * @return Docs Missing - No description given
 */
  public static OGArray<? extends Number> erfc(OGArray<? extends Number> arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMAErfc.erfc(arg0);
  }

/**
 * DOGMA Function: Erfc( arg0 )
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example(s):
 * <p>
 * Docs Missing - No description given
 * <p>
 * @param arg0 Docs Missing - No description given
 * <p>
 * @return Docs Missing - No description given
 */
  public static Number erfc(Number arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMAErfc.erfc(arg0);
  }

/**
 * DOGMA Function: Full( arg0 )
 * <p>
 * Short Description:
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * Docs Missing - No description given
 * <p>
 * Example(s):
 * <p>
 * Docs Missing - No description given
 * <p>
 * @param arg0 Docs Missing - No description given
 * <p>
 * @return Docs Missing - No description given
 */
  public static OGArray<? extends Number> full(OGArray<? extends Number> arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMAFull.full(arg0);
  }

/**
 * DOGMA Function: Full( arg0 )
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example(s):
 * <p>
 * Docs Missing - No description given
 * <p>
 * @param arg0 Docs Missing - No description given
 * <p>
 * @return Docs Missing - No description given
 */
  public static Number full(Number arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMAFull.full(arg0);
  }

  public static OGMatrix hilb(int arg0) {
    return     com.opengamma.maths.dogma.autogen.DOGMAHilb.hilb(arg0);
  };


/**
 * DOGMA Function: Horzcat
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> horzcat(OGArray<? extends Number> arg0, OGArray<? extends Number> arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAHorzcat.horzcat(arg0, arg1);
  }


/**
 * DOGMA Function: Horzcat
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> horzcat(Number arg0, OGArray<? extends Number> arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAHorzcat.horzcat(arg0, arg1);
  }


/**
 * DOGMA Function: Horzcat
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> horzcat(OGArray<? extends Number> arg0, Number arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAHorzcat.horzcat(arg0, arg1);
  }


/**
 * DOGMA Function: Horzcat
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static Number horzcat(Number arg0, Number arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAHorzcat.horzcat(arg0, arg1);
  }

  public static OGMatrix invhilb(int arg0) {
    return     com.opengamma.maths.dogma.autogen.DOGMAInvHilb.invhilb(arg0);
  };


/**
 * DOGMA Function: Minus
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> minus(OGArray<? extends Number> arg0, OGArray<? extends Number> arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAMinus.minus(arg0, arg1);
  }


/**
 * DOGMA Function: Minus
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> minus(Number arg0, OGArray<? extends Number> arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAMinus.minus(arg0, arg1);
  }


/**
 * DOGMA Function: Minus
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> minus(OGArray<? extends Number> arg0, Number arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAMinus.minus(arg0, arg1);
  }


/**
 * DOGMA Function: Minus
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static Number minus(Number arg0, Number arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAMinus.minus(arg0, arg1);
  }


/**
 * DOGMA Function: Mldivide
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> mldivide(OGArray<? extends Number> arg0, OGArray<? extends Number> arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAMldivide.mldivide(arg0, arg1);
  }


/**
 * DOGMA Function: Mldivide
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> mldivide(Number arg0, OGArray<? extends Number> arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAMldivide.mldivide(arg0, arg1);
  }


/**
 * DOGMA Function: Mldivide
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> mldivide(OGArray<? extends Number> arg0, Number arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAMldivide.mldivide(arg0, arg1);
  }


/**
 * DOGMA Function: Mldivide
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static Number mldivide(Number arg0, Number arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAMldivide.mldivide(arg0, arg1);
  }


/**
 * DOGMA Function: Mtimes
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> mtimes(OGArray<? extends Number> arg0, OGArray<? extends Number> arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAMtimes.mtimes(arg0, arg1);
  }


/**
 * DOGMA Function: Mtimes
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> mtimes(Number arg0, OGArray<? extends Number> arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAMtimes.mtimes(arg0, arg1);
  }


/**
 * DOGMA Function: Mtimes
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> mtimes(OGArray<? extends Number> arg0, Number arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAMtimes.mtimes(arg0, arg1);
  }


/**
 * DOGMA Function: Mtimes
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static Number mtimes(Number arg0, Number arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAMtimes.mtimes(arg0, arg1);
  }


/**
 * DOGMA Function: Plus
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> plus(OGArray<? extends Number> arg0, OGArray<? extends Number> arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAPlus.plus(arg0, arg1);
  }


/**
 * DOGMA Function: Plus
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> plus(Number arg0, OGArray<? extends Number> arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAPlus.plus(arg0, arg1);
  }


/**
 * DOGMA Function: Plus
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> plus(OGArray<? extends Number> arg0, Number arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAPlus.plus(arg0, arg1);
  }


/**
 * DOGMA Function: Plus
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static Number plus(Number arg0, Number arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMAPlus.plus(arg0, arg1);
  }


/**
 * DOGMA Function: Rdivide
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> rdivide(OGArray<? extends Number> arg0, OGArray<? extends Number> arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMARdivide.rdivide(arg0, arg1);
  }


/**
 * DOGMA Function: Rdivide
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> rdivide(Number arg0, OGArray<? extends Number> arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMARdivide.rdivide(arg0, arg1);
  }


/**
 * DOGMA Function: Rdivide
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static OGArray<? extends Number> rdivide(OGArray<? extends Number> arg0, Number arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMARdivide.rdivide(arg0, arg1);
  }


/**
 * DOGMA Function: Rdivide
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Docs Missing - No description given
 * }
 * </pre>
 *
 * @param arg0  Docs Missing - No description given
 * @param arg1  Docs Missing - No description given
 * 
 * @return Docs Missing - No description given
 */

  public static Number rdivide(Number arg0, Number arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMARdivide.rdivide(arg0, arg1);
  }

  public static OGMatrix rosser() {
    return     com.opengamma.maths.dogma.autogen.DOGMARosser.rosser();
  };

/**
 * DOGMA Function: Sin( arg0 )
 * <p>
 * Short Description:
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * Docs Missing - No description given
 * <p>
 * Example(s):
 * <p>
 * Docs Missing - No description given
 * <p>
 * @param arg0 Docs Missing - No description given
 * <p>
 * @return Docs Missing - No description given
 */
  public static OGArray<? extends Number> sin(OGArray<? extends Number> arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMASin.sin(arg0);
  }

/**
 * DOGMA Function: Sin( arg0 )
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example(s):
 * <p>
 * Docs Missing - No description given
 * <p>
 * @param arg0 Docs Missing - No description given
 * <p>
 * @return Docs Missing - No description given
 */
  public static Number sin(Number arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMASin.sin(arg0);
  }

/**
 * DOGMA Function: Sparse( arg0 )
 * <p>
 * Short Description:
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * Docs Missing - No description given
 * <p>
 * Example(s):
 * <p>
 * Docs Missing - No description given
 * <p>
 * @param arg0 Docs Missing - No description given
 * <p>
 * @return Docs Missing - No description given
 */
  public static OGArray<? extends Number> sparse(OGArray<? extends Number> arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMASparse.sparse(arg0);
  }

/**
 * DOGMA Function: Sparse( arg0 )
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example(s):
 * <p>
 * Docs Missing - No description given
 * <p>
 * @param arg0 Docs Missing - No description given
 * <p>
 * @return Docs Missing - No description given
 */
  public static Number sparse(Number arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMASparse.sparse(arg0);
  }

/**
 * DOGMA Function: Sqrt( arg0 )
 * <p>
 * Short Description:
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * Docs Missing - No description given
 * <p>
 * Example(s):
 * <p>
 * Docs Missing - No description given
 * <p>
 * @param arg0 Docs Missing - No description given
 * <p>
 * @return Docs Missing - No description given
 */
  public static OGArray<? extends Number> sqrt(OGArray<? extends Number> arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMASqrt.sqrt(arg0);
  }

/**
 * DOGMA Function: Sqrt( arg0 )
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example(s):
 * <p>
 * Docs Missing - No description given
 * <p>
 * @param arg0 Docs Missing - No description given
 * <p>
 * @return Docs Missing - No description given
 */
  public static Number sqrt(Number arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMASqrt.sqrt(arg0);
  }


/**
 * DOGMA Function: Times
 * <p>
 * Short Description:
 * <p>
 * Returns the element wise multiplication of the input arguments.
 * <p>
 * Full Description:
 * <p>
 * Returns the element wise multiplication of the input arguments. This is the
 * equivalent of the m-code operation arg0.*arg1. The operation is
 * vectorised such that if either argument is a single number it is applied as a
 * scaling, whereas if both arguments are the same dimension element wise
 * multiplication takes place.
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * OGMatrix foo = new OGMatrix(new double[][]{{1,2,3,4}});
 * OGArray<? extends Number> bar = times(7, foo);
 * disp(bar);
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Output [7, 14, 21, 28]
 * }
 * </pre>
 *
 * @param arg0  The first argument to multiply.
 * @param arg1  The second argument to multiply
 * 
 * @return No return value description given
 */

  public static OGArray<? extends Number> times(OGArray<? extends Number> arg0, OGArray<? extends Number> arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMATimes.times(arg0, arg1);
  }


/**
 * DOGMA Function: Times
 * <p>
 * Short Description:
 * <p>
 * Returns the element wise multiplication of the input arguments.
 * <p>
 * Full Description:
 * <p>
 * Returns the element wise multiplication of the input arguments. This is the
 * equivalent of the m-code operation arg0.*arg1. The operation is
 * vectorised such that if either argument is a single number it is applied as a
 * scaling, whereas if both arguments are the same dimension element wise
 * multiplication takes place.
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * OGMatrix foo = new OGMatrix(new double[][]{{1,2,3,4}});
 * OGArray<? extends Number> bar = times(7, foo);
 * disp(bar);
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Output [7, 14, 21, 28]
 * }
 * </pre>
 *
 * @param arg0  The first argument to multiply.
 * @param arg1  The second argument to multiply
 * 
 * @return No return value description given
 */

  public static OGArray<? extends Number> times(Number arg0, OGArray<? extends Number> arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMATimes.times(arg0, arg1);
  }


/**
 * DOGMA Function: Times
 * <p>
 * Short Description:
 * <p>
 * Returns the element wise multiplication of the input arguments.
 * <p>
 * Full Description:
 * <p>
 * Returns the element wise multiplication of the input arguments. This is the
 * equivalent of the m-code operation arg0.*arg1. The operation is
 * vectorised such that if either argument is a single number it is applied as a
 * scaling, whereas if both arguments are the same dimension element wise
 * multiplication takes place.
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * OGMatrix foo = new OGMatrix(new double[][]{{1,2,3,4}});
 * OGArray<? extends Number> bar = times(7, foo);
 * disp(bar);
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Output [7, 14, 21, 28]
 * }
 * </pre>
 *
 * @param arg0  The first argument to multiply.
 * @param arg1  The second argument to multiply
 * 
 * @return No return value description given
 */

  public static OGArray<? extends Number> times(OGArray<? extends Number> arg0, Number arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMATimes.times(arg0, arg1);
  }


/**
 * DOGMA Function: Times
 * <p>
 * Short Description:
 * <p>
 * Returns the element wise multiplication of the input arguments.
 * <p>
 * Full Description:
 * <p>
 * Returns the element wise multiplication of the input arguments. This is the
 * equivalent of the m-code operation arg0.*arg1. The operation is
 * vectorised such that if either argument is a single number it is applied as a
 * scaling, whereas if both arguments are the same dimension element wise
 * multiplication takes place.
 * <p>
 * Example Code:
 * <pre>
 * {@code 
 * OGMatrix foo = new OGMatrix(new double[][]{{1,2,3,4}});
 * OGArray<? extends Number> bar = times(7, foo);
 * disp(bar);
 * }
 * </pre>
 * <p>
 * Example Output:
 * <pre>
 * {@code 
 * Output [7, 14, 21, 28]
 * }
 * </pre>
 *
 * @param arg0  The first argument to multiply.
 * @param arg1  The second argument to multiply
 * 
 * @return No return value description given
 */

  public static Number times(Number arg0, Number arg1) {
    return com.opengamma.maths.dogma.autogen.DOGMATimes.times(arg0, arg1);
  }

/**
 * DOGMA Function: Transpose( arg0 )
 * <p>
 * Short Description:
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * Docs Missing - No description given
 * <p>
 * Example(s):
 * <p>
 * Docs Missing - No description given
 * <p>
 * @param arg0 Docs Missing - No description given
 * <p>
 * @return Docs Missing - No description given
 */
  public static OGArray<? extends Number> transpose(OGArray<? extends Number> arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMATranspose.transpose(arg0);
  }

/**
 * DOGMA Function: Transpose( arg0 )
 * <p>
 * Short Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Full Description:
 * <p>
 * Docs Missing - No description given
 * <p>
 * Example(s):
 * <p>
 * Docs Missing - No description given
 * <p>
 * @param arg0 Docs Missing - No description given
 * <p>
 * @return Docs Missing - No description given
 */
  public static Number transpose(Number arg0) {
    return com.opengamma.maths.dogma.autogen.DOGMATranspose.transpose(arg0);
  }

  public static OGMatrix vander(OGMatrix arg0, int arg1) {
    return     com.opengamma.maths.dogma.autogen.DOGMAVander.vander(arg0, arg1);
  };

  public static OGMatrix vander(OGMatrix arg0) {
    return     com.opengamma.maths.dogma.autogen.DOGMAVander.vander(arg0);
  };

  public static OGMatrix wilkinson(int arg0) {
    return     com.opengamma.maths.dogma.autogen.DOGMAWilkinson.wilkinson(arg0);
  };

}

