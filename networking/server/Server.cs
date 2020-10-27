using System;
using System.Net;
using System.Net.Sockets;
using System.Text;

class Program{
    public static void ExecuteServer(){
        IPHostEntry iPHost = Dns.GetHostEntry(Dns.GetHostName());
        IPAddress ipAddr = iPHost.AddressList[0];
        IPEndPoint localEndPoint = new IPEndPoint(ipAddr, 8085);

        Socket listener = new Socket(ipAddr.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
        try{
            listener.Bind(localEndPoint);
            listener.Listen(10);
            while(true){
                Console.WriteLine("esperando por conexión ..");
                Socket clientSocket = listener.Accept();

                byte[] bytes = new byte[1024];
                string data = null;

                while(true){
                    int numByte = clientSocket.Receive(bytes);
                    data += Encoding.ASCII.GetString(bytes,0,numByte);
                    
                    if(data.IndexOf("<EOF>") > -1)
                        break;
                }

                Console.WriteLine($"Text received -> {data}");
                byte[] message = Encoding.ASCII.GetBytes("Test Server");

                clientSocket.Send(message);

                clientSocket.Shutdown(SocketShutdown.Both);
                clientSocket.Close();
            }
        }catch(Exception ex){
            Console.WriteLine(ex.ToString());
        }
    }
    static void Main(string[] args){
        ExecuteServer();
    }
}