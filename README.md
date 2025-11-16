  **📘Spring은 왜 객체지향적인가?** 

**변경되는 요구사항**을 좋은 객체 지향 설계의 5가지 원칙을 준수하면서 순수 java 코드로 구현해보고 객체지향을 이해하는 동시에,
spring이 어떻게 객체지향 설계를 돕는지를 학습 및 복습하기 위한 공간입니다.

## 🎯학습 목표
- 객체지향 프로그래밍(OOP)의 핵심 개념(추상화, 캡슐화, 상속, 다형성)을 코드로 이해하기.

- Spring이 OOP 철학을 어떻게 구현하고 있는지 탐구하기.

---

### [객체지향이 왜 필요한가?]
본인의 "객체지향이 왜 필요한가?"에 대한 생각은

``현대적인 서비스는 계속해서 요구사항이 변화하고 확장되어나가기 때문에 변경에 유리해야하고 확장에 
유리해야한다. 또한 이러한 서비스를 구현하기 위해서는 여러 팀원들의 유기적인 협업을 통해 구현되어야 한다.
이때, 객체지향적 설계를 하면 변경에 유리하고, 팀원들은 각자가 맡은 책임만을 구현하며 서비스를 만들어 나갈 수 있다.``

이는 "왜 Spring인가?"로 연결된다.

Spring은 순수 Java코드만으로 구현하기 어렵고 복잡한 부분들을 
비교적 편하게 구현할 수 있도록 미리 코드를 설계해 놓아서 개발자들의 객체지향 설계를 돕는다.

---


### [SOLID 원칙]
좋은 객체지향 설계를 위해서 따라야 하는 5가지 설계원칙을 의미한다.

**SRP : 단일 책임의 원칙**
- 하나의 클래스는 하나의 책임을 가져야 한다.
- 변경사항이 있을때 하나의 클래스는 하나의 지점만 고치면 될 수 있게 한 것이 이를 잘 지킨 것이라 할 수 있다. 
 &nbsp

**OCP : 개방 폐쇄의 원칙**
- 확장에는 열려있고, 변경에는 닫혀있어야 한다.
- 다형성을 활용해서 (ex, 인터페이스를 추가하는 식으로) 구현할 수 있다.
- 역할과 구현을 분리해 놓으면 구현체만 갈아끼우는 식(변경에 닫혀있고)으로 새로운 기능을 구현할 수 있다.(확장에 열려있다.)
- 하지만 여기서 문제점은 클라이언트 코드에서 구현체를 갈아끼우는 것 자체가 변경에 닫혀있다고 할 수 없다.  아래의 코드를 보면,
  &nbsp
```java
// 이와 같이 사용하면  클라이언트 쪽에서 직접 구현체를 변경해야 한다는 것이다.(변경에 닫혀있지 않음)
class PayController {
//PaymentService = new DebitServiceImpl();
    PaymentService paymentService= new PayServiceImpl();
}

// 따라서, 이렇게 구현체를 바꾸는 코드 없이(OCP를 지키려면) 별도의 조립자가 필요하다.
// 이를 Spring이 대신 해주게 된다 ☆☆☆
```
&nbsp

**LSP : 리스코프 치환 법칙**
- 프로그램의 객체는 프로그램의 정확성을 깨뜨리지 않으면서 하위 타임의 인스턴스로 바꿀 수 있어야 한다.
- 예를 들어) 하나의 인터페이스에 "앞으로 가기"라는 기능이 있다고 한다면 하위 구현체가 "앞으로 느리게 가기"와 같이 기능을 구현해도 되지만,
"뒤로가기" 와 같이 정의해놓은 기능을 깨뜨리면 안된다는 것이다.

&nbsp

**ISP : 인터페이스 분리 원칙**
- 특정 클라이언트를 위한 인터페이스 여러 개가 범용 인터페이스 하나보다 낫다.
- 자동차 인터페이스 -> 운전 인터페이스, 정비 인터페이스로 분리
- 사용자 클라이언트 -> 운전자 클라이언트, 정비사 클라이언트로 분리 될 수 있다.
- 분리하면 인터페이스가 명확해지고 대체 가능성이 높아진다.

