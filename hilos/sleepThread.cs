using System;
using System.Numerics;
using System.Threading;

namespace hilos
{
    class sleepThread
    {
        public static void Sleeps(){
            int num = 1;
            for (int i = 0; i < 10; i++)
            {
                Console.WriteLine(num);
                Thread.Sleep(1000);
                num++;
            }
            System.Console.WriteLine("Thread end");
        }

        public static void BankExample(){
             BankAcc acount = new BankAcc(10);
            Thread[] threads = new Thread[15];      //create 15 threads
            Thread.CurrentThread.Name = "main";

            for(int i = 0; i< 15; i++){
                //function that is going to execute once it started
                Thread t = new Thread(new ThreadStart(acount.IssueWithdraw));
                t.Name = i.ToString();
                threads[i] = t;
            }

            for(int i = 0; i< 15; i++){
                System.Console.WriteLine("Thread {0} is Alive {1}.",threads[i].Name,threads[i].IsAlive);
                threads[i].Start();
                System.Console.WriteLine("Thread {0} is Alive {1}.",threads[i].Name,threads[i].IsAlive);
            }
            //you can chance priorities but is not recommended
            System.Console.WriteLine("Current Priority {0}", Thread.CurrentThread.Priority);

            System.Console.WriteLine("Thread Ending {0}", Thread.CurrentThread.Name);
        }
    }

    class BankAcc{
        private Object acctLock = new object();
        double Balance {set;get;}
        public BankAcc(double bal){
            Balance = bal;
        }
        public double Withdraw(double amount){
            if((Balance - amount) < 0){
                Console.WriteLine($"The balance cant be ${Balance}");
                return Balance;
            }
            lock(acctLock){
                if(Balance >= amount){
                    Console.WriteLine("Removed {0}, left in acc {1}",amount,Balance-amount);
                    Balance -= amount;
                }
                return Balance;
            }
        }

        public void IssueWithdraw(){
            Withdraw(1);
        }
    }
}