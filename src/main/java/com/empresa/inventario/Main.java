package com.empresa.inventario;

import com.empresa.inventario.model.Producto;
import com.empresa.inventario.service.InventarioService;
import com.empresa.inventario.util.RegistroUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Clase principal del sistema de gestión de inventario.
 * Proporciona un menú interactivo para todas las operaciones del sistema.
 */
public class Main {
    
    private static final InventarioService service = new InventarioService();
    private static final Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════╗");
        System.out.println("║   SISTEMA DE GESTIÓN DE INVENTARIO     ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println();
        
        RegistroUtil.registrarInicioSistema();
        
        boolean salir = false;
        
        while (!salir) {
            mostrarMenuPrincipal();
            int opcion = leerOpcion();
            
            switch (opcion) {
                case 1: // Gestión de Inventario
                    boolean volverInventario = false;
                    while (!volverInventario) {
                        mostrarMenuGestionInventario();
                        int opcionInventario = leerOpcion();
                        
                        switch (opcionInventario) {
                            case 1: // Listar productos
                                listarProductos();
                                break;
                            case 2: // Buscar producto
                                buscarProducto();
                                break;
                            case 3: // Añadir producto
                                anadirProducto();
                                break;
                            case 4: // Modificar producto
                                modificarProducto();
                                break;
                            case 5: // Eliminar producto
                                eliminarProducto();
                                break;
                            case 6: // Productos con stock bajo
                                productosStockBajo();
                                break;
                            case 0: // Volver al menú principal
                                volverInventario = true;
                                break;
                            default: // Opción no válida
                                System.out.println("\n[INFO] Opción no válida.");
                        }
                    }
                    break;
                    
                case 2: // Consultar Historial
                    consultarHistorial();
                    break;
                    
                case 3: // Gestión de Backups
                    boolean volverBackup = false;
                    while (!volverBackup) {
                        mostrarMenuBackup();
                        int opcionBackup = leerOpcion();
                        
                        switch (opcionBackup) {
                            case 1: // Crear backup
                                service.crearBackup();
                                break;
                            case 2: // Listar backups
                                listarBackups();
                                break;
                            case 3: // Restaurar backup
                                restaurarBackup();
                                break;
                            case 4: // Información de backups
                                System.out.println("\n" + service.obtenerInfoBackups());
                                break;
                            case 0: // Volver al menú principal
                                volverBackup = true;
                                break;
                            default: // Opción no válida
                                System.out.println("\n[INFO] Opción no válida.");
                        }
                    }
                    break;
                    
                case 4: // Estadísticas
                    mostrarEstadisticas();
                    break;
                    
                case 0: // Salir
                    salir = true;
                    System.out.println("\n¡Hasta pronto!");
                    break;
                    
                default:
                    System.out.println("\n❌ Opción no válida. Intente nuevamente.");
            }
        }
        
        scanner.close();
    }
    
    // ==================== MÉTODOS DE MENÚ (SOLO MUESTRAN EL MENÚ) ====================
    
    /**
     * Muestra el menú principal
     */
    private static void mostrarMenuPrincipal() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│          MENÚ PRINCIPAL                │");
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│ 1. Gestión de Inventario               │");
        System.out.println("│ 2. Consultar Historial de Operaciones  │");
        System.out.println("│ 3. Gestión de Copias de Seguridad      │");
        System.out.println("│ 4. Ver Estadísticas                    │");
        System.out.println("│ 0. Salir                               │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.print("Seleccione una opción: ");
    }
    
    /**
     * Muestra el menú de gestión de inventario
     */
    private static void mostrarMenuGestionInventario() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│      GESTIÓN DE INVENTARIO             │");
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│ 1. Listar todos los productos          │");
        System.out.println("│ 2. Buscar producto                     │");
        System.out.println("│ 3. Añadir producto                     │");
        System.out.println("│ 4. Modificar producto                  │");
        System.out.println("│ 5. Eliminar producto                   │");
        System.out.println("│ 6. Productos con stock bajo            │");
        System.out.println("│ 0. Volver al menú principal            │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.print("Seleccione una opción: ");
    }
    
