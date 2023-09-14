package handlers;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.JOptionPane;

import ClientAndServerLogin.LoginFrameController;
import Config.Exam;
import Config.FinishedExam;
import Config.HeadOfDepartment;
import Config.MyFile;
import Config.Question;
import Config.QuestionInExam;
import headofdepartment.HODDashboardFrameController;
import headofdepartment.ViewReportFrameController;
import lecturer.AddQuestionFrameController;
import lecturer.CheckExam_ChooseStudentFrameController;
import lecturer.CheckExam_ReviewAndApproveFrameController;
import lecturer.CreateExam_ReviewFrameController;
import lecturer.LecturerDashboardFrameController;
import lecturer.ManageExam_ChangeTimeFrameController;
import student.ComputerizedExamController;
import student.ManualExamController;
import student.StudentDashboardFrameController;

public class MessageHandler_Client {
	
	private static String userID = null;//


	/**
	 * Handles the received message based on its type.
	 *
	 * @param msg the received message object
	 */
	@SuppressWarnings("unchecked")
	public static void handleMessage(Object msg) {
	    MessageType messageType = getMessageType(msg);
	    if (messageType == null) {
	        return;
	    }

	    switch (messageType) {
	        case STRING:
	            handleStringMessage((String) msg);
	            break;
	        case ARRAY_LIST_STRING:
	            handleStringArrayListMessage((ArrayList<String>) msg);
	            break;
	        case ARRAY_LIST_QUESTION:
	            handleQuestionArrayListMessage((ArrayList<Question>) msg);
	            break;
	        case ARRAY_LIST_QUESTIONINEXAM:
	            handleQuestionInExamArrayListMessage((ArrayList<QuestionInExam>) msg);
	            break;
	        case ARRAY_LIST_HOD:
	            handleHodArrayListMessage((ArrayList<HeadOfDepartment>) msg);
	            break;	
	        case ARRAY_LIST_EXAM:
	            handleExamArrayListMessage((ArrayList<Exam>) msg);
	            break;
	        case MAP_STRING_ARRAYLIST_STRING:
	            handleMapStringKeyArrayListStringValueMessage((Map<String, ArrayList<String>>) msg);
	            break;
	        case MAP_STRING_STRING:
	            handleMapStringStringValueMessage((Map<String, String>) msg);
	            break;
			case ARRAY_LIST_FINISHED_EXAM:
				handleFinishedExamArrayListValueMessage((ArrayList<FinishedExam>) msg);
				break;
			case MY_FILE:
				handleMyFileValueMessage((MyFile) msg);
				break;
	        default:
	            //System.out.println("Message type does not exist");
	            break;
	    }
	}



	/**
	 * Determines the MessageType based on the type of the message object.
	 *
	 * @param msg the message object
	 * @return the corresponding MessageType
	 */
	private static MessageType getMessageType(Object msg) {
		// Check if the message is a String.
		if (msg instanceof String) {
			// If it is, return the corresponding MessageType.
			return MessageType.STRING;
		}
		// Check if the message is an ArrayList.
		else if (msg instanceof ArrayList) {
			// Cast the message to an ArrayList.
			ArrayList<?> arrayList = (ArrayList<?>) msg;
			// Check if the ArrayList is not empty.
			if (!arrayList.isEmpty()) {
				// Get the first element of the ArrayList.
				Object firstElement = arrayList.get(0);
				// Check the type of the first element and return the corresponding MessageType.
				if (firstElement instanceof String) {
					return MessageType.ARRAY_LIST_STRING;
				} else if (firstElement instanceof QuestionInExam) {
					return MessageType.ARRAY_LIST_QUESTIONINEXAM;
				} else if (firstElement instanceof Question && !(firstElement instanceof QuestionInExam)) {
					return MessageType.ARRAY_LIST_QUESTION;
				} else if (firstElement instanceof Exam && !(firstElement instanceof FinishedExam)) {
					return MessageType.ARRAY_LIST_EXAM;
				} else if (firstElement instanceof HeadOfDepartment) {
					return MessageType.ARRAY_LIST_HOD;
				} else if (firstElement instanceof FinishedExam) {
					return MessageType.ARRAY_LIST_FINISHED_EXAM;
				}
			}
		}
		// Check if the message is a Map.
		else if (msg instanceof Map) {
			// Cast the message to a Map.
			Map<?, ?> map = (Map<?, ?>) msg;
			// Check if the Map is not empty.
			if (!map.isEmpty()) {
				// Get the first key and value from the Map.
				Object firstKey = map.keySet().iterator().next();
				Object firstValue = map.get(firstKey);
				// Check the types of the first key and value and return the corresponding MessageType.
				if (firstKey instanceof String && firstValue instanceof ArrayList
						&& ((ArrayList<?>) firstValue).get(0) instanceof String) {
					return MessageType.MAP_STRING_ARRAYLIST_STRING;
				} else if (firstKey instanceof String && firstValue instanceof String) {
					return MessageType.MAP_STRING_STRING;
				}
			}
		} else if (msg instanceof MyFile) {
			return MessageType.MY_FILE;

		}
		// If none of the above conditions are met, return null.
		return null;
	}


