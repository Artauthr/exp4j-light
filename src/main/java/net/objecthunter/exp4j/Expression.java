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
package net.objecthunter.exp4j;

import net.objecthunter.exp4j.function.Function;
import net.objecthunter.exp4j.function.Function0;
import net.objecthunter.exp4j.function.Function1;
import net.objecthunter.exp4j.function.Function2;
import net.objecthunter.exp4j.function.Functions;
import net.objecthunter.exp4j.operator.Operator;
import net.objecthunter.exp4j.operator.BinaryOperator;
import net.objecthunter.exp4j.operator.UnaryOperator;
import net.objecthunter.exp4j.tokenizer.*;

import java.util.*;

public class Expression {
    private final Token[] tokens;

    private final Map<String, MutableDouble> variables;

    private final int requiredStackSize;

    private final ArrayStack stack;

    /**
     * Creates a new expression that is a copy of the existing one.
     *
     * @param existing the expression to copy
     */
    public Expression(final Expression existing) {
        this.tokens = Arrays.copyOf(existing.tokens, existing.tokens.length);
        this.variables = new HashMap<>(existing.variables.size());
        for (Map.Entry<String, MutableDouble> entry : existing.variables.entrySet()) {
            this.variables.put(entry.getKey(), new MutableDouble(entry.getValue().value));
        }
        this.requiredStackSize = getRequiredStackSize(tokens);
        this.stack = new ArrayStack(this.requiredStackSize);
    }

    Expression(final Token[] tokens) {
        this.tokens = tokens;
        this.variables = new HashMap<>();
        this.requiredStackSize = getRequiredStackSize(tokens);
        this.stack = new ArrayStack(this.requiredStackSize);
    }

    public Expression setVariable(final String name, final double value) {
        this.checkVariableName(name);
        final MutableDouble holder = this.variables.get(name);
        if (holder != null) {
            holder.value = value;
        } else {
            this.variables.put(name, new MutableDouble(value));
        }
        return this;
    }

    private void clearStack () {
        this.stack.clear();
    }

    private void checkVariableName(String name) {
        if (Functions.getBuiltinFunction(name) != null) {
            throw new IllegalArgumentException("The variable name '" + name + "' is invalid. Since there exists a function with the same name");
        }
    }

    public Expression setVariables(Map<String, Double> variables) {
        for (Map.Entry<String, Double> v : variables.entrySet()) {
            this.setVariable(v.getKey(), v.getValue());
        }
        return this;
    }

    public Expression clearVariables() {
        this.variables.clear();
        return this;
    }

    public boolean hasVariable(String name) {
        return variables.containsKey(name);
    }

    public Set<String> getVariableNames() {
        return variables.keySet();
    }

    public ValidationResult validate(boolean checkVariablesSet) {
        final List<String> errors = new ArrayList<>(0);
        if (checkVariablesSet) {
            /* check that all vars have a value set */
            for (final Token t : this.tokens) {
                if (t.getType() == Token.TOKEN_VARIABLE) {
                    final String var = ((VariableToken) t).getName();
                    if (!variables.containsKey(var)) {
                        errors.add("The setVariable '" + var + "' has not been set");
                    }
                }
            }
        }

        /* Check if the number of operands, functions and operators match.
           The idea is to increment a counter for operands and decrease it for operators.
           When a function occurs the number of available arguments has to be greater
           than or equals to the function's expected number of arguments.
           The count has to be larger than 1 at all times and exactly 1 after all tokens
           have been processed */
        int count = 0;
        for (Token tok : this.tokens) {
            switch (tok.getType()) {
                case Token.TOKEN_NUMBER:
                case Token.TOKEN_VARIABLE:
                    count++;
                    break;
                case Token.TOKEN_FUNCTION:
                    final Function func = ((FunctionToken) tok).getFunction();
                    final int argsNum = func.getNumArguments();
                    if (argsNum > count) {
                        errors.add("Not enough arguments for '" + func.getName() + "'");
                    }
                    if (argsNum > 1) {
                        count -= argsNum - 1;
                    } else if (argsNum == 0) {
                        // see https://github.com/fasseg/exp4j/issues/59
                        count++;
                    }
                    break;
                case Token.TOKEN_OPERATOR:
                    Operator op = ((OperatorToken) tok).getOperator();
                    if (op.getNumOperands() == 2) {
                        count--;
                    }
                    break;
            }
            if (count < 1) {
                errors.add("Too many operators");
                return new ValidationResult(false, errors);
            }
        }
        if (count > 1) {
            errors.add("Too many operands");
        }
        return errors.isEmpty() ? ValidationResult.SUCCESS : new ValidationResult(false, errors);

    }

