package com.ssunivs.shopdemo.service;

import com.ssunivs.shopdemo.domain.*;
import com.ssunivs.shopdemo.exception.NotEnoughStockException;
import com.ssunivs.shopdemo.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("주문 생성 테스트")
    public void orderCreateTest() {
        //given
        int orderCount = 2;
        int price = 1000;
        int stockQuantity = 10;
        Member member = createMember();
        Item item = createItem("아메리카노", price, stockQuantity);

        //when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //then
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        assertEquals(OrderStatus.ORDER, order.getStatus());
        assertEquals(1, order.getOrderItems().size());
        assertEquals(price * orderCount, order.getTotalPrice());
        assertEquals(stockQuantity - orderCount, item.getStockQuantity());
    }

    @Test
    @DisplayName("재고 수량 초과 테스트")
    public void orderTooMuchTest() throws Exception {
        //given
        Member member = createMember();
        Item item = createItem("아메리카노", 1000, 10);

        int orderCount = 11;

        //when
        Throwable e = assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), item.getId(), orderCount);
        });

        //then
        assertEquals("재고가 부족합니다.", e.getMessage());
    }

    @Test
    @DisplayName("주문 취소 테스트")
    public void orderCancelTest() {
        //given
        int orderCount = 2;
        int price = 1000;
        int stockQuantity = 10;
        Member member = createMember();
        Item item = createItem("아메리카노", price, stockQuantity);

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);
        assertEquals(OrderStatus.CANCEL, order.getStatus());
        assertEquals(stockQuantity, item.getStockQuantity());
    }

    private Member createMember() {
        Member member = Member.builder()
                .name("테스트")
                .address(Address.builder().build())
                .build();
        em.persist(member);
        return member;
    }

    private Item createItem(String name, int price, int stockQuantity) {
        Item item = Item.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .build();
        em.persist(item);
        return item;
    }
}