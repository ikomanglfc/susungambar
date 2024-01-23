package com.triyakom.susun.config;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.Loader;
import com.mitchellbosecke.pebble.loader.ServletLoader;
import com.mitchellbosecke.pebble.spring4.PebbleViewResolver;
import com.mitchellbosecke.pebble.spring4.extension.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.ServletContext;

@Configuration
public class PebbleConfig {

    @Autowired
    private ServletContext servletContext;

    @Bean
    public SpringExtension springExtension() {
        return new SpringExtension();
    }

    @Bean
    public PebbleEngine pebbleEngine() {
        return new PebbleEngine.Builder().loader(this.templateLoader()).extension(this.springExtension()).build();
    }

    @Bean
    public Loader templateLoader(){
        return new ServletLoader(servletContext);
    }

    @Bean
    public ViewResolver viewResolver() {
        PebbleViewResolver viewResolver = new PebbleViewResolver();
        viewResolver.setPrefix("/pages/");
        viewResolver.setSuffix(".peb");
        viewResolver.setPebbleEngine(this.pebbleEngine());
        return viewResolver;
    }
}
