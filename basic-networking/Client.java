import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.io.Console;

public class Client{
    public static void main(String[] args) throws Exception {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        Socket s = null;
        Console console = System.console();
        try {
            s = new Socket("25.5.218.12",5432);
            oos = new ObjectOutputStream(s.getOutputStream());
            ois = new ObjectInputStream(s.getInputStream());
           // System.out.println("Ingresa un número cualquiera");

            //usuario ingresa valores
            String valor1 = "Valor desde clienteeeeeee"; //console.readLine();
            //System.out.println("Ingresa un segundo número cualquiera");
            //String valor2 = console.readLine();
            OperatingSystem sys = OperatingSyste.getInstance();

            String datitos = System.getProperties().toString();
            System.out.println();
            oos.writeObject(datitos);
            //oos.writeObject(valor2);

            String ret = (String)ois.readObject();
            System.out.println( "Resultado de multiplicación : " + ret);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally{
            if( ois != null ) ois.close();
            if( oos != null ) oos.close();
            if( s != null ) s.close();
        }
    }
}