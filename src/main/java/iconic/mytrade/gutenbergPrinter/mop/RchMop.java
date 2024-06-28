package iconic.mytrade.gutenbergPrinter.mop;

public class RchMop {
	private String description = "";
	private int code = 0;
	
	RchMop (String description, int code)
	{
		this.setDescription(description);
		this.setCode(code);
	}

	String getDescription() {
		return description;
	}

	private void setDescription(String description) {
		this.description = description;
	}

	int getCode() {
		return code;
	}

	private void setCode(int code) {
		this.code = code;
	}
}
