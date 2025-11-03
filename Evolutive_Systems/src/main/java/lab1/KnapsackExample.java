package lab1;

import org.jgap.*;
import org.jgap.impl.*;

public class KnapsackExample {
    private static final int NUM_EVOLUTIONS = 150;

    public static void main(String[] args) throws InvalidConfigurationException {
        Configuration conf = new DefaultConfiguration();
        Configuration.resetProperty(Configuration.PROPERTY_FITEVAL_INST);
        conf.setFitnessEvaluator(new DeltaFitnessEvaluator());  // scor mic = mai bun
        conf.setPreservFittestIndividual(true);
        conf.setKeepPopulationSizeConstant(true);

        FitnessFunction fitness = new KnapsackFitnessFunction();
        conf.setFitnessFunction(fitness);

        // Cromozom binar: 10 gene BooleanGene (1 = obiect inclus, 0 = exclus)
        Gene[] genes = new Gene[KnapsackFitnessFunction.N];
        for (int i = 0; i < genes.length; i++) {
            genes[i] = new BooleanGene(conf);
        }
        IChromosome sampleChromosome = new Chromosome(conf, genes);
        conf.setSampleChromosome(sampleChromosome);

        conf.setPopulationSize(120);

        Genotype population = Genotype.randomInitialGenotype(conf);

        IChromosome best = null;
        for (int gen = 0; gen < NUM_EVOLUTIONS; gen++) {
            population.evolve();
            best = population.getFittestChromosome();
            display(best, gen);
        }

        System.out.println("\nBest solution:");
        display(best, NUM_EVOLUTIONS);
    }

    private static void display(IChromosome chr, int gen) {
        double volume = 0, value = 0;
        StringBuilder items = new StringBuilder();
        for (int i = 0; i < KnapsackFitnessFunction.N; i++) {
            boolean inc = KnapsackFitnessFunction.getBoolean(chr, i);
            items.append(inc ? 1 : 0).append(' ');
            if (inc) {
                int vol = i + 1;
                int val = (i + 1) * (i + 1);
                volume += vol;
                value  += val;
            }
        }
        System.out.printf("Gen %d | Fitness: %.2f | vol=%.0f/%d | value=%.0f | items: %s%n",
                gen,
                chr.getFitnessValue(),
                volume,
                KnapsackFitnessFunction.CAPACITY,
                value,
                items.toString());
    }
}
