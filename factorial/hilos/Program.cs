using System;
using System.Numerics;
using System.Threading;
using System.Threading.Tasks;
using System.Diagnostics;

    //ESTEFANIA CACERES PEREZ 1727744
    //HILOS CON ITERACIONES Y CONSULTA DE TIEMPO

namespace hilos
{
    class Factorial{
        private Object factLck = new object();
        public BigInteger partFactorial(BigInteger start, BigInteger end, BigInteger fact){
            lock(factLck){
                for(BigInteger i = start;i <= end; i++){
                    fact *= i;
                }
                return fact;
            }
        }
    }
    class Program
    {
        static object stap = new object();
        static BigInteger factorial = 1;
        static BigInteger obtenerFactorial(BigInteger n){
            BigInteger factTodo = 1;
            for(int i  = 2; i<= n; i++){
                factTodo *= i;
            }
            return factTodo;
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
            Console.WriteLine("Menciona el número de iteraciones de hilos");
            int n = 0;
            try{
                n = int.Parse(Console.ReadLine());
                if(n < 0) return obtenerIteraciones();
                return n;
            }catch(Exception){
                Console.WriteLine("Escribe un número, porfavor");
                return obtenerIteraciones();
            }
        }
        static void Main(string[] args)
        {
            BigInteger num = obtenerNumero();
            int iteraciones = obtenerIteraciones();
            BigInteger primerNum = 1;
            BigInteger segundNum = 1;
            
            for(int i = 1; i<= iteraciones; i++){
                TimeSpan tiempo = new TimeSpan();
                BigInteger y = 1;
                Factorial objFactorial = new Factorial();
                int numHilos = i*2;
                segundNum = num/numHilos;
                BigInteger temp1 = primerNum;
                BigInteger temp2 = segundNum;
                for(int j=1; j<= numHilos; j++){
                    Stopwatch sw = Stopwatch.StartNew();
                    Thread hilos = new Thread(()=>{
                        y *= objFactorial.partFactorial( temp1,temp2,factorial);
                    });
                    hilos.Start();
                    hilos.Join();
                    tiempo += sw.Elapsed;
                    temp1 = primerNum + temp2;
                    temp2 = (j == numHilos - 1) ? num : segundNum + temp2;
                }
                Console.WriteLine("Factorial de {0} con {1} hilos : {2}. Tiempo {3} ms",num,numHilos,string.Format("{0:#.####E+0}",y),tiempo);
            }
            TimeSpan tiempoSec = new TimeSpan();
            Stopwatch swSec = Stopwatch.StartNew();
            BigInteger factorialSec = obtenerFactorial(num);
            tiempoSec = swSec.Elapsed;
            Console.WriteLine("Factorial de {0} de manera secuencial : {1} Tiempo: {2} ms",num, string.Format("{0:#.####E+0}",factorialSec),tiempoSec);
            
        }
    }
}
