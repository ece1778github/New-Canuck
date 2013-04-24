package newcanuck.entity;

public class QuizQuestionRecord {
	private Long id;
	private Long questionId;
	private Long time;
	private String question;
	private String suggestion;
	private String type;
	private String correctness;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCorrectness() {
		return correctness;
	}

	public void setCorrectness(String correctness) {
		this.correctness = correctness;
	}
}
