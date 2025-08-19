package com.boca.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String homePage() {
        return """
            <html>
            <head><title>Spring Boot Demo</title></head>
            <body style='text-align:center; font-family:sans-serif'>
                <h1>Hello from Spring Boot + Minikube!</h1>
                <div>
                    <img src="/images/foto1.jpg" width="200"/>
                    <img src="/images/foto2.jpg" width="200"/>
                    <img src="/images/foto3.jpg" width="200"/>
                    <br/><br/>
                    <img src="/images/foto4.jpg" width="200"/>
                    <img src="/images/foto5.jpg" width="200"/>
                    <img src="/images/foto6.jpg" width="200"/>
                </div>
            </body>
            </html>
        """;
    }
}
