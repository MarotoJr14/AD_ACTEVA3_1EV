package com.empresa.inventario.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utilidad para crear y gestionar copias de seguridad del inventario.
 * Genera archivos de backup con nombre único basado en la fecha.
 */
public class BackupUtil {
    
    private static final String DIRECTORIO_BACKUPS = "data/backups";
    private static final String ARCHIVO_INVENTARIO = "data/inventario.txt";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    /**
     * Crea una copia de seguridad del archivo de inventario.
     * El archivo de backup se guarda con el formato: inventario_backup_YYYY-MM-DD.txt
     * 
     * @return Nombre del archivo de backup creado
     * @throws IOException si ocurre un error al crear el backup
     */
    public static String crearBackup() throws IOException {
        // Crear directorio de backups si no existe
        crearDirectorioBackups();
        
        // Generar nombre único para el backup
        String nombreBackup = generarNombreBackup();
        Path rutaOrigen = Paths.get(ARCHIVO_INVENTARIO);
        Path rutaDestino = Paths.get(DIRECTORIO_BACKUPS, nombreBackup);
        
        // Verificar que existe el archivo de inventario
        if (!Files.exists(rutaOrigen)) {
            throw new IOException("El archivo de inventario no existe: " + ARCHIVO_INVENTARIO);
        }
        
        // Copiar el archivo
        Files.copy(rutaOrigen, rutaDestino, StandardCopyOption.REPLACE_EXISTING);
        
        // Registrar la operación
        RegistroUtil.registrarBackup(nombreBackup);
        
        return nombreBackup;
    }
    
    /**
     * Genera un nombre único para el archivo de backup basado en la fecha actual.
     * Si ya existe un backup del mismo día, añade un contador.
     * 
     * @return Nombre del archivo de backup
     */
    private static String generarNombreBackup() {
        String fechaActual = LocalDate.now().format(FORMATO_FECHA);
        String nombreBase = "inventario_backup_" + fechaActual;
        String extension = ".txt";
        
        Path directorio = Paths.get(DIRECTORIO_BACKUPS);
        String nombreFinal = nombreBase + extension;
        Path rutaCompleta = directorio.resolve(nombreFinal);
        
        // Si ya existe el archivo, añadir un contador
        int contador = 1;
        while (Files.exists(rutaCompleta)) {
            nombreFinal = nombreBase + "_" + contador + extension;
            rutaCompleta = directorio.resolve(nombreFinal);
            contador++;
        }
        
        return nombreFinal;
    }
    
    /**
     * Crea el directorio de backups si no existe
     */
    private static void crearDirectorioBackups() throws IOException {
        Path directorio = Paths.get(DIRECTORIO_BACKUPS);
        if (!Files.exists(directorio)) {
            Files.createDirectories(directorio);
        }
    }
    
    /**
     * Lista todos los backups existentes en el directorio de backups
     * 
     * @return Array con los nombres de los archivos de backup
     * @throws IOException si ocurre un error al leer el directorio
     */
    public static String[] listarBackups() throws IOException {
        Path directorio = Paths.get(DIRECTORIO_BACKUPS);
        
        if (!Files.exists(directorio)) {
            return new String[0];
        }
        
        return Files.list(directorio)
                .filter(Files::isRegularFile)
                .filter(p -> p.getFileName().toString().startsWith("inventario_backup_"))
                .map(p -> p.getFileName().toString())
                .sorted()
                .toArray(String[]::new);
    }
    
    /**
     * Restaura un backup específico al archivo principal de inventario
     * 
     * @param nombreBackup Nombre del archivo de backup a restaurar
     * @throws IOException si ocurre un error durante la restauración
     */
    public static void restaurarBackup(String nombreBackup) throws IOException {
        Path rutaBackup = Paths.get(DIRECTORIO_BACKUPS, nombreBackup);
        Path rutaInventario = Paths.get(ARCHIVO_INVENTARIO);
        
        if (!Files.exists(rutaBackup)) {
            throw new IOException("El backup especificado no existe: " + nombreBackup);
        }
        
        // Crear un backup del archivo actual antes de restaurar
        try {
            crearBackup();
        } catch (IOException e) {
            System.err.println("Advertencia: No se pudo crear backup de seguridad antes de restaurar");
        }
        
        // Restaurar el backup
        Files.copy(rutaBackup, rutaInventario, StandardCopyOption.REPLACE_EXISTING);
        
        RegistroUtil.registrar(RegistroUtil.TipoOperacion.BACKUP, 
                "Restaurado backup: " + nombreBackup);
    }
    
    /**
     * Obtiene información sobre el espacio ocupado por los backups
     * 
     * @return Tamaño total en bytes de todos los backups
     * @throws IOException si ocurre un error al calcular el tamaño
     */
    public static long obtenerEspacioBackups() throws IOException {
        Path directorio = Paths.get(DIRECTORIO_BACKUPS);
        
        if (!Files.exists(directorio)) {
            return 0;
        }
        
        return Files.walk(directorio)
                .filter(Files::isRegularFile)
                .filter(p -> p.getFileName().toString().startsWith("inventario_backup_"))
                .mapToLong(p -> {
                    try {
                        return Files.size(p);
                    } catch (IOException e) {
                        return 0;
                    }
                })
                .sum();
    }
}