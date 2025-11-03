package lab1;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

public class SampleFitnessFunction extends FitnessFunction {
    private final double targetAmount;

    public SampleFitnessFunction(double targetAmount) {
        this.targetAmount = targetAmount;
    }

    @Override
    protected double evaluate(IChromosome chr) {
        double changeAmount = amountOfChange(chr); // mapping: suma monedelor
        int totalCoins = getTotalNumberOfCoins(chr); // mapping: nr. monede

        double changeDifference = Math.abs(targetAmount - changeAmount);
        double fitness = 300 * changeDifference * changeDifference; // penalizare progresivă
        fitness += totalCoins > 1 ? totalCoins : 0;                 // penalizare nr. monede
        return fitness; // DeltaEvaluator: scor mai mic = mai bun
    }

    public static double amountOfChange(IChromosome chr) {
        int numQuarters = getNrCoinsAtGene(chr, 0);
        int numDimes = getNrCoinsAtGene(chr, 1);
        int numNickels = getNrCoinsAtGene(chr, 2);
        int numPennies = getNrCoinsAtGene(chr, 3);
        return (numQuarters * 0.25) + (numDimes * 0.1) + (numNickels * 0.05) + (numPennies * 0.01);
    }

    public static int getNrCoinsAtGene(IChromosome chr, int pos) {
        Integer numCoins = (Integer) chr.getGene(pos).getAllele();
        return numCoins.intValue();
    }

    public static int getTotalNumberOfCoins(IChromosome chr) {
        int total = 0;
        for (int i = 0; i < chr.size(); i++) total += getNrCoinsAtGene(chr, i);
        return total;
    }
}