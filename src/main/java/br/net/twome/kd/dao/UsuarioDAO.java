package br.net.twome.kd.dao;

import java.util.List;

import org.joda.time.DateTime;
import org.mentabean.BeanException;
import org.mentabean.jdbc.QueryBuilder;
import org.mentabean.jdbc.QueryBuilder.Alias;
import org.mentabean.sql.conditions.NotEquals;

import br.net.twome.kd.business.Usuario;
import br.net.twome.kd.db.types.BetweenJoda;

public class UsuarioDAO extends AbstractDAO {

	public Usuario login(Usuario usuario) {
		validaUsuario(usuario, true);
		return usuario;
	}
	
	public List<Usuario> updateLocalizacao(Usuario usuario) {
		validaUsuario(usuario, false);
		usuario.setDataUltimaAtualizacao(new DateTime());
		session.update(usuario);
		
		QueryBuilder builder = session.buildQuery();
		Alias<Usuario> aliasUsu = builder.aliasTo(Usuario.class, "usu");
		Usuario usu = aliasUsu.pxy();
		aliasUsu.setReturnMinus(usu.getSenha(), usu.getDataUltimaAtualizacao());
		DateTime time = new DateTime().minusHours(8);
		
		List<Usuario> list = builder.selectFrom(aliasUsu)
				.where()
				.clause(usu.getDataUltimaAtualizacao())
				.condition(new BetweenJoda(time, null))
				.and()
				.clause(usu.getNickname())
				.condition(new NotEquals(usuario.getNickname()))
				.orderBy()
				.desc(aliasUsu, usu.getDataUltimaAtualizacao())
				.executeQuery();
		list.add(0, usuario);
		return list;
	}
	
	private void validaUsuario(Usuario usuario, boolean insertIfNoExists) {
		if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
			throw new BeanException("Nenhuma senha informada!");
		}
		Usuario bd = session.createBasicInstance(usuario);
		if (!session.load(bd)) {
			if (insertIfNoExists) {
				session.insert(usuario);
				return;
			}else {
				throw new BeanException("Usuário não encontrado!");
			}
		}
		if (!usuario.getSenha().equals(bd.getSenha())) {
			throw new BeanException("Senha não confere!");			
		}
	}
}