	/**
	 * Handles the message containing a string value.
	 *
	 * @param message the string message
	 */
	private static void handleStringMessage(String message) {
        // Handle string messages
    	
    	try {
	    	switch (message) {
	    		case "server is disconnected":
	    			// if get client get the message server is disconnected, get him out of the program
					JOptionPane.showMessageDialog(null, "server is disconnected.", "Connect to Server", JOptionPane.INFORMATION_MESSAGE);
					//System.out.println("exited");
					System.exit(0); 
	    			break;
	    			
	    		case "UserAlreadyLoggedIn":
	    			LoginFrameController.getInstance().userLoginFailed("Error: This user is already connected");
	    			break;
	    			
    			case "UserEnteredWrondPasswwordOrUsername":
    				LoginFrameController.getInstance().userLoginFailed("Error: Wrong Username or Password");
    				break;
	    	}
	    	
    	}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    	}
    	
    }

	/**
	 * Handles the message containing an ArrayList of strings.
	 *
	 * @param arrayList the ArrayList of strings
	 */
    @SuppressWarnings("unchecked")
    private static void handleStringArrayListMessage(ArrayList<?> arrayList) {

            ArrayList<String> arrayListStr = (ArrayList<String>) arrayList;
            String messageType = arrayListStr.get(0);
            try {
	            switch (messageType) {
	                case "UserLoginSucceed":
	                    // Handle UserLoginSucceed message
	                	
		  				// 1 - login As
		  				// 2 - user ID
		  				// 3 - userName
		  				// 4 - user Password
		  				// 5 - user Name
		  				// 6 - user Email
						LoginFrameController.hideCurrentScene(); // hide login frame
						if(arrayListStr.get(1).equals("Lecturer")) { // login as Lecturer
							LecturerDashboardFrameController.start(arrayListStr); // to save the user details in the dashboard controller
						}
						else if(arrayListStr.get(1).equals("Student")) { // login as student
							// 7 - Department Name
							// 8 - DepartmentID
							//System.out.println("Student Login in\n");
							StudentDashboardFrameController.start(arrayListStr);
						}
						else if(arrayListStr.get(1).equals("HeadOfDepartment")) { // login as head of department
							// 7 - Department Name
							// 8 - DepartmentID
							HODDashboardFrameController.start(arrayListStr);
						}
						//System.out.println("logged in succesfully");
						userID = arrayListStr.get(2);
	                    break;
	                    
	                case "MaximunQuestionIdForSelectedSubject":
	                	// 1 - maximum question id for selected subject
	                	AddQuestionFrameController.saveMaxIdOfQuestionInSelectedSubject(arrayListStr.get(1));
	                	
	                	break;
	                	
	                case "MaxExamNumberOfCourse":
	                	// 1 - max exam number in the course
	                	CreateExam_ReviewFrameController.saveIdOfExamInCourse(arrayListStr.get(1));
	                	break;
	                	
	                case "an exam has been closed":
	                	// 1 - examID
	                	try {
		                	if(ComputerizedExamController.getExamId().equals(arrayListStr.get(1))) { // if in the specific exam
		                		ComputerizedExamController.getInstance().submitExam(); // close the exam for the student in the specific exam
								StudentDashboardFrameController.getInstance()
										.displayErrorMessage("The exam you have started has been closed by your lecturer before the time ended!");
		                	}
	                	}catch (NullPointerException e){}
	                	
	                	break;
	                	
	                case "SendToHeadOfDepartmentsThatRequestRecieved":
	                	// 1 - Head of department ID
	                	try {
		                	if(userID.equals(arrayListStr.get(1))) { // if the current client is the head of department with the correct ID
								HODDashboardFrameController.getInstance()
										.displaySuccessMessage("New exam time change request recieved!");
		                		// send pop up message that Change time request recieved!!!!!
		                		
		                	}
                		}catch (NullPointerException e){}
	                	
	            
	                	break;
	                	
	                case "LoadAllRequestsForHOD":
	                	arrayListStr.remove(0);
	                	HODDashboardFrameController.getInstance().loadRequestsFromDB(arrayListStr);          	
	                	break;
	                	
	                case "RequestForChangeTimeAcceptedByHodToLecturer":
						// 1 - lecturer ID
						// 2 - exam ID
						// 3 - exam Duration to Add
						// 4 - txt Message from hod to lecturer
	                	
	                	if(userID.equals(arrayListStr.get(1))) {
	                		LecturerDashboardFrameController.getInstance().displaySuccessMessage("Your request for Change time of " + arrayListStr.get(3)
	                		+ " minutes on exam (" + arrayListStr.get(2) + ") confirmed!\nHead Of Department's message: " + arrayListStr.get(4));
	                	}
	                	
	                	break;
	                	
	                case "RequestForChangeTimeAcceptedByHodToStudent":
	                	
						// 1 - exam ID
						// 2 - exam Duration to Add
	                	if(ComputerizedExamController.getExamId().equals(arrayListStr.get(1))) { // if in the specific exam
	                		ComputerizedExamController.getInstance().updateExamDuration(arrayListStr.get(1), Integer.parseInt(arrayListStr.get(2)));
	                	}
	                	
	                	break;
	                	
	                case "RequestForChangeTimeDeniedByHodToLecturer":
	                	
						// 1 - lecturer ID
						// 2 - exam ID
						// 3 - exam Duration to Add
						// 4 - txt Message from hod to lecturer
	                	
	                	if(userID.equals(arrayListStr.get(1))) {
							LecturerDashboardFrameController.getInstance()
									.displaySuccessMessage("Your request for Change time of " + arrayListStr.get(3)
											+ " minutes on exam (" + arrayListStr.get(2)
											+ ") DENIED!\nHead Of Department's message: " + arrayListStr.get(4));
	                	}
	                	
	                	break;
	                	
	                case "NewGradeIsAvailableForStudent":
	                	
						// 1 - Student ID
						// 2 - Grade
						// 3 - Course Name
	                	// 4 - lecturer Name
	                	// 5 - Comments for Student
	                	// 6 - Comment For New Grade
	                	
	                	
	                	String status = "";
	                	if(Double.parseDouble(arrayListStr.get(2)) >= 55){
	                		status = "passed";
	                	}
	                	else {
	                		status = "failed";
	                	}
	                	
	                	if(userID.equals(arrayListStr.get(1))) {
	                		StudentDashboardFrameController.getInstance().displaySuccessMessage("A new grade has been recieved:\n"
	                				+ "Lecturer " + arrayListStr.get(4) + " entered a new grade (" + arrayListStr.get(2) + " - " + status + ") "
	                				+ "for the exam in the course: " + arrayListStr.get(3) + "\n" + arrayListStr.get(5) + "\n" + arrayListStr.get(6));
	                	}
	                	
	                	break;
	                	
	                case "Exam approved send message to lecturer":
	                	// 1 - student's email
						CheckExam_ChooseStudentFrameController.getInstance().displaySuccessMessage(
								"Exam approved! Message will be send to the student's email: " +
	                			arrayListStr.get(1));
	                	break;
	                	
	                case "LoadStatisticsOfExamByIdForLecturer":
	                	// 0 - Identifying string
	                	// 1 - totalExminees
	                	// 2 - submittedOnTime
	                	// 3 - notSubmittedOnTime
	                	try {
	                	LecturerDashboardFrameController.getInstance().set_StatisticsOfExam(arrayListStr.get(1), arrayListStr.get(2), arrayListStr.get(3)); 	
	    	        	} catch (Exception e) {}
	                	break;
	                	
	                case "LoadToListOfDataForHOD_report":
	                	// 0 - Identifying string
	                	// 1.. - the options to choose list
	                	
	                	arrayListStr.remove(0); // Removing identifying string

	                	HODDashboardFrameController.getInstance().loadAllOptionsForChosenReport(arrayListStr);
	                	
	                	break;
	                	
	                case "LoadToGradesToChart_HOD":
	                	// 0 - Identifying string
	                	// 1.. - grades
	                	
	                	arrayListStr.remove(0); // Removing identifying string
	                	
	                	ViewReportFrameController.getInstance().loadAllGradesToChart(arrayListStr);
	                	
	                	
	                	break;
	                	
	                case "LoadAllSelectedGeneralInfo_HOD":
	                	// 0 - Identifying string
	                	// 1.. - general info selected
	                	
	                	arrayListStr.remove(0); // Removing identifying string
	                	
	                	HODDashboardFrameController.getInstance().loadAllInfoForChosenTypeInfo(arrayListStr);
	                	
	                	break;
	                	
	            }       
	            
            }catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            }
	}

	/**
	 * Handles the message containing an ArrayList of Question objects.
	 *
	 * @param questionList the ArrayList of Question objects
	 */
    private static void handleQuestionArrayListMessage(ArrayList<Question> questionList) {
        // Handle ArrayList<Question> messages
    	
    	String messageType = questionList.get(0).getId();

    	switch (messageType) {
    		case "LoadQuestionsFromDB":
    			// Handle LoadQuestionsFromDB message
    			questionList.remove(0); // remove the first question (the question that identified)
				LecturerDashboardFrameController.getInstance().loadArrayQuestionsToTable_ManageQuestions(questionList);
				//System.out.println("The questions succesfully loaded from the DB to the table.");
				break;
				
    		case "LoadQuestionsFromDB_CreateExamTable":
    			
    			questionList.remove(0); // remove the first question (the question that identified)
    			LecturerDashboardFrameController.getInstance().loadArrayQuestionsToTable_CreateExam(questionList);
    			//System.out.println("The questions succesfully loaded from the DB to the create exam table.");
    			break;
    	} 	
    }

	/**
	 * Handles the message containing a Map of String keys to ArrayList of String values.
	 *
	 * @param map the Map of String keys to ArrayList of String values
	 */
    private static void handleMapStringKeyArrayListStringValueMessage(Map<String, ArrayList<String>> map) {
        // Handle Map<String, ArrayList<String>> messages
    		
    	if(map.containsKey("HashMapWithLecturerSubjectsAndCourses")) {
			map.remove("HashMapWithLecturerSubjectsAndCourses");
			LecturerDashboardFrameController.loadLecturerSubjectsAndCourses(map);
    	}

    }

	/**
	 * Handles the message containing a Map of String to String values.
	 *
	 * @param map the Map of String to String values
	 */
    private static void handleMapStringStringValueMessage(Map<String, String> map) {
        // Handle the Map<String, String> here
    	
    	if(map.containsKey("HashMapWithSubjects_names_ids")) {
			map.remove("HashMapWithSubjects_names_ids");
			LecturerDashboardFrameController.loadAllSubjectsFromDB(map);
    	}
    	
    	else if(map.containsKey("HashMapWithCourses_names_ids")) {
			map.remove("HashMapWithCourses_names_ids");
			LecturerDashboardFrameController.loadAllCoursesFromDB(map);
    	}
    	
    }
	/**
	 * Handles the message containing an ArrayList of Exam objects.
	 * First object is the message
	 * @param examList the ArrayList of Exam objects
	 */
	//This method is to get Computerized Exams to the client
	private static void handleExamArrayListMessage(ArrayList<Exam> examList) {
		//System.out.println("Reached the handleExamArrayListMessage | ClientHandler");
		// Handle ArrayList<Exam> messages
		// You're supposed to call a function within the Controller class and pass the List to it to update the fields
    	String messageType = examList.get(0).getExamID();
    	try {
	    	switch (messageType) {
	    	
	    		case "loadActiveExamsIntoLecturerTable":
	    			examList.remove(0); // removing the identifying exam
	    			LecturerDashboardFrameController.getInstance().loadAllActiveExamsToTable(examList);
					break;
					
	    		case "loadInActiveExamsIntoLecturerTable":
	    			examList.remove(0); // removing the identifying exam
	    			LecturerDashboardFrameController.getInstance().loadAllInActiveExamsToTable(examList);
					break;
				case "computerizedExamsForStudentTable":
					//System.out.println("Reached the Client Handler");
					examList.remove(0);
					StudentDashboardFrameController.getInstance().loadComputerizedExamsIntoTable(examList);
					break;
				case "LoadAllExamsToCheckForLecturer":
					examList.remove(0);
					LecturerDashboardFrameController.getInstance().loadAllExamsToCheckInTable(examList);
					break;
				case "manualExamsForStudentTable":
					examList.remove(0);
					StudentDashboardFrameController.getInstance().loadManualExamsIntoTable(examList);
					break;
	    	}
    	} catch (IndexOutOfBoundsException e) {}
		
	}


	/**
	 * Handles the message containing an ArrayList of QuestionInExam objects.
	 *
	 * @param questionInExamList the ArrayList of QuestionInExam objects
	 */
	private static void handleQuestionInExamArrayListMessage(ArrayList<QuestionInExam> questionInExamList) {
		// Handle ArrayList<QuestionInExam> messages

		//System.out.println("Reached handleQuestionInExamArrayListMessage | ClientHandler");
		//System.out.println(questionInExamList + " Inside Handler!");
    	String messageType = questionInExamList.get(0).getId();
		questionInExamList.remove(0);

	    	switch (messageType) {
	    	
	    		case "questionsByExamIdForClient":
					ComputerizedExamController.getInstance().loadExamQuestions(questionInExamList);
					break;
					
	    		case "questionsInExamForLecturerApproval":
	    			CheckExam_ReviewAndApproveFrameController.saveQuestionsInExam(questionInExamList);
					break;
	    	} 

	}

	/**
	 * Handles the message containing an ArrayList of HeadOfDepartment objects.
	 *
	 * @param hodList the ArrayList of HeadOfDepartment objects
	 */
	private static void handleHodArrayListMessage(ArrayList<HeadOfDepartment> hodList) {
		//System.out.println("Reached the handleHodArrayListMessage | ClientHandler");
		// Handle ArrayList<HeadOfDepartment> messages

    	String messageType = hodList.get(0).getId();

	    	switch (messageType) {
	    	
	    		case "LoadRelevantHodForLecturer":
	    			hodList.remove(0); // removing the identifying exam
	    			ManageExam_ChangeTimeFrameController.getInstance().loadHODsForLecturer(hodList);
					break;
					
	    	}
		
	}

	/**
	 * Handles the message containing an ArrayList of FinishedExam objects.
	 *
	 * @param finishedExam the ArrayList of FinishedExam objects
	 */
	private static void handleFinishedExamArrayListValueMessage(ArrayList<FinishedExam> finishedExam){
		//Handle ArrayList<FinishedExam> messages
		//System.out.println("Reached handleFinishedExamValueMessage | Server Handler");
		String messageType = finishedExam.get(0).getExamID();
		try{
			
			switch (messageType){
			
				case "LoadAllStudentsFinishedExamsToCheckForLecturer":
					// 1 - finished exams array list
					finishedExam.remove(0);
					CheckExam_ChooseStudentFrameController.getInstance().loadAllFinishedExamsOfSelectedExam(finishedExam);
					break;
					
				case "studentGradesForClient":
					finishedExam.remove(0);
					StudentDashboardFrameController.getInstance().loadStudentGradesIntoTable(finishedExam);
					break;
					
				case "Load all finished exams grades and info for lecturer":
					finishedExam.remove(0);
					LecturerDashboardFrameController.loadStudentsGradesOfLecturer(finishedExam);
					break;
			}
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Handles the message containing a file value.
	 *
	 * @param file the MyFile object representing the file
	 */
	private static void handleMyFileValueMessage(MyFile file){
		//System.out.println("Reach handleMyFileValueMessage | Client Handler");
		ManualExamController.getInstance().saveExamToComputer(file);
	}
}
