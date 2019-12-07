package com.example.demo;

import com.example.demo.model.AyUser;
import com.example.demo.service.AyUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class MySpringBootApplicationTests {

	@Test
	void contextLoads() {
	}

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Test
    public  void  mySqlTest(){
        String sql = "select id,name,password from ay_user ";
        List<AyUser> userList = (List<AyUser>) jdbcTemplate.query(sql, new RowMapper<AyUser>() {
            @Override
            public  AyUser mapRow(ResultSet re, int rowNum) throws SQLException {
                AyUser user = new AyUser();
                user.setId(re.getString("id"));
                user.setName(re.getString("name"));
                user.setPassword(re.getString("password"));
                return  user;
            }

        });
        System.out.println("查询成功：");
        for (AyUser user:userList){
            System.out.println("[id]："+user.getId()+";[name]:"+user.getName());
        }
    }

    @Resource
    private AyUserService ayUserService;

    @Test
    public  void testRepository(){
        List<AyUser> userList = ayUserService.findAll();
        System.out.println("findAll():"+userList.size());

        List<AyUser> userList2 = ayUserService.findByName("文鹏");
        System.out.println("findByName():"+userList2.size());
        Assert.isTrue(userList2.get(0).getName().equals(("文鹏")));


        List<AyUser> userList3 = ayUserService.findByNameLike("文%");
        System.out.println("findByNameLike():"+userList3.size());
        Assert.isTrue(userList3.get(0).getName().equals(("文鹏")));

        List<String> ids = new ArrayList<String>();
        ids.add("1");
        ids.add("2");
        List<AyUser> userList4 = ayUserService.findByIdIn(ids);
        System.out.println("findByIdIn:"+userList4.size());

        PageRequest pageRequest = PageRequest.of(0,10);
        Page<AyUser> userList5 = ayUserService.findAll(pageRequest);
        System.out.println("page findAll:"+userList5.getTotalPages()+"/"+userList5.getSize());

        List<AyUser> userList6 = ayUserService.findByNameLike("%国%");
        System.out.println("findByNameLike():"+userList6.size());
        Assert.isTrue(userList6.get(0).getName().equals(("文国平")));

        AyUser ayUser = new AyUser();
        ayUser.setId("4");
        ayUser.setName("李国正");
        ayUser.setPassword("123");
        ayUserService.save(ayUser);

        List<AyUser> userList7 = ayUserService.findByNameLike("%国%");
        System.out.println("findByNameLike():"+userList7.size());
        Assert.isTrue(userList7.get(0).getName().equals(("文国平")));

        ayUserService.delete("4");

        List<AyUser> userList8 = ayUserService.findByNameLike("%国%");
        System.out.println("findByNameLike():"+userList8.size());
        Assert.isTrue(userList8.get(0).getName().equals(("文国平")));

    }

    @Test
    public  void  testTransaction(){
        AyUser ayUser = new AyUser();
        ayUser.setId("6");
        ayUser.setName("woca2");
        ayUser.setPassword("123");
        ayUserService.save(ayUser);
        ayUserService.delete("5");
    }

}
