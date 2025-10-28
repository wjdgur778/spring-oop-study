package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;

public class MemberApplication {
    public static void main(String[] args) {
        MemberService memberService = new MemberServiceImpl();
        Member member1 = new Member(1L, "Kim", Grade.VIP);
        memberService.join(member1);

        Member member2 = memberService.getMember(1L);
        System.out.println("member1 = " + member1.getName());
        System.out.println("member2 = " + member2.getName());
    }
}

