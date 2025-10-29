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
&nbsp;

**LSP : 리스코프 치환 법칙**
- 프로그램의 객체는 프로그램의 정확성을 깨뜨리지 않으면서 하위 타임의 인스턴스로 바꿀 수 있어야 한다.
- 예를 들어) 하나의 인터페이스에 "앞으로 가기"라는 기능이 있다고 한다면 하위 구현체가 "앞으로 느리게 가기"와 같이 기능을 구현해도 되지만,
"뒤로가기" 와 같이 정의해놓은 기능을 깨뜨리면 안된다는 것이다.

&nbsp;

**ISP : 인터페이스 분리 원칙**
- 특정 클라이언트를 위한 인터페이스 여러 개가 범용 인터페이스 하나보다 낫다.
- 자동차 인터페이스 -> 운전 인터페이스, 정비 인터페이스로 분리
- 사용자 클라이언트 -> 운전자 클라이언트, 정비사 클라이언트로 분리 될 수 있다.
- 분리하면 인터페이스가 명확해지고 대체 가능성이 높아진다.

&nbsp;

**DIP : 의존관계의 원칙**
- 추상화에 의존해야하고, 구체화에 의존하면 안된다.
- 인터페이스(ex. OrderService) 에 의존해야지 구현체(ex. OrderServiceImpl)에 의존하면 안된다는 것이다.
- **역할에 의존**해야한다는 것이다. 이렇게 하면 유연하게 구현체를 변경할 수 있다.
- 앞서 말했던 **OCP의 문제점 코드**에서 보면 인터페이스 뿐만 아니라 구현체에도 의존하고 있다. 이는 DIP를 위반하고 있는것이다.
- 그럼, 어떻게? spring이 IOC를 통한 DI로 DIP를 지킬 수 있게 해줄 것!!

---

&nbsp;
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
&nbsp;
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

### [JAVA to Spring]
#### ★스프링 컨테이너★
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




**<참고 자료>**
* 스프링 핵심 원리 - 기본편 (김영한, 인프런)
