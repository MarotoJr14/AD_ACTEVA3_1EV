package com.empresa.inventario.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utilidad para registrar todas las operaciones realizadas en el sistema.
 * Mantiene un archivo de registro (registro.txt) con fecha y hora de cada operación.
 */
public class RegistroUtil {
    
    private static final String ARCHIVO_REGISTRO = "data/registro.txt";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    /**
     * Tipos de operaciones que se pueden registrar
     */
    public enum TipoOperacion {
        ALTA("ALTA"),
        BAJA("BAJA"),
        MODIFICACION("MODIFICACIÓN"),
        BUSQUEDA("BÚSQUEDA"),
        LISTAR("LISTAR"),
        BACKUP("BACKUP"),
        CONSULTA_HISTORIAL("CONSULTA HISTORIAL"),
        INICIALIZACION("INICIALIZACIÓN");
        
        private final String descripcion;
        
        TipoOperacion(String descripcion) {
            this.descripcion = descripcion;
        }
        
        public String getDescripcion() {
            return descripcion;
        }
    }
    
    /**
     * Registra una operación en el archivo de registro.
     * @param tipo Tipo de operación realizada
     * @param detalle Detalles adicionales de la operación
     */
    public static void registrar(TipoOperacion tipo, String detalle) {
        String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
        String entrada = String.format("[%s] %s: %s", timestamp, tipo.getDescripcion(), detalle);
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO_REGISTRO, true))) {
            writer.write(entrada);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error al escribir en el registro: " + e.getMessage());
        }
    }
    
    /**
     * Registra una operación de alta de producto
     * @param idProducto ID del producto dado de alta
     * @param nombre Nombre del producto
     */
    public static void registrarAlta(int idProducto, String nombre) {
        registrar(TipoOperacion.ALTA, String.format("Producto ID=%d, Nombre='%s'", idProducto, nombre));
    }
    
    /**
     * Registra una operación de baja de producto
     * @param idProducto ID del producto dado de baja
     * @param nombre Nombre del producto
     */
    public static void registrarBaja(int idProducto, String nombre) {
        registrar(TipoOperacion.BAJA, String.format("Producto ID=%d, Nombre='%s'", idProducto, nombre));
    }
    
    /**
     * Registra una operación de modificación de producto
     * @param idProducto ID del producto modificado
     * @param camposModificados Descripción de los campos modificados
     */
    public static void registrarModificacion(int idProducto, String camposModificados) {
        registrar(TipoOperacion.MODIFICACION, 
                String.format("Producto ID=%d - Cambios: %s", idProducto, camposModificados));
    }
    
    /**
     * Registra una operación de búsqueda
     * @param criterio Criterio de búsqueda utilizado
     * @param resultados Número de resultados encontrados
     */
    public static void registrarBusqueda(String criterio, int resultados) {
        registrar(TipoOperacion.BUSQUEDA, 
                String.format("Criterio='%s', Resultados=%d", criterio, resultados));
    }
    
    /**
     * Registra una operación de listado de productos
     * @param totalProductos Número total de productos listados
     */
    public static void registrarListado(int totalProductos) {
        registrar(TipoOperacion.LISTAR, 
                String.format("Total de productos listados: %d", totalProductos));
    }
    
    /**
     * Registra una operación de backup
     * @param nombreArchivo Nombre del archivo de backup generado
     */
    public static void registrarBackup(String nombreArchivo) {
        registrar(TipoOperacion.BACKUP, 
                String.format("Backup creado: %s", nombreArchivo));
    }
    
    /**
     * Registra una consulta al historial de operaciones
     */
    public static void registrarConsultaHistorial() {
        registrar(TipoOperacion.CONSULTA_HISTORIAL, "Consulta del registro de operaciones");
    }
    
    /**
     * Registra el inicio del sistema
     */
    public static void registrarInicioSistema() {
        registrar(TipoOperacion.INICIALIZACION, "Sistema de gestión de inventario iniciado");
    }
}