&nbsp

**DIP : 의존관계 역전의 원칙**
- 추상화에 의존해야하고, 구체화에 의존하면 안된다.
- 인터페이스(ex. OrderService) 에 의존해야지 구현체(ex. OrderServiceImpl)에 의존하면 안된다는 것이다.
- **역할에 의존**해야한다는 것이다. 이렇게 하면 유연하게 구현체를 변경할 수 있다.
- 앞서 말했던 **OCP의 문제점 코드**에서 보면 인터페이스 뿐만 아니라 구현체에도 의존하고 있다. 이는 DIP를 위반하고 있는것이다.
- 그럼, 어떻게? spring이 IOC를 통한 DI로 DIP를 지킬 수 있게 해줄 것!!

---


### [OCP와 DIP를 위반하는 코드에서 알아보는 Spring의 DI]
#### OCP와 DIP를 위반하는 코드
아래의 코드는 클라이언트 코드에서 구현체에 의존하고(DIP 위반)
변경시에는 클라이언트 코드도 변경해야하는 문제점이있다.(OCP 위반)
```java
public class OrderServiceImpl implements OrderService{
    private final MemberRepository memberRepository = new MemoryMemberRepository();
    //    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
}
```
#### OCP와 DIP를 준수하기 위한 코드
OCP와 DIP를 준수하기 위해 인터페이스만을 의존하게 만들었다.
이렇게 하면  OCP와 DIP를 준수하지만 실제로 실행하면 NPE가 발생할 것이다.
```java
public class OrderServiceImpl implements OrderService{
    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;
}
```
**따라서,** 이 문제를 해결하기 위해서는 누군가가 클라이언트인 ```OrderServiceImpl```에 
```DiscountPolicy``` 구현 객체를 "**★생성 및 주입★**" 해주어야한다. 

### [AppConfig를 통한 생성자 주입]
클라이언트에서는 구현체를 직접 넣지 앟고 AppConfig를 통해 대신 "**객체를 생성하고 주입" 시킨다.** 
이를 통해 역할과 책임을 나눈다. 이렇게 하면 기존의 클라이언트가 역할(인터페이스)에만 의존하게 하여 클라이언트 코드 변경없이
변경에 유연해진다. 

하지만 명확하게 역할과 책임이 나뉘어 졌는지가 불분명하므로 리팩토링을 해야할 것이다.
```java
public class AppConfig {
    /**
     * todo [memo]
     *  AppConfig를 통해서 애플리케이션의 실제 동작에 필요한 '구현 객체를 생성' 한다.
     *  AppConfig는 생성한 객체 인스턴스의 레퍼런스를 '생성자를 통해서 주입'해준다.
     */
    public MemberService memberService(){
        return new MemberServiceImpl(new MemoryMemberRepository());
    }
    public OrderService orderService(){
        return new OrderServiceImpl(new MemoryMemberRepository(),new FixDiscountPolicy());
    }
}
```
---


### [AppConfig 리팩토링]
한눈에 각각의 인터페이스가 어떤 역할을 하고, 역할에 대한 구현이 어떤것인지 한눈에 볼 수 있게한다.
```java
public class AppConfig {

    public MemberService memberService(){
        return new MemberServiceImpl(memberRepository());
    }
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
    public OrderService orderService(){
        return new OrderServiceImpl(memberRepository(),discountPolicy());
    }
    public DiscountPolicy discountPolicy(){
        return new FixDiscountPolicy();
    }
}

```
### [좋은 객체지향 설계의 5가지 원칙 적용]
**SRP 단일 책임 원칙** 

**"한 클래스는 하나의 책임만 가져야한다."**

* 클라이언트 객체가 직접 구현 객체를 생성하고 연결하고 실행하는 책임을 가지고 있었다.
* SRP 단일 책임 원칙을 따르면서 관심사를 분리하여
* 구현 객체를 생성하고 연결하는 책임은 ```AppConfig``` 가 담당하도록 했다.
* 클라이언트 객체는 실행하는 책임만 담당

**DIP 의존관계 원칙**

"**추상화에 의존하고, 구체화에 의존하면 안된다.**"

* 새로운 할인 정책을 개발하여 적용하기 위해 클라이언트의 코드를 바꿔야했다.
* 기존 클라이언트인 ```OrderServiceImpl```은 DIP를 지키면서 ```DiscountPolicy```인터페이스에 의존하는 것처럼 보였지만 ```FixDiscountPolicy``` 구현체에도
의존하고 있었다. 
* 따라서 클라이언트가 ```DiscountPolicy```인터페이스에만 의존하도록 코드를 변경
* 하지만 클라이언트는 ```DiscountPolicy```의 구현체를 알 수 없기 때문에 NPE가 발생
* AppConfig를 통해서 대신 ```FixDiscountPolicy``` 객체 인스턴스를 생성하고 클라이언트 코드에 의존관계를 주입했다.
* 이를 통해 DIP 원칙을 지킬 수 있다.

**OCP 개방 폐쇄 원칙**

"**소프트웨어 요소는 확장에 열려있으나, 변경에 닫혀있어야 한다.**"

* AppConfig를 통해서 구현체를 생성하고 대신 주입해줌으로써
* 기능을 변경할 때(확장에 열려있고) 클라이언트 코드가 변경되지 않도록 했다.(변경에 닫혀있다)
* AppConfig에서 구현체 생성 코드를 ```FixDiscountPolicy``` -> ```RateDiscountPolicy```로만 바꾸면된다.

### [IOC 와 DI, 그리고 컨테이너]

#### IOC : 제어의 역전
* 기존에는 클라이언트 코드가 사용하는 구현 객체를 직접 생성하고 연결하고 실행했다. 이는 클라이언트가 제어권을 가지고 있게 된다.
* 예를 들어 ```OrderServiceImpl```에서 ```MemberRepository```와 ```DiscountPolicy``` 사용하기 위해 
직접 ```MemmoryMemberRepositry```와  ```FixDiscountPolicy```를 생성하고 연결하며 실행한다. 
* 하지만 AppConfig 처럼 구성 영역을 사용해서 구현체(객체 인스턴스) 대신해서 생성하고 연결하며 실행하도록 하면, 프로그램의 제어흐름을
AppConfig가 가지게 된다. 
* 이렇게 프로그램의 제어 흐름을 직접 제어하는 것이 아니라 외부에서 제어하는 것을 "**IOC**"라고 한다.

**예제. 프레임워크 vs 라이브러리**
* 라이브러리는 내가 작성한 코드로 내가 직접 제어하지만(내가 필요할때 직접 호출), (Lombok)
* 프레임워크는 내가 작성한 코드를 제어하고 대신 실행한다. (Spring, JUnit)


#### DI : 의존 관계 주입
* 실제 구현 객체를 자기 자신이 아닌 외부에서 생성하고 주입하는 것을 말한다.
* 의존 관계는 "정적인 클래스 의존 관계와 실행 시점에 결정되는 동적인 객체(인스턴스) 의존관계"를 분리해서 생각해야한다.

**"정적인 클래스 의존관계"**

클래스에서 "import" 코드만 보고 의존관계를 판단할 수 있다. 정적인 의존관계는 애플리케이션을 실행하지 않아도 분석할 수 있다.

**"동적인 클래스 의존관계"**

애플리케이션 실행 시점에서 실제 생성된 인스턴스의 참조가 연결된 의존 관계이다.
* 애플리케이션의 **실행시점(런타임)**에 외부에서 실제 구현 객체를 생성하고 클라이언트에 전달해서 클라이언트와 서버의 실제 의존 관계가
연결되는 것을 의존 관계 주입이라 한다.
* 객체 인스턴스를 생성하고, 그 참조값을 전달해서 연결된다.
* 이를 통해 클라이언트 코드가 변경되지 않고, 클라이언트가 호출하는 대상의 구현체를 변경할 수 있다.

### [스프링 컨테이너]

* ```ApplicationContext```를 스프링 컨테이너라고 부른다.
* 기존에는 개발자가 AppConfig와 같은 설정 클래스를 통해 객체를 생성하고 주입했지만, Spring으로 넘어오면서 이를
  ```ApplicationContext```에 위임한다.
* 스프링 컨테이너는 ```@Configuration```이 붙은 클래스를 설정 정보로 사용한다. 여기서 ```@Bean```
이라 적힌 메서드를 모두 호출해서 반환된 객체를 스프링 컨테이너에 등록한다. 이렇게 스프링 컨테이너에 등록된 객체를 스프링 빈이라고 한다.
* 이전에는 AppConfig에서 직접 찾아서 사용해야했지만, 스프링 컨테이너를 사용하면 ```applicationContext.getBean()```을 통해서 객체를 찾을 수 있다.
  * **이렇게 수행하면 이전 코드보다 더 복잡해지는것 같은데 어떤 장점이 있을까?**
### **[스프링 빈 상속관계]**
* 부모 타입으로 조회하면, 자식 타입도 함께 조회된다.★
* 그래서 결국 "Object"타입으로 조회하면 모든 빈이 조회된다.

&nbsp;
* ```ApplicationContext```는 ```BeanFactory```를 상속받아 빈을 관리하고 검색하는 모든 기능을 제공한다. 
* ```ApplicationContext```는 빈 관리기능 + 편리한 부가기능
  * **메시지 소스를 활용한 국제화 기능**
    * 한국에서 들어오면 한국어, 영어권에서 들어오면 영어로 출력
  * **환경변수**
    * 로컬, 개발, 운영을 구분해서 처리
  * **애플리케이션 이벤트**
    * 이벤트 발행 및 구독하는 모델을 편리하게 지원
  * **편리한 리소스 조회**
    * 파일, 클래스패스, 외부등에서 리소스를 편리하게 조회

**사용 방법**
1. 어노테이션 기반 자바 코드로 사용
   1. ```new AnnotaionConfigApplicationContext(AppConfig.class)```를 통해 사용
2. XML 설정 사용
---

&nbsp;


---
&nbsp;

### [웹 어플리케이션과 싱글톤]

웹 어플리케이션은 보통 여러 고객이 동시에 요청을 한다.

이때 고객은 객체를 요청을 하게되고, 이 요청마다 새로운 인스턴스를 반환하게 된다.
그렇다는건 요청이 올때마다 계속 인스턴스를 만든다는 것이다.
```java
    @Test
    @DisplayName("스프링 없는 순수 DI컨테이너")
    void pureContainer(){
        AppConfig appConfig = new AppConfig();
        //1. 조회 : 호출할때마다 객체 생성
        MemberService memberService = appConfig.memberService();

        //2. 조회 : 호출할때마다 객체 생성
        MemberService memberService1 = appConfig.memberService();
        Assertions.assertThat(memberService).isNotSameAs(memberService1);
    }
```
위와 같이 테스트를 진행하면 ```memberService```와 ```memberService1```이 서로 다른 인스턴스가 할당됨을 알 수 있다.

* 기존에 직접 작성한 ```AppConfig```는 요청을 할 때 마다 객체를 새로 생성한다.
* 고객 트래픽이 초당 100개라면 초당 100개의 인스턴스가 생성되고 소멸될 것이다. -> 메모리 낭비가 심하다.
* 따라서 해당 객체에 대한 인스턴스는 1개만 생성되게하고, 공유하도록 설계한다 -> 싱글톤 패턴

### [싱글톤 패턴]

* 클래스의 인스턴스가 1개만 생성되도록 보장하는 패턴이다.
* 객체 인스턴스를 2개이상 생성하지 못하도록 막아야한다.
  * private 생성자를 통해 new 키워드를 막고
  * public static메소드를 통해 외부에서 인스턴스를 요청해서 가져오게 만든다.

#### 문제점
* 코드가 길어진다.
* 클라이언트가 구체 클래스에 의존해야한다.(ex. ```구체클래스.getInstance()```  ) ->  DIP 위반하게된다.
* 클라이언트가 구체 클래스에 의존하기때문에 OCP를 위한반 할 수 있다. 
* 내부 속성을 변경하거나 초기화 하기 어렵다.
* private 생성자로 자식 클래스를 만들기 어렵다.
* 결론적으로 유연성이 떨어진다.

★ 스프링은 이 문제점들을 해결하는 싱글톤 패턴을 사용!!!

### [싱글톤 컨테이너]
앞서 언급되었던 ApplicationConext(스프링 컨테이너)가 싱글톤의 본래 문제점을 해결한다.

* 객체를 "Bean"으로 관리함에 따라 싱글턴 패턴의 모든 단점을 해결하면서 객체를 싱글톤으로 유지
  * 싱글톤을 위한 지저분한 코드가 들어가지 않다도 되고
  * DIP, OCP, 테스트, private 생성자로부터 자유롭다.

이 덕분에 고객의 요청이 올 때 마다 객체를 생성하는 것이 아니라, 이미 만들어진 객체를 공유해서 효율적으로 재사용할 수 있다.

★ 참고 ★
스프링의 기본 빈 등록 방식은 싱글톤이지만, 요청할 때 마다 새로운 객체를 생성해서 반환하는 기능도 제공한다.

### [싱글톤 패턴의 주의점]
문제점을 해결한 싱글톤은 객체를 재사용하면서 장점만을 가질 것 같지만 주의해야할 사항들이 있다.

객체 인스턴스 하나만 생성해서 공유하는 방식인 싱글톤은 클라이언트가 하나의 같은 인스턴스를 공유하기 때문에 싱글톤 객체는 stateful하게 설계하면 안된다.

* ★ 무상태로 설계해야한다! ★
  * 특정 클라이언트에 의존적인 필드가 있으면 안된다.
  * 특정 클라이언트가 값을 변경할 수 있는 필드가 있으면 안된다.
  * 가급적 읽기만 가능해야한다.
  * 필드 대신에 자바에서 공유되지 않는, 지역변수, 파라미터, ThreadLocal 등을 사용해야한다.
* ★ 스프링 빈의 필드에 공유 값을 설정하면 큰 장애가 발생할 수 있다.★

**장애 발생 예시**
```java
class StatefulServiceTest {
    @Test
    void statefuleServiceSingleton(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);
        //Thread A : 사용자 a 10000원 주문
        statefulService1.order("userA",10000);
        //Thread B : 사용자 b 20000원 주문
        statefulService1.order("userB",20000);

        //기댓값은 10000원이지만, 20000원이 나온다..
        int price = statefulService1.getPrice();
        System.out.println(price);
    }

    static class TestConfig{
        @Bean
        public StatefulService statefulService(){
            return new StatefulService();
        }
    }
}
```
* 여러 쓰래드가```statefuleSerice```을 호출할 경우 스프링은 이를 빈으로 관리하여 같은 인스턴스를 반환한다.
* 이때 ```price```필드는 공유되는 필드인데, 특정 클라이언트가 값을 변경한다.
* 사용자 A의 주문금액은 10000원이지만 20000원이라는 조회 값이 나오게된다.
* 실무에서 종종 나오는 문제로.. 
* ★ 공유 필드는 정말 조심해야한다! 스프링 빈은 항상 stateless로 설계하자.

&nbsp;

### [컴포넌트 스캔]
스프링은 설정 정보없이 자동으로 스프링 빈을 등록하는 컴포넌트 스캔이라는 기능을 사용한다.

* ```@ComponentScan```을 설정 정보에 붙여주는 방식으로 수행할 수 있다.
* 그럼 ```@Component```가 붙은 클래스를 스캔하여 빈으로 등록한다.
* ```@Configuration```의 경우 ```@Component```가 붙어있어 자동 스캔 대상이다.
* 빈 생성 시에 각각의 빈이 필요한 객체들은 "DI" 즉 객체 주입을 통해 이루어 진다.
* 주입의 방법은 여러가지가 있지만 생성자 주입의 경우 컴포넌트 스캔 시에 스프링이 ```@Autowird```가 붙은 생성자의 파라미터에 필요한 객체를 스프링 빈으로 찾아서 주입시켜준다.

#### **참고.**
```@Configuration``` 이나 ```@Transactional```등이 붙은 객체는 추가 기능이나 관리를 위해 spring이 ```CGLIB```라는 
바이트코드 조작 라이브러리를 사용해서 원본 객체를 상속받은 임의의 객체를 사용한다.

여기서 말하는 추가기능 및 관리라 함은,

* ```@Transactional``` : 트랜잭션 관리를 위한 기능
* ```@Configuration``` : 설정 내에서 여러번의 같은 인스턴스를 생성하더라도 일정하게 싱글톤을 유지

---
#### **[스캔 범위 지정하기]**
```java
@ComponentScan(
    basePackages = "hello.core"
)
```
```basePackages``` 속성을 통해 스캔의 범위를 지정하게 된다.
* 만약 지정하지 않게되면 어떻게 될까?
* ```ComponentScan```이 붙은 설정 정보 클래스의 패키지가 시작 위치가 된다.
* 참고로, 스프링 부트를 사용하면 스프링 부트의 대표 시작 정보인 ```@SpringBootApplication```안에 ```@ComponentScan```이 있어서 ```@SpringBootApplication```가 있는 클래스 위치를 잘 파악해야한다.

#### [컴포넌트 스캔 기본대상]
* ```@Component``` : 컴포넌트 스캔에서 사용
* ```@Controller``` : 스프링 MVC 컨트롤러
* ```@Service``` : 비즈니스로직
* ```@Repository``` : 데이터 접근 계층
* ```@Configuration``` : 설정 정보


#### [컴포넌트 스캔 필터]
컴포넌트 스캔시에 ```@ComponentScan```의 설정 시에 ```includeFilters```와 ```excludeFilters``` 를 통해서 빈 등록을 제외시키거나 추가시킬 수 있다.

---

### [다양한 의존 관계 주입]
#### 필드 주입
* ```@Autowired``` 해당 어노테이션을 주입이 필요한 필드에 붙이는 방식
* 빈이 생성된 이후에 의존관계주입 단계에서 
* **필드 주입은 피해라!!**
  * final을 사용하지 못해 불변성 유지가 어렵다.
  * 순환 참조가 일어날 수 있다.
  * 
#### 수정자 주입
#### 생성자 주입


### [왜 생성자 주입을 해야할까?]
* 대부분의 의존관계 주입은 한번 일어나면 애플리케이션 종료시점까지 의존 관계를 변경할 일이 없다. (불변하다.)
* 수정자 주입을 사용하게 되면 누군가가 실수로 변경할 수도 있다.
* 생성자 주입은 객체가 생성될 때 딱 1번만 일어나기 때문에 안전하다. 또한 final 키워드를 통해 **누락**을 막을 수 있다. 
* 프레임워크 없이 순수 자바 코드만으로 테스트를 수행할 수 있다. 
* 종합적으로 생성자 주입을 사용하면 컴파일 시점에서 오류를 발견할 가능성이 높다.
* 옵션이 필요한 경우에만 수정자 주입을 사용하자.

---

&nbsp;

### [조회 대상 빈이 2개 이상일때는??]

예를 들어 ```DisCountPolicy```의 하위타입이 2개 이상(```FixDisCountPolicy```,```RateDisCountPolicy```)이라면
자동의존관계 주입 시에 ```NoUniqueBeanDefinitionException``` 오류가 발생한다.


따라서 스프링이 이를 구분할 수 있게 도와줘야한다.

#### 해결방법
1. ```@Autowired``` 필드 명 매칭
```java
<<기존코드>>
@Autowired
private DiscountPolicy discountPolicy 
```

```java
<<필드 명을 빈 이름으로 변경>>
@Autowired
private DiscountPolicy fixDiscountPolicy 
```
이렇게 하면 빈 이름이 ```fixDiscountPolicy```인 구현체가 ```DiscountPolicy```에 정상적으로 주입된다.

2. ```@Qualifier``` 사용

구분자의 역할을 하게 된다.

```java
<<구현체 클래스>>
@Component
@Qualifier("fixDiscountPolicy")
public class FixDiscountPolicy implements DiscounPolicy{
    
}
...
```

```java
<<생성자 자동 주입 예시>>
public OrderServiceImpl(MemberRepository memberRepository,
                        @Qualifier("fixDiscountPolicy")DiscountPolicy discountPolicy){
        
    
}
```

3. ```@Primary``` 사용

이는 우선순위를 지정함으로써 어떤 빈이 주입되어야 하는지 구분해주는 역할을 한다.
의존관계 주입시에 여러 빈이 매칭되면 ```@Primary```가 우선권을 가지게 된다.

```java
<<RateDiscountPolicy가 우선권을 가진다.>>

@Component
@Primary
public class RateDiscountPolicy implements DiscountPolicy{
    
}

@Component
public class FixDiscountPolicy implements DiscountPolicy{

}
```
언제 많이쓰나?

바로,

"메인 데이터베이스가 있고 보조 데이터베이스가 있다고 했을때, 메인 데이터베이스의 커넥션을 가져오고자 할때" 처럼 
코드에서 자주 사용하는 구현체에 붙임으로써 코드를 보다 깔끔하게 구현할 수 있다.

####[참고할 점]
참고할 점은 ```@Primary```보다 ```@Qualifier``` 가 우선순위를 가진다는 점이다!!


&nbsp;

### [어노테이션 직접 만들기]

의존관계 주입 시에 어노테이션을 직접 만들어서 주입할 수도 있다.
```@Qualifier("mainmDiscountPolicy")```와 같이 오탈자가 있어도 컴파일 오류가 나지 않는데 이를 방지하기 위해서
어노테이션을 직접 만들어 사용할 수 있다.

```java
// Qualifier에 달린 어노테이션을 끌어오고 
// @Qualifier("mainDiscountPolicy")와 같이 직접 지정해주면 @MainDiscountPolicy를 사용할 수 있다.
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Qualifier("mainDiscountPolicy") // ★★ 중요 ★★
public @interface MainDiscountPolicy {

}

// 사용시에 @MainDiscountPolicy를 붙이면 된다. 오탈자가 생기면 컴파일 오류가 날것이다.
public OrderServiceImpl(MemberRepository memberRepository,@MainDiscountPolicy DiscountPolicy discountPolicy) {
  this.memberRepository = memberRepository;
  this.discountPolicy = discountPolicy;
}
```


&nbsp;

### [빈의 생명주기]
빈의 생명 주기는 다음과 같다.

1. 스프링 컨테이너가 먼저 생성된다.
2. 스프링 컨테이너가 컴포넌트스캔을 통해 빈을 등록한다.
3. 빈이 생성 되고 의존관계 주입 후 초기화 콜백이 동작
4. 어플리케이션에서 빈이 동작
5. 빈이 소멸 전에 소멸콜백이 호출되고 어플리케이션이 종료된다.

#### [콜백을 다루는 2가지 방법(3가지가 있긴함)]
1. ```@Bean``` 의 속성을 활용한 초기화 콜백 및 소멸 콜백. 
```java
  // 해당 클래스 (NetworkClient) 안에 메소드를 만들고, 그 이름을 
  // initMethod 과 destoryMethod 에 적으면 된다.
  // 예를 들어 init과 close는 NetworkClient 안의 메서드인 것이다.
  @Bean(initMethod = "init", destoryMethod = "close")
  public NetworkClient networkClient(){
        NetworkClient networkClient = new NetworkClient();
        networkClient.setUrl("http~~");
        return networkClient;
  }
```
2. ```@PostConstruct``` , ```@PreConstruct```를 통한 콜백 메서드
```java 
  public class NetworkClient{
    private String url;
    
    public void setUrl(String url){
      this.url = url;
    }
    @PostContruct
    public void init(){
      // init 로직
    }
    
    @PreContruct
    public void close(){
      // close 로직
    }
  } 
```
**참고사항** : 외부 라이브러리에는 적용하지 못한다는 단점이 있기 때문에 외부 라이브러리를 초기화 및 종료해야한다면 @Bean 기능(1번)
을 사용하자~!

&nbsp;



**<참고 자료>**
* 스프링 핵심 원리 - 기본편 (김영한, 인프런)