    public double evaluate() {
        clearStack();
        final ArrayStack output = this.stack;

        output.ensureCapacity(this.requiredStackSize);
        for (Token t : tokens) {
            if (t.getType() == Token.TOKEN_NUMBER) {
                output.push(((NumberToken) t).getValue());
            } else if (t.getType() == Token.TOKEN_VARIABLE) {
                final String name = ((VariableToken) t).getName();
                final MutableDouble value = this.variables.get(name);
                if (value == null) {
                    throw new IllegalArgumentException("No value has been set for the setVariable '" + name + "'.");
                }
                output.push(value.value);
            } else if (t.getType() == Token.TOKEN_OPERATOR) {
                OperatorToken op = (OperatorToken) t;
                final Operator operator = op.getOperator();
                final int operandCount = operator.getNumOperands();
                if (output.size() < operandCount) {
                    throw new IllegalArgumentException("Invalid number of operands available for '" + operator.getSymbol() + "' operator");
                }
                if (operator instanceof BinaryOperator) {
                    double rightArg = output.pop();
                    double leftArg = output.pop();
                    output.push(((BinaryOperator) operator).apply(leftArg, rightArg));
                } else if (operator instanceof UnaryOperator) {
                    double arg = output.pop();
                    output.push(((UnaryOperator) operator).apply(arg));
                }
            } else if (t.getType() == Token.TOKEN_FUNCTION) {
                FunctionToken func = (FunctionToken) t;
                final Function function = func.getFunction();
                final int numArguments = function.getNumArguments();
                if (output.size() < numArguments) {
                    throw new IllegalArgumentException("Invalid number of arguments available for '" + function.getName() + "' function");
                }
                if (function instanceof Function0) {
                    output.push(((Function0) function).apply());
                } else if (function instanceof Function1) {
                    output.push(((Function1) function).apply(output.pop()));
                } else if (function instanceof Function2) {
                    double arg2 = output.pop();
                    double arg1 = output.pop();
                    output.push(((Function2) function).apply(arg1, arg2));
                }
            }
        }
        if (output.size() > 1) {
            throw new IllegalArgumentException("Invalid number of items on the output queue. Might be caused by an invalid number of arguments for a function.");
        }
        return output.pop();
    }

    private static final class MutableDouble {
        double value;
        MutableDouble(final double value) {
            this.value = value;
        }
    }

    private static int getRequiredStackSize(final Token[] tokens) {
        int currentStackDepth = 0;
        int maxStackDepth = 0;
        int maxArity = 0;

        for (final Token token : tokens) {
            switch (token.getType()) {
                case Token.TOKEN_NUMBER:
                case Token.TOKEN_VARIABLE:
                    currentStackDepth++;
                    break;
                case Token.TOKEN_FUNCTION:
                    final Function function = ((FunctionToken) token).getFunction();
                    final int functionArity = function.getNumArguments();
                    if (functionArity > maxArity) {
                        maxArity = functionArity;
                    }
                    currentStackDepth -= functionArity;
                    if (currentStackDepth < 0) {
                        currentStackDepth = 0;
                    }
                    currentStackDepth++;
                    break;
                case Token.TOKEN_OPERATOR:
                    final Operator operator = ((OperatorToken) token).getOperator();
                    currentStackDepth -= operator.getNumOperands();
                    if (currentStackDepth < 0) {
                        currentStackDepth = 0;
                    }
                    currentStackDepth++;
                    break;
                default:
                    break;
            }
            if (currentStackDepth > maxStackDepth) {
                maxStackDepth = currentStackDepth;
            }
        }

        return Math.max(maxStackDepth, 1);
    }
}
