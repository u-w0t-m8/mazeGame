
/**
 * Interface for a generic maze generator. In testing we may have multiple
 * generator designs which can coexist through this interface.
 *
 */
public interface GridGenerator {

    public Grid generate(int sx, int sy, Difficulty diff);

}
