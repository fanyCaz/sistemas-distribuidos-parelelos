namespace factorial
{
    partial class Form1
    {
        /// <summary>
        /// Variable del diseñador necesaria.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Limpiar los recursos que se estén usando.
        /// </summary>
        /// <param name="disposing">true si los recursos administrados se deben desechar; false en caso contrario.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Código generado por el Diseñador de Windows Forms

        /// <summary>
        /// Método necesario para admitir el Diseñador. No se puede modificar
        /// el contenido de este método con el editor de código.
        /// </summary>
        private void InitializeComponent()
        {
            this.numeroFact = new System.Windows.Forms.TextBox();
            this.numItera = new System.Windows.Forms.TextBox();
            this.btnSubmit = new System.Windows.Forms.Button();
            this.lblConfirmed = new System.Windows.Forms.Label();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.btnSubmitHilos = new System.Windows.Forms.Button();
            this.label2 = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.txtResFact = new System.Windows.Forms.TextBox();
            this.lblError = new System.Windows.Forms.Label();
            this.lblResponse = new System.Windows.Forms.Label();
            this.groupBox1.SuspendLayout();
            this.SuspendLayout();
            // 
            // numeroFact
            // 
            this.numeroFact.Location = new System.Drawing.Point(16, 66);
            this.numeroFact.Name = "numeroFact";
            this.numeroFact.Size = new System.Drawing.Size(237, 20);
            this.numeroFact.TabIndex = 0;
            // 
            // numItera
            // 
            this.numItera.Location = new System.Drawing.Point(16, 120);
            this.numItera.Name = "numItera";
            this.numItera.Size = new System.Drawing.Size(237, 20);
            this.numItera.TabIndex = 1;
            // 
            // btnSubmit
            // 
            this.btnSubmit.Location = new System.Drawing.Point(17, 162);
            this.btnSubmit.Name = "btnSubmit";
            this.btnSubmit.Size = new System.Drawing.Size(105, 41);
            this.btnSubmit.TabIndex = 2;
            this.btnSubmit.Text = "Secuencial";
            this.btnSubmit.UseVisualStyleBackColor = true;
            this.btnSubmit.Click += new System.EventHandler(this.btnSubmit_Click);
            // 
            // lblConfirmed
            // 
            this.lblConfirmed.AutoSize = true;
            this.lblConfirmed.Location = new System.Drawing.Point(43, 242);
            this.lblConfirmed.Name = "lblConfirmed";
            this.lblConfirmed.Size = new System.Drawing.Size(230, 13);
            this.lblConfirmed.TabIndex = 3;
            this.lblConfirmed.Text = "La imágen con los resultados se han guardado.";
            this.lblConfirmed.Visible = false;
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.lblError);
            this.groupBox1.Controls.Add(this.btnSubmitHilos);
            this.groupBox1.Controls.Add(this.label2);
            this.groupBox1.Controls.Add(this.label1);
            this.groupBox1.Controls.Add(this.btnSubmit);
            this.groupBox1.Controls.Add(this.numItera);
            this.groupBox1.Controls.Add(this.numeroFact);
            this.groupBox1.Location = new System.Drawing.Point(26, 12);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.Size = new System.Drawing.Size(267, 209);
            this.groupBox1.TabIndex = 4;
            this.groupBox1.TabStop = false;
            this.groupBox1.Text = "Inicializar Datos ";
            // 
            // btnSubmitHilos
            // 
            this.btnSubmitHilos.Location = new System.Drawing.Point(139, 162);
            this.btnSubmitHilos.Name = "btnSubmitHilos";
            this.btnSubmitHilos.Size = new System.Drawing.Size(114, 41);
            this.btnSubmitHilos.TabIndex = 5;
            this.btnSubmitHilos.Text = "Hilos";
            this.btnSubmitHilos.UseVisualStyleBackColor = true;
            this.btnSubmitHilos.Click += new System.EventHandler(this.btnSubmitHilos_Click);
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(14, 50);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(161, 13);
            this.label2.TabIndex = 4;
            this.label2.Text = "Número para obtener su factorial";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(13, 104);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(155, 13);
            this.label1.TabIndex = 3;
            this.label1.Text = "Número de Iteraciones de Hilos";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(318, 31);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(113, 13);
            this.label3.TabIndex = 5;
            this.label3.Text = "Resultado de factorial:";
            // 
            // txtResFact
            // 
            this.txtResFact.Enabled = false;
            this.txtResFact.Location = new System.Drawing.Point(437, 28);
            this.txtResFact.Name = "txtResFact";
            this.txtResFact.Size = new System.Drawing.Size(175, 20);
            this.txtResFact.TabIndex = 6;
            // 
            // lblError
            // 
            this.lblError.AutoSize = true;
            this.lblError.Font = new System.Drawing.Font("Microsoft Sans Serif", 9.75F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.lblError.ForeColor = System.Drawing.Color.DarkRed;
            this.lblError.Location = new System.Drawing.Point(14, 20);
            this.lblError.Name = "lblError";
            this.lblError.Size = new System.Drawing.Size(233, 16);
            this.lblError.TabIndex = 6;
            this.lblError.Text = "Ingresa los datos en número, porfavor";
            this.lblError.Visible = false;
            // 
            // lblResponse
            // 
            this.lblResponse.AutoSize = true;
            this.lblResponse.Location = new System.Drawing.Point(320, 62);
            this.lblResponse.Name = "lblResponse";
            this.lblResponse.Size = new System.Drawing.Size(35, 13);
            this.lblResponse.TabIndex = 7;
            this.lblResponse.Text = "label4";
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(733, 277);
            this.Controls.Add(this.lblResponse);
            this.Controls.Add(this.txtResFact);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.lblConfirmed);
            this.Name = "Form1";
            this.Text = "Form1";
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox numeroFact;
        private System.Windows.Forms.TextBox numItera;
        private System.Windows.Forms.Button btnSubmit;
        private System.Windows.Forms.Label lblConfirmed;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.TextBox txtResFact;
        private System.Windows.Forms.Button btnSubmitHilos;
        private System.Windows.Forms.Label lblError;
        private System.Windows.Forms.Label lblResponse;
    }
}

