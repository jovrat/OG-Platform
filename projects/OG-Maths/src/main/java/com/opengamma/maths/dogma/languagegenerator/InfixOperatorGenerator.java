/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.maths.dogma.languagegenerator;


/**
 * 
 */
public class InfixOperatorGenerator implements DogmaLangTokenToCodeGenerator {
  InfixOperatorGenerator() {
  }

  private static InfixOperatorGenerator s_instance = new InfixOperatorGenerator();

  public static InfixOperatorGenerator getInstance() {
    return s_instance;
  }

  private static String s_autogenPath = "com.opengamma.maths.dogma.autogen.";

  @Override
  public String generateMethodCode(FullToken f) {
    StringBuffer tmp = new StringBuffer();
    String lname = f.getSimpleName().toLowerCase();
    tmp.append("\n");
    tmp.append("public static OGArray<? extends Number>");
    tmp.append(lname);
    tmp.append("(OGArray<? extends Number> arg1, OGArray<? extends Number> arg2) {\n");
    tmp.append("  Catchers.catchNullFromArgList(arg1, 1);\n");
    tmp.append("  Catchers.catchNullFromArgList(arg2, 2);\n");
    tmp.append("  int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());\n");
    tmp.append("  int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2.getClass());\n");
    tmp.append("  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_");
    tmp.append(lname);
    tmp.append("Instructions[type1][type2], arg1, arg2);\n");
    tmp.append("  return tmp;\n");
    tmp.append("}\n\n");

    tmp.append("public static OGArray<? extends Number> ");
    tmp.append(lname);
    tmp.append("(Number arg1, OGArray<? extends Number> arg2) {\n");
    tmp.append("  Catchers.catchNullFromArgList(arg1, 1);\n");
    tmp.append("  Catchers.catchNullFromArgList(arg2, 2);\n");
    tmp.append("OGArray<? extends Number> arg1rewrite;\n");
    tmp.append("if (arg1.getClass() == ComplexType.class) {\n");
    tmp.append("arg1rewrite = new OGComplexScalar(arg1);\n");
    tmp.append("} else {\n");
    tmp.append("arg1rewrite = new OGRealScalar(arg1.doubleValue());\n");
    tmp.append("}\n");
    tmp.append("int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1rewrite.getClass());\n");
    tmp.append("int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2.getClass());\n");
    tmp.append("  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_");
    tmp.append(lname);
    tmp.append("Instructions[type1][type2], arg1rewrite, arg2);\n");
    tmp.append("  return tmp;\n");
    tmp.append("}\n\n");

    tmp.append("public static OGArray<? extends Number>");
    tmp.append(lname);
    tmp.append("(OGArray<? extends Number> arg1, Number arg2) {\n");
    tmp.append("  Catchers.catchNullFromArgList(arg1, 1);\n");
    tmp.append("  Catchers.catchNullFromArgList(arg2, 2);\n");
    tmp.append("OGArray<? extends Number> arg2rewrite;\n");
    tmp.append("if (arg2.getClass() == ComplexType.class) {\n");
    tmp.append("arg2rewrite = new OGComplexScalar(arg2);\n");
    tmp.append("} else {\n");
    tmp.append(" arg2rewrite = new OGRealScalar(arg2.doubleValue());\n");
    tmp.append(" }\n");
    tmp.append(" int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1.getClass());\n");
    tmp.append(" int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2rewrite.getClass());\n");
    tmp.append("  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_");
    tmp.append(lname);
    tmp.append("Instructions[type1][type2], arg1, arg2rewrite);\n");
    tmp.append("  return tmp;\n");
    tmp.append("}\n\n");

    tmp.append("public static Number ");
    tmp.append(lname);
    tmp.append(" (Number arg1, Number arg2) {\n");
    tmp.append("  Catchers.catchNullFromArgList(arg1, 1);\n");
    tmp.append("  Catchers.catchNullFromArgList(arg2, 2);\n");
    tmp.append("OGArray<? extends Number> arg1rewrite;\n");
    tmp.append("if (arg1.getClass() == ComplexType.class) {\n");
    tmp.append("arg1rewrite = new OGComplexScalar(arg1);\n");
    tmp.append("} else {\n");
    tmp.append("arg1rewrite = new OGRealScalar(arg1.doubleValue());\n");
    tmp.append("}\n");
    tmp.append("OGArray<? extends Number> arg2rewrite;\n");
    tmp.append("if (arg2.getClass() == ComplexType.class) {\n");
    tmp.append("arg2rewrite = new OGComplexScalar(arg2);\n");
    tmp.append("} else {\n");
    tmp.append(" arg2rewrite = new OGRealScalar(arg2.doubleValue());\n");
    tmp.append(" }\n");
    tmp.append(" int type1 = MatrixTypeToIndexMap.getIndexFromClass(arg1rewrite.getClass());\n");
    tmp.append(" int type2 = MatrixTypeToIndexMap.getIndexFromClass(arg2rewrite.getClass());\n");
    tmp.append("  OGArray<? extends Number> tmp = s_infixOpChainRunner.dispatch(s_");
    tmp.append(lname);
    tmp.append("Instructions[type1][type2], arg1rewrite, arg2rewrite);\n");
    tmp.append("  return tmp.getEntry(0,0);\n");
    tmp.append("}\n\n");
    return tmp.toString();
  }

