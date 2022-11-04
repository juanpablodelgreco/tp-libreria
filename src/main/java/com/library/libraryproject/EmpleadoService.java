/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.library.libraryproject;

import static com.library.libraryproject.Biblioteca.ruta;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.Vector;

/**
 *
 * @author Juan Pablo
 */
public class EmpleadoService {

    public static Vector<Empleado> empleadosVector;
    public static String ruta = "empleados.tsv";

    public static Vector<Empleado> getEmpleadosFromFile() {
        // agregada 2.
        // Trae los libros de "libros.tsv"

        String[] campos;
        Empleado empleado;
        Vector<Empleado> vector = new Vector<Empleado>();

        try {
            Scanner entrada = new Scanner(new FileReader(ruta));
            while (entrada.hasNextLine()) {
                campos = entrada.nextLine().split("\t");
                empleado = new Empleado(campos[1], campos[2]);
                empleado.setId(campos[0]);
                vector.add(empleado);
            }
            entrada.close();

        } catch (FileNotFoundException e) {
        }
        return vector;
    }

    public static void printEnArchivo(Vector<Empleado> vector) {
        // agregada 3
        // guarda en archivo de ruta seteada

        int i, n;

        Funcion<Empleado> imprimirEnArchivo = new Funcion<Empleado>() {
            @Override
            public void funcion(Empleado empleado, Object parametros) {
                PrintStream archivo = (PrintStream) parametros;
                archivo.print(empleado.getId() + "\t");
                archivo.print(empleado.getUserName() + "\t");
                archivo.print(empleado.getPassword() + "\n");
            }
        };
        try {
            PrintStream salida = new PrintStream(ruta);
            n = vector.size();
            for (i = 0; i < n; i++) {
                imprimirEnArchivo.funcion(vector.get(i), salida);
            }
            salida.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadEmpleados() {
        EmpleadoService.empleadosVector = EmpleadoService.getEmpleadosFromFile();
    }

    public static Empleado searchEmpleado(String userName) {
        int pos = 0;

        for (Empleado empleado : empleadosVector) {
            if (userName.equalsIgnoreCase(empleado.getUserName())) {
                return empleadosVector.get(pos);
            }
            pos++;
        }

        return null;
    }
}
