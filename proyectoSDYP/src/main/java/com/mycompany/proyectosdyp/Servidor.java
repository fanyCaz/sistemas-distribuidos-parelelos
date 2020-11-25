/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.proyectosdyp;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author raulc
 */
public class Servidor {
    public static void main(String[] args) throws Exception {
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;
        Socket s = null;
        ServerSocket ss = new ServerSocket(5432);
        int counter = 1;
        while (true) {
            try {
                // el ServerSocket me da el Socket
                s = ss.accept();
                // enmascaro la entrada y salida de bytes
                ois = new ObjectInputStream(s.getInputStream());
                oos = new ObjectOutputStream(s.getOutputStream());
                String[] clientSysInfo = (String[])ois.readObject();
                String[] newClientSysInfo = {
                		"Nombre del host: " + s.getInetAddress().getHostName(),
                		"Direccion IP: " + s.getInetAddress(),
                		clientSysInfo[0],
                		clientSysInfo[1],
                		clientSysInfo[2],
                		clientSysInfo[3],
                		clientSysInfo[4]
                		};
                oos.writeObject("Respuesta recibida!");
                System.out.println("Numero: " + counter);
                for(String info: newClientSysInfo) {
                	System.out.println(info);                	
                } 
                
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
            	counter += 1;
                if (oos != null)
                    oos.close();
                if (ois != null)
                    ois.close();
                if (s != null)
                    s.close();
                System.out.println("Conexion cerrada!");
            }
        }
    }
}