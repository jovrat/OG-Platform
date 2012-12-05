// Autogenerated, do not edit!
/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.maths.dogma.autogen;
import com.opengamma.maths.commonapi.numbers.ComplexType;
import com.opengamma.maths.dogma.engine.language.InfixOperator;
import com.opengamma.maths.dogma.engine.language.UnaryFunction;
import com.opengamma.maths.dogma.engine.language.Function;
import com.opengamma.maths.dogma.engine.operationstack.InfixOpChain;
import com.opengamma.maths.dogma.engine.operationstack.MethodScraperForInfixOperators;
import com.opengamma.maths.dogma.engine.operationstack.MethodScraperForUnaryFunctions;
import com.opengamma.maths.dogma.engine.operationstack.OperatorDictionaryPopulator;
import com.opengamma.maths.dogma.engine.operationstack.RunInfixOpChain;
import com.opengamma.maths.dogma.engine.operationstack.RunUnaryFunctionChain;
import com.opengamma.maths.dogma.engine.operationstack.UnaryFunctionChain;
import com.opengamma.maths.lowlevelapi.functions.checkers.Catchers;
import com.opengamma.maths.dogma.engine.matrixinfo.ConversionCostAdjacencyMatrixStore;
import com.opengamma.maths.dogma.engine.matrixinfo.MatrixTypeToIndexMap;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opengamma.maths.dogma.engine.methodhookinstances.infix.Minus;
/**
 * Provides the DOGMA Language
 */
public class DOGMAMinus {
  private static DOGMAMinus s_instance;
  DOGMAMinus() {
  }
  public static DOGMAMinus getInstance() {
    return s_instance;
  }
  private static Logger s_log = LoggerFactory.getLogger(Minus.class);
  // switch for chatty start up
  private static boolean s_verbose;
  public DOGMAMinus(boolean verbose) {
    s_verbose = verbose;
  };
  private static RunInfixOpChain s_infixOpChainRunner = new RunInfixOpChain();
  private static RunUnaryFunctionChain s_unaryFunctionChainRunner = new RunUnaryFunctionChain();
  private static InfixOpChain[][] s_minusInstructions;
static {
final double[][] DefaultInfixFunctionEvalCosts = new double[][] {
{1.00, 1.00, 1.00, 1.00, 0.00, 1.00, 1.00, 1.00, 1.00, 1.00 },//
{1.00, 1.00, 0.00, 1.00, 0.00, 0.00, 0.00, 1.00, 0.00, 1.00 },//
{1.00, 0.00, 1.00, 1.00, 0.00, 0.00, 1.00, 1.00, 1.00, 1.00 },//
{1.00, 1.00, 1.00, 1.00, 0.00, 0.00, 0.00, 1.00, 0.00, 1.00 },//
{0.00, 0.00, 0.00, 0.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 },//
{1.00, 0.00, 0.00, 0.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 },//
{1.00, 0.00, 1.00, 0.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 },//
{1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 0.00, 1.00 },//
{1.00, 0.00, 1.00, 0.00, 1.00, 1.00, 1.00, 0.00, 1.00, 1.00 },//
{1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 } };
OGMatrix defaultInfixFunctionEvalCostsMatrix = new OGMatrix(DefaultInfixFunctionEvalCosts);
final double[][] DefaultUnaryFunctionEvalCosts = new double[][] {//
{1 },//
{1 },//
{2 },//
{3 },//
{3 },//
{5 },//
{5 },//
{5 },//
{10 },//
{20 } };
OGMatrix defaultUnaryFunctionEvalCostsMatrix = new OGMatrix(DefaultUnaryFunctionEvalCosts);
// Build instructions sets
 OperatorDictionaryPopulator<InfixOperator<OGArray<? extends Number>, OGArray<? extends Number>, OGArray<? extends Number>>> operatorDictInfix = new OperatorDictionaryPopulator<InfixOperator<OGArray<? extends Number>, OGArray<? extends Number>, OGArray<? extends Number>>>();
OperatorDictionaryPopulator<UnaryFunction<OGArray<? extends Number>, OGArray<? extends Number>>> operatorDictUnary = new OperatorDictionaryPopulator<UnaryFunction<OGArray<? extends Number>, OGArray<? extends Number>>>();
InfixOperator<OGArray<? extends Number>, OGArray<? extends Number>, OGArray<? extends Number>>[][] MinusFunctionTable = MethodScraperForInfixOperators.availableMethodsForInfixOp(operatorDictInfix.getOperationsMap(),Minus.class, s_verbose);
s_minusInstructions = MethodScraperForInfixOperators.computeFunctions(ConversionCostAdjacencyMatrixStore.getWeightedAdjacencyMatrix(),MinusFunctionTable, defaultInfixFunctionEvalCostsMatrix);


}

  public static OGArray<? extends Number> minus(OGArray<? extends Number> arg0, OGArray<? extends Number> arg1) {
    Catchers.catchNullFromArgList(arg0, 1);
    Catchers.catchNullFromArgList(arg1, 2);
    int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg0.getClass());
    int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
    OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_minusInstructions[type1][type2], arg0, arg1);
    return tmp;
  }

public static OGArray<? extends Number> minus(Number arg0, OGArray<? extends Number> arg1) {
  Catchers.catchNullFromArgList(arg0, 1);
  Catchers.catchNullFromArgList(arg1, 2);
OGArray<? extends Number> arg0rewrite;
if (arg0.getClass() == ComplexType.class) {
arg0rewrite = new OGComplexScalar(arg0);
} else {
arg0rewrite = new OGRealScalar(arg0.doubleValue());
}
int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg0rewrite.getClass());
int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_minusInstructions[type1][type2], arg0rewrite, arg1);
  return tmp;
}

public static OGArray<? extends Number>minus(OGArray<? extends Number> arg0, Number arg1) {
  Catchers.catchNullFromArgList(arg0, 1);
  Catchers.catchNullFromArgList(arg1, 2);
OGArray<? extends Number> arg1rewrite;
if (arg1.getClass() == ComplexType.class) {
arg1rewrite = new OGComplexScalar(arg1);
} else {
 arg1rewrite = new OGRealScalar(arg1.doubleValue());
 }
 int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg0.getClass());
 int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg1rewrite.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_minusInstructions[type1][type2], arg0, arg1rewrite);
  return tmp;
}

public static Number minus (Number arg0, Number arg1) {
  Catchers.catchNullFromArgList(arg0, 1);
  Catchers.catchNullFromArgList(arg1, 2);
OGArray<? extends Number> arg0rewrite;
if (arg0.getClass() == ComplexType.class) {
arg0rewrite = new OGComplexScalar(arg0);
} else {
arg0rewrite = new OGRealScalar(arg0.doubleValue());
}
OGArray<? extends Number> arg1rewrite;
if (arg1.getClass() == ComplexType.class) {
arg1rewrite = new OGComplexScalar(arg1);
} else {
 arg1rewrite = new OGRealScalar(arg1.doubleValue());
 }
 int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg0rewrite.getClass());
 int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg1rewrite.getClass());
  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_minusInstructions[type1][type2], arg0rewrite, arg1rewrite);
  return tmp.getEntry(0,0);
}


}
