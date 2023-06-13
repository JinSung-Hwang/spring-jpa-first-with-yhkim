package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional // 테스트가 끝나면 Rollback을 진행함
class MemberServiceTest {

  @Autowired MemberService memberService;
  @Autowired MemberRepository memberRepository;
  @Autowired EntityManager em;

  @DisplayName("회원 가입")
  @Test
  @Rollback(value = false) // 실제로 값이 들어가는지 확인하려면 이 어노테이션을 걸고 테스트가 끝나도 롤백이 안되도록 설정함
  public void 회원가입() throws Exception {
    // Given
    Member member = new Member();
    member.setName("Kim");

    // When
    Long savedId = memberService.join(member);
//    em.flush(); // em.flush를 하면 insert into 쿼리가 콘솔창에서 보인다. 이것이 없어도 테스트케이스에서는 문제 없다.

    // Then
    assertThat(member).isEqualTo(memberRepository.findOne(savedId));
  }

  @Test()
  public void 중복_회원_예외() throws IllegalStateException {
    // Given
    Member member1 = new Member();
    member1.setName("kim");;

    Member member2 = new Member();
    member2.setName("kim");;

    // When
    memberService.join(member1);
    Throwable thrown = catchThrowable(() -> { memberService.join(member2); });

    // Then
    assertThat(thrown).isInstanceOf(IllegalStateException.class);
  }
}