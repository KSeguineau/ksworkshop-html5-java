package dev.pizzeria;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dev.pizzeria.controller.clients.ClientController;
import dev.pizzeria.controller.clients.ListeClientController;
import dev.pizzeria.controller.livreur.ListeLivreurController;
import dev.pizzeria.controller.livreur.LivreurController;
import dev.pizzeria.controller.pizza.ListePizzaController;
import dev.pizzeria.controller.pizza.PizzaController;
import dev.pizzeria.controller.utils.ConnectionUtils;
import dev.pizzeria.exception.TechnicalException;

/**
 * point d'entrée du site et gestion du serveur web
 * 
 * @author Kevin.s
 *
 */
public class PizzeriaApp {
	private static final Logger SERVICE_LOG = LoggerFactory.getLogger(PizzeriaApp.class);

	public static void main(String[] args) throws Exception {

		try {
			String driverName = ConnectionUtils.getDriverName();
			Class.forName(driverName);
		} catch (ClassNotFoundException e1) {
			SERVICE_LOG.error("impossible de charger le driver", e1);
			throw new TechnicalException("impossible de charger le driver", e1);
		}

		Server server = new Server();

		ServerConnector connector = new ServerConnector(server);
		connector.setPort(8080);
		server.setConnectors(new Connector[] { connector });
		ServletContextHandler context = new ServletContextHandler();
		context.setContextPath("/");

		DefaultServlet defaultServlet = new DefaultServlet();
		ServletHolder holderPwd = new ServletHolder("default", defaultServlet);
		holderPwd.setInitParameter("resourceBase", "./src/main/webapp/static");
		context.addServlet(holderPwd, "/*");

		// Les contrôleurs de l'application

		// ClientController prend la main pour les requêtes /clients
		context.addServlet(ClientController.class, "/clients");
		context.addServlet(ListeClientController.class, "/listeClients");
		context.addServlet(PizzaController.class, "/pizzas");
		context.addServlet(ListePizzaController.class, "/listePizzas");
		context.addServlet(LivreurController.class, "/livreurs");
		context.addServlet(ListeLivreurController.class, "/listeLivreurs");

		server.setHandler(context);
		server.start();
		server.join();
	}
}
