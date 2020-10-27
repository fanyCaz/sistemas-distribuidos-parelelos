using System;
using System.Net;
using System.Net.Sockets;
using System.Text;

class Program
{
    static void ExecuteClient(){
        try{
            IPHostEntry ipHost = Dns.GetHostEntry(Dns.GetHostName());
            IPAddress ipAddr = ipHost.AddressList[0];
            IPEndPoint localEndPoint = new IPEndPoint(ipAddr, 8085);

            Socket sender = new Socket(ipAddr.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
            try{
                sender.Connect(localEndPoint);
                Console.WriteLine($"Socket conectado a -> {sender.RemoteEndPoint.ToString()}");

                byte[] messageSent = Encoding.ASCII.GetBytes("TEst clientt<EOF>");
                int byteSent = sender.Send(messageSent);

                byte[] messageReceived = new byte[1024];

                int byteRecv = sender.Receive(messageReceived);
                Console.WriteLine($"Message from Server -> {Encoding.ASCII.GetString(messageReceived,0,byteRecv)}");
                sender.Shutdown(SocketShutdown.Both);
                sender.Close();
            }catch(ArgumentNullException ane){
                Console.WriteLine($"ArgumentNullException : {ane.ToString()}");
            }
        }catch(SocketException se){
            Console.WriteLine($"Socket Exception {se.ToString()}");
        }catch(Exception ex){
            Console.WriteLine($"algo rarisisisisimo : {ex.ToString()}");
        }
    }
    static void Main(string[] args)
    {
        ExecuteClient();
    }
}
