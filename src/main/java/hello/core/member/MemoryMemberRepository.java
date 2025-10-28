package hello.core.member;

import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryMemberRepository implements MemberRepository{

    //concurrentHashMap을 통한 동시성 이슈 방지
    private Map<Long, Member> storage  = new ConcurrentHashMap<>();

    @Override
    public void save(Member member) {
        if(member==null) throw new RuntimeException();// todo global에러 처리를 통해 에러 response를 던지도록 한다.
        storage.put(member.getId(), member);
    }

    @Override
    public Member findById(Long memberId) {
        return storage.get(memberId);
    }
}
