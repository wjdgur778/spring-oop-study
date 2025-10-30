package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class SingleTonTest {

    @Test
    @DisplayName("스프링 없는 순수 DI컨테이너")
    void pureContainer(){
        AppConfig appConfig = new AppConfig();
        //1. 조회 : 호출할때마다 객체 생성
        MemberService memberService = appConfig.memberService();

        //2. 조회 : 호출할때마다 객체 생성
        MemberService memberService1 = appConfig.memberService();

        assertThat(memberService).isNotSameAs(memberService1);
    }
    @Test
    @DisplayName("싱글톤을 사용한 객체 사용")
    void singletonServiceTest(){
//        SingleTonService singleTonService1 = new SingleTonService(); // 외부에서 생성못함
        SingleTonService singleTonService1 = SingleTonService.getInstance();
        SingleTonService singleTonService2 = SingleTonService.getInstance();

        assertThat(singleTonService1).isSameAs(singleTonService2);
    }

    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void springContainer(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        //1. 조회 : 호출할때마다 객체 생성
        MemberService memberService = ac.getBean(MemberServiceImpl.class);

        //2. 조회 : 호출할때마다 객체 생성
        MemberService memberService1 = ac.getBean(MemberServiceImpl.class);

        assertThat(memberService).isSameAs(memberService1);
    }
}
