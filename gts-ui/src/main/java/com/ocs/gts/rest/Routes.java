package com.ocs.gts.rest;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class Routes extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        rest("/zeg")
                .get("/xml").to("direct:hello2").produces("application/json");

        from("direct:hello2")
                .transform().constant("Hello World");
    }
}
