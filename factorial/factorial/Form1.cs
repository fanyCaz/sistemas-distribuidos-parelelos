using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Text;
using System.Windows.Forms;
using System.Numerics;
using ScottPlot;
using System.Diagnostics;

//ESTEFANIA CACERES PEREZ 1727744

namespace factorial
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void btnSubmit_Click(object sender, EventArgs e)
        {
            txtResFact.Clear();
            txtResFact.Visible = true;
            int numero = 0;
            try
            {
                numero = int.Parse(numeroFact.Text);
            }
            catch (Exception)
            {
                numero = 0;
            }
            
            if(numero > 0)
            {
                lblError.Visible = false;
                TimeSpan tiempoSec = new TimeSpan();
                Stopwatch swSec = Stopwatch.StartNew();
                BigInteger factorial = Principal.obtenerFactorial(numero);
                tiempoSec = swSec.Elapsed;
                string formated = string.Format("{0:#.#####E+0}", factorial);
                txtResFact.Text = formated;
                lblResponse.Text = "Tiempo transcurrido para función secuencial: " + tiempoSec + " ms";
            }
            else{
                lblError.Visible = true;
            }

        }

        private void btnSubmitHilos_Click(object sender, EventArgs e)
        {
            txtResFact.Clear();
            lblResponse.Text = "";
            BigInteger numero = 0;
            int iteraciones = 0;
            try
            {
                numero = int.Parse(numeroFact.Text);
                iteraciones = int.Parse(numItera.Text);
            }
            catch (Exception)
            {
                numero = 0;
                iteraciones = 0;
            }
            if(numero > 0 && iteraciones > 0)
            {
                txtResFact.Visible = false;
                Dictionary<double[], string> response = new Dictionary<double[], string>();
                lblError.Visible = false;
                string formated = string.Format("{0}", numero);
                //OBTENER Numero Hilos, Tiempo en ms , y el string con info completa
                response = Principal.ObtenerFactoriales(numero, iteraciones);
                //PARA GRAFICA
                var plt = new ScottPlot.Plot(600, 400);
                double[] tiempos = new double[response.Count];
                double[] hilos = new double[response.Count];
                int i = 0;
                foreach (double[] val in response.Keys)
                {
                    hilos[i] = val[0];
                    tiempos[i] = val[1];
                    i++;
                }
                plt.PlotScatter(hilos, tiempos);
                plt.Legend();

                plt.Title("Hilos vs Tiempo de Obtener Factorial");
                plt.YLabel("Tiempo(ms)");
                plt.XLabel("Hilos");
                string nombreImagen = string.Format("../../{0}_{1}_Grafica.png",numero,iteraciones);
                try
                {
                    plt.SaveFig(nombreImagen);
                    lblConfirmed.Text = "Se ha guardado la gráfica en png";
                }
                catch (Exception)
                {
                    lblConfirmed.Text = "No se ha podido guardar la información";
                }
                lblConfirmed.Visible = true;
                //IMPRIMIR EN FORMS
                foreach(string val in response.Values)
                {
                    lblResponse.Text += string.Format($"{val} \n");
                    Console.WriteLine(val);
                }

            }
            else
            {
                lblError.Visible = true;
            }
        }
    }
}
