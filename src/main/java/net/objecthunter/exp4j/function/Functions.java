/*
 * Copyright 2014 Frank Asseg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.objecthunter.exp4j.function;

/**
 * Class representing the builtin functions available for use in expressions
 */
public class Functions {

    private static final int INDEX_SIN = 0;
    private static final int INDEX_COS = 1;
    private static final int INDEX_TAN = 2;
    private static final int INDEX_CSC = 3;
    private static final int INDEX_SEC = 4;
    private static final int INDEX_COT = 5;
    private static final int INDEX_SINH = 6;
    private static final int INDEX_COSH = 7;
    private static final int INDEX_TANH = 8;
    private static final int INDEX_CSCH = 9;
    private static final int INDEX_SECH = 10;
    private static final int INDEX_COTH = 11;
    private static final int INDEX_ASIN = 12;
    private static final int INDEX_ACOS = 13;
    private static final int INDEX_ATAN = 14;
    private static final int INDEX_SQRT = 15;
    private static final int INDEX_CBRT = 16;
    private static final int INDEX_ABS = 17;
    private static final int INDEX_CEIL = 18;
    private static final int INDEX_FLOOR = 19;
    private static final int INDEX_POW = 20;
    private static final int INDEX_EXP = 21;
    private static final int INDEX_EXPM1 = 22;
    private static final int INDEX_LOG10 = 23;
    private static final int INDEX_LOG2 = 24;
    private static final int INDEX_LOG = 25;
    private static final int INDEX_LOG1P = 26;
    private static final int INDEX_LOGB = 27;
    private static final int INDEX_SGN = 28;
    private static final int INDEX_TO_RADIAN = 29;
    private static final int INDEX_TO_DEGREE = 30;

    private static final Function[] BUILT_IN_FUNCTIONS = new Function[31];