    /**
     * Muestra el menú de gestión de backups
     */
    private static void mostrarMenuBackup() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│   GESTIÓN DE COPIAS DE SEGURIDAD       │");
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│ 1. Crear copia de seguridad            │");
        System.out.println("│ 2. Listar copias de seguridad          │");
        System.out.println("│ 3. Restaurar copia de seguridad        │");
        System.out.println("│ 4. Información de backups              │");
        System.out.println("│ 0. Volver al menú principal            │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.print("Seleccione una opción: ");
    }
    
    /**
     * Muestra el menú de búsqueda de productos
     */
    private static void mostrarMenuBusqueda() {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.println("│      BUSCAR PRODUCTO                   │");
        System.out.println("├────────────────────────────────────────┤");
        System.out.println("│ 1. Buscar por ID                       │");
        System.out.println("│ 2. Buscar por nombre                   │");
        System.out.println("│ 3. Buscar por categoría                │");
        System.out.println("│ 0. Volver                              │");
        System.out.println("└────────────────────────────────────────┘");
        System.out.print("Seleccione una opción: ");
    }
    
    // ==================== MÉTODOS DE FUNCIONALIDAD ====================
    
    /**
     * Lista todos los productos
     */
    private static void listarProductos() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║       LISTADO DE PRODUCTOS             ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        List<Producto> productos = service.listarProductos();
        
        if (productos.isEmpty()) {
            System.out.println("[WARN] No hay productos en el inventario.");
            return;
        }
        
        System.out.printf("%-6s %-30s %-20s %12s %8s%n", 
                "ID", "NOMBRE", "CATEGORÍA", "PRECIO", "STOCK");
        System.out.println("───────────────────────────────────────────────────────────────────────────────────");
        
        for (Producto p : productos) {
            System.out.printf("%-6d %-30s %-20s %12.2f %8d%n",
                    p.getIdProducto(),
                    truncar(p.getNombre(), 30),
                    truncar(p.getCategoria(), 20),
                    p.getPrecio(),
                    p.getStock());
        }
        
        System.out.println("═══════════════════════════════════════════════════════════════════════════════════");
        System.out.println("[INFO] Total de productos: " + productos.size());
    }
    
    /**
     * Busca productos según criterio del usuario
     */
    private static void buscarProducto() {
        mostrarMenuBusqueda();
        int opcion = leerOpcion();
        
        switch (opcion) {
            case 1:
                buscarPorId();
                break;
            case 2:
                buscarPorNombre();
                break;
            case 3:
                buscarPorCategoria();
                break;
            case 0:
                // Volver
                break;
            default:
                System.out.println("\n[ERROR] Opción no válida.");
        }
    }
    
    /**
     * Busca un producto por ID
     */
    private static void buscarPorId() {
        System.out.print("\nIngrese el ID del producto: ");
        int id = leerOpcion();
        
        Optional<Producto> productoOpt = service.buscarProductoPorId(id);
        
        if (productoOpt.isPresent()) {
            Producto p = productoOpt.get();
            mostrarDetalleProducto(p);
            System.out.println("\n[INFO] Producto encontrado.");
        } else {
            System.out.println("\n[ERROR] No se encontró ningún producto con ID: " + id);
        }
    }
    
    /**
     * Busca productos por nombre
     */
    private static void buscarPorNombre() {
        System.out.print("\nIngrese el nombre (o parte del nombre): ");
        scanner.nextLine(); // Limpiar buffer
        String nombre = scanner.nextLine();
        
        List<Producto> productos = service.buscarProductosPorNombre(nombre);
        
        if (productos.isEmpty()) {
            System.out.println("\n[ERROR] No se encontraron productos que coincidan con: " + nombre);
        } else {
            System.out.println("\n[INFO] Se encontraron " + productos.size() + " producto(s):");
            mostrarListaProductos(productos);
        }
    }
    
    /**
     * Busca productos por categoría
     */
    private static void buscarPorCategoria() {
        System.out.print("\nIngrese la categoría: ");
        scanner.nextLine(); // Limpiar buffer
        String categoria = scanner.nextLine();
        
        List<Producto> productos = service.buscarProductosPorCategoria(categoria);
        
        if (productos.isEmpty()) {
            System.out.println("\n[ERROR] No se encontraron productos en la categoría: " + categoria);
        } else {
            System.out.println("\n[INFO] Se encontraron " + productos.size() + " producto(s):");
            mostrarListaProductos(productos);
        }
    }
    
    /**
     * Añade un nuevo producto
     */
    private static void anadirProducto() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      AÑADIR NUEVO PRODUCTO             ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        scanner.nextLine(); // Limpiar buffer
        
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();
        
        System.out.print("Precio: ");
        BigDecimal precio = new BigDecimal(scanner.nextLine());
        
        System.out.print("Stock: ");
        int stock = Integer.parseInt(scanner.nextLine());
        
        if (service.agregarProducto(nombre, categoria, precio, stock)) {
            System.out.println("\n[INFO] Producto añadido correctamente.");
        } else {
            System.out.println("\n[ERROR] Error al añadir el producto.");
        }
    }
    
    /**
     * Modifica un producto existente
     */
    private static void modificarProducto() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      MODIFICAR PRODUCTO                ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        System.out.print("Ingrese el ID del producto a modificar: ");
        int id = leerOpcion();
        
        Optional<Producto> productoOpt = service.buscarProductoPorId(id);
        
        if (!productoOpt.isPresent()) {
            System.out.println("\n[ERROR] No se encontró el producto con ID: " + id);
            return;
        }
        
        Producto p = productoOpt.get();
        System.out.println("\nProducto actual:");
        mostrarDetalleProducto(p);
        
        scanner.nextLine(); // Limpiar buffer
        
        System.out.println("\nIngrese los nuevos valores (Enter para no modificar):");
        
        System.out.print("Nombre [" + p.getNombre() + "]: ");
        String nombre = scanner.nextLine();
        
        System.out.print("Categoría [" + p.getCategoria() + "]: ");
        String categoria = scanner.nextLine();
        
        System.out.print("Precio [" + p.getPrecio() + "]: ");
        String precioStr = scanner.nextLine();
        BigDecimal precio = precioStr.isEmpty() ? null : new BigDecimal(precioStr);
        
        System.out.print("Stock [" + p.getStock() + "]: ");
        String stockStr = scanner.nextLine();
        Integer stock = stockStr.isEmpty() ? null : Integer.parseInt(stockStr);
        
        if (service.modificarProducto(id, 
                nombre.isEmpty() ? null : nombre,
                categoria.isEmpty() ? null : categoria,
                precio, stock)) {
            System.out.println("\n[INFO] Producto modificado correctamente.");
        } else {
            System.out.println("\n[ERROR] Error al modificar el producto.");
        }
    }
    
    /**
     * Elimina un producto
     */
    private static void eliminarProducto() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      ELIMINAR PRODUCTO                 ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        System.out.print("Ingrese el ID del producto a eliminar: ");
        int id = leerOpcion();
        
        Optional<Producto> productoOpt = service.buscarProductoPorId(id);
        
        if (!productoOpt.isPresent()) {
            System.out.println("\n[ERROR] No se encontró el producto con ID: " + id);
            return;
        }
        
        Producto p = productoOpt.get();
        mostrarDetalleProducto(p);
        
        System.out.print("\n¿Está seguro de eliminar este producto? (S/N): ");
        scanner.nextLine(); // Limpiar buffer
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("S")) {
            if (service.eliminarProducto(id)) {
                System.out.println("\n[INFO] Producto eliminado correctamente.");
            } else {
                System.out.println("\n[ERROR] Error al eliminar el producto.");
            }
        } else {
            System.out.println("\n[INFO] Operación cancelada.");
        }
    }
    
    /**
     * Muestra productos con stock bajo
     */
    private static void productosStockBajo() {
        System.out.print("\nIngrese el umbral de stock: ");
        int umbral = leerOpcion();
        
        List<Producto> productos = service.buscarProductosStockBajo(umbral);
        
        if (productos.isEmpty()) {
            System.out.println("\n[INFO] No hay productos con stock menor o igual a " + umbral);
        } else {
            System.out.println("\n[WARN] Productos con stock bajo (≤ " + umbral + "):");
            mostrarListaProductos(productos);
        }
    }
    
    /**
     * Consulta el historial de operaciones
     */
    private static void consultarHistorial() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   HISTORIAL DE OPERACIONES             ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        System.out.print("¿Cuántas líneas desea ver? (0 para todas): ");
        int numLineas = leerOpcion();
        
        String historial = service.consultarHistorial(numLineas);
        System.out.println("\n" + historial);
    }
    
    /**
     * Lista los backups disponibles
     */
    private static void listarBackups() {
        String[] backups = service.listarBackups();
        
        if (backups.length == 0) {
            System.out.println("\n[WARN] No hay copias de seguridad disponibles.");
            return;
        }
        
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   COPIAS DE SEGURIDAD DISPONIBLES      ║");
        System.out.println("╚════════════════════════════════════════╝");
        
        for (int i = 0; i < backups.length; i++) {
            System.out.printf("%2d. %s%n", i + 1, backups[i]);
        }
    }
    
    /**
     * Restaura un backup
     */
    private static void restaurarBackup() {
        String[] backups = service.listarBackups();
        
        if (backups.length == 0) {
            System.out.println("\n[ERROR] No hay copias de seguridad disponibles para restaurar.");
            return;
        }
        
        listarBackups();
        
        System.out.print("\nIngrese el número del backup a restaurar (0 para cancelar): ");
        int opcion = leerOpcion();
        
        if (opcion < 1 || opcion > backups.length) {
            System.out.println("Operación cancelada.");
            return;
        }
        
        System.out.print("[WARN] ADVERTENCIA: Se reemplazará el inventario actual. ¿Continuar? (S/N): ");
        scanner.nextLine(); // Limpiar buffer
        String confirmacion = scanner.nextLine();
        
        if (confirmacion.equalsIgnoreCase("S")) {
            service.restaurarBackup(backups[opcion - 1]);
        } else {
            System.out.println("[INFO] Operación cancelada.");
        }
    }
    
    /**
     * Muestra estadísticas del inventario
     */
    private static void mostrarEstadisticas() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      ESTADÍSTICAS DEL INVENTARIO       ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\n" + service.obtenerEstadisticas());
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║      INFORMACIÓN DE BACKUPS            ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("\n" + service.obtenerInfoBackups());
    }
    
    // ==================== MÉTODOS AUXILIARES ====================
    
    /**
     * Lee una opción numérica del usuario
     */
    private static int leerOpcion() {
        try {
            return scanner.nextInt();
        } catch (Exception e) {
            scanner.nextLine(); // Limpiar buffer
            return -1;
        }
    }
    
    /**
     * Muestra el detalle de un producto
     */
    private static void mostrarDetalleProducto(Producto p) {
        System.out.println("\n┌────────────────────────────────────────┐");
        System.out.printf("│ ID:        %-28d│%n", p.getIdProducto());
        System.out.printf("│ Nombre:    %-28s│%n", truncar(p.getNombre(), 28));
        System.out.printf("│ Categoría: %-28s│%n", truncar(p.getCategoria(), 28));
        System.out.printf("│ Precio:    %-28.2f│%n", p.getPrecio());
        System.out.printf("│ Stock:     %-28d│%n", p.getStock());
        System.out.println("└────────────────────────────────────────┘");
    }
    
    /**
     * Muestra una lista de productos en formato tabla
     */
    private static void mostrarListaProductos(List<Producto> productos) {
        System.out.printf("%-6s %-30s %-20s %12s %8s%n", 
                "ID", "NOMBRE", "CATEGORÍA", "PRECIO", "STOCK");
        System.out.println("───────────────────────────────────────────────────────────────────────────────────");
        
        for (Producto p : productos) {
            System.out.printf("%-6d %-30s %-20s %12.2f %8d%n",
                    p.getIdProducto(),
                    truncar(p.getNombre(), 30),
                    truncar(p.getCategoria(), 20),
                    p.getPrecio(),
                    p.getStock());
        }
    }
    
    /**
     * Trunca un texto a una longitud máxima
     */
    private static String truncar(String texto, int maxLength) {
        if (texto.length() <= maxLength) {
            return texto;
        }
        return texto.substring(0, maxLength - 3) + "...";
    }
}