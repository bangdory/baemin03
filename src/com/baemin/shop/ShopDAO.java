package com.baemin.shop;

import javax.naming.*;
import javax.sql.*;

import com.baemin.util.CoordDistance;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShopDAO {

    // DB변수
    Connection con = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Context cont = null;
    DataSource ds = null;

    // getListByCategory_start-----------------------------------------------------------------------------
    public List<ShopDTO> getListByCategory(String catogory, double memberX, double memberY) throws Exception {
        // 출력객체
        List<ShopDTO> list = new ArrayList<>();
        System.out.println("---ShopDAO getListByCategory");
        try {
            // 1+2
            con = getConnection();
            // 3. sql
            String a1 = "shopNo, shopName, shopCategory, shopX, shopY, reviewRank ";
            String a2 = "shopNo, shopName, shopCategory, shopX, shopY, avg(reviewRank) reviewRank ";
            String sql = "(select " + a1 + " from shop LEFT JOIN review on shop.shopNo = reviewShopNo)";
            sql = "select " + a2 + " from " + sql + " where shopCategory = ? GROUP BY shopNo ORDER BY reviewRank DESC ";

            // select shopNo, shopName, shopCategory, shopX, shopY,
            // avg(reviewRank) reviewRank
            // FROM
            // (select shopNo, shopName, shopCategory, shopX, shopY, reviewRank
            // FROM shop LEFT JOIN review on shop.shopNo = reviewShopNo )
            // WHERE
            // shopCategory = ? GROUP BY shopNo ORDER BY reviewRank DESC;

            System.out.println(sql);
            // 4. 실행객체
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, catogory);
            // 5. 실행
            rs = pstmt.executeQuery();
            // 6. 표시 --- select 때만 표시
            if (rs != null) {
                while (rs.next()) {
                    ShopDTO dto = new ShopDTO();
                    dto.setShopNo(rs.getInt("shopNo"));
                    dto.setShopName(rs.getString("shopName"));
                    dto.setShopCategory(rs.getString("shopCategory"));
                    dto.setReviewRank(rs.getDouble("reviewRank"));
                    // 좌표
                    double shopX = rs.getDouble("shopX");
                    double shopY = rs.getDouble("shopY");
                    dto.setShopX(shopX);
                    dto.setShopY(shopY);
                    double distance = CoordDistance.getDistance(shopX, shopY, memberX, memberY);
                    // 거리 계산해서, BaeDalLimit 보다 작을때만 리스트 add
                    if (distance < CoordDistance.BaeDalLimit)
                        list.add(dto);
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
            throw new Exception(" getListByCategory() 예외  ");
        } finally {
            close(con, pstmt, rs);
        } // finally end
        return list;
    } // getListByCategory_end-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-/-

    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////

    // 기본생성자 private 처리
    private ShopDAO() {
    }

    // 싱글톤 인스턴스
    private static ShopDAO dao = new ShopDAO();

    // public get인스턴스
    public static ShopDAO getInstance() {
        return dao;
    }

    // 커넥션 풀
    private Connection getConnection() throws Exception {
        cont = new InitialContext();
        ds = (DataSource) cont.lookup("java:comp/env/jdbc/mysql");
        return ds.getConnection();
    }

    // close 1
    static final void close(Connection con, PreparedStatement pstmt, ResultSet rs) throws Exception {
        close(con, pstmt);
        if (rs != null) {
            rs.close();
        }
    } // close () end

    // close 2
    static final void close(Connection con, PreparedStatement pstmt) throws Exception {
        if (con != null) {
            con.close();
        }
        if (pstmt != null) {
            pstmt.close();
        }
    } // close () end

    public static void main(String[] args) {

        String a1 = "shopNo, shopName, shopCategory, shopX, shopY, reviewRank ";
        String a2 = "shopNo, shopName, shopCategory, shopX, shopY, avg(reviewRank) reviewRank ";
        String sql = "(select " + a1 + " from shop LEFT JOIN review on shop.shopNo = reviewShopNo )";
        sql = "select " + a2 + " from " + sql + " where shopCategory = ?  group by shopNo order by shopNo ";

        System.out.println(sql);

    }
}