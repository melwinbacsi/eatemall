package malna314.springfeeder;

import malna314.springfeeder.services.Menu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringfeederApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringfeederApplication.class, args);
        Menu mainMenu = (Menu) context.getBeanFactory();
        mainMenu.menu();

    }

}

