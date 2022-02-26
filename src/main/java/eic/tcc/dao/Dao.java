package eic.tcc.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.MatchMode;
import org.springframework.jdbc.core.RowMapper;

@SuppressWarnings("rawtypes")
public interface Dao
{
	/**
	 * Força a aplicação de quaisquer modificações pendentes na transação atual.
	 */
	public void flushSession();
	
	public void executeSQL(String sql);
	public List<?> queryHQL(String hqlQuery, Object... values);
	public List<?> queryLimitHQL(String hqlQuery, int limit, Object... values);
	public Map<String,Object> querySingleRowSQL(String sqlQuery);
	public <T> List<T> queryMultiRowSQL(String sqlQuery,RowMapper<T> rowMapper);
	
	/**
	 * Recupera o objeto mais recente pelo seu timestamp - REQUER A COLUNA TIMESTAMP.
	 * <p>
	 * @return Objeto recuperado ou NULL caso a tabela esteja vazia
	 */ 
	public <ENTITY> ENTITY retrieveLast(Class<ENTITY> clazz);
	public <ENTITY> ENTITY retrieveLastBySingleLike(Class<ENTITY> clazz,String field, Object value);
	
	/**
	 * Recupera um objeto pelo seu Id.
	 * <p>
	 * @param id Id do objeto 
	 * @return Objeto recuperado pelo Id ou NULL caso o ID não exista
	 */ 
	public <ENTITY> ENTITY retrieveById(Class<ENTITY> clazz, String id);
	
	/**
	 * Recupera todos os objetos do tipo gerenciado pelo DAO
	 * <p>
	 * @return Lista com todos os objetos disponíveis
	 */         
	public <ENTITY> List<ENTITY> retrieveAll(Class<ENTITY> clazz);

	/**
	 * Recupera todos os objetos do tipo gerenciado pelo DAO, com ordenação.
	 * <p>
	 * @param sortBy Nome do campo a ser usado para ordenação 
	 * @param ascending Direção da ordenação
	 * <p>
	 * @return Lista com todos os objetos disponíveis
	 */         
	public <ENTITY> List<ENTITY> retrieveAll(Class<ENTITY> clazz,String sortBy,boolean ascending);
	
	/**
	 * Busca todos os objetos do tipo gerenciado pelo DAO que correspondam
	 * ao <em>'like'</em> de um campo específico, em modo MatchMode.EXACT
	 * <p>
	 * Exemplo de utilização:
	 * <p>
	 * retrieveByLikeInSingleField("CAMPO","termoDeBusca");
	 * <p>
	 * @param field Nome do campo do objeto a ser filtrado 
	 * @param value Valor de critério da filtragem  
	 * <p>
	 * @return Lista com todos os objetos encontrados
	 */
	public <ENTITY> List<ENTITY> retrieveBySingleLike(Class<ENTITY> clazz, String field, String value);
	
	/**
	 * Busca todos os objetos do tipo gerenciado pelo DAO que correspondam
	 * ao <em>'like'</em> de um campo específico. O modo como o <em>'like'</em> é 
	 * realizado depende do argumento MatchMode, dentro das seguintes possibilidades:
	 * <p>
	 * <ul>
	 * <li><b>MatchMode.EXACT</b> - Testa a correspondência exata ao termo de busca
	 * <li><b>MatchMode.START</b> - Testa correspondências que comecem como o termo de busca
	 * <li><b>MatchMode.END</b> - Testa correspondências que terminem como o termo de busca
	 * <li><b>MatchMode.ANYWHERE</b> - Testa correspondências que contenham o termo de busca
	 * </ul>
	 * <p>
	 * Exemplo de utilização:
	 * <p>
	 * retrieveByLikeInSingleField("CAMPO","termoDeBusca",MatchMode.START);
	 * <p>
	 * @param field Nome do campo do objeto a ser filtrado 
	 * @param value Valor de critério da filtragem 
	 * @param matchMode Modo de filtragem 
	 * <p>
	 * @return Lista com todos os objetos encontrados
	 */
	public <ENTITY> List<ENTITY> retrieveBySingleLike(Class<ENTITY> clazz, String field, String value, MatchMode matchMode);
	public <ENTITY> List<ENTITY> retrieveBySingleLike(Class<ENTITY> clazz, String field, Object value);

	/**
	 * Busca todos os objetos do tipo gerenciado pelo DAO que correspondam
	 * a um conjunto de <em>'likes'</em>. Funciona como o método {@link #retrieveBySingleLike(String field, String value, MatchMode matchMode) retrieveByLikeInSingleField},
	 * mas recebe como argumento um ou mais objetos do tipo {@link com.castaware.optimus.api.dao.query.DaoLike DaoLike},
	 * cada um encapsulando um critério adicional de <em>'like'</em>.
	 * <p>
	 * Exemplo de utilização: 
	 * <p>
	 * retrieveByLikeInManyFields(new DaoLike("CAMPO_1", "termoDeBusca1", MatchMode.EXACT), new DaoLike("CAMPO_2", "termoDeBusca2", MatchMode.START));  
	 * <p>
	 * @return Lista com todos os objetos encontrados
	 */
	public <ENTITY> List<ENTITY> retrieveByManyLikes(Class<ENTITY> clazz, DaoLike... daoLikes);
	
	/**
     * Persiste um objeto. Caso o objeto não exista um novo registro é inserido 
     * no banco de dados. Caso o objeto já exista suas inforamções são atualizadas.
     * A diferenciação entre objetos novos ou pré-existentes é feita consultando-se
     * a o valor do atributo de identificação do objeto.  
     * <p>
     * @param object O objeto a ser persitido
     */	    
    public void persist(Object object);     	    	    
	
    /**
     * Remove um objeto. O ID do objeto deve estar preenchido para que a operação 
     * funcione corretamente.
     * <p>
     * @param object O objeto a ser removido
     */		    
	public void delete(Object object);
	
	/**
	 * Remove todos os objetos.
	 */ 	
	public void deleteAll(Class clazz);
	public void deleteBySingleLike(Class clazz, String field, String value);
	public void deleteBySingleLike(Class clazz, String field, Object value);
}
