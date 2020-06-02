package application;

import java.util.ArrayList;
import graph.Graph;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

public class ShowDetailController {
	@FXML
	private TextArea forwardTxt;
	@FXML
	private TextArea loopTxt;
	@FXML
	private TextArea nonTouchingTxt;
	@FXML
	private TextArea deltaTxt;

	private Graph graph;
	private ArrayList<String> forwardPaths;
	private ArrayList<String> loops;
	private ArrayList<ArrayList<String>> nonTouchingLoops;
	private double Delta;
	private double[] deltaForward;

	@FXML
	public void initialize() {
		graph = Controller.getGraph();
		forwardPaths = Controller.getForward();
		loops = Controller.getloops();
		nonTouchingLoops = Controller.getNontouching();
		Delta = Controller.getDelta();
		deltaForward = Controller.getDeltaForward();
		for (int i = 0; i < forwardPaths.size(); i++) {
			String[] ss = forwardPaths.get(i).split("-");
			String s = "";
			for (int x = 0; x < ss.length; x++) {
				s = s + (Integer.valueOf(ss[x]) + 1);
				if (x < ss.length - 1) {
					s = s + "-";
				}
			}
			forwardTxt
					.appendText("M" + (i + 1) + " = " + s + " = " + graph.pathGain(graph, forwardPaths.get(i)) + "\n");
		}
		for (int i = 0; i < loops.size(); i++) {
			String[] ss = loops.get(i).split("-");
			String s = "";
			for (int x = 0; x < ss.length; x++) {
				s = s + (Integer.valueOf(ss[x]) + 1);
				if (x < ss.length - 1) {
					s = s + "-";
				}
			}
			loopTxt.appendText("L" + (i + 1) + " = " + s + " = " + graph.pathGain(graph, loops.get(i)) + "\n");
		}
		for (int i = 0; i < nonTouchingLoops.size(); i++) {
			ArrayList<String> arr = nonTouchingLoops.get(i);
			for (int j = 0; j < arr.size(); j++) {
				String[] ss = arr.get(j).split("-");
				String s = "";
				for (int x = 0; x < ss.length; x++) {
					s = s + (Integer.valueOf(ss[x]) + 1);
					if (x < ss.length - 1) {
						s = s + "-";
					}
				}
				if (j < arr.size() - 1) {
					nonTouchingTxt.appendText(s + " & ");
				} else {
					nonTouchingTxt.appendText(s + "\n");
				}
			}
		}
		deltaTxt.appendText("Delta = " + Delta + "\n");
		for (int i = 0; i < deltaForward.length; i++) {
			deltaTxt.appendText("Delta(" + (i + 1) + ") = " + deltaForward[i] + "\n");
		}
	}
}
