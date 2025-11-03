package lab1;

import org.jgap.*;
import org.jgap.impl.*;

public class ConstraintExample {
    private static final int NUM_EVOLUTIONS = 100;

    public static void main(String[] args) throws InvalidConfigurationException {
        double targetAmount = 1.84;

        Configuration conf = new DefaultConfiguration();
        Configuration.resetProperty(Configuration.PROPERTY_FITEVAL_INST);
        conf.setFitnessEvaluator(new DeltaFitnessEvaluator()); // scor mic = mai bun
        conf.setPreservFittestIndividual(true);                // păstrează cel mai bun
        conf.setKeepPopulationSizeConstant(true);

        FitnessFunction fitness = new SampleFitnessFunction(targetAmount);
        conf.setFitnessFunction(fitness);

        // 4 gene întregi: Quarters(0..20), Dimes(0..30), Nickels(0..50), Pennies(0..80)
        Gene[] sampleGenes = new Gene[4];
        sampleGenes[0] = new IntegerGene(conf, 0, 20);
        sampleGenes[1] = new IntegerGene(conf, 0, 30);
        sampleGenes[2] = new IntegerGene(conf, 0, 50);
        sampleGenes[3] = new IntegerGene(conf, 0, 80);
        IChromosome sampleChromosome = new Chromosome(conf, sampleGenes);
        conf.setSampleChromosome(sampleChromosome);

        conf.setPopulationSize(80);

        Genotype population = Genotype.randomInitialGenotype(conf);

        for (int i = 0; i < NUM_EVOLUTIONS; i++) {
            population.evolve();
            IChromosome best = population.getFittestChromosome();
            display(best, i);
        }
    }

    private static void display(IChromosome chr, int gen) {
        System.out.print("Gen " + gen + " | Fitness: " + chr.getFitnessValue());
        System.out.print(" | Coins: ");
        for (int i = 0; i < 4; i++)
            System.out.print(SampleFitnessFunction.getNrCoinsAtGene(chr, i) + " ");
        System.out.println(" | total: " + SampleFitnessFunction.amountOfChange(chr));
    }
}
