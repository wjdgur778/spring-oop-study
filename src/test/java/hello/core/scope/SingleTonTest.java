package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.*;

public class SingleTonTest {
    @Test
    void singletonBeanFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(SingleTonBean.class);
        SingleTonBean bean1 = ac.getBean(SingleTonBean.class);
        SingleTonBean bean2 = ac.getBean(SingleTonBean.class);
        System.out.println("bean1 = " + bean1);
        System.out.println("bean2 = " + bean2);
        assertThat(bean1).isSameAs(bean2);
        ac.close();
    }


    @Scope("singleton")
    static class SingleTonBean {
        @PostConstruct
        public void init(){
            System.out.println("SingleTonBean.init");
        }

        @PreDestroy
        public void destory(){
            System.out.println("SingleTonBean.destory");
        }
    }

}
