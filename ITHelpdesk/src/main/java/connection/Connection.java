package connection;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Connection {
	@Id
	private Integer id;
	private String url;
	private String login;
	private String password;
	private String description;
	
	@Override
	public String toString() {
		return "Connection [url=" + url + ", login=" + login + ", description=" + description + "]";
	}
	
}
