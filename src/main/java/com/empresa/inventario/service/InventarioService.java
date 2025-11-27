package com.empresa.inventario.service;

import com.empresa.inventario.dao.InventarioDAO;
import com.empresa.inventario.model.Producto;
import com.empresa.inventario.util.BackupUtil;
import com.empresa.inventario.util.RegistroUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Servicio que encapsula la lógica de negocio para la gestión del inventario.
 * Actúa como intermediario entre la capa de presentación y la capa de acceso a datos.
 */
public class InventarioService {
    
    private final InventarioDAO inventarioDAO;
    
    /**
     * Constructor del servicio
     */
    public InventarioService() {
        this.inventarioDAO = new InventarioDAO();
    }
    
    // ==================== OPERACIONES CRUD ====================
    
    /**
     * Lista todos los productos del inventario
     * @return Lista de productos
     */
    public List<Producto> listarProductos() {
        return inventarioDAO.listarTodos();
    }
    
    /**
     * Busca un producto por su ID
     * @param id ID del producto
     * @return Optional con el producto si existe
     */
    public Optional<Producto> buscarProductoPorId(int id) {
        return inventarioDAO.buscarPorId(id);
    }
    
    /**
     * Busca productos por nombre (búsqueda parcial)
     * @param nombre Nombre o parte del nombre
     * @return Lista de productos que coinciden
     */
    public List<Producto> buscarProductosPorNombre(String nombre) {
        return inventarioDAO.buscarPorNombre(nombre);
    }
    
    /**
     * Busca productos por categoría
     * @param categoria Categoría a buscar
     * @return Lista de productos de la categoría
     */
    public List<Producto> buscarProductosPorCategoria(String categoria) {
        return inventarioDAO.buscarPorCategoria(categoria);
    }
    
    /**
     * Busca productos con stock bajo
     * @param umbral Umbral de stock
     * @return Lista de productos con stock bajo
     */
    public List<Producto> buscarProductosStockBajo(int umbral) {
        return inventarioDAO.buscarStockBajo(umbral);
    }
    
    /**
     * Añade un nuevo producto al inventario
     * @param nombre Nombre del producto
     * @param categoria Categoría del producto
     * @param precio Precio del producto
     * @param stock Stock inicial
     * @return true si se añadió correctamente
     */
    public boolean agregarProducto(String nombre, String categoria, BigDecimal precio, int stock) {
        // Validaciones
        if (nombre == null || nombre.trim().isEmpty()) {
            System.err.println("El nombre del producto no puede estar vacío");
            return false;
        }
        if (categoria == null || categoria.trim().isEmpty()) {
            System.err.println("La categoría no puede estar vacía");
            return false;
        }
        if (precio == null || precio.compareTo(BigDecimal.ZERO) < 0) {
            System.err.println("El precio debe ser un valor positivo");
            return false;
        }
        if (stock < 0) {
            System.err.println("El stock no puede ser negativo");
            return false;
        }
        
        int nuevoId = inventarioDAO.generarNuevoId();
        Producto producto = new Producto(nuevoId, nombre, categoria, precio, stock);
        
        return inventarioDAO.crear(producto);
    }
    
    /**
     * Modifica un producto existente
     * @param id ID del producto a modificar
     * @param nombre Nuevo nombre (null para no cambiar)
     * @param categoria Nueva categoría (null para no cambiar)
     * @param precio Nuevo precio (null para no cambiar)
     * @param stock Nuevo stock (null para no cambiar)
     * @return true si se modificó correctamente
     */
    public boolean modificarProducto(int id, String nombre, String categoria, 
                                    BigDecimal precio, Integer stock) {
        Optional<Producto> productoOpt = inventarioDAO.buscarPorId(id);
        
        if (!productoOpt.isPresent()) {
            System.err.println("No se encontró el producto con ID: " + id);
            return false;
        }
        
        Producto producto = productoOpt.get();
        
        // Actualizar solo los campos que no sean null
        if (nombre != null && !nombre.trim().isEmpty()) {
            producto.setNombre(nombre);
        }
        if (categoria != null && !categoria.trim().isEmpty()) {
            producto.setCategoria(categoria);
        }
        if (precio != null && precio.compareTo(BigDecimal.ZERO) >= 0) {
            producto.setPrecio(precio);
        }
        if (stock != null && stock >= 0) {
            producto.setStock(stock);
        }
        
        return inventarioDAO.actualizar(producto);
    }
    
