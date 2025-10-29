package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService{

    /**
     * todo[memo]
     *  주문 시에 할인 정책을 discountPolicy에 던져줌으로써 "단일책임의 원칙"을 잘 지킬 수 있다.
     *  또한 역할(인터페이스)와 구현(MemoryMemberRepository,FixDiscountPolicy)을 잘 분리했지만, 구현체에도 의존함에따라 "DIP를 위반
     *  그럼 어떻게 문제를 해결할까?
     *  인터페이스만을 의존하게 구현해야한다.
     */
    private final MemberRepository memberRepository;
//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    private final DiscountPolicy discountPolicy;

    public OrderServiceImpl(MemoryMemberRepository memoryMemberRepository, FixDiscountPolicy fixDiscountPolicy) {
        this.memberRepository = memoryMemberRepository;
        this.discountPolicy = fixDiscountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
         Member member = memberRepository.findById(memberId);
         int discountPrice = discountPolicy.discount(member,itemPrice);

        return new Order(memberId,itemName,itemPrice,discountPrice);
    }
}
