package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServidorDNS {
    static void main() throws IOException {
        HashMap<String, List<String>> listaMap = new HashMap<>();
        List<String> listaServidores = new ArrayList<>();

        int puerto = 5000;
        try(ServerSocket servidor = new ServerSocket(puerto)) {
            Socket cliente = servidor.accept();
            System.out.println("Cliente conectado desde: " + cliente.getInetAddress().getHostAddress());

            // Flujos de entrada/salida
            BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true);



        }

    }
}
