package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServidorDNS {
    static void main() throws IOException {
        HashMap<String, List<String>> listaMap = new HashMap<>();
        List<String> listaServidores = new ArrayList<>();

        int puerto = 5000;
        try(ServerSocket serverSocket = new ServerSocket(puerto)) {

        }

    }
}
