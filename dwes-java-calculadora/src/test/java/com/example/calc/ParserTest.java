package com.example.calc;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
    private double run(String s) {
        var tokens = new Lexer(s).lex();
        var ast = new Parser(tokens).parse();
        return Evaluator.eval(ast);
    }

    @Test void precedence_basic() {
        assertEquals(7.0, run("1+2*3"), 1e-9);
        assertEquals(9.0, run("(1+2)*3"), 1e-9);
    }

    @Test void power_right_assoc() {
        assertEquals(Math.pow(2, Math.pow(3, 2)), run("2^3^2"), 1e-9);
    }

    @Test void trig() {
        assertEquals(1.0, run("sin(3.1415926535/2)"), 1e-6);
        assertEquals(1.0, run("cos(0)"), 1e-9);
    }
}
