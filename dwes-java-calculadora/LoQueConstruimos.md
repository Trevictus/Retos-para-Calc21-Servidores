

---

## 📘 Conceptos básicos para entender la calculadora

### 🔹 ¿Qué es un **lexer**?

Un *lexer* (o *analizador léxico*) es un programa que convierte una **cadena de texto** en una lista de **tokens**.

* Entrada: `"3 + 5 * 2"`
* Salida: `[NUMBER("3"), PLUS("+"), NUMBER("5"), STAR("*"), NUMBER("2")]`

👉 Simplifica el trabajo posterior, porque el parser no tiene que leer caracteres sueltos, sino elementos con significado.

---

### 🔹 ¿Qué es un **token**?

Un **token** es una unidad mínima de significado en un lenguaje.
Ejemplos en nuestra calculadora:

* `NUMBER("3.14")` → un número decimal
* `PLUS("+")` → el operador suma
* `LPAREN("(")` → paréntesis izquierdo
* `IDENT("sin")` → un identificador (nombre de función)

Cada token tiene:

* **Tipo** (NUMBER, PLUS, …)
* **Lexema** (el texto exacto: `"3.14"`)
* **Posición** (para dar mensajes de error útiles).

---

### 🔹 ¿Qué es una **gramática**?

Una **gramática** define las reglas del lenguaje, es decir, **qué combinaciones de tokens forman expresiones válidas**.
La usamos para manejar la **precedencia de operadores** y la **asociatividad**.

Ejemplo reducido de nuestra gramática:

```
expr    -> term (('+'|'-') term)*
term    -> factor (('*'|'/') factor)*
factor  -> power
power   -> unary ('^' power)?     // asociatividad a derecha
unary   -> ('+'|'-') unary | primary
primary -> NUMBER | IDENT '(' expr ')' | '(' expr ')'
```

Esto significa, por ejemplo:

* Un `expr` es una o más sumas/restas de `term`.
* Un `term` es una o más multiplicaciones/divisiones de `factor`.
* Un `primary` puede ser un número, una llamada a función o algo entre paréntesis.

---

### 🔹 ¿Qué es un **parser**?

El *parser* (o *analizador sintáctico*) toma la lista de **tokens** y los organiza en una estructura jerárquica llamada **AST** (*Abstract Syntax Tree*).

Ejemplo:
`"1 + 2 * 3"`
→ AST:

```
   (+)
  /   \
(1)   (*)
      /  \
    (2)  (3)
```

Esto permite después evaluar de forma ordenada según precedencia.

---

### 🔹 ¿Qué es un **parser recursivo descendente**?

Es un tipo de parser que se implementa con **funciones recursivas**, donde cada función corresponde a una **regla de la gramática**.

Ejemplo:

* `expr()` → procesa sumas/restas
* `term()` → procesa multiplicaciones/divisiones
* `factor()` → procesa exponentes
* `primary()` → procesa números, funciones y paréntesis

👉 Es una técnica didáctica y directa: el código sigue casi literalmente la gramática.

---

### 🔹 ¿Qué es el **AST**?

El *Abstract Syntax Tree* (árbol de sintaxis abstracta) es una representación en memoria de la expresión.

* Nodo hoja: `NumberLit(3.14)`
* Nodo interno: `Binary(left=…, op='*', right=…)`

El AST es **independiente de la sintaxis exacta** (no guarda paréntesis ni espacios), y es lo que evaluamos después.

---

### 🔹 ¿Qué hace el **evaluador**?

El evaluador recorre el AST y calcula el resultado:

* Si es `NumberLit(3)`, devuelve `3`.
* Si es `Binary(+, left, right)`, evalúa `left` y `right` y los suma.
* Si es `Call("sin", arg)`, evalúa `arg` y llama a `Math.sin()`.

---



