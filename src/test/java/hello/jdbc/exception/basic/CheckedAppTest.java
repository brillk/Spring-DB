package hello.jdbc.exception.basic;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

// 기본은 언체크 예외처리를 사용하는것 이다

public class CheckedAppTest {

    @Test
    void checked() {
        Controller controller = new Controller();
        Assertions.assertThatThrownBy(() -> controller.request())
                .isInstanceOf(SQLException.class);
    }


    static class Controller {

        // 서비스에서 컨트롤러까지 에러를 명시해줘야 한다
        Service service = new Service();
        public void request() throws SQLException, ConnectException {
            service.logic();
        }
    }

    //서비스는 리포지토리와 NetworkClient 를 둘다 호출한다.
    //따라서 두 곳에서 올라오는 체크 예외인 SQLException 과 ConnectException 을 처리해야 한다.
    //그런데 서비스는 이 둘을 처리할 방법을 모른다.
    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() throws ConnectException, SQLException {
            repository.call();
            networkClient.call();
        }

    }

    static class NetworkClient {
        public void call() throws ConnectException {
            throw new ConnectException("연결 실패");
        }
    }

    static class Repository {
        public void call() throws SQLException {
        throw new SQLException("ex");

        }
    }
}
