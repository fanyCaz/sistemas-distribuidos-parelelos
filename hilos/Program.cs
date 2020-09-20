using System;
using System.Numerics;
using System.Threading;

namespace hilos
{
    class Program
    {
        static long obtenerFactorial(int n){
            long factorial = 1;
            for(int i  = 2; i<= n; i++){
                factorial *= i;
            }
            return factorial;
        }

        static BigInteger partFactorial(BigInteger start, BigInteger end){
            BigInteger fact1 = 1;
            for(BigInteger i = start;i <= end; i++){
                fact1 *= i;
            }
            return fact1;
        }

        static BigInteger obtenerNumero(){
            BigInteger n = 0;
            Console.WriteLine("Menciona el numero para obtener su factorial");
            try{
                n = BigInteger.Parse(Console.ReadLine());
                return n;
            }catch(Exception){
                Console.WriteLine("Escribe un número, porfavor");
                return obtenerNumero();
            }
        }
        static void Main(string[] args)
        {
            BigInteger num = obtenerNumero();
            BigInteger primerNum = num/4;
            BigInteger segundNum = primerNum*2;
            BigInteger tercerNum = primerNum*3;
            BigInteger factorial = 1;
            BigInteger f1=0,f2=0,f3=0,f4 = 0;
            Thread hiloA = new Thread(()=>{
                f1 = partFactorial(1,primerNum);
                Console.WriteLine("Termino HILO A");
            });
            Thread hiloB = new Thread(()=>{
                f2 = partFactorial(primerNum+1,segundNum);
                Console.WriteLine("Termino HILO B");
            });
            Thread hiloC = new Thread(()=>{
                f3 = partFactorial(segundNum+1,tercerNum);
                Console.WriteLine("Termino HILO C");
            });
            Thread hiloD = new Thread(()=>{
                f4 = partFactorial(tercerNum+1,num);
                Console.WriteLine("Termino HILO D");
            });
            hiloA.Start();
            hiloB.Start();
            hiloD.Start();
            hiloC.Start();
            hiloA.Join();
            hiloB.Join();
            hiloC.Join();
            hiloD.Join();
            
            factorial = f1*f2*f3*f4;
            Console.WriteLine("valor value : {0}",factorial);
        }
    }
}
