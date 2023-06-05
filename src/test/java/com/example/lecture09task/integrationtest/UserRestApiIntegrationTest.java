package com.example.lecture09task.integrationtest;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.spring.api.DBRider;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

@SpringBootTest
@AutoConfigureMockMvc
@DBRider
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

public class UserRestApiIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    // GETメソッドで存在するIDを指定した時に、指定したIDのユーザーが取得できステータスコード200が返されること
    @Test
    @DataSet(value = "users.yml")
    @Transactional
    void 指定したIDのユーザーが取得できること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                {
                "id": 1, 
                "name": "tanaka", 
                "age": 25
                }
            """, response, JSONCompareMode.STRICT);
    }

    // GETメソッドで存在しないIDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること
    @Test
    @DataSet(value = "users.yml")
    @Transactional
    void 指定したIDのユーザーが存在しない時に例外がスローされること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/users/4"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                    {
                    "timestamp": "2023-06-05T18:13:22.414491+09:00[Asia/Tokyo]",
                    "status": "404",
                    "error": "Not Found",
                    "message": "This id is not found",
                    "path": "/users/4"
                    }
                """,
                response,
                new CustomComparator(JSONCompareMode.STRICT,
                        new Customization("timestamp", ((o1, o2) -> true))));
    }

    // GETメソッドでクエリパラメータで年齢を指定しない時に、ユーザーが全件取得できステータスコード200が返されること
    @Test
    @DataSet(value = "users.yml")
    @Transactional
    void 年齢を指定しない時にユーザーが全件取得できること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                    [
                    {
                    "id": 1, 
                    "name": "tanaka", 
                    "age": 25
                    }, 
                    {
                    "id": 2, 
                    "name": "suzuki", 
                    "age": 30
                    }, 
                    {
                    "id": 3, 
                    "name": "yamada", 
                    "age": 35
                    }
                    ]                    
                """, response, JSONCompareMode.STRICT);
    }

    // GETメソッドでDBが空の時に、空のListが返されステータスコード200が返されること
    @Test
    @DataSet(value = "empty.yml")
    @Transactional
    void DBが空の時に空のListが返されること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                    []                    
                """, response, JSONCompareMode.STRICT);
    }

    // GETメソッドでクエリパラメータで年齢を指定した時に、指定した年齢より上のユーザーが取得できステータスコード200が返されること
    @Test
    @DataSet(value = "users.yml")
    @Transactional
    void 指定した年齢より上のユーザーが取得できること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/users?age=30"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                [
                {
                "id": 3, 
                "name": "yamada", 
                "age": 35
                }
                ]
            """, response, JSONCompareMode.STRICT);
    }

    // GETメソッドでクエリパラメータで年齢を指定し指定した年齢より上のユーザーが存在しない時に、空のListとステータスコード200が返されること
    @Test
    @DataSet(value = "users.yml")
    @Transactional
    void 指定した年齢より上のユーザーが存在しない時に空のListが返されること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/users?age=35"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
                []
            """, response, JSONCompareMode.STRICT);
    }

    // POSTメソッドで正しくリクエストした時に、ユーザーが登録できステータスコード201とメッセージが返されること
    @Test
    @DataSet(value = "users.yml")
    @ExpectedDataSet(value = "datasets/insert_users.yml", ignoreCols = "id")
    @Transactional
    void ユーザーが登録できること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content("""
                        {
                        "name": "takahashi", 
                        "age": 40
                        }
                        """))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
            {
            "message": "user successfully created"
            }
            """, response, JSONCompareMode.STRICT);
    }

    // POSTメソッドでリクエストのnameがnullの時に、ステータスコード400とエラーメッセージが返されること
    @Test
    @DataSet(value = "users.yml")
    @Transactional
    void 登録時のリクエストのnameがnullの時にエラーメッセージが返されること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                        {
                        "name": null, 
                        "age": 40
                        }
                        """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
            {
                "timestamp": "2023-06-05T18:13:22.414491+09:00[Asia/Tokyo]",
                "status": "400",
                "error": "Bad Request",
                "message": "Please enter your name and age",
                "path": "/users"
            }
            """,
                response,
                new CustomComparator(JSONCompareMode.STRICT,
                        new Customization("timestamp", ((o1, o2) -> true))));
    }

    // POSTメソッドでリクエストのageがnullの時に、ステータスコード400とエラーメッセージが返されること
    @Test
    @DataSet(value = "users.yml")
    @Transactional
    void 登録時のリクエストのageがnullの時にエラーメッセージが返されること() throws Exception{
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("""
                        {
                        "name": "takahashi", 
                        "age": null
                        }
                        """))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        JSONAssert.assertEquals("""
            {
                "timestamp": "2023-06-05T18:13:22.414491+09:00[Asia/Tokyo]",
                "status": "400",
                "error": "Bad Request",
                "message": "Please enter your name and age",
                "path": "/users"
            }
            """,
                response,
                new CustomComparator(JSONCompareMode.STRICT,
                        new Customization("timestamp", ((o1, o2) -> true))));
    }

    // PATCHメソッドで存在するIDを指定した時に、ユーザーが更新できステータスコード200とメッセージが返されること

    // PATCHメソッドで存在するIDを指定しリクエストのnameがnullの時に、ageのみ更新されステータスコード200とメッセージが返されること

    // PATCHメソッドで存在するIDを指定しリクエストのageがnullの時に、nameのみ更新されステータスコード200とメッセージが返されること

    // PATCHメソッドで存在しないIDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること

    // DELETEメソッドで存在するIDを指定した時に、ユーザーが削除できステータスコード200とメッセージが返されること

    // DELETEメソッドで存在しないIDを指定した時に、例外がスローされステータスコード404とエラーメッセージが返されること

}
