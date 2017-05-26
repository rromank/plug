package ua.nure.plug.service.minhash;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import ua.nure.plug.model.Shingle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MinHashServiceImpl implements MinHashService {

    private static final int FUNCTIONS_COUNT = 200;

    private static int[] SEEDS = {24854, 14098, 14276, 36453, 23028, 13302, 21420, 27517, 5537, 10511, 1396, 39226, 12859, 27154, 11212, 5651, 19798, 36740, 13030, 29113, 15677, 11851, 21659, 3174, 3186, 12414, 18239, 11084, 19916, 33021, 33530, 23891, 30741, 37710, 3980, 13696, 4532, 12572, 24718, 20052, 35305, 39673, 25831, 1344, 35760, 20224, 1779, 27965, 256, 30573, 8512, 10454, 24629, 21536, 8841, 23249, 21852, 29703, 13842, 23530, 25405, 18828, 36531, 14652, 6306, 30020, 23802, 24962, 5107, 11397, 3579, 25328, 16781, 1039, 16350, 12504, 24251, 11440, 33764, 20486, 15433, 9393, 1650, 28317, 18996, 2091, 20841, 10314, 35681, 28444, 16371, 3502, 24820, 13975, 4124, 7082, 29846, 18379, 3711, 20006, 11909, 26685, 7362, 5342, 7320, 28174, 36798, 3828, 32401, 11589, 3586, 4111, 8421, 848, 33138, 33419, 29640, 2382, 38564, 19967, 29094, 13401, 37746, 17192, 29607, 9765, 35530, 9942, 21604, 12854, 2633, 24031, 4671, 6690, 13840, 33131, 26387, 39787, 302, 725, 29828, 20551, 24216, 29927, 20217, 20789, 18897, 20226, 1580, 18737, 33227, 7775, 35831, 21221, 16685, 14628, 39600, 3960, 39668, 32358, 38181, 19699, 3495, 36790, 21362, 25748, 35495, 33125, 12473, 35707, 6807, 24494, 29870, 24525, 33966, 2063, 9132, 11745, 30117, 11054, 24652, 34677, 37645, 14619, 20859, 21411, 1246, 16278, 16179, 3007, 28940, 31675, 16952, 24835, 38257, 21691, 33855, 8995, 35802, 11497};

    @Override
    public List<Integer> createMinHashSignature(List<Shingle> shingles) {
        Set<Shingle> shingleSet = shingles.stream()
                .collect(Collectors.toSet());

        Integer[] result = new Integer[FUNCTIONS_COUNT];
        for (int i = 0; i < FUNCTIONS_COUNT; i++) {
            result[i] = findMin(new ArrayList<>(shingleSet), SEEDS[i]);
        }
        return Lists.newArrayList(result);
    }

    private int findMin(List<Shingle> set, int seed) {
        int min = 0xFFFFFFFF;
        for (Shingle shingle : set) {
            int hash = hash(shingle.getShingle(), seed);
            if (hash < min) min = hash;
        }
        return min;
    }

    private int hash(String string, int seed) {
        int result = 1;
        for (int i = 0; i < string.length(); i++) {
            result = (seed * result + string.charAt(i));
        }
        return result;
    }

}
