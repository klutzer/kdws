package br.net.twome.kd;

import java.sql.Connection;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.mentabean.BeanConfig;
import org.mentabean.BeanManager;
import org.mentabean.BeanSession;
import org.mentabean.util.PropertiesProxy;
import org.mentacontainer.Container;
import org.mentacontainer.Scope;
import org.mentacontainer.impl.MentaContainer;
import org.mentacontainer.impl.SingletonFactory;

import br.net.twome.kd.business.Usuario;
import br.net.twome.kd.dao.UsuarioDAO;
import br.net.twome.kd.db.ConnectionFactory;
import br.net.twome.kd.db.ConnectionManager;
import br.net.twome.kd.db.PostgreConnectionManager;
import br.net.twome.kd.db.types.DBTypes;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;

@ApplicationPath("/api/*")
public class App extends ResourceConfig {

	private static Container container;
	private final BeanManager beanManager;
	
	public App(ConnectionManager cm) {
		
		container = new MentaContainer();
		beanManager = new BeanManager();
		
		//Mapping recursively by package name
		packages(getClass().getPackage().getName());
		
		setUpSwagger();
		beans();
		ioc(cm);
	}
	
	public App() {
		this(new PostgreConnectionManager());
	}
	
	public static Container container() {
		return container;
	}
	
	public static void releaseAndShutdown() {
		ConnectionManager cm = container.get(ConnectionManager.class);
		container.clear(Scope.THREAD);
		container.clear(Scope.SINGLETON);
		cm.shutdown();
	}
	
	private void ioc(ConnectionManager cm) {
		container.ioc(ConnectionManager.class, new SingletonFactory(cm));
		container.ioc(BeanManager.class, new SingletonFactory(beanManager));
		container.ioc(Connection.class, new ConnectionFactory(), Scope.THREAD);
		container.ioc(BeanSession.class, cm.getSessionClass(), Scope.THREAD)
			.addConstructorDependency(BeanManager.class)
			.addConstructorDependency(Connection.class);
		container.autowire(BeanSession.class);

		//Here add your own IoC settings...
		container.ioc(UsuarioDAO.class, UsuarioDAO.class);
		//...
	}
	
	private void beans() {
		
		//Here add the mapping for your beans
		
		Usuario bean = PropertiesProxy.create(Usuario.class);
		BeanConfig config = new BeanConfig(Usuario.class, "usuarios")
				.pk(bean.getNickname(), DBTypes.STRING)
				.field(bean.getSenha(), DBTypes.STRING)
				.field(bean.getLatitude(), DBTypes.DOUBLE)
				.field(bean.getLongitude(), DBTypes.DOUBLE)
				.field(bean.getDataUltimaAtualizacao(), DBTypes.JODA_DATETIME);
		
		beanManager.addBeanConfig(config);
	}
	
	private void setUpSwagger() {
		
		registerClasses(SwaggerSerializers.class, ApiListingResource.class);
		
		io.swagger.jaxrs.config.BeanConfig conf = new io.swagger.jaxrs.config.BeanConfig();
		conf.setTitle("KdTodoMundo API");
		conf.setDescription("Documentação da API KdTodoMundo");
        conf.setVersion("v1");
        conf.setResourcePackage("br.net.twome.kd.resources");
        conf.setPrettyPrint(true);
        conf.setBasePath("/KdTodoMundo/api");
        conf.setScan(true);
	}
	
}
