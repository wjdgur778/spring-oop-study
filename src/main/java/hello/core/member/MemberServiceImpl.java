package hello.core.member;

public class MemberServiceImpl implements MemberService{

    /**
     * todo [memo]
     *  serviceImpl이 인터페이스 뿐만 아니라 구현체에 의존하고 있다. (DIP 위반)
     *  저장 repository를 변경하고자하면 클라이언트의 코드도 변경해야하고(OCP 위반)
     *
     *
     */
    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memoryMemberRepository) {
        this.memberRepository = memoryMemberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member getMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
