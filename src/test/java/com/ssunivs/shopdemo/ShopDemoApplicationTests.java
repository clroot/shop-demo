package com.ssunivs.shopdemo;

import com.ssunivs.shopdemo.entity.BaseEntity;
import com.ssunivs.shopdemo.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
class ShopDemoApplicationTests {

    @PersistenceContext
    EntityManager em;

    @Test
    void contextLoads() {
    }

    @Test
    @DisplayName("JPA Auditing 테스트")
    public void japAuditingTest() {
        //given
        BaseEntity baseEntity = Member.builder().build();

        //when
        em.persist(baseEntity);

        //then
        assertTrue(LocalDateTime.now().isAfter(baseEntity.getCreatedAt()));
        assertTrue(LocalDateTime.now().isAfter(baseEntity.getUpdatedAt()));
    }
}
