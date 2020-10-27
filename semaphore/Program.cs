using System;
using System.Threading;

namespace semaphore
{
    class Program
    {
        public static int sumados= 0;
        static void Main(string[] args)
        {
            Principal();
        }

        static void Principal(){
            System.Console.WriteLine("¿Cuantos hilos quieres?");
            int hilos = int.Parse(Console.ReadLine());
            int i = 0;
            Contador cuenta = new Contador();
            cuenta.Final = 10;
            cuenta.Str = 0;
            while(i < hilos){
                Thread t = new Thread(()=>{
                   sumados += cuenta.sumatoria(sumados);
                });
                t.Start();
                i++;
                Console.WriteLine(sumados);
                t.Join();
            }
            
            System.Console.WriteLine("Thread ends");
        }
    }

    class Contador{
        private Object acclck = new object();
        private int final {set;get;}
        private int str {get;set;}
        private int end {get;set;}
        public int serieFinal{get;set;}
        public int Final{
            get {return final;}
            set {final = value;}
        }
        public int Str{
            get{return str;}
            set{str = value;}
        }
        public int End{
            get{return end;}
            set{end = value;}
        }
        public int SerieFinal{
            get{return serieFinal;}
            set{serieFinal=value;}
        }
        public void cuenta(){
            //no entra otro valor hasta que termina el anterior
            lock(acclck){
                int n = 1;
                for (int i = 0; i < final; i++)
                {   
                    System.Console.WriteLine("numero {0}",n);
                    Thread.Sleep(1000);
                    n++;
                }
            }
        }

        public void serie(int contarHasta){
            lock(acclck){
                Console.WriteLine("cuenta Hasta {0}",contarHasta);
                for(int i = 0; i <= contarHasta; i++){
                    Console.WriteLine("numero {0}",i);
                }
            }
        }

        public int sumatoria(int sumados){
            lock(acclck){
                for(int i = 1; i <= 4; i++){
                    sumados += i;
                    Console.WriteLine("valor {0}",sumados);
                }
                return sumados;
            }
        }
    }
}