# Calc21 — Calculadora educativa (Java 21)

- Expresiones: `+ - * / ^`, paréntesis, funciones `sin(x)`, `cos(x)`.
- Arquitectura por capas: Lexer → Parser → AST → Evaluator → REPL.
- Java moderno: records + sealed, switch con pattern matching, text blocks.
- Ejecutar:
  ```bash
  ./gradlew run
  ```
- Tests:
  ```bash
  ./gradlew test
  ```

> Si falta el wrapper JAR: `gradle wrapper --gradle-version 8.9` (una vez).
# dwes-java-calculadora
