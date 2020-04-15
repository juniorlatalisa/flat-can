package br.com.virtualsistemas.persistence.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.persistence.PersistenceException;
import javax.sql.DataSource;

/**
 * Auxiliar na criação de conexão com o bando de dados.
 * <p>Atenção: Dê preferência em utilizar conexões fornecidas por um {@link DataSource}.
 * <p>Exemplo:<pre class="prettyprint linenums"><code class="language-java"
 * >Connection connection = builder
 *	.addDriver("org.apache.derby.jdbc.EmbeddedDriver")
 *	.setUrl("jdbc:derby:target/VSPersistencia;create=true;")
 * .getConnection();</code></pre>
 * Favor consultar a documentação do driver para maiores informações dos valores possíveis para a
 * URL e propriedades.
 * @author Junior Latalisa
 * @since 3.0
 * @see DriverManager
 */
public class ConnectionBuilder {
	
	private String url;
	private Properties properties;
	
	/**
	 * Registrar a classe do driver.
	 * @param driver Classe do driver.
	 * @return A própria instância desse builder.
	 * @see DriverManager#registerDriver(Driver)
	 */
	public ConnectionBuilder addDriver(Class<? extends Driver> driver) {
		try {
			DriverManager.registerDriver(driver.newInstance());
			return this;
		} catch (Exception e) {
			throw new PersistenceException(e);
		}
	}
	
	/**
	 * Registrar a classe do driver.
	 * @param driver Classe do driver.
	 * @return A própria instância desse builder.
	 * @see #addDriver(Class)
	 */
	@SuppressWarnings("unchecked")
	public ConnectionBuilder addDriver(String driver) {
		try {
			return addDriver((Class<? extends Driver>) Class.forName(driver));
		} catch (ClassNotFoundException e) {
			throw new PersistenceException(e);
		}
	}
	
	/**
	 * Retorna a URL de conexão com o banco de dados.
	 * @return URL de conexão com o banco de dados.
	 */
	public String getUrl() {
		return url;
	}
	
	/**
	 * Atribui a URL de conexão com o banco de dados.
	 * @param url URL de conexão com o banco de dados.
	 * @return A própria instância desse builder.
	 */
	public ConnectionBuilder setUrl(String url) {
		this.url = url;
		return this;
	}
	
	/**
	 * Retorna as propriedades de conexão com o banco de dados.
	 * @return Propriedades de conexão com o banco de dados.
	 */
	public Properties getProperties() {
		return getProperties(false);
	}
	
	protected Properties getProperties(boolean create) {
		if ((create) && (properties == null)) {
			properties = createProperties();
		}
		return properties;
	}
	
	protected Properties createProperties() {
		return new Properties();
	}

	/**
	 * Substituir as propriedades atuais pelas novas. Para adicionar favor utilzar o método
	 * {@link #addProperties(Properties)} ou {@link #setProperty(String, String)}
	 * @param properties Propriedades de conexão com o banco de dados.
	 * @return A própria instância desse builder.
	 */
	public ConnectionBuilder setProperties(Properties properties) {
		this.properties = properties;
		return this;
	}
	
	/**
	 * Adicionar as propriedades.
	 * @param properties Propriedades de conexão com o banco de dados.
	 * @return A própria instância desse builder.
	 * @see Properties#putAll(java.util.Map)
	 */
	public ConnectionBuilder addProperties(Properties properties) {
		getProperties(true).putAll(properties);
		return this;	
	}
	
	/**
	 * Adicionar a propriedade.
	 * @param key Chave da propriedade.
	 * @param value Valor da propriedade.
	 * @return A própria instância desse builder.
	 * @see Properties#setProperty(String, String)
	 */
	public ConnectionBuilder setProperty(String key, String value) {
		getProperties(true).setProperty(key, value);
		return this;
	}
	
	/**
	 * Atribui null às propriedades de conexão com o banco de dados.
	 * @return A própria instância desse builder.
	 */
	public ConnectionBuilder clearProperties() {
		properties = null;
		return this;
	}
	
	/**
	 * Atribuir o usuário às propriedades de conexão com o banco de dados.
	 * @param user Usuário.
	 * @return A própria instância desse builder.
	 */
	public ConnectionBuilder setUser(String user) {
		return setProperty("user", user);
	}
	
	/**
	 * Atribuir a senha às propriedades de conexão com o banco de dados.
	 * @param password Senha.
	 * @return A própria instância desse builder.
	 */
	public ConnectionBuilder setPassword(String password) {
		return setProperty("password", password);
	}
	
	/**
	 * Conexão com o banco de dados conforme os parâmetros do builder.
	 * @return Conexão com o banco de dados.
	 * @see DriverManager#getConnection(String, Properties)
	 */
	public Connection build() {
		try {
			return DriverManager.getConnection(getUrl(), getProperties(false)); 
		} catch (SQLException e) {
			throw new PersistenceException(e);
		}
	}
}