import java.util.List;

/**
 * Listens for
 *
 */
public class Menu {

	private List<String> items;
	private int selected = 0;
	
	public class Item {
		String text;
	}
	
	public void addItems(String... text){
		for (String s: text){
			items.add(s);
		}
	}
	
	public String getSelectedItem(){
		return items.get(selected);
	}
	
	public String getItem(int index){
		return items.get(index);
	}
	
	public void up(){
		selected--;
		if (selected < 0){
			selected = items.size()-1;
		}
	}
	
	public void down(){
		if (selected >= items.size()){
			selected = 0;
		}
	}

}
