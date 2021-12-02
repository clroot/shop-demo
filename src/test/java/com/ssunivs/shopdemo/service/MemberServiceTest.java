package com.ssunivs.shopdemo.service;

import com.ssunivs.shopdemo.entity.Member;
import com.ssunivs.shopdemo.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("회원가입 테스트")
    public void joinTest() {
        //given
        Member member = Member.builder()
                .name("테스트")
                .build();

        //when
        Long saveId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findById(saveId)
                .orElseThrow(EntityNotFoundException::new));
    }

    @Test
    @DisplayName("중복 회원 예외 테스트")
    public void duplicateMemberTest() {
        //given
        Member member1 = Member.builder()
                .name("테스트")
                .build();
        Member member2 = Member.builder()
                .name("테스트")
                .build();

        //when
        Throwable e = assertThrows(IllegalStateException.class, () -> {
            memberService.join(member1);
            memberService.join(member2);
        });

        //then
        assertEquals("이미 존재하는 회원입니다.", e.getMessage());
    }
}