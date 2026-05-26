package util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class JPAInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println(">>> Inicializando JPA al arrancar...");
        try {
            JPAUtil.getEntityManager().close();
            System.out.println(">>> JPA listo.");
        } catch (Exception e) {
            System.err.println(">>> Error al inicializar JPA: " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
