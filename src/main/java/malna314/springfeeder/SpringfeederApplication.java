package malna314.springfeeder;

import malna314.springfeeder.service.Menu;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringfeederApplication {

        public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringfeederApplication.class, args);
        Menu mainMenu = context.getBean(Menu.class);
        mainMenu.menu();
     }

}

