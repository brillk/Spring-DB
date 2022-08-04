package hello.jdbc.repository;


import hello.jdbc.connection.DBConnectionUtil;
import hello.jdbc.domain.Member;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;

/**
 * JDBC - DataSource 사용, JdbcUtils 사용
 */


@Slf4j
public class MemberRepositoryV1 {


    private final DataSource dataSource;

    public MemberRepositoryV1(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Member save(Member member) throws SQLException {

        // sql문 작성
        String sql = "insert into member(member_id, money) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            // 외부 리소스를 쓰는 거라 닫아줘야 한다
            // 둘 중 하나라도 오류가 난다면 뒤에 있는 함수가 실행이 안되서 함수로 뽑았다
            close(con, pstmt, null);
        }
    }

    //조회하기
    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            //next 실행시 다음 함수가 시작된다
            rs = pstmt.executeQuery();
            if(rs.next()) {
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else  { // 못 찾으면 실행
                throw new NoSuchElementException("member not found memberId=" + memberId);
             }
        } catch( SQLException e)  {
            log.error("db erorr", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money=? where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);

            // execute 는 쿼리를 실행하고 영향받은 row수를 반환
            int resultSize =  pstmt.executeUpdate();
            log.info("resultSize={}", resultSize);

        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            // 외부 리소스를 쓰는 거라 닫아줘야 한다
            // 둘 중 하나라도 오류가 난다면 뒤에 있는 함수가 실행이 안되서 함수로 뽑았다
            close(con, pstmt, null);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {

        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);


    }


    private Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.info("get Connection={}, class={}", con, con.getClass());

        return con;
    }

}
