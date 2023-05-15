package gui;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import Config.Question;
import client.ClientUI;

public class UpdateQuestionFrameController implements Initializable {
	private Question question;
	@FXML
	private Label lblidQuestion;
	@FXML
	private Label lblSubjectInfo;
	@FXML
	private Label lblCourseNameInfo;
	@FXML
	private Label lblQuestionTextInfo;
	@FXML
	private Label lblQuestionNumberInfo;
	@FXML
	private Label lblAuthorInfo;
	@FXML
	private Label lblMessage;

	@FXML
	private Label txtQuestionID;
	@FXML
	private Label txtSubject;
	@FXML
	private Label txtCourseName;
	@FXML
	private TextField txtQuestionNumber;
	@FXML
	private TextArea txtQuestionText;
	@FXML
	private Label txtQuestionAuthor;

	@FXML
	private Button btnBack = null;

	@FXML
	private Button btnSave = null;

	// Loading a specific qustion into the GUI table
	public void loadSelectedQuestion(Question question) {
		this.question = question;
		this.txtQuestionID.setText(question.getId());
		this.txtSubject.setText(question.getSubject());
		this.txtCourseName.setText(question.getCourseName());
		this.txtQuestionText.setText(question.getQuestionText());
		this.txtQuestionNumber.setText(question.getQuestionNumber());
		this.txtQuestionAuthor.setText(question.getLecturer());
	}

	// Back Button functionality
	public void getBackbtn(ActionEvent event) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		Stage primaryStage = new Stage();
		Pane root = loader.load(getClass().getResource("/gui/QuestionBankGUI.fxml").openStream());

		Scene scene = new Scene(root);
		// scene.getStylesheets().add(getClass().getResource("/gui/QuestionBank.css").toExternalForm());
		primaryStage.setTitle("Question Managment Tool");
		primaryStage.setScene(scene);

		primaryStage.setResizable(false); // disable window resize option
		primaryStage.initStyle(StageStyle.UNDECORATED); // disable the menu row on the top of the window
		// we can move window without the menu row
		scene.setOnMousePressed(pressEvent -> scene.setOnMouseDragged(dragEvent -> {
			primaryStage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
			primaryStage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
		}));

		primaryStage.show();
	}

	// Save button functionality
	public void getSavebtn(ActionEvent event) throws Exception {
		if (txtQuestionText.getText().equals("") || txtQuestionNumber.getText().equals("")) {
			lblMessage.setTextFill(Color.color(1, 0, 0));
			lblMessage.setText("[Error] Missing fields");
		} else {
			lblMessage.setTextFill(Color.rgb(0, 102, 0));
			lblMessage.setText("Question Saved Successfully");
			// Create an ArrayList to hold the data for updating the question
			ArrayList<String> sArr = new ArrayList<>();
			sArr.add("UpdateQuestionDataByID");
			sArr.add(question.getId()); // Add the question ID
			sArr.add(txtQuestionText.getText()); // Add the updated question text
			sArr.add(txtQuestionNumber.getText()); // Add the updated question number
			// Send the ArrayList to the server for updating the question data
			ClientUI.chat.accept(sArr);
		}
	}

	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/UpdateQuestionGUI.fxml"));

		Scene scene = new Scene(root);
		// scene.getStylesheets().add(getClass().getResource("/gui/UpdateQuestion.css").toExternalForm());
		primaryStage.setTitle("Question Managment Tool");
		primaryStage.setScene(scene);

		primaryStage.setResizable(false); // disable window resize option
		primaryStage.initStyle(StageStyle.UNDECORATED); // disable the menu row on the top of the window
		// we can move window without the menu row
		scene.setOnMousePressed(pressEvent -> scene.setOnMouseDragged(dragEvent -> {
			primaryStage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
			primaryStage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
		}));

		primaryStage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadSelectedQuestion(QuestionBankController.questionSelected);
	}

}
