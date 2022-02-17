package eic.tcc.dao;

import org.hibernate.criterion.MatchMode;

/**
 * @author Rafael Castaneda (rafael.ribeiro@cefet-rj.br)
 * <p>
 * Encapsula um critério genérico de busca do tipo 
 * <em>'like'</em> para ser utilizado no método 
 * {@link com.castaware.optimus.api.dao.HibernateDao#retrieveByManyLikes(DaoLike... daoLikes) _DaoHibernate.retrieveByLikeInManyFields}
*/
public class DaoLike 
{
	private String field;
	private Object value;
	private MatchMode matchMode;
	
	/**
	 * Cria um critério genérico de busca do tipo <em>'like'</em>. O modo como 
	 * o <em>'like'</em> é realizado depende do argumento MatchMode, dentro das 
	 * seguintes possibilidades:
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
	 * new DaoLike("CAMPO","termoDeBusca",MatchMode.START);
	 * <p>
	 * @param field Nome do campo do objeto a ser filtrado 
	 * @param value Valor de critério da filtragem 
	 * @param matchMode Modo de filtragem 
	 */
	public DaoLike(String field, Object value, MatchMode matchMode) 
	{
		super();
		this.field = field;
		this.value = value;
		this.matchMode = matchMode;
	}
	
	public DaoLike(String field, Object value) 
	{
		super();
		this.field = field;
		this.value = value;
		this.matchMode = MatchMode.EXACT;
	}

	public String getField() {
		return field;
	}
	
	public Object getValue() {
		return value;
	}

	public MatchMode getMatchMode() {
		return matchMode;
	}
}
