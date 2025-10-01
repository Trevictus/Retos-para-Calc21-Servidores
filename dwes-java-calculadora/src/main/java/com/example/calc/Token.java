package com.example.calc;

public record Token(TokenType type, String lexeme, int position) { }
