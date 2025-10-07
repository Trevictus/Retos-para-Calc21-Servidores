# Tarea 2 Java por equipos, Calculadora

### Alumno(s):
- Víctor Gómez Tejada
- Pablo Fernández Fernández
- David Benavides Foncubierta
- José Antonio Díaz Busati

# Retos propuestos por nuestro equipo

### 1. Exportar resultados a .txt
Con esta funcionalidad el usuario, al evaluar una expresión, dispondra del historial de la expresión junto a los resultados de esta, guardados en un archivo .txt.  
Dicho de otro modo se guardaran las operaciones en un archivo .txt.  
Para ello utilizaremos `FileWriter` en el propio `Main.java`.

### 2. Convertir el Main en una sola linea de código
Encontrar la manera de dejar el Main en una sola linea, de forma que tendra que analizar una `EXPRESIÓN`

### 3. Dí las ventajas de separar `Parser` y `Lexer`
Con tus palabras.

### 4. Las ventajas de una interfaz sellada ante una jerarquía abierta
Con tus palabras.

# Retos afrontados por nuestro equipo

### Retos de comprensión
- **¿Qué hace la función peek()?**  
  Examina el siguiente carácter sin avanzar el índice en el lexer.
- **¿Qué hace la función number()?**  
  Extrae un literal numérico completo (incluyendo punto decimal) y lo convierte en token.
- **¿Qué hace la clase Evaluator()?**  
  Recorre el AST (Expr) usando pattern-matching y calcula el valor de la expresión.

### Retos de modificación
Para ejecutar el programa y realizar las comprobaciones hemos debido marcar la carpeta `src/main/java` como Sources Root y la carpeta `src/test/java` como Test Resources Root.
- **Calcular la división entera //**
  Se actualiza Binary en Expres.java para que admita operadores de más de un caracter.  
```java
record Binary(Expr left, String op, Expr right) implements Expr {}
```
  El método `lex()` de `Lexer.java` se modifica para poder capturar `DOUBLE_SLASH`.
```java
case '/' -> {
i++;
if (hasNext() && peek() == '/') { // detecta "//"
i++;
tokens.add(new Token(TokenType.DOUBLE_SLASH, "//", start));
} else {
tokens.add(new Token(TokenType.SLASH, "/", start));
}
}
```
Se añade una nueva constante en `TokenType` para representar la división entera.
```java
public enum TokenType {
    NUMBER, IDENT,
    PLUS, MINUS, STAR, SLASH, DOUBLE_SLASH,
    CARET,
    LPAREN, RPAREN,
    EOF
}
```
Se incorpora `DOUBLE_SLASH` en term() de `Parser.java`.
```java
private Expr term() {
        Expr left = factor();
        while (match(STAR) || match(SLASH) || match(DOUBLE_SLASH)) {
            String op = prev().lexeme(); // "/" o "//"
            Expr right = factor();
            left = new Binary(left, op, right);
        }
        return left;
    }
```
Se añade "//" a un caso nuevo en el switch que procesa `Binary`.
```java
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
                    case "^" -> Math.pow(l, r);
                    default -> throw new IllegalStateException("Operador no soportado: " + b.op());
                };
            }
```

- **Calcular el resto de una división %**

Se añade una nueva constante en `TokenType` para representar el resto.
```java
public enum TokenType {
    NUMBER, IDENT,
    PLUS, MINUS, STAR, SLASH, DOUBLE_SLASH, REST,
    CARET,
    LPAREN, RPAREN,
    EOF
}
```
En `Lexer.java`, añade el caso para el operador `%`.
```java
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
                case '/' -> {
                    i++;
                    if (hasNext() && peek() == '/') { // detecta "//"
                        i++;
                        tokens.add(new Token(TokenType.DOUBLE_SLASH, "//", start));
                    } else {
                        tokens.add(new Token(TokenType.SLASH, "/", start));
                    }
                }
                case '%' -> {
                    i++;
                    tokens.add(new Token(TokenType.REST, "%", start));
                }
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
```
En `Parser.java`, actualiza el método `term()` para incluir `REST`:
```java
private Expr term() {
    Expr left = factor();
    while (match(STAR) || match(SLASH) || match(DOUBLE_SLASH) || match(REST)) {
        String op = prev().lexeme(); // "/" o "//"
        Expr right = factor();
        left = new Binary(left, op, right);
    }
    return left;
}
```
En `Evaluator.java`, añade el caso para el operador `%` en el switch de `Binary`
```java
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
```
