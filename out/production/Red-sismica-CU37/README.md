# Red Sísmica – Cierre de Orden de Inspección (CU 37)

Este proyecto implementa el **Caso de Uso 37 – Dar cierre a orden de inspección** de la Red Sísmica, simulando todo el flujo principal y permitiendo probar los **flujos alternativos** cambiando la sesión activa.

---

## 📁 Estructura del proyecto

```
src/
└── control/
    └── GestorCerrarOrdenInspeccion.java
└── boundary/
    ├── InterfazCerrarOrdenInspeccion.java
    ├── InterfazNotificacionMail.java
    └── InterfazMonitorCCRS.java
└── entity/
    ├── Empleado.java
    ├── Estado.java
    ├── MotivoTipo.java
    ├── OrdenDeInspeccion.java
    ├── Rol.java
    ├── Sesion.java
    ├── Sismografo.java
    └── CambioEstado.java
README.md
.gitignore
```

- **control/**: gestor que orquesta todo el flujo.
- **boundary/**: clases que simulan la UI y notificaciones (mail y monitor CCRS).
- **entity/**: entidades de dominio y sus lógicas de negocio.

---

## ⚙️ Cómo ejecutarlo

1. **Clona** el repositorio y sitúate en la carpeta del proyecto:
   ```bash
   git clone https://github.com/GinOnline04/Red-sismica-CU37.git
   cd Red-sismica-CU37
   ```
2. **Compila** las clases Java:
   ```bash
   javac -d out src/**/*.java
   ```
3. **Ejecuta** un `Main` de prueba (o creá uno propio) que invoque:
   ```java
   GestorCerrarOrdenInspeccion gestor =
       new GestorCerrarOrdenInspeccion(
         new InterfazCerrarOrdenInspeccionImpl(),
         new InterfazNotificacionMail(),
         new InterfazMonitorCCRS()
       );
   gestor.iniciarCierreOrdenInspeccion();
   // luego llama a los métodos de interacción simulada...
   ```
4. Observá por **consola** la selección de orden, entrada de observaciones, motivos, y las notificaciones (mail + monitor).

---

## 🔀 Flujos alternativos

Para probar los flujos A1–A7, editá **solo** la línea de creación de la sesión en el método `generardatos()` de `GestorCerrarOrdenInspeccion`:

```java
// Caso normal: el RI tiene órdenes realizadas
this.sesionActual = new Sesion(usuario1, LocalDateTime.now());

// Flujo A1: el RI NO tiene órdenes realizadas
// this.sesionActual = new Sesion(usuario2, LocalDateTime.now());
```

- **A1**: descomentar la segunda línea y comentar la primera → la lista de órdenes estará vacía.
- **A2–A7**: podés combinar cambios en los datos (por ejemplo, cambiar estados, eliminar motivos o cancelar confirmación) para simular los distintos flujos alternativos.

---

## 📝 Notas y próximos pasos

- La función `generardatos()` carga datos simulados (mock) para: usuarios, empleados, roles, estaciones, sismógrafos, estados, órdenes y motivos.
- Para integrarlo con una base de datos real, reemplazar las listas internas (`todasLasOrdenes`, `empleados`, etc.) por servicios o repositorios.
- Se recomienda agregar validaciones más robustas y manejo de errores.
- Implementar pruebas unitarias con JUnit para verificar el comportamiento del flujo principal y de los alternativos.
- Comentar los métodos clave con JavaDoc para facilitar el mantenimiento colaborativo.

---