package com.proyecto;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Esta clase representa una interfaz gráfica de usuario (GUI) para un autómata celular unidimensional.
 * Permite a los usuarios visualizar e interactuar con el autómata estableciendo reglas, generando generaciones
 * y guardando las imágenes generadas.
 */
public class AutomataCelularGUI3 extends JFrame {

    /**
     * El objeto de autómata celular.
     */
    private AutomataCelular1d automataCelular;

    /**
     * Número de generaciones a dibujar.
     */
    private int generacionesParaDibujar;

    /**
     * Matriz para almacenar los estados de las generaciones.
     */
    private int[][] estadosGeneraciones;

    /**
     * La regla actual para el autómata celular.
     */
    private int reglaActual;

    /**
     * Bandera booleana para indicar si llenar con una configuración inicial aleatoria.
     */
    private boolean ran=false;

    /**
     * Lista para almacenar los checkboxes para la selección de reglas.
     */
    private ArrayList<JCheckBox> lista=new ArrayList<>();

    /**
     * Construye un nuevo objeto AutomataCelularGUI3.
     *
     * @param numCeldas           El número de células en el autómata.
     * @param numGeneraciones     El número de generaciones a mostrar.
     */
    public AutomataCelularGUI3(int numCeldas, int numGeneraciones) {
        // Inicializa el autómata celular
        this.automataCelular = new AutomataCelular1d(numCeldas, 0); // Regla inicializada en 0
        this.generacionesParaDibujar = numGeneraciones;
        this.estadosGeneraciones = new int[numGeneraciones][numCeldas];
        this.reglaActual = 0;

        // Configura el JFrame
        setTitle("Automata Celular GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 650);

        // Crea y configura el panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Crea un panel de entrada para la selección de reglas y botones
        JPanel inputPanel = new JPanel();
        // Agrega checkboxes para la selección de reglas
        for (int i = 0; i <= 7; i++) {
            String reg= Integer.toBinaryString(i);
            if(reg.equals("0"))
                reg="000";
            if(reg.equals("1"))
                reg="001";
            if(reg.equals("10"))
                reg="010";
            if(reg.equals("11"))
                reg="011";
            JCheckBox checkBox = new JCheckBox(reg);
            checkBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int valor=Integer.parseInt(e.getActionCommand(),2);
                    if (checkBox.isSelected()) {
                        reglaActual += Math.pow(2,valor);
                    } else {
                        reglaActual -= Math.pow(2,valor);
                    }
                }
            });
            lista.add(checkBox);
            inputPanel.add(checkBox);
        }

        JTextField reglaTextField = new JTextField(5);
        inputPanel.add(new JLabel("Número de regla:"));
        inputPanel.add(reglaTextField);

        JButton drawButton = new JButton("Procesar");
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String reglaText = reglaTextField.getText();
                if (!reglaText.isEmpty()) {
                    try {
                        reglaActual = Integer.parseInt(reglaText);
                        rellenarCheckbox();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Por favor, ingrese un número de regla válido.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                reglaTextField.setText(String.valueOf(reglaActual));

                dibujarAutomata();
            }
        });

        inputPanel.add(drawButton);
        // Agrega un número aleatorio de bichos
        JButton drawButton2 = new JButton("Llenar");
        drawButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ran=true;
            }
        });
        inputPanel.add(drawButton2);
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        JPanel drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujar(g);
            }
        };

        mainPanel.add(drawingPanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);

    }

    /**
     * Método para crear un nuevo arreglo con un número aleatorio de células vivas.
     *
     * @param numero El tamaño del arreglo.
     * @return El arreglo con un número aleatorio de células vivas.
     */
    private int[] nuevoArreglo(int numero){
        int[] arreglo = new int[numero];
        Random random = new Random();
        int num= random.nextInt(numero+1);
        for(int i=0;i<num;i++){
            int random2 = random.nextInt(numero);
            arreglo[random2]=1;
        }
        return arreglo;
    }

    /**
     * Método para marcar los checkboxes según la regla actual.
     */
    private void rellenarCheckbox(){
        String representacion=Integer.toBinaryString(reglaActual);
        representacion=String.format("%8s",representacion.replace(" ","0"));
        for(int i=0;i<representacion.length();i++){
            if(representacion.charAt(i)=='1') {
                lista.get(7-i).setSelected(true);
            }
            else
                lista.get(i).setSelected(false);
        }
    }

    /**
     * Método para dibujar el autómata celular y generar las generaciones.
     */
    private void dibujarAutomata() {
        int numCeldas = automataCelular.getMalla().length;

        if(ran){
            int[] arreglo= nuevoArreglo(numCeldas);
            automataCelular= new AutomataCelular1d(arreglo,reglaActual);
        }
        else {
            automataCelular = new AutomataCelular1d(numCeldas, reglaActual);
        }
        System.out.println(automataCelular);
        for (int i = 0; i < generacionesParaDibujar; i++) {
            automataCelular.nuevaGeneracion();
            int[] malla = automataCelular.getMalla();
            System.arraycopy(malla, 0, estadosGeneraciones[i], 0, malla.length);
        }

        JFrame frame = new JFrame("Dibujo del Autómata Celular");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                dibujar(g);
            }
        };

        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setVisible(true);

        for (int i = 0; i < generacionesParaDibujar; i++) {
            try {
                Thread.sleep(100); // Agregar un pequeño retraso para ver el dibujo
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            panel.repaint();
        }

        JButton saveButton = new JButton("Guardar Imagen");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarImagen(panel);
            }
        });
        frame.getContentPane().add(saveButton, BorderLayout.SOUTH);

    }

    /**
     * Método para guardar la imagen generada.
     *
     * @param component El componente del que se generará la imagen.
     */
    private void guardarImagen(Component component) {
        BufferedImage image = new BufferedImage(component.getWidth(), component.getHeight(), BufferedImage.TYPE_INT_RGB);
        component.paint(image.getGraphics());

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar Imagen");
        fileChooser.setSelectedFile(new File("automata_celular_image.jpg"));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                ImageIO.write(image, "jpg", fileToSave);
                JOptionPane.showMessageDialog(this, "Imagen guardada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error al guardar la imagen.", "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    /**
     * Método para dibujar el autómata celular en el componente gráfico.
     *
     * @param g El contexto gráfico.
     */
    private void dibujar(Graphics g) {
        int celdaAncho = 800 / automataCelular.getMalla().length;
        int celdaAlto = 600 / generacionesParaDibujar;

        for (int j = 0; j < generacionesParaDibujar; j++) {
            int[] malla = estadosGeneraciones[j];
            for (int i = 0; i < malla.length; i++) {
                if (malla[i] == 1) {
                    g.setColor(Color.BLACK);
                } else {
                    g.setColor(Color.WHITE);
                }
                g.fillRect(i * celdaAncho, j * celdaAlto, celdaAncho, celdaAlto);
            }
        }
        ran=false;
    }

    /**
     * Método principal para iniciar la aplicación.
     *
     * @param args Los argumentos de la línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AutomataCelularGUI3 gui = new AutomataCelularGUI3(70, 70);
                gui.setVisible(true);
            }
        });
    }
}
