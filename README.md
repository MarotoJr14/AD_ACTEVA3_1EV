# Sistema de Gestión de Inventario - AE03
## Acceso a Datos mediante Ficheros - Fase III

### Descripción

Sistema de gestión de inventario que utiliza archivos de texto plano para el almacenamiento de datos, implementando un registro completo de operaciones y un sistema de copias de seguridad.

Este proyecto desarrolla la Actividad Evaluable 3 (AE03) de la asignatura **Acceso a Datos** de 2º DAM.

---

## Objetivos de Aprendizaje

- Desarrollar aplicaciones que gestionen información almacenada en ficheros
- Identificar el campo de aplicación de ficheros secuenciales
- Utilizar clases específicas para operaciones con ficheros
- Implementar operaciones de creación, lectura, actualización y eliminación (CRUD)
- Gestionar flujos de datos basados en caracteres
- Implementar sistemas de respaldo y recuperación de información

---

## Estructura del Proyecto

```
AE03_Ficheros/
│
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── inventario/
│                   ├── Main.java                 # Clase principal con menú interactivo
│                   ├── model/
│                   │   └── Producto.java         # Modelo de datos
│                   ├── dao/
│                   │   └── InventarioDAO.java    # Acceso a datos
│                   ├── util/
│                   │   ├── RegistroUtil.java     # Gestión de logs
│                   │   └── BackupUtil.java       # Copias de seguridad
│                   └── service/
│                       └── InventarioService.java # Lógica de negocio
│
├── data/
│   ├── inventario.txt           # Archivo principal de inventario
│   ├── registro.txt             # Registro de operaciones
│   └── backups/                 # Copias de seguridad
│
└── README.md                    # Este archivo
```

---

## Características Principales

### 1. **Archivo Principal de Inventario** (`inventario.txt`)
- Formato CSV con separador punto y coma (`;`)
- Estructura: `id_producto;nombre;categoria;precio;stock`
- Cabecera descriptiva en la primera línea
- Gestión completa de productos

### 2. **Registro de Operaciones** (`registro.txt`)
- Registro automático de todas las operaciones
- Incluye fecha y hora de cada operación
- Tipos de operaciones registradas:
  - ALTA: Creación de productos
  - BAJA: Eliminación de productos
  - MODIFICACIÓN: Actualización de productos
  - BÚSQUEDA: Consultas realizadas
  - LISTAR: Listados de productos
  - BACKUP: Copias de seguridad
  - CONSULTA HISTORIAL: Acceso al registro

### 3. **Sistema de Copias de Seguridad**
- Generación automática de backups con nombre único
- Formato: `inventario_backup_YYYY-MM-DD.txt`
- Múltiples backups por día con contador incremental
- Funcionalidad de restauración
- Listado de backups disponibles
- Información sobre espacio ocupado

### 4. **Menú Interactivo**
Menú principal con cuatro secciones:
- **Gestión de Inventario**: CRUD completo de productos
- **Consultar Historial**: Ver registro de operaciones
- **Gestión de Backups**: Crear y restaurar copias de seguridad
- **Estadísticas**: Información general del inventario

---

## Funcionalidades Detalladas

### Gestión de Inventario

#### Listar Productos
Muestra todos los productos en formato tabla con:
- ID del producto
- Nombre
- Categoría
- Precio
- Stock disponible

#### Buscar Producto
Tres tipos de búsqueda:
1. **Por ID**: Búsqueda exacta por identificador
2. **Por nombre**: Búsqueda parcial (case-insensitive)
3. **Por categoría**: Filtrado por categoría específica

#### Añadir Producto
- Generación automática de ID único
- Validación de datos de entrada
- Registro automático de la operación

#### Modificar Producto
- Búsqueda del producto por ID
- Modificación selectiva de campos
- Preservación de valores no modificados
- Registro detallado de cambios

#### Eliminar Producto
- Búsqueda y visualización del producto
- Confirmación antes de eliminar
- Registro de la operación

#### Productos con Stock Bajo
- Filtrado por umbral de stock personalizable
- Alertas visuales para productos críticos

### Historial de Operaciones

- Consulta del archivo `registro.txt`
- Opción de ver todas las operaciones o las últimas N líneas
- Formato cronológico con timestamp
- Registro automático de la consulta

### Gestión de Copias de Seguridad

#### Crear Backup
- Copia completa del archivo de inventario
- Nombre único basado en fecha
- Almacenamiento en directorio dedicado
- Registro automático

#### Listar Backups
- Visualización de todos los backups disponibles
- Ordenados cronológicamente

#### Restaurar Backup
- Listado de backups disponibles
- Creación automática de backup de seguridad antes de restaurar
- Confirmación de restauración
- Registro de la operación

#### Información de Backups
- Número total de backups
- Espacio ocupado en disco

---

## Uso del Sistema

### Compilación y Ejecución

#### Opción 1: Con IDE (VSCode, IntelliJ IDEA, Eclipse)
1. Abrir el proyecto en tu IDE favorito
2. Ejecutar la clase `Main.java`

#### Opción 2: Línea de comandos
```bash
# Compilar
javac -d bin src/main/java/com/inventario/**/*.java

# Ejecutar
java -cp bin com.inventario.Main
```

### Navegación por el Menú

El sistema presenta un menú numérico intuitivo:
- Ingrese el número de la opción deseada
- Presione Enter
- Siga las instrucciones en pantalla

### Formato de Datos

#### Al añadir/modificar productos:
- **Nombre**: Texto libre
- **Categoría**: Texto libre (se recomienda usar categorías existentes)
- **Precio**: Número decimal (usar punto como separador decimal)
- **Stock**: Número entero positivo

---

## Archivos de Datos

### inventario.txt
Ejemplo de contenido:
```
id_producto;nombre;categoria;precio;stock
1;Auriculares 212;Electrónica;1322.25;423
2;Gorra 629;Ropa;745.71;116
3;Portátil 670;Informática;1082.42;261
```

### registro.txt
Ejemplo de contenido:
```
[2025-01-28 10:30:15] INICIALIZACIÓN: Sistema de gestión de inventario iniciado
[2025-01-28 10:30:45] LISTAR: Total de productos listados: 49
[2025-01-28 10:31:20] BÚSQUEDA: Criterio='ID=5', Resultados=1
[2025-01-28 10:32:10] ALTA: Producto ID=50, Nombre='Tablet Samsung'
[2025-01-28 10:33:05] MODIFICACIÓN: Producto ID=50 - Cambios: Stock: 10 -> 15
[2025-01-28 10:35:00] BACKUP: Backup creado: inventario_backup_2025-01-28.txt
```

---

## Características Técnicas

### Tecnologías Utilizadas
- **Lenguaje**: Java (versión 11 o superior)
- **I/O**: `java.io.*` (BufferedReader, BufferedWriter, FileReader, FileWriter)
- **NIO**: `java.nio.file.*` (Files, Path, Paths)
- **Fecha/Hora**: `java.time.*` (LocalDateTime, LocalDate, DateTimeFormatter)
- **Colecciones**: `java.util.*` (List, ArrayList, Optional, Scanner)

### Patrones de Diseño Implementados
- **DAO (Data Access Object)**: Separación de lógica de acceso a datos
- **Service Layer**: Capa de lógica de negocio
- **Utility Classes**: Clases auxiliares para funcionalidades específicas
- **MVC modificado**: Separación de modelo, lógica y presentación

### Manejo de Excepciones
- Try-with-resources para gestión automática de recursos
- Manejo específico de `IOException`
- Validación de datos de entrada
- Mensajes de error descriptivos

### Buenas Prácticas Implementadas
- **Separación de responsabilidades**: Cada clase tiene una función específica
- **DRY (Don't Repeat Yourself)**: Métodos reutilizables
- **Javadoc completo**: Documentación de todas las clases y métodos públicos
- **Nombres descriptivos**: Variables y métodos con nombres claros
- **Validación de entrada**: Control de datos del usuario
- **Logging completo**: Registro de todas las operaciones importantes

---

## Estadísticas del Inventario

El sistema proporciona:
- **Total de productos**: Cantidad total de productos en inventario
- **Stock total**: Suma de todas las unidades
- **Valor total**: Valor económico del inventario completo
- **Número de categorías**: Categorías únicas presentes
- **Información de backups**: Número y tamaño de copias de seguridad

---

## Consideraciones Importantes

### Limitaciones
- Los archivos deben mantener la estructura CSV especificada
- El sistema no soporta multiusuario concurrente
- No hay sistema de autenticación
- Los backups no se comprimen

### Recomendaciones
1. **Realizar backups regulares** antes de operaciones masivas
2. **Verificar el formato** al importar datos externos
3. **Revisar el registro** periódicamente para auditoría
4. **Limpiar backups antiguos** para liberar espacio
5. **Validar datos** antes de modificaciones importantes

### Seguridad de Datos
- El sistema crea un backup automático antes de restaurar
- Las eliminaciones requieren confirmación
- Todos los cambios quedan registrados
- Los archivos son de texto plano (sin encriptación)

---

## Posibles Mejoras Futuras

1. **Exportación/Importación**: 
   - Formato JSON
   - Formato XML
   - Excel (XLSX)

2. **Búsqueda Avanzada**:
   - Filtros combinados
   - Rangos de precio
   - Ordenación personalizada

3. **Gestión de Categorías**:
   - CRUD de categorías
   - Validación de categorías existentes

4. **Reportes**:
   - Productos más vendidos
   - Valor por categoría
   - Productos sin movimiento

5. **Interfaz Gráfica**:
   - JavaFX o Swing
   - Gráficos estadísticos
   - Tablas interactivas

6. **Seguridad**:
   - Encriptación de archivos
   - Sistema de usuarios
   - Permisos por rol

---

## Autor

**Gonzalo Bravo Maroto**  
Asignatura: Acceso a Datos  
Actividad: AE03 - Acceso a datos en ficheros – Fase III  
Fecha: 28 de diciembre de 2025

---

## Licencia

Este proyecto es de uso educativo para la asignatura de Acceso a Datos del ciclo de Desarrollo de Aplicaciones Multiplataforma (DAM).

---

## Contribuciones

Este es un proyecto académico individual. Sin embargo, se aceptan sugerencias y mejoras a través del profesor de la asignatura.

---

## Soporte

Para dudas o problemas con el sistema:
1. Revisar este README
2. Consultar el código fuente (está documentado)
3. Revisar el archivo `registro.txt` para diagnóstico
4. Contactar al profesor de Acceso a Datos

---

## Criterios de Evaluación Cumplidos

- Utilización de archivo principal de inventario (inventario.txt)  
- Archivo independiente de registro de operaciones (registro.txt)  
- Registro de todas las operaciones con fecha y hora  
- Sistema de copias de seguridad con nombre único  
- Menú de opciones completo y funcional  
- Gestión de ficheros secuenciales  
- Operaciones CRUD implementadas  
- Manejo de excepciones  
- Código bien estructurado y documentado  

---

**¡Gracias por utilizar el Sistema de Gestión de Inventario!** 🚀