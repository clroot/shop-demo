package com.ssunivs.shopdemo.repository;

import com.ssunivs.shopdemo.domain.Item;
import com.ssunivs.shopdemo.domain.Member;
import com.ssunivs.shopdemo.service.ItemService;
import com.ssunivs.shopdemo.service.MemberService;
import com.ssunivs.shopdemo.service.OrderService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class OrderRepositoryTest {

    @Autowired
    MemberService memberService;

    @Autowired
    ItemService itemService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    @DisplayName("주문 저장 테스트")
    public void orderSaveTest() {
        //given
        long before = orderRepository.count();
        Member member = createMember("홍길동");
        Item item = createItem("아메리카노", 10);

        //when
        orderService.order(member.getId(), item.getId(), 1);

        //then
        assertEquals(before + 1, orderRepository.count());
    }

    private Member createMember(String name) {
        Member member = Member.builder()
                .name(name)
                .build();
        memberService.join(member);
        return member;
    }

    private Item createItem(String name, int stockQuantity) {
        Item item = Item.builder()
                .name(name)
                .stockQuantity(stockQuantity)
                .build();
        itemService.saveItem(item);
        return item;
    }
}