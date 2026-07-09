package com.uca.pncparcialfinalhotel;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "jwt.secret=dGVzdC1zZWNyZXQta2V5LXBhcmEtY2ktcHJ1ZWJhcy1ob3RlbC0xMjM=",
        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=PostgreSQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class PncParcialFinalHotelApplicationTests {

    @Test
    void contextLoads() {
        // Si el contexto de Spring levanta correctamente (entidades, security, JWT config), OK
    }

}
