package com.empresa.inventario.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Clase que representa un producto del inventario.
 * Implementa Serializable para facilitar la persistencia en ficheros.
 */
public class Producto implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int idProducto;
    private String nombre;
    private String categoria;
    private BigDecimal precio;
    private int stock;

    /**
     * Constructor por defecto
     */
    public Producto() {
    }

    /**
     * Constructor con todos los parámetros
     * @param idProducto Identificador único del producto
     * @param nombre Nombre del producto
     * @param categoria Categoría a la que pertenece
     * @param precio Precio del producto
     * @param stock Cantidad en stock
     */
    public Producto(int idProducto, String nombre, String categoria, BigDecimal precio, int stock) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.categoria = categoria;
        this.precio = precio;
        this.stock = stock;
    }

    // Getters y Setters
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Convierte el producto a formato CSV con separador punto y coma
     * @return String en formato: id;nombre;categoria;precio;stock
     */
    public String toCSV() {
        return idProducto + ";" + nombre + ";" + categoria + ";" + precio + ";" + stock;
    }

    /**
     * Crea un producto desde una línea CSV
     * @param linea Línea CSV con formato: id;nombre;categoria;precio;stock
     * @return Producto creado a partir de la línea
     * @throws IllegalArgumentException si el formato es inválido
     */
    public static Producto fromCSV(String linea) {
        if (linea == null || linea.trim().isEmpty()) {
            throw new IllegalArgumentException("Línea CSV vacía o nula");
        }
        
        String[] partes = linea.split(";");
        if (partes.length != 5) {
            throw new IllegalArgumentException("Formato CSV inválido. Se esperan 5 campos.");
        }
        
        try {
            int id = Integer.parseInt(partes[0].trim());
            String nombre = partes[1].trim();
            String categoria = partes[2].trim();
            BigDecimal precio = new BigDecimal(partes[3].trim());
            int stock = Integer.parseInt(partes[4].trim());
            
            return new Producto(id, nombre, categoria, precio, stock);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Error al parsear números en la línea CSV: " + linea, e);
        }
    }

    @Override
    public String toString() {
        return String.format("Producto[ID=%d, Nombre='%s', Categoría='%s', Precio=%.2f, Stock=%d]",
                idProducto, nombre, categoria, precio, stock);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return idProducto == producto.idProducto;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idProducto);
    }
}