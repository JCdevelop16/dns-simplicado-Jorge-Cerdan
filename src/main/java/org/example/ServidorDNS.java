package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ServidorDNS {

    static HashMap<String, List<Registro>> listaMap;

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
        try(ServerSocket servidor = new ServerSocket(puerto)) {
            Socket cliente = servidor.accept();
            System.out.println("Cliente conectado desde: " + cliente.getInetAddress().getHostAddress());

            // Flujos de entrada/salida
            BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true);



            // Cierre
            System.out.println("Conexión cerrada con el cliente.");
            cliente.close();
            System.exit(0);

        }

    }
}
