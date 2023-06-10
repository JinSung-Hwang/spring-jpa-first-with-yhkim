package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.hibernate.tool.schema.internal.exec.GenerationTargetToDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.engine.AttributeDefinition;


// junit5을 쓰면 @RunWith(SpringRunner.class)에서 @@ExtendWith(SpringExtension.class)로 바뀌었다.
@ExtendWith(SpringExtension.class) // junit에서 spring과 관련된 테스트를 진행할 것이라고 말해주는 어노테이션이다.
@SpringBootTest // springBoot를 실제 구동시켜서 테스트를 진행하는 것 어노테이션이다.
class MemberRepositoryTest {

  @Autowired MemberRepository memberRepository;

  @Test
  @Transactional // 테스트가 하나의 트랜잭션으로 진행될 수 있도록함, 테스트가 끝나면 자동으로 rollback을 진행함
  @Rollback(false) // test가 끝나도 rollback을 진행하고 싶지 않을때 사용함
  public void testMember() throws Exception {
    // Given
    Member member = new Member();
    member.setUsername("memberA");

    //when
    Long savedId = memberRepository.save(member);
    Member findMember = memberRepository.find(savedId);

    //then
    Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
    Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    Assertions.assertThat(findMember).isEqualTo(member);
  }

}