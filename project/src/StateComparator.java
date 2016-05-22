/**
 * Used to override the functionality of the priority queue in the A* search
 */
import java.util.Comparator;

public class StateComparator implements Comparator<State>{

	@Override
	public int compare(State s1, State s2) {
		if(s1.getCost() < s2.getCost()){
			return -1;
		}
		else if(s1.getCost() > s2.getCost()){
			return 1;
		}
		else {
			return 0;
		}
	}

	
}
