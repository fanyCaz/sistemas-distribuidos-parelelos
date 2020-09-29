using System;
using System.Numerics;
using System.Threading;
using System.Threading.Tasks;
using System.Diagnostics;

namespace hilos
{
    class Program
    {
        //ESTEFANIA CACERES PEREZ 1727744
        static object stap = new object();
        static BigInteger factorial = 1;
        static long obtenerFactorial(int n){
            long factorial = 1;
            for(int i  = 2; i<= n; i++){
                factorial *= i;
            }
            return factorial;
        }

        static void partFactorial(BigInteger start, BigInteger end){
            lock(stap){
                BigInteger fact1 = 1;
                for(BigInteger i = start;i <= end; i++){
                    fact1 *= i;
                }
                factorial *= fact1;
            }
        }

        static BigInteger obtenerNumero(){
            BigInteger n = 0;
            Console.WriteLine("Menciona el numero para obtener su factorial, debe ser entero positivo");
            try{
                n = BigInteger.Parse(Console.ReadLine());
                if(n < 0) return obtenerNumero();
                return n;
            }catch(Exception){
                Console.WriteLine("Escribe un número, porfavor");
                return obtenerNumero();
            }
        }

        static int obtenerIteraciones(){
            return 0;
        }
        static void Main(string[] args)
        {
            BigInteger num = obtenerNumero();
            BigInteger primerNum = num/4;
            BigInteger segundNum = primerNum*2;
            BigInteger tercerNum = primerNum*3;
            BigInteger fact = 1;
            
            Thread hiloA = new Thread();
            
            Stopwatch sc = Stopwatch.StartNew();
            
            long tiempoHilos = sc.ElapsedTicks/1000;

            
            
            Console.WriteLine("{0} tiempo en ms",tiempoHilos);
            Console.WriteLine("Factorial LOCAL {0} : {1}",num,string.Format("{0:#.###E+0}",fact));
            Console.WriteLine("Factorial GLOBAL {0} : {1}",num,string.Format("{0:#.###E+0}",factorial));
        }
    }
}
