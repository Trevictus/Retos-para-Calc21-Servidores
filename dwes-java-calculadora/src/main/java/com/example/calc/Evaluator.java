package com.example.calc;

import static com.example.calc.Expr.*;

public final class Evaluator {
    private Evaluator() { throw new AssertionError("No instanciable"); }

    public static double eval(Expr e) {
        return switch (e) {
            case NumberLit n -> n.value();
            case Unary u -> {
                double v = eval(u.expr());
                yield (u.op() == '-') ? -v : +v;
            }
            case Binary b -> {
                double l = eval(b.left());
                double r = eval(b.right());
                yield switch (b.op()) {
                    case "+" -> l + r;
                    case "-" -> l - r;
                    case "*" -> l * r;
                    case "/" -> l / r;
                    case "//" -> {
                        if (r == 0) throw new ArithmeticException("División por cero");
                        yield Math.floor(l / r);
                    }
                    case "%" -> {
                        if (r == 0) throw new ArithmeticException("División por cero en operación módulo");
                        yield l % r;
                    }
                    case "^" -> Math.pow(l, r);
                    default -> throw new IllegalStateException("Operador no soportado: " + b.op());
                };
            }
            case Call c -> {
                double x = eval(c.arg());
                yield switch (c.name()) {
                    case "sin" -> Math.sin(x);
                    case "cos" -> Math.cos(x);
                    default -> throw new IllegalArgumentException("Función no soportada: " + c.name());
                };
            }
        };
    }
}
