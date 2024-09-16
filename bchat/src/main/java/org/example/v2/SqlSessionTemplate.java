package org.example.v2;

import java.io.IOException;
import java.io.Reader;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class SqlSessionTemplate {

  private static SqlSessionFactory sqlSessionFactory;

  public static SqlSessionFactory getSqlSessionFactory() {
    return sqlSessionFactory;
  }

  //1번 초기화 하면 충분. 싱글패턴. 정적 초기화로 ㄱ.
  static {
    try {
      String resource = "mybatis-config.xml";
      Reader reader = Resources.getResourceAsReader(resource);
      sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader); //Builder 로 설정 간단히.
    } catch (IOException e) {
      throw new RuntimeException("Failed to initialize SqlSessionFactory: " + e.getMessage(), e);
    }
  }

}
