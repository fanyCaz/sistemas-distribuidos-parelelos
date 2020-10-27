using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;
using System.Numerics;
//ESTEFANIA CACERES PEREZ 1727744

namespace factorial
{
    class PartFactorial
    {
        private Object factLck = new object();
        public BigInteger partFactorial(BigInteger start, BigInteger end, BigInteger fact)
        {
            lock (factLck)
            {
                for (BigInteger i = start; i <= end; i++)
                {
                    fact *= i;
                }
                return fact;
            }
        }
    }
}
