package malna314.springfeeder;

import malna314.springfeeder.service.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringfeederApplication implements CommandLineRunner {

    private Menu menu;

    @Autowired
    public void setMenu(Menu menu){
        this.menu = menu;
    }

        public static void main(String[] args) {
        SpringApplication.run(SpringfeederApplication.class, args);;
     }

    @Override
    public void run(String... args) throws Exception {
        menu.menu();
    }
}

