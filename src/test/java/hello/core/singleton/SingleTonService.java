package hello.core.singleton;

public class SingleTonService {
    // jvm이 뜰때 자기자신을 생성해서 instance에 참조값을 넣어놓는다.
    private static final SingleTonService instance = new SingleTonService();

    public static SingleTonService getInstance(){
        return instance;
    }
    //생성자를 private으로 만들어 외부에서 생성하지 못하게 해놓고, 내부에서만 자기 자신의 인스턴스를 만들어서 사용할 수 있도록 하는 것.
    private SingleTonService(){
    }
    public void logic(){
        System.out.println("싱글톤 로직 호출");
    }
}
