# Calc21 — Guía de lectura y diseño

## [§1] Objetivo
Calculadora en Java con `+ - * / ^`, paréntesis y `sin(x)`, `cos(x)`. Enseña buenas prácticas y Java 17–21.

## [§2] Arquitectura por fases
- **Lexer** → tokens.
- **Parser** → AST.
- **AST** (sealed + records) → modelo inmutable.
- **Evaluator** (switch + pattern matching) → evalúa el AST.
- **REPL** con text block.

## [§3] Gramática
```
expr    -> term (('+'|'-') term)*
term    -> factor (('*'|'/') factor)*
factor  -> power
power   -> unary ('^' power)?     // derecha
unary   -> ('+'|'-') unary | primary
primary -> NUMBER | IDENT '(' expr ')' | '(' expr ')'
```

## [§4] AST con sealed + records
- `NumberLit(double)`
- `Unary(char, Expr)`
- `Binary(Expr, char, Expr)`
- `Call(String, Expr)`

## [§5] Evaluación (switch + pattern matching)
- `switch (Expr)` con casos por subtipo.
- En `Binary`, `switch` por operador.

## [§6] Errores
- Lexer valida números y caracteres.
- Parser valida tokens esperados con posición.
- REPL captura y da mensajes claros.

## [§7] REPL con text block
- Ayuda inicial en `Main.HELP`.
- Bucle simple con `Scanner`.

## [§8] Extensiones
- Funciones: `tan, sqrt, log`.
- Variables y asignaciones.
- Funciones usuario y multi-argumento.
