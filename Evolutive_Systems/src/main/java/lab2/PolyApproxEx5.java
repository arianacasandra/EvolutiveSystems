package lab2;

import org.jgap.*;
import org.jgap.impl.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class PolyApproxEx5 {

    // === CONFIGURARE POLINOM ===
    public static final int DEG = 5;
    public static final int BITS_PER_COEF = 11;
    public static final int GENOME_SIZE = (DEG + 1) * BITS_PER_COEF; // 6*11 = 66 gene

    public static void main(String[] args) throws Exception {

        //  Reset complet JGAP o singură dată la început
        Configuration.reset();

        // CSV pentru salvarea rezultatelor comparative
        PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream("ex5_eficienta.csv"), StandardCharsets.UTF_8));
        writer.println("Populatie,Generatii,Evaluari,Fitness");

        //==========================
        // EXERCIȚIUL 5 - Analiza eficienței algoritmului
        // ==========================
        for (int popSize : new int[]{100, 200, 300}) {
            for (int generations : new int[]{50, 100, 200}) {

                // --- Configurare GA ---
                Configuration conf = new DefaultConfiguration();
                conf.setFitnessEvaluator(new DeltaFitnessEvaluator());
                conf.setPreservFittestIndividual(true);
                conf.setKeepPopulationSizeConstant(true);
                conf.setFitnessFunction(new PolyFitnessEx5());

                // --- Structura cromozomului ---
                Gene[] genes = new Gene[GENOME_SIZE];
                for (int i = 0; i < genes.length; i++)
                    genes[i] = new BooleanGene(conf);
                conf.setSampleChromosome(new Chromosome(conf, genes));
                conf.setPopulationSize(popSize);

                // --- Populație inițială aleatoare ---
                Genotype pop = Genotype.randomInitialGenotype(conf);

                // --- Evoluție ---
                for (int g = 0; g < generations; g++) {
                    pop.evolve();
                }

                // --- Evaluare performanță ---
                double fitness = pop.getFittestChromosome().getFitnessValue();
                int evaluations = popSize * generations;

                System.out.printf("Pop=%3d | Gen=%3d | Evaluări=%6d | Fitness=%.6f%n",
                        popSize, generations, evaluations, fitness);

                writer.printf("%d,%d,%d,%.6f%n", popSize, generations, evaluations, fitness);
            }
        }

        writer.close();
        System.out.println("\n Rezultatele au fost salvate în 'ex5_eficienta.csv'.");
    }
}

// =============================
// Funcția de fitness pentru aproximarea sin(x)
// =============================
class PolyFitnessEx5 extends FitnessFunction {
    private static final int DEG = PolyApproxEx5.DEG;
    private static final int BITS_PER_COEF = PolyApproxEx5.BITS_PER_COEF;
    private static final double COEF_MIN = -10.0;
    private static final double COEF_MAX = 10.0;
    private static final int GRID = 1000; // discretizare interval [0, π]
    private static final double POSITIVE_BIAS = 1_000_000.0; // fitness pozitiv

    @Override
    protected double evaluate(IChromosome chr) {
        double[] coef = decodeCoefficients(chr);
        double error = computeError(coef);
        return error + POSITIVE_BIAS; // DeltaFitnessEvaluator => mai mic e mai bun
    }

    // Decodifică coeficienții din biți în valori reale [-10, 10]
    private double[] decodeCoefficients(IChromosome chr) {
        double[] coef = new double[DEG + 1];
        for (int i = 0; i <= DEG; i++) {
            long val = 0;
            for (int b = 0; b < BITS_PER_COEF; b++) {
                Boolean bit = (Boolean) chr.getGene(i * BITS_PER_COEF + b).getAllele();
                if (bit != null && bit)
                    val |= (1L << (BITS_PER_COEF - 1 - b));
            }
            long maxInt = (1L << BITS_PER_COEF) - 1;
            double ratio = (double) val / (double) maxInt;
            coef[i] = COEF_MIN + ratio * (COEF_MAX - COEF_MIN);
        }
        return coef;
    }

    // Calculează valoarea polinomului p(x) pentru un x dat (Horner)
    private double poly(double[] a, double x) {
        double res = 0;
        for (int i = a.length - 1; i >= 0; i--)
            res = res * x + a[i];
        return res;
    }

    // Calculează eroarea integrală aproximată prin sumă discretă
    private double computeError(double[] a) {
        double sum = 0;
        for (int k = 0; k <= GRID; k++) {
            double x = Math.PI * k / GRID;
            double px = poly(a, x);
            double fx = Math.sin(x);
            sum += Math.abs(px - fx);
        }
        return sum;
    }
}
