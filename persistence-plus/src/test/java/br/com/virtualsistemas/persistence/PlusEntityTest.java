package br.com.virtualsistemas.persistence;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;

import br.com.virtualsistemas.common.Constants;
import br.com.virtualsistemas.common.validations.CPFValidator.CPF;

@Entity
@SuppressWarnings("serial")
@Table(name = "entity_test")
public class PlusEntityTest implements Entidade, Codificavel<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;

	@CPF(required = true)
	private String cpf;
	@Pattern(regexp = Constants.EMAIL_PATTERN)
	private String email;
	
	@Override
	public Long getCodigo() {
		return codigo;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		return Objects.hash(cpf, email, codigo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlusEntityTest other = (PlusEntityTest) obj;
		return Objects.equals(cpf, other.cpf) && Objects.equals(email, other.email) && Objects.equals(codigo, other.codigo);
	}

	@Override
	public String toString() {
		return "PlusEntityTest [" + (codigo != null ? "id=" + codigo + ", " : "") + (cpf != null ? "cpf=" + cpf + ", " : "")
				+ (email != null ? "email=" + email : "") + "]";
	}
}