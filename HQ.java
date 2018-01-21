import java.awt.AWTException;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Scanner;

public class HQ {
	// parameters detailing the portion and structure of screenshot to read in question & choice data
	private static final int QUESTION_WIDTH = 310; // dimensions of the question/choice boxes in pixels
	private static final int QUESTION_HEIGHT = 120;
	private static final int CHOICE_WIDTH = 180;
	private static final int CHOICE_HEIGHT = 37;

	private static final int QUESTION_X = 20; // position of video capture on screen
	private static final int QUESTION_Y = 110;

	private static final int CHOICE_X = 35; // all x-coordinates of the choices are the same
	private static final int CHOICE1_Y = 258;
	private static final int CHOICE2_Y = 313;
	private static final int CHOICE3_Y = 371;

	public static void main(String[] args) {
		
		Scanner scanner = new Scanner(System.in);

		while (true) {
			System.out.print("press enter when ready, type \"quit\" to quit");
			if (scanner.nextLine().equals("quit")) break;
			
		// take screenshots of the game screen
			ScreenShotter shooter = new ScreenShotter();

			Rectangle qLoc = new Rectangle(QUESTION_X, QUESTION_Y, QUESTION_WIDTH, QUESTION_HEIGHT);
			Rectangle c1Loc = new Rectangle(CHOICE_X, CHOICE1_Y, CHOICE_WIDTH, CHOICE_HEIGHT);
			Rectangle c2Loc = new Rectangle(CHOICE_X, CHOICE2_Y, CHOICE_WIDTH, CHOICE_HEIGHT);
			Rectangle c3Loc = new Rectangle(CHOICE_X, CHOICE3_Y, CHOICE_WIDTH, CHOICE_HEIGHT);
			
			try {
				shooter.capture(qLoc, "/private/tmp/q.jpg");
				shooter.capture(c1Loc, "/private/tmp/c1.jpg");
				shooter.capture(c2Loc, "/private/tmp/c2.jpg");
				shooter.capture(c3Loc, "/private/tmp/c3.jpg");
			} catch (AWTException | IOException e) { e.printStackTrace(); }

		// scan them
			OCRScanner ocrScanner = new OCRScanner();

			String q = ocrScanner.scan("/private/tmp/q.jpg");
			String[] choices = {
					ocrScanner.scan("/private/tmp/c1.jpg").trim(),
					ocrScanner.scan("/private/tmp/c2.jpg").trim(),
					ocrScanner.scan("/private/tmp/c3.jpg").trim()
			};
			
		// create a Question and find the answer
			Question question = new Question(q.replaceAll("\n", " "), choices);

			l("OCR results: \n\t" + question.getQuestion());
			for (Choice c : question.getChoices()) l("\t  - " + c.getValue());
			l("Googling...");
			question.searchQuestionInBrowser(); //open the browser for extra manual searching
			question.findAnswer();

			for (Choice c : question.getChoices())
				l("\t  - " + c.getValue() + ":  " + c.getTotalHits() + "\t"
					+ c.getLooseHits() + "\t" + c.getStrictHits());
			l("Best guess: " + question.getAnswer().getValue());
		}
		scanner.close();
	}

	public static void l(Object s) { System.out.println(s); }
}






