import java.util.HashMap;
import java.util.Map;

/**
 * Representa un autómata celular unidimensional.
 */
public class AutomataCelular1d {
    protected int malla[];
    protected Map<String, Integer> reglas;

    /**
     * Construye un autómata celular con el estado inicial y las reglas proporcionadas.
     *
     * @param malla El estado inicial del autómata.
     * @param reglas Las reglas que definen el comportamiento del autómata.
     */
    public AutomataCelular1d(int[] malla, Map<String, Integer> reglas) {
        this.malla = malla;
        this.reglas = reglas;
    }

    /**
     * Construye un autómata celular con el estado inicial y un número de regla.
     *
     * @param malla El estado inicial del autómata.
     * @param n El número de regla utilizado para llenar las reglas.
     */
    public AutomataCelular1d(int[] malla, int n) {
        this.malla = malla;
        reglas = llenarReglas(n);
    }

    /**
     * Construye un autómata celular con un estado inicial predeterminado y un número de regla.
     *
     * @param n El número de regla utilizado para llenar las reglas.
     */
    public AutomataCelular1d(int n) {
        malla = new int[50];
        malla[25] = 1;
        reglas = llenarReglas(n);
    }

    /**
     * Construye un autómata celular con una longitud específica y un número de regla.
     *
     * @param m La longitud del autómata.
     * @param n El número de regla utilizado para llenar las reglas.
     */
    public AutomataCelular1d(int m, int n) {
        malla = new int[m];
        malla[m / 2] = 1;
        reglas = llenarReglas(n);
    }

    public int[] getMalla() {
        return malla;
    }

    public void setMalla(int[] malla) {
        this.malla = malla;
    }

    /**
     * Construye un autómata celular predeterminado con una longitud de 50 y regla 0.
     */
    public AutomataCelular1d() {
        malla = new int[50];
        malla[25] = 1;
        reglas = llenarReglas(0);
    }

    /**
     * Llena las reglas basándose en el número de regla proporcionado.
     *
     * @param regla El número de regla para llenar las reglas.
     * @return Las reglas en forma de Map.
     * @throws IllegalArgumentException si el número de regla es mayor a 255.
     */
    private Map<String, Integer> llenarReglas(int regla) throws IllegalArgumentException {
        if (regla > 255)
            throw new IllegalArgumentException("Solamente tenemos 256 reglas");

        Map<String, Integer> reglas = new HashMap<>();
        reglas.put("111", 0);
        reglas.put("110", 0);
        reglas.put("101", 0);
        reglas.put("100", 0);
        reglas.put("011", 0);
        reglas.put("010", 0);
        reglas.put("001", 0);
        reglas.put("000", 0);

        Map<Integer, String> apoyo = new HashMap<>();
        apoyo.put(7, "111");
        apoyo.put(6, "110");
        apoyo.put(5, "101");
        apoyo.put(4, "100");
        apoyo.put(3, "011");
        apoyo.put(2, "010");
        apoyo.put(1, "001");
        apoyo.put(0, "000");

        if (regla == 0)
            return reglas;

        for (int i = 0; i < 8; i++) {
            int resido = regla % 2;
            regla /= 2;
            reglas.put(apoyo.get(i), resido);
        }
        return reglas;
    }

    /**
     * Genera una nueva generación del autómata basándose en las reglas.
     */
    public void nuevaGeneracion() {
        int i = 1;
        int malla2[] = new int[malla.length];
        while (i < malla.length - 2) {
            String binario = malla[i - 1] + "" + malla[i] + "" + malla[i + 1];
            malla2[i] = reglas.get(binario);
            i++;
        }
        malla = malla2;
    }

    /**
     * Convierte la malla a una cadena de caracteres para su impresión.
     *
     * @return La representación de la malla como cadena.
     */
    private String imprimemalla() {
        String res = "";
        for (int i : malla) {
            res += i + " ";
        }
        return res;
    }

    /**
     * Devuelve una representación en cadena del autómata para su impresión.
     *
     * @return La representación en cadena del autómata.
     */
    public String toString() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < malla.length; i++) {
            if (malla[i] == 0)
                res.append(" ");
            else
                res.append("*");
        }
        return res.toString();
    }
}

