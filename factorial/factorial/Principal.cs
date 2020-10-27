using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Numerics;
using System.Diagnostics;
//ESTEFANIA CACERES PEREZ 1727744

namespace factorial
{
    class Principal
    {
        static object stap = new object();
        static BigInteger factorial = 1;
        public static BigInteger obtenerFactorial(BigInteger n)
        {
            BigInteger factTodo = 1;
            for (int i = 2; i <= n; i++)
            {
                factTodo *= i;
            }
            return factTodo;
        }

        static BigInteger obtenerNumero()
        {
            BigInteger n = 0;
            Console.WriteLine("Menciona el numero para obtener su factorial, debe ser entero positivo");
            try
            {
                n = BigInteger.Parse(Console.ReadLine());
                if (n < 0) return obtenerNumero();
                return n;
            }
            catch (Exception)
            {
                Console.WriteLine("Escribe un número, porfavor");
                return obtenerNumero();
            }
        }

        static int obtenerIteraciones()
        {
            Console.WriteLine("Menciona el número de iteraciones de hilos");
            int n = 0;
            try
            {
                n = int.Parse(Console.ReadLine());
                if (n < 0) return obtenerIteraciones();
                return n;
            }
            catch (Exception)
            {
                Console.WriteLine("Escribe un número, porfavor");
                return obtenerIteraciones();
            }
        }

        public static Dictionary<double[],string> ObtenerFactoriales(BigInteger num, int iteraciones)
        {
            Dictionary<double[], string> valores = new Dictionary<double[], string>();
            
            BigInteger primerNum = 1;
            BigInteger segundNum = 1;

            for (int i = 1; i <= iteraciones; i++)
            {
                TimeSpan tiempo = new TimeSpan();
                BigInteger y = 1;
                PartFactorial objFactorial = new PartFactorial();
                int numHilos = i * 2;
                segundNum = num / numHilos;
                BigInteger temp1 = primerNum;
                BigInteger temp2 = segundNum;
                for (int j = 1; j <= numHilos; j++)
                {
                    Stopwatch sw = Stopwatch.StartNew();
                    Thread hilos = new Thread(() => {
                        y *= objFactorial.partFactorial(temp1, temp2, factorial);
                    });
                    hilos.Start();
                    hilos.Join();
                    tiempo += sw.Elapsed;
                    temp1 = primerNum + temp2;
                    temp2 = (j == numHilos - 1) ? num : segundNum + temp2;
                }
                string sentencia = string.Format("Factorial de {0} con {1} hilos : {2}. Tiempo {3} ms", num, numHilos, string.Format("{0:#.####E+0}", y), tiempo);
                double milisegundos = tiempo.TotalMilliseconds;
                valores.Add(new double[] { numHilos, milisegundos }, sentencia );
            }
            return valores;
        }

        static void Inicializar()
        {
            BigInteger num = obtenerNumero();
            int iteraciones = obtenerIteraciones();
            ObtenerFactoriales(num,iteraciones);
            TimeSpan tiempoSec = new TimeSpan();
            Stopwatch swSec = Stopwatch.StartNew();
            BigInteger factorialSec = obtenerFactorial(num);
            tiempoSec = swSec.Elapsed;
            Console.WriteLine("Factorial de {0} de manera secuencial : {1} Tiempo: {2} ms", num, string.Format("{0:#.####E+0}", factorialSec), tiempoSec);

        }
    }
}