  @Override
  public String generateEntryPointsCode(FullToken f) {
    StringBuffer tmp = new StringBuffer();
    String lname = f.getSimpleName().toLowerCase();
    String callStr = s_autogenPath + "DOGMA" + f.getSimpleName() + "." + lname + "(arg1, arg2);";
    tmp.append("\n");

    tmp.append("public static OGArray<? extends Number>");
    tmp.append(lname);
    tmp.append("(OGArray<? extends Number> arg1, OGArray<? extends Number> arg2) {\n");
    tmp.append("  return ");
    tmp.append(callStr);
    tmp.append("\n");
    tmp.append("}\n\n");

    tmp.append("public static OGArray<? extends Number>");
    tmp.append(lname);
    tmp.append("(Number arg1, OGArray<? extends Number> arg2) {\n");
    tmp.append("  return ");
    tmp.append(callStr);
    tmp.append("\n");
    tmp.append("}\n\n");

    tmp.append("public static OGArray<? extends Number>");
    tmp.append(lname);
    tmp.append("(OGArray<? extends Number> arg1,Number arg2) {\n");
    tmp.append("  return ");
    tmp.append(callStr);
    tmp.append("\n");
    tmp.append("}\n\n");

    tmp.append("public static Number ");
    tmp.append(lname);
    tmp.append("(Number arg1, Number arg2) {\n");
    tmp.append("  return ");
    tmp.append(callStr);
    tmp.append("\n");
    tmp.append("}\n\n");
    return tmp.toString();
  }

  @Override
  public String generateTableCode(FullToken f) {
    StringBuffer tmp = new StringBuffer();
    tmp.append("InfixOperator<OGArray<? extends Number>, OGArray<? extends Number>, OGArray<? extends Number>>[][] ");
    tmp.append(f.getSimpleName());
    tmp.append("FunctionTable");
    tmp.append(" = MethodScraperForInfixOperators.availableMethodsForInfixOp(operatorDictInfix.getOperationsMap(),");
    tmp.append(f.getSimpleName());
    tmp.append(".class, s_verbose);\n");
    tmp.append("s_");
    tmp.append(f.getSimpleName().toLowerCase());
    tmp.append("Instructions = MethodScraperForInfixOperators.computeFunctions(ConversionCostAdjacencyMatrixStore.getWeightedAdjacencyMatrix(),");
    tmp.append(f.getSimpleName());
    tmp.append("FunctionTable, defaultInfixFunctionEvalCostsMatrix);\n\n");
    return tmp.toString();
  }

  @Override
  public String generateTableCodeVariables(FullToken f) {
    StringBuffer tmp = new StringBuffer();
    tmp.append("  private static InfixOpChain[][] s_");
    tmp.append(f.getSimpleName().toLowerCase());
    tmp.append("Instructions; //CSOFF");
    tmp.append("\n");
    return tmp.toString();
  }

}
