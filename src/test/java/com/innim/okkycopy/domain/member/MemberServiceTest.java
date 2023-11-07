package com.innim.okkycopy.domain.member;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import com.innim.okkycopy.domain.member.dto.request.SignupRequest;
import com.innim.okkycopy.domain.member.dto.response.BriefMemberInfo;
import com.innim.okkycopy.domain.member.entity.Member;
import com.innim.okkycopy.global.error.exception.DuplicateException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    MemberService memberService;

    @Autowired
    MemberService memberServiceBean;

    @Test
    void insertMemberTest() {

        // given
        SignupRequest signupRequest = signupRequest();
        given(memberRepository.save(any(Member.class))).willReturn(null);

        // when
        BriefMemberInfo briefMemberInfo = memberService.insertMember(signupRequest);

        // then
        then(memberRepository).should(times(1)).save(any(Member.class));
        then(memberRepository).should(times(1)).existsById(any(String.class));
        then(memberRepository).should(times(1)).existsByEmail(any(String.class));
        then(memberRepository).shouldHaveNoMoreInteractions();
        assertEquals(briefMemberInfo.getEmail(), signupRequest.getEmail());
        assertEquals(briefMemberInfo.getName(), signupRequest.getName());
        assertEquals(briefMemberInfo.getNickname(), signupRequest.getNickname());

    }

    @Test
    void given_samePassword_then_returnDifferentEncodedPassword() {

        // given
        SignupRequest signupRequest1 = signupRequest();
        SignupRequest signupRequest2 = signupRequest();
        passwordEncoder = new BCryptPasswordEncoder();

        // when
        signupRequest1.encodePassword(passwordEncoder);
        signupRequest2.encodePassword(passwordEncoder);

        // then
        assertNotEquals(signupRequest1.getPassword(), signupRequest2.getPassword());
    }

    @Test
    @Transactional
    void given_duplicatedIdOrEmail_then_duplicateException() {

        // given
        SignupRequest signupRequest1 = signupRequest();
        SignupRequest signupRequest2 = signupRequest();

        // when
        memberServiceBean.insertMember(signupRequest1);

        // then
        assertThrows(DuplicateException.class, () -> {
            memberServiceBean.insertMember(signupRequest2);
        });
    }

    private SignupRequest signupRequest() {
        return SignupRequest.builder()
            .id("test1")
            .password("test1234**")
            .email("test1@test.com")
            .name("testNameOne")
            .nickname("testNickname1")
            .emailCheck(true)
            .build();
    }


}