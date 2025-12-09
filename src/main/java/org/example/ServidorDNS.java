package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ServidorDNS {

    static HashMap<String, List<Registro>> listaMap;
    private static final int MAX_CLIENTS = 5;

    private static final List<DataOutputStream> clients =
            Collections.synchronizedList(new ArrayList<>());

    static void main() throws IOException {


        HashMap<String, List<Registro>> diccionario = new HashMap<String, List<Registro>>();

        try {
            File direcciones = new File("src/direcciones.txt");
            BufferedReader br = new BufferedReader(new FileReader(direcciones));
            String line;
            while ((line = br.readLine()) != null) {
                String[] partes = line.split(" ");
                if(partes.length <= 3){
                    Registro registro = new Registro(partes[0], partes[1], partes[2]);
                    if(!diccionario.containsKey(partes[0])){
                        diccionario.put(partes[0], new ArrayList<>());
                        diccionario.get(partes[0]).add(registro);
                    }else{
                        diccionario.get(partes[0]).add(registro);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("No se encuentra el direcciones.txt");
        }


        int puerto = 5000;
        try (ServerSocket servidor = new ServerSocket(puerto)) {

            System.out.println("Servidor DNS esperando clientes...");

            List<Socket> clientesActivos = Collections.synchronizedList(new ArrayList<>());

            while (true) {

                if (clientesActivos.size() < MAX_CLIENTS) {

                    Socket cliente = servidor.accept();
                    clientesActivos.add(cliente);

                    System.out.println("Cliente conectado desde: " + cliente.getInetAddress());

                    new Thread(() -> {

                        try {
                            BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                            PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true);

                            String linea;

                            while ((linea = entrada.readLine()) != null) {

                                if (linea.equals("EXIT")) {
                                    salida.println("EXIT");
                                    break;
                                }

                                if (linea.equals("LIST")) {
                                    salida.println("150 Inicio Listado");
                                    salida.println("===========================================");
                                    diccionario.forEach((clave, registros) -> {
                                        registros.forEach(registro -> salida.println(registro));
                                    });
                                    salida.println("===========================================");
                                    salida.println("226 Fin Listado");
                                    continue;
                                }

                                if (linea.startsWith("REGISTER")) {
                                    String[] partes = linea.split(" ");
                                    if (partes.length != 4) {
                                        salida.println("400 Bad Request");
                                        continue;
                                    }

                                    String dominio = partes[1];
                                    String tipo = partes[2];
                                    String valor = partes[3];

                                    Registro registro = new Registro(dominio, tipo, valor);
                                    diccionario.putIfAbsent(dominio, new ArrayList<>());
                                    diccionario.get(dominio).add(registro);

                                    try (FileWriter fr = new FileWriter("src/direcciones.txt", true);
                                         PrintWriter pw = new PrintWriter(fr)) {
                                        pw.println(dominio + " " + tipo + " " + valor);
                                    } catch (IOException e) {
                                        salida.println("500 Server Error");
                                        continue;
                                    }

                                    salida.println("200 Record added");
                                    continue;
                                }

                                String[] partes = linea.split(" ");
                                if (partes.length != 3 || !partes[0].equals("LOOKUP")) {
                                    salida.println("400 Bad Request");
                                    continue;
                                }

                                String tipo = partes[1];
                                String dominio = partes[2];

                                if (!diccionario.containsKey(dominio)) {
                                    salida.println("404 Not Found");
                                    continue;
                                }

                                boolean encontrado = false;

                                for (Registro r : diccionario.get(dominio)) {
                                    if (r.tipo.equals(tipo)) {
                                        salida.println("200 " + r.valor);
                                        encontrado = true;
                                    }
                                }

                                if (!encontrado) {
                                    salida.println("404 Not Found");
                                }
                            }

                            cliente.close();
                            System.out.println("Cliente desconectado");

                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        } finally {
                            clientesActivos.remove(cliente);
                        }

                    }).start();

                } else {
                    System.out.println("Máximo de 5 clientes alcanzado");
                    try { Thread.sleep(1000); } catch (InterruptedException ignored) {}
                }
            }
        }


    }
}
