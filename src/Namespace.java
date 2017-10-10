import java.util.HashMap;

public class Namespace {
	private HashMap<String, Double> names;

	public Namespace() {
		this.names = new HashMap<>();
	}

	public void setName(String name, Double value){
		this.names.put(name, value);
	}

	public Double getValueFromName(String name){
		return this.names.get(name);
	}

}
