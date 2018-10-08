package content.entities;

import java.io.Serializable;
import java.util.List;

public class DirectoryContent implements Serializable {
	private static final long serialVersionUID = 4934379702205247323L;
	String folderName;
	List<String> files;
	List<String> folders;

	public DirectoryContent() {

	}

	public DirectoryContent(String folderName, List<String> files, List<String> folders) {
		this.folderName = folderName;
		this.files = files;
		this.folders = folders;
	}
}
