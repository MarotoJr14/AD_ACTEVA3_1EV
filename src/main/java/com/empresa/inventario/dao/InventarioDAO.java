package com.empresa.inventario.dao;

import com.empresa.inventario.model.Producto;
import com.empresa.inventario.util.RegistroUtil;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Data Access Object para la gestión del inventario en archivo de texto.
 * Implementa operaciones CRUD sobre el archivo inventario.txt
 */
public class InventarioDAO {
    
    private static final String ARCHIVO_INVENTARIO = "data/inventario.txt";
    private static final String CABECERA_CSV = "id_producto;nombre;categoria;precio;stock";
    
    /**
     * Constructor que inicializa el archivo de inventario si no existe
     */
    public InventarioDAO() {
        inicializarArchivo();
    }
    
    /**
     * Inicializa el archivo de inventario con la cabecera si no existe
     */
    private void inicializarArchivo() {
        Path ruta = Paths.get(ARCHIVO_INVENTARIO);
        
        try {
            // Crear directorio data si no existe
            Files.createDirectories(ruta.getParent());
            
            // Crear archivo con cabecera si no existe
            if (!Files.exists(ruta)) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_INVENTARIO))) {
                    writer.write(CABECERA_CSV);
                    writer.newLine();
                }
                RegistroUtil.registrar(RegistroUtil.TipoOperacion.INICIALIZACION, 
                        "Archivo de inventario creado");
            }
        } catch (IOException e) {
            System.err.println("Error al inicializar archivo de inventario: " + e.getMessage());
        }
    }
    
    /**
     * Lee todos los productos del archivo de inventario
     * @return Lista de productos
     */
    public List<Producto> listarTodos() {
        List<Producto> productos = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO_INVENTARIO))) {
            // Saltar la cabecera
            reader.readLine();
            
            String linea;
            while ((linea = reader.readLine()) != null) {
                try {
                    if (!linea.trim().isEmpty()) {
                        Producto producto = Producto.fromCSV(linea);
                        productos.add(producto);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Error al parsear línea: " + linea);
                }
            }
            
            RegistroUtil.registrarListado(productos.size());
            
        } catch (IOException e) {
            System.err.println("Error al leer el archivo de inventario: " + e.getMessage());
        }
        
        return productos;
    }
    
    /**
     * Busca un producto por su ID
     * @param id ID del producto a buscar
     * @return Optional con el producto si se encuentra, Optional.empty() si no
     */
    public Optional<Producto> buscarPorId(int id) {
        Optional<Producto> resultado = listarTodos().stream()
                .filter(p -> p.getIdProducto() == id)
                .findFirst();
        
        RegistroUtil.registrarBusqueda("ID=" + id, resultado.isPresent() ? 1 : 0);
        return resultado;
    }
    
    /**
     * Busca productos por nombre (búsqueda parcial, case-insensitive)
     * @param nombre Nombre o parte del nombre a buscar
     * @return Lista de productos que coinciden con el criterio
     */
    public List<Producto> buscarPorNombre(String nombre) {
        List<Producto> resultados = listarTodos().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(Collectors.toList());
        
        RegistroUtil.registrarBusqueda("Nombre contiene '" + nombre + "'", resultados.size());
        return resultados;
    }
    
    /**
     * Busca productos por categoría
     * @param categoria Categoría a buscar
     * @return Lista de productos de la categoría especificada
     */
    public List<Producto> buscarPorCategoria(String categoria) {
        List<Producto> resultados = listarTodos().stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
        
        RegistroUtil.registrarBusqueda("Categoría=" + categoria, resultados.size());
        return resultados;
    }
    
    /**
     * Busca productos con stock por debajo de un umbral
     * @param umbral Umbral de stock
     * @return Lista de productos con stock menor o igual al umbral
     */
    public List<Producto> buscarStockBajo(int umbral) {
        List<Producto> resultados = listarTodos().stream()
                .filter(p -> p.getStock() <= umbral)
                .collect(Collectors.toList());
        
        RegistroUtil.registrarBusqueda("Stock <= " + umbral, resultados.size());
        return resultados;
    }
    
    /**
     * Añade un nuevo producto al inventario
     * @param producto Producto a añadir
     * @return true si se añadió correctamente, false si ya existe un producto con ese ID
     */
    public boolean crear(Producto producto) {
        // Verificar que no existe un producto con ese ID
        if (buscarPorId(producto.getIdProducto()).isPresent()) {
            System.err.println("Ya existe un producto con ID: " + producto.getIdProducto());
            return false;
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_INVENTARIO, true))) {
            writer.write(producto.toCSV());
            writer.newLine();
            
            RegistroUtil.registrarAlta(producto.getIdProducto(), producto.getNombre());
            return true;
            
        } catch (IOException e) {
            System.err.println("Error al añadir producto: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Actualiza un producto existente
     * @param producto Producto con los datos actualizados (el ID debe coincidir)
     * @return true si se actualizó correctamente, false si no se encontró el producto
     */
    public boolean actualizar(Producto producto) {
        List<Producto> productos = listarTodos();
        boolean encontrado = false;
        StringBuilder cambios = new StringBuilder();
        
        // Buscar y actualizar el producto
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getIdProducto() == producto.getIdProducto()) {
                Producto anterior = productos.get(i);
                
                // Registrar cambios
                if (!anterior.getNombre().equals(producto.getNombre())) {
                    cambios.append(String.format("Nombre: '%s' -> '%s'; ", 
                            anterior.getNombre(), producto.getNombre()));
                }
                if (!anterior.getCategoria().equals(producto.getCategoria())) {
                    cambios.append(String.format("Categoría: '%s' -> '%s'; ", 
                            anterior.getCategoria(), producto.getCategoria()));
                }
                if (!anterior.getPrecio().equals(producto.getPrecio())) {
                    cambios.append(String.format("Precio: %.2f -> %.2f; ", 
                            anterior.getPrecio(), producto.getPrecio()));
                }
                if (anterior.getStock() != producto.getStock()) {
                    cambios.append(String.format("Stock: %d -> %d; ", 
                            anterior.getStock(), producto.getStock()));
                }
                
                productos.set(i, producto);
                encontrado = true;
                break;
            }
        }
        
        if (!encontrado) {
            return false;
        }
        
        // Reescribir el archivo completo
        if (reescribirArchivo(productos)) {
            RegistroUtil.registrarModificacion(producto.getIdProducto(), 
                    cambios.length() > 0 ? cambios.toString() : "Sin cambios");
            return true;
        }
        
        return false;
    }
    
    /**
     * Elimina un producto del inventario por su ID
     * @param id ID del producto a eliminar
     * @return true si se eliminó correctamente, false si no se encontró el producto
     */
    public boolean eliminar(int id) {
        List<Producto> productos = listarTodos();
        Optional<Producto> productoEliminado = productos.stream()
                .filter(p -> p.getIdProducto() == id)
                .findFirst();
        
        if (!productoEliminado.isPresent()) {
            return false;
        }
        
        // Eliminar el producto de la lista
        productos.removeIf(p -> p.getIdProducto() == id);
        
        // Reescribir el archivo
        if (reescribirArchivo(productos)) {
            RegistroUtil.registrarBaja(id, productoEliminado.get().getNombre());
            return true;
        }
        
        return false;
    }
    
    /**
     * Genera un nuevo ID único para un producto
     * @return Nuevo ID (máximo ID actual + 1)
     */
    public int generarNuevoId() {
        return listarTodos().stream()
                .mapToInt(Producto::getIdProducto)
                .max()
                .orElse(0) + 1;
    }
    
    /**
     * Reescribe completamente el archivo de inventario con la lista de productos proporcionada
     * @param productos Lista de productos a escribir
     * @return true si se escribió correctamente, false en caso contrario
     */
    private boolean reescribirArchivo(List<Producto> productos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_INVENTARIO))) {
            // Escribir cabecera
            writer.write(CABECERA_CSV);
            writer.newLine();
            
            // Escribir productos
            for (Producto p : productos) {
                writer.write(p.toCSV());
                writer.newLine();
            }
            
            return true;
            
        } catch (IOException e) {
            System.err.println("Error al reescribir el archivo de inventario: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene estadísticas del inventario
     * @return String con información estadística
     */
    public String obtenerEstadisticas() {
        List<Producto> productos = listarTodos();
        
        int totalProductos = productos.size();
        int stockTotal = productos.stream().mapToInt(Producto::getStock).sum();
        BigDecimal valorTotal = productos.stream()
                .map(p -> p.getPrecio().multiply(new BigDecimal(p.getStock())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        long categorias = productos.stream()
                .map(Producto::getCategoria)
                .distinct()
                .count();
        
        return String.format(
                "Total de productos: %d%n" +
                "Stock total: %d unidades%n" +
                "Valor total del inventario: %.2f EUR%n" +
                "Número de categorías: %d",
                totalProductos, stockTotal, valorTotal, categorias
        );
    }
}