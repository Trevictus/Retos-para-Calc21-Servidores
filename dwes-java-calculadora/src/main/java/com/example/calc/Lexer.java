package com.example.calc;

import java.util.ArrayList;
import java.util.List;

public final class Lexer {
    private final String src;
    private final int n;
    private int i = 0;

    public Lexer(String src) {
        this.src = src;
        this.n = src.length();
    }

    public List<Token> lex() {
        List<Token> tokens = new ArrayList<>();
        while (hasNext()) {
            char c = peek();
            if (Character.isWhitespace(c)) { i++; continue; }
            int start = i;
            switch (c) {
                case '+' -> { i++; tokens.add(new Token(TokenType.PLUS, "+", start)); }
                case '-' -> { i++; tokens.add(new Token(TokenType.MINUS, "-", start)); }
                case '*' -> { i++; tokens.add(new Token(TokenType.STAR, "*", start)); }
                case '/' -> { i++; tokens.add(new Token(TokenType.SLASH, "/", start)); }
                case '^' -> { i++; tokens.add(new Token(TokenType.CARET, "^", start)); }
                case '(' -> { i++; tokens.add(new Token(TokenType.LPAREN, "(", start)); }
                case ')' -> { i++; tokens.add(new Token(TokenType.RPAREN, ")", start)); }
                default -> {
                    if (Character.isDigit(c) || c == '.') {
                        tokens.add(number());
                    } else if (Character.isLetter(c)) {
                        tokens.add(ident());
                    } else {
                        throw new IllegalArgumentException("Carácter inesperado '" + c + "' en pos " + i);
                    }
                }
            }
        }
        tokens.add(new Token(TokenType.EOF, "", i));
        return tokens;
    }

    private Token number() {
        int start = i;
        boolean seenDot = false;
        while (hasNext()) {
            char c = peek();
            if (c == '.') {
                if (seenDot) break;
                seenDot = true;
                i++;
            } else if (Character.isDigit(c)) {
                i++;
            } else break;
        }
        String lex = src.substring(start, i);
        if (lex.equals(".")) throw new IllegalArgumentException("Número inválido en pos " + start);
        return new Token(TokenType.NUMBER, lex, start);
    }

    private Token ident() {
        int start = i;
        while (hasNext() && (Character.isLetterOrDigit(peek()) || peek() == '_')) i++;
        String name = src.substring(start, i);
        return new Token(TokenType.IDENT, name, start);
    }

    private boolean hasNext() { return i < n; }
    private char peek() { return src.charAt(i); }
}