    static {
        BUILT_IN_FUNCTIONS[INDEX_SIN] = new Function1("sin") {
            @Override
            public double apply(final double arg) {
                return Math.sin(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_COS] = new Function1("cos") {
            @Override
            public double apply(final double arg) {
                return Math.cos(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_TAN] = new Function1("tan") {
            @Override
            public double apply(final double arg) {
                return Math.tan(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_COT] = new Function1("cot") {
            @Override
            public double apply(final double arg) {
                double tan = Math.tan(arg);
                if (tan == 0d) {
                    throw new ArithmeticException("Division by zero in cotangent!");
                }
                return 1d / tan;
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_LOG] = new Function1("log") {
            @Override
            public double apply(final double arg) {
                return Math.log(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_LOG2] = new Function1("log2") {
            @Override
            public double apply(final double arg) {
                return Math.log(arg) / Math.log(2d);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_LOG10] = new Function1("log10") {
            @Override
            public double apply(final double arg) {
                return Math.log10(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_LOG1P] = new Function1("log1p") {
            @Override
            public double apply(final double arg) {
                return Math.log1p(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_ABS] = new Function1("abs") {
            @Override
            public double apply(final double arg) {
                return Math.abs(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_ACOS] = new Function1("acos") {
            @Override
            public double apply(final double arg) {
                return Math.acos(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_ASIN] = new Function1("asin") {
            @Override
            public double apply(final double arg) {
                return Math.asin(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_ATAN] = new Function1("atan") {
            @Override
            public double apply(final double arg) {
                return Math.atan(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_CBRT] = new Function1("cbrt") {
            @Override
            public double apply(final double arg) {
                return Math.cbrt(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_FLOOR] = new Function1("floor") {
            @Override
            public double apply(final double arg) {
                return Math.floor(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_SINH] = new Function1("sinh") {
            @Override
            public double apply(final double arg) {
                return Math.sinh(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_SQRT] = new Function1("sqrt") {
            @Override
            public double apply(final double arg) {
                return Math.sqrt(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_TANH] = new Function1("tanh") {
            @Override
            public double apply(final double arg) {
                return Math.tanh(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_COSH] = new Function1("cosh") {
            @Override
            public double apply(final double arg) {
                return Math.cosh(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_CEIL] = new Function1("ceil") {
            @Override
            public double apply(final double arg) {
                return Math.ceil(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_POW] = new Function2("pow") {
            @Override
            public double apply(final double arg1, final double arg2) {
                return Math.pow(arg1, arg2);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_EXP] = new Function1("exp") {
            @Override
            public double apply(final double arg) {
                return Math.exp(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_EXPM1] = new Function1("expm1") {
            @Override
            public double apply(final double arg) {
                return Math.expm1(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_SGN] = new Function1("signum") {
            @Override
            public double apply(final double arg) {
                if (arg > 0) {
                    return 1;
                } else if (arg < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_CSC] = new Function1("csc") {
            @Override
            public double apply(final double arg) {
                double sin = Math.sin(arg);
                if (sin == 0d) {
                    throw new ArithmeticException("Division by zero in cosecant!");
                }
                return 1d / sin;
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_SEC] = new Function1("sec") {
            @Override
            public double apply(final double arg) {
                double cos = Math.cos(arg);
                if (cos == 0d) {
                    throw new ArithmeticException("Division by zero in secant!");
                }
                return 1d / cos;
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_CSCH] = new Function1("csch") {
            @Override
            public double apply(final double arg) {
                if (arg == 0d) {
                    return 0;
                }
                return 1d / Math.sinh(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_SECH] = new Function1("sech") {
            @Override
            public double apply(final double arg) {
                return 1d / Math.cosh(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_COTH] = new Function1("coth") {
            @Override
            public double apply(final double arg) {
                return Math.cosh(arg) / Math.sinh(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_LOGB] = new Function2("logb") {
            @Override
            public double apply(final double base, final double value) {
                return Math.log(value) / Math.log(base);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_TO_RADIAN] = new Function1("toradian") {
            @Override
            public double apply(final double arg) {
                return Math.toRadians(arg);
            }
        };
        BUILT_IN_FUNCTIONS[INDEX_TO_DEGREE] = new Function1("todegree") {
            @Override
            public double apply(final double arg) {
                return Math.toDegrees(arg);
            }
        };

    }

    /**
     * Get the builtin function for a given name
     *
     * @param name te name of the function
     * @return a Function instance
     */
    public static Function getBuiltinFunction(final String name) {

        switch (name) {
            case "sin":
                return BUILT_IN_FUNCTIONS[INDEX_SIN];
            case "cos":
                return BUILT_IN_FUNCTIONS[INDEX_COS];
            case "tan":
                return BUILT_IN_FUNCTIONS[INDEX_TAN];
            case "cot":
                return BUILT_IN_FUNCTIONS[INDEX_COT];
            case "asin":
                return BUILT_IN_FUNCTIONS[INDEX_ASIN];
            case "acos":
                return BUILT_IN_FUNCTIONS[INDEX_ACOS];
            case "atan":
                return BUILT_IN_FUNCTIONS[INDEX_ATAN];
            case "sinh":
                return BUILT_IN_FUNCTIONS[INDEX_SINH];
            case "cosh":
                return BUILT_IN_FUNCTIONS[INDEX_COSH];
            case "tanh":
                return BUILT_IN_FUNCTIONS[INDEX_TANH];
            case "abs":
                return BUILT_IN_FUNCTIONS[INDEX_ABS];
            case "log":
                return BUILT_IN_FUNCTIONS[INDEX_LOG];
            case "log10":
                return BUILT_IN_FUNCTIONS[INDEX_LOG10];
            case "log2":
                return BUILT_IN_FUNCTIONS[INDEX_LOG2];
            case "log1p":
                return BUILT_IN_FUNCTIONS[INDEX_LOG1P];
            case "ceil":
                return BUILT_IN_FUNCTIONS[INDEX_CEIL];
            case "floor":
                return BUILT_IN_FUNCTIONS[INDEX_FLOOR];
            case "sqrt":
                return BUILT_IN_FUNCTIONS[INDEX_SQRT];
            case "cbrt":
                return BUILT_IN_FUNCTIONS[INDEX_CBRT];
            case "pow":
                return BUILT_IN_FUNCTIONS[INDEX_POW];
            case "exp":
                return BUILT_IN_FUNCTIONS[INDEX_EXP];
            case "expm1":
                return BUILT_IN_FUNCTIONS[INDEX_EXPM1];
            case "signum":
                return BUILT_IN_FUNCTIONS[INDEX_SGN];
            case "csc":
                return BUILT_IN_FUNCTIONS[INDEX_CSC];
            case "sec":
                return BUILT_IN_FUNCTIONS[INDEX_SEC];
            case "csch":
                return BUILT_IN_FUNCTIONS[INDEX_CSCH];
            case "sech":
                return BUILT_IN_FUNCTIONS[INDEX_SECH];
            case "coth":
                return BUILT_IN_FUNCTIONS[INDEX_COTH];
            case "toradian":
                return BUILT_IN_FUNCTIONS[INDEX_TO_RADIAN];
            case "todegree":
                return BUILT_IN_FUNCTIONS[INDEX_TO_DEGREE];
            default:
                return null;
        }
    }

}
