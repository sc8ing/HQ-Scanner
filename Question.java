import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Question {
	private String question;
	private Choice[] choices;
	private Choice answer;
	
	public Question(String question, String[] choices) {
		this.question = question;
		this.choices = new Choice[choices.length];
		for (int i=0; i<choices.length; i++) this.choices[i] = new Choice(choices[i]);
		this.answer = this.choices[0]; // a default in case an answer isn't found in time
	}
	public Choice[] getChoices() { return this.choices; }
	public String getQuestion() { return this.question; }
	public Choice getAnswer() { return this.answer; }
	private String looseRegex(String s) {
		s = s.replaceAll("\\p{P}", "").toLowerCase().replaceAll("\n", "");
		String next = "";
		for (int i=1; i<s.length(); i++) {
			next += s.substring(0, i) + ".?" + s.substring(i+1) + "|";
		}
		next = next.substring(0, next.length()>0?next.length()-1:0);
		String r = next.replaceAll(" ", ".?");
		System.out.println("looseRegex: " + s + " –> " + r);
		return r;
	}
	private String strictRegex(String s) {
		String r = s.replaceAll("\\p{P}", "").toLowerCase().replaceAll("\n", "");
		System.out.println("strictRegex: " + s + " –> " + r);
		return r;
	}

	public void findAnswer() {
		try {
			String google = "http://www.google.com/search?q=";
			Document doc = Jsoup.connect(google + URLEncoder.encode(getQuestion(), "UTF-8")).get();
			String pageText = doc.text().toLowerCase();
			
			for (Choice c : getChoices()) {
				Pattern loosePat = Pattern.compile(looseRegex(c.getValue()));
				Matcher looseMat = loosePat.matcher(pageText);
				while (looseMat.find()) c.setLooseHits(c.getLooseHits()+1);

				Pattern strictPat = Pattern.compile(strictRegex(c.getValue()));
				Matcher strictMat = strictPat.matcher(pageText);
				while (strictMat.find()) c.setStrictHits(c.getStrictHits()+1);
			}

			// choose best option
			Choice max = getChoices()[0];

			for (Choice c : getChoices())
				if (c.getStrictHits() > max.getStrictHits()) max = c;

			if (max.getStrictHits() == 0) {
				max = getChoices()[0];
				for (Choice c : getChoices())
					if (c.getTotalHits() > max.getTotalHits()) max = c;
			}
			this.answer = max;
		} catch (Exception e) { e.printStackTrace(); }
	}

	public String toString() {
		String s = getQuestion() + " " + getAnswer().getValue() + "\n";
		for (Choice c : getChoices()) s += "\t" + c.getValue() + ": " + c.getTotalHits() + "\n";
		return s;
	}

	public static void l(Object s) { System.out.println(s); }
	public void searchQuestionInBrowser() {
		String google = "http://www.google.com/search?q=";
		try {
			Desktop.getDesktop().browse(new URI(google + URLEncoder.encode(getQuestion(), "UTF-8")));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	/*
	 * If correct answer can't be found in snippets, check the links
	 * 
	private static ArrayList<String> googlesTop(String query) throws IOException {
String google = "http://www.google.com/search?q=";
		Document doc = Jsoup.connect(google + query).get();
		ArrayList<String> urlResults = new ArrayList<>();
		for (Element el : doc.getElementsByClass("r"))
			urlResults.add(el.getElementsByTag("a").get(0).attr("href"));
		return urlResults;
	}
	*/
}