    /**
     * Elimina un producto del inventario
     * @param id ID del producto a eliminar
     * @return true si se eliminó correctamente
     */
    public boolean eliminarProducto(int id) {
        return inventarioDAO.eliminar(id);
    }
    
    // ==================== OPERACIONES DE BACKUP ====================
    
    /**
     * Crea una copia de seguridad del inventario
     * @return Nombre del archivo de backup creado, o null si hubo error
     */
    public String crearBackup() {
        try {
            String nombreBackup = BackupUtil.crearBackup();
            System.out.println("[INFO] Copia de seguridad creada exitosamente: " + nombreBackup);
            return nombreBackup;
        } catch (IOException e) {
            System.err.println("[ERROR] al crear la copia de seguridad: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Lista todos los backups disponibles
     * @return Array con los nombres de los backups
     */
    public String[] listarBackups() {
        try {
            return BackupUtil.listarBackups();
        } catch (IOException e) {
            System.err.println("Error al listar los backups: " + e.getMessage());
            return new String[0];
        }
    }
    
    /**
     * Restaura un backup específico
     * @param nombreBackup Nombre del backup a restaurar
     * @return true si se restauró correctamente
     */
    public boolean restaurarBackup(String nombreBackup) {
        try {
            BackupUtil.restaurarBackup(nombreBackup);
            System.out.println("✓ Backup restaurado exitosamente: " + nombreBackup);
            return true;
        } catch (IOException e) {
            System.err.println("Error al restaurar el backup: " + e.getMessage());
            return false;
        }
    }
    
    // ==================== OPERACIONES DE HISTORIAL ====================
    
    /**
     * Consulta el historial de operaciones
     * @param numLineas Número de últimas líneas a mostrar (0 para todas)
     * @return Contenido del registro
     */
    public String consultarHistorial(int numLineas) {
        StringBuilder historial = new StringBuilder();
        String archivoRegistro = "data/registro.txt";
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivoRegistro))) {
            List<String> lineas = reader.lines().collect(java.util.stream.Collectors.toList());
            
            int inicio = (numLineas > 0 && numLineas < lineas.size()) 
                    ? lineas.size() - numLineas 
                    : 0;
            
            for (int i = inicio; i < lineas.size(); i++) {
                historial.append(lineas.get(i)).append("\n");
            }
            
            RegistroUtil.registrarConsultaHistorial();
            
        } catch (IOException e) {
            return "Error al leer el archivo de registro: " + e.getMessage();
        }
        
        return historial.length() > 0 ? historial.toString() : "No hay registros disponibles";
    }
    
    // ==================== OPERACIONES ESTADÍSTICAS ====================
    
    /**
     * Obtiene estadísticas del inventario
     * @return String con información estadística
     */
    public String obtenerEstadisticas() {
        return inventarioDAO.obtenerEstadisticas();
    }
    
    /**
     * Obtiene información sobre el espacio ocupado por los backups
     * @return String con información sobre backups
     */
    public String obtenerInfoBackups() {
        try {
            String[] backups = BackupUtil.listarBackups();
            long espacio = BackupUtil.obtenerEspacioBackups();
            
            return String.format(
                    "Número de backups: %d%n" +
                    "Espacio ocupado: %.2f KB",
                    backups.length, espacio / 1024.0
            );
        } catch (IOException e) {
            return "Error al obtener información de backups: " + e.getMessage();
        }
    }
}