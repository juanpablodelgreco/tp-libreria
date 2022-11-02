package com.library.libraryproject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Scanner;

import java.util.Vector;

public class Biblioteca {
    //para leer de teclado

    public static Scanner teclado = new Scanner(System.in);
    //para imprimir
    public static PrintStream out = System.out;

    //para poder ver los prints
    public static void pausar(String mensage) {
        out.print(mensage + "\nPresione <ENTER> para continuar . . . ");
        teclado.nextLine();
        out.println();
    }

    //lee un string
    public static String leer_cadena(String mensaje) {
        out.print(mensaje + ": ");
        return teclado.nextLine();
    }

    //su nombre es elocuente
    public static int leer_entero(String mensaje) {
        try {
            return Integer.parseInt(leer_cadena(mensaje));
        } catch (NumberFormatException e) {
            out.print("N\u00FAmero incorrecto.");
            return leer_entero(mensaje);
        }
    }

    //ruta del archivo generado
    public static String ruta = "libros.tsv";

    public static void validarSystemProperties() {
        // agregada 1
        // valida si es Linux
        // para configurar el printstream y el scanner

        if (!System.getProperties().get("os.name").equals("Linux") && System.console() != null)
            try {
            out = new PrintStream(System.out, true, "CP850");
            teclado = new Scanner(System.in, "CP850");
        } catch (UnsupportedEncodingException e) {
        }
    }

    public static Vector<Libro> getLibrosFromFile() {
        // agregada 2.
        // Trae los libros de "libros.tsv"

        String[] campos;
        Libro libro;
        Vector<Libro> vector = new Vector<Libro>();

        try {
            Scanner entrada = new Scanner(new FileReader(ruta));
            while (entrada.hasNextLine()) {
                campos = entrada.nextLine().split("\t");
                libro = new Libro();
                libro.setISBN(campos[0]);
                libro.setTitulo(campos[1]);
                libro.setAutor(campos[2]);
                libro.setEditorial(campos[3]);
                libro.setEdicion(Integer.parseInt(campos[4]));
                libro.setAnno_de_publicacion(Integer.parseInt(campos[5]));
                vector.add(libro);
            }
            entrada.close();

        } catch (FileNotFoundException e) {
        }
        return vector;
    }

    public static void printEnArchivo(Vector<Libro> vector) {
        // agregada 3
        // guarda en archivo de ruta seteada

        int i, n;

        Funcion<Libro> imprimirEnArchivo = new Funcion<Libro>() {
            @Override
            public void funcion(Libro libro, Object parametros) {
                PrintStream archivo = (PrintStream) parametros;
                archivo.print(libro.getISBN() + "\t");
                archivo.print(libro.getTitulo() + "\t");
                archivo.print(libro.getAutor() + "\t");
                archivo.print(libro.getEditorial() + "\t");
                archivo.print(libro.getEdicion() + "\t");
                archivo.print(libro.getAnno_de_publicacion() + "\n");
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

    public static void imprimirOpciones() {
        // agregada 4
        //despliega el menú principal
        out.println("MEN\u00DA");
        out.println("1.- Altas");
        out.println("2.- Consultas");
        out.println("3.- Actualizaciones");
        out.println("4.- Bajas");
        out.println("5.- Ordenar registros");
        out.println("6.- Listar registros");
        out.println("7.- Salir");
    }

    public static int getOpcionValida() {
        // agregada 5
        //se separó para lograr mayor y mejor modularidad
        //ya que tiene varias condiciones para hacer algo simple
        int opcion;

        do {
            opcion = leer_entero("Seleccione una opci\u00F3n");
            if (opcion < 1 || opcion > 7) {
                out.println("Opci\u00F3nn no v\u00E1lida.");
            }
        } while (opcion < 1 || opcion > 7);
        return opcion;
    }

    public static boolean hayRegistros(Vector<Libro> vector, int opcion) {

        // agregada 6
        //cheque si hay registros en el archivo
        if (vector.isEmpty() && opcion != 1 && opcion != 7) {
            pausar("No hay registros.\n");
            return false;
        }
        return true;
    }

    public static void procesarOpcion(int opcion, Vector<Libro> vector) {
        // agregada 7
        //procesa la opcion elegida
        Funcion<Libro> imprimir = new Funcion<Libro>() {
            @Override
            public void funcion(Libro libro, Object parametros) {
                out.println(libro);
                int[] contador = (int[]) parametros;
                contador[0]++;
            }
        };

        int i, n;
        Libro dato = null, libro;
        int[] contador = {0};
        libro = new Libro();

        // cargar isbn
        if (opcion < 5) {

            libro.setISBN(leer_cadena("Ingrese el ISBN del libro"));
            i = vector.indexOf(libro);
            dato = i < 0 ? null : vector.get(i);
            if (dato != null) {
                out.println();
                imprimir.funcion(dato, contador);
            }
        }

        if (esOpcionValida(opcion, dato)) {
            switch (opcion) {
                case 1: // dar de ALTA
                    libro = darDeAltaLibro(libro.getISBN());
                    vector.add(libro);
                    break;
                case 3: // ACTUALIZAR
                    i = vector.indexOf(libro);
                    dato = actualizarLibro(vector.get(i));
                    vector.set(vector.indexOf(libro), dato);

                    break;
                case 4: //BAJA
                    vector.remove(dato);
                    out.println("Registro borrado correctamente.");
                    break;
                case 5: //ORDENAR
                    Collections.sort(vector);
                    out.println("Registros ordenados correctamente.");
                    break;
                case 6: //IMPRIMIR
                    n = vector.size();
                    contador[0] = 0;
                    for (i = 0; i < n; i++) {
                        imprimir.funcion(vector.get(i), contador);
                    }
                    out.println("Total de registros: " + contador[0] + ".");
                    break;

            }
        }

        if (opcion < 7 && opcion >= 1) {
            pausar("");
        }
    }

    public static boolean esOpcionValida(int opcion, Libro dato) {
        // agregada 8

        // si es dar de ALTA y ya existe isbn
        if (opcion == 1 && dato != null) {
            out.println("El registro ya existe.");
            return false;

        } // si es CONSULTA, ACTUALIZACION O BAJA y no existe libro
        else if (opcion >= 2 && opcion <= 4 && dato == null) {
            out.println("\nRegistro no encontrado.");
            return false;
        }
        return true;
    }

    public static Libro darDeAltaLibro(String ISBN) {
        // agregada 9
        //setea los atributos del objeto
        Libro libro = new Libro();
        libro.setISBN(ISBN);
        libro.setTitulo(leer_cadena("Ingrese el titulo"));
        libro.setAutor(leer_cadena("Ingrese el autor"));
        libro.setEditorial(leer_cadena("Ingrese el editorial"));
        libro.setEdicion(leer_entero("Ingrese el edicion"));
        libro.setAnno_de_publicacion(leer_entero("Ingrese el anno de publicacion"));

        out.println("\nRegistro agregado correctamente.");

        return libro;
    }

    public static Libro actualizarLibro(Libro libro) {
        // agregada 10
        //se usa en caso de que quiera modificarse un libro ya existente

        Libro dato = libro;
        int subopcion;

        //se despliegan las opciones
        out.println("Men\u00FA de modificaci\u00F3n de campos");
        out.println("1.- titulo");
        out.println("2.- autor");
        out.println("3.- editorial");
        out.println("4.- edicion");
        out.println("5.- anno de publicacion");
        do {
            subopcion = leer_entero("Seleccione un n\u00FAmero de campo a modificar");
            if (subopcion < 1 || subopcion > 5)//se valida la opcion elegida
            {
                out.println("Opci\u00F3n no v\u00E1lida.");
            }
        } while (subopcion < 1 || subopcion > 5);
        switch (subopcion) {//según la opcion elegida se imprime el mensaje adecuado
            case 1:
                dato.setTitulo(leer_cadena("Ingrese el nuevo titulo"));
                break;
            case 2:
                dato.setAutor(leer_cadena("Ingrese el nuevo autor"));
                break;
            case 3:
                dato.setEditorial(leer_cadena("Ingrese el nuevo editorial"));
                break;
            case 4:
                dato.setEdicion(leer_entero("Ingrese el nuevo edicion"));
                break;
            case 5:
                dato.setAnno_de_publicacion(leer_entero("Ingrese el nuevo anno de publicacion"));
                break;
        }
        out.println("\nRegistro actualizado correctamente.");
        return dato;

    }
}

interface Funcion<T extends Comparable<T>> {

    void funcion(T dato, Object parametros);
}
