package hello.core.member;

import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryMemberRepository implements MemberRepository{
    /**
     * todo[memo]
     *  - concurrentHashMap을 통한 동시성 이슈 방지
     *  - static을 통해 전역에서 storage를 공유하도록 한다.
     *    static이 없으면 MemoryMemberRepository가 새롭게 할당될때마다 다른 storage가 생성되며,
     *    이에 따라 저장된 회원을 찾지 못하게 될 수 있다.
     *  - 해당 경험은 java의 메모리 구조에 대해 다시 생각하게한다.
     */
    private static Map<Long, Member> storage  = new ConcurrentHashMap<>();

    @Override
    public void save(Member member) {
        if(member==null) throw new RuntimeException();// todo global에러 처리를 통해 에러 response를 던지도록 한다.
        System.out.println("save : "+member.getId());
        storage.put(member.getId(), member);
    }

    @Override
    public Member findById(Long memberId) {
        System.out.println(storage.get(memberId));
        return storage.get(memberId);
    }
}
