# Tarea 2 Java por equipos, Calculadora

### Alumno(s):
- Víctor Gómez Tejada
- Pablo Fernández Fernández
- David Benavides Foncubierta
- José Antonio Díaz Busati

# Retos propuestos por nuestro equipo

### Exportar resultados a .txt
Con esta funcionalidad el usuario, al evaluar una expresión, dispondra del historial de la expresión junto a los resultados de esta, guardados en un archivo .txt.  
Dicho de otro modo se guardaran las operaciones en un archivo .txt.  
Para ello utilizaremos `FileWriter` en el propio `Main.java`.

### Convertir el Main en una sola linea de código
Encontrar la manera de dejar el Main en una sola linea, de forma que tendra que analizar una `EXPRESIÓN`

### Dí las ventajas de separar `Parser` y `Lexer`
Con tus palabras.

### Las ventajas de una interfaz sellada ante una jerarquía abierta
Con tus palabras.

# Retos a afrontar por el equipo:

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

