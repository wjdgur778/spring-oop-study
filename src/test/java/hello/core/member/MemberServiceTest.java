package hello.core.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {

    MemberService memberService = new MemberServiceImpl();

    @Test
    void join(){
        Member joinMember = new Member(1L, "kim",Grade.VIP);

        memberService.join(joinMember);
        Member findMember = memberService.getMember(1L);

        Assertions.assertThat(joinMember).isEqualTo(findMember);

    }
}
