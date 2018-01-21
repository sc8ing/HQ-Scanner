
public class Choice {
	private String choice;
	private int looseHits;
	private int strictHits;
	public Choice(String c) { this.choice = c; this.looseHits = this.strictHits = 0; }

	public String getValue() { return this.choice; }
	public int getTotalHits() { return getLooseHits() + getStrictHits(); }
	public int getLooseHits() { return this.looseHits; }
	public int getStrictHits() { return this.strictHits; }
	public void setStrictHits(int h) { this.strictHits = h; }
	public void setLooseHits(int h) { this.looseHits = h; }
}
