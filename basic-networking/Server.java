import java.io.Console;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Server{
    public static void main(String[] args) throws Exception{
        System.out.println("Esperando por cliente...");
        ObjectInputStream ois = null;
        ObjectOutputStream oos = null;

        Socket s = null;
        ServerSocket ss = new ServerSocket(5432);
        while(true){
            try{
                s = ss.accept();
                System.out.println("Se conectaron desde la IP: "+ s.getInetAddress());

                ois = new ObjectInputStream( s.getInputStream() );
                oos = new ObjectOutputStream( s.getOutputStream() );

                int v1 = Integer.parseInt( (String)ois.readObject() );
                int v2 = Integer.parseInt( (String)ois.readObject() );
                System.out.println("Los valores enviados son :" + v1 + " y " +v2);
                int resultado = v1*v2;

                //String saludo = "hola () _ " + System.currentTimeMillis() + " bals : " + v1 + " v " + v2;

                oos.writeObject( String.valueOf(resultado) );
                System.out.println("Resultado enviado...");
            }catch(Exception ex){
                ex.printStackTrace();
            }finally{
                if( oos != null ) oos.close();
                if( ois != null ) ois.close();
                if( s != null ) s.close();
                System.out.println("Conexión cerrada");
            }
        }
    }
}