package com.example.restapp;

import static org.junit.Assert.*;

import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import br.net.twome.kd.ExceptionMapper.ResponseEntity;
import br.net.twome.kd.business.Usuario;

public class UsuarioTest extends AbstractTest {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		session().createTable(Usuario.class);
	}
	
	@Test
	public void testPostAsBean() throws Exception {
		Usuario dummy = new Usuario();
		dummy.setNickname("Teste 1");
		
		Response response = target("usuario")
				.request()
				.post(Entity.json(dummy));
		ResponseEntity entity = response.readEntity(ResponseEntity.class);
		assertEquals(401, response.getStatus());
		assertEquals("Nenhuma senha informada!", entity.getError());
		
		dummy.setSenha("123");
		Usuario resposta = target("usuario")
				.request()
				.post(Entity.json(dummy), Usuario.class);
		assertNotNull(resposta);
		
		dummy = new Usuario();
		dummy.setNickname("Teste 2");
		dummy.setSenha("123");
		resposta = target("usuario")
				.request()
				.post(Entity.json(dummy), Usuario.class);
		assertNotNull(resposta);
		
		dummy = new Usuario();
		dummy.setNickname("Teste 1");
		dummy.setSenha("123");
		dummy.setLatitude(10D);
		dummy.setLongitude(11D);
		List<Usuario> list = target("usuario")
				.request().put(Entity.json(dummy), new GenericType<List<Usuario>>(){});
		assertEquals(1, list.size());
		
		dummy = new Usuario();
		dummy.setNickname("Teste 2");
		dummy.setSenha("123");
		dummy.setLatitude(20D);
		dummy.setLongitude(21D);
		list = target("usuario")
				.request().put(Entity.json(dummy), new GenericType<List<Usuario>>(){});
		assertEquals(2, list.size());
		assertEquals("Teste 2", list.get(0).getNickname());
		assertEquals(new Double(20), list.get(0).getLatitude());
		assertEquals("Teste 1", list.get(1).getNickname());
		assertEquals(new Double(10), list.get(1).getLatitude());
	}
	
}
