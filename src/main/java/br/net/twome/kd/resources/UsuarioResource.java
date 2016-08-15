package br.net.twome.kd.resources;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.net.twome.kd.App;
import br.net.twome.kd.business.Usuario;
import br.net.twome.kd.dao.UsuarioDAO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("Usuários")
@Path("/usuario")
public class UsuarioResource {
	
	private UsuarioDAO usuarioDAO = App.container().get(UsuarioDAO.class);
	
	@ApiOperation("Faz login ou cria um novo usuário")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Usuario login(Usuario bean) {
		return usuarioDAO.login(bean);
	}
	
	@ApiOperation("Atualiza a localização atual e recebe a posição de todos como resposta")
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<Usuario> updateLocalizacao(Usuario usuario) {
		return usuarioDAO.updateLocalizacao(usuario);
	}

}
