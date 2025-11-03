package lab1;

import org.jgap.FitnessFunction;
import org.jgap.IChromosome;

public class KnapsackFitnessFunction extends FitnessFunction {
    static final int N = 10;        // numărul de obiecte
    static final int CAPACITY = 30; // V = 30
    static final double PENALTY = 1000.0; // penalizare mare pentru depășire

    @Override
    protected double evaluate(IChromosome chr) {
        double totalVolume = 0;
        double totalValue  = 0;

        for (int i = 0; i < N; i++) {
            boolean included = getBoolean(chr, i);
            if (included) {
                int vol = i + 1;             // vol(i) = i
                int val = (i + 1) * (i + 1); // val(i) = i^2
                totalVolume += vol;
                totalValue  += val;
            }
        }

        // offset pozitiv ca JGAP să fie mulțumit (fitness > 0 mereu)
        final double OFFSET = 1000.0;

        if (totalVolume > CAPACITY) {
            double excess = totalVolume - CAPACITY;
            // soluție invalidă: scor mare (proast, dar pozitiv)
            return OFFSET + PENALTY * excess * excess;
        } else {
            // soluție validă: vrem valoare mare => fitness mic (dar > 0)
            // cu cât totalValue e mai mare, cu atât 1000 - totalValue e mai mic
            double score = OFFSET - totalValue;
            // asigură-te că nu devine <= 0 (rareori, dacă valoarea e uriașă)
            return (score <= 0) ? 1e-6 : score;
        }
    }


    static boolean getBoolean(IChromosome chr, int pos) {
        return ((Boolean) chr.getGene(pos).getAllele());
    }
}
