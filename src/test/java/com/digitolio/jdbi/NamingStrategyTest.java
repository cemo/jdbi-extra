package com.digitolio.jdbi;

import com.digitolio.jdbi.strategy.TranslatingStrategy;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class NamingStrategyTest {

   @Test
   public void testLowerUnderscore() {
      assertEquals("user_name", TranslatingStrategy.LOWER_UNDERSCORE.translate("userName"));
      assertEquals("user_name", TranslatingStrategy.LOWER_UNDERSCORE.translate("UserName"));
      assertEquals("user_name", TranslatingStrategy.LOWER_UNDERSCORE.translate("USER_NAME"));
      assertEquals("user_name", TranslatingStrategy.LOWER_UNDERSCORE.translate("user_name"));
      assertEquals("user__name", TranslatingStrategy.LOWER_UNDERSCORE.translate("user__name"));
      assertEquals("user", TranslatingStrategy.LOWER_UNDERSCORE.translate("user"));
      assertEquals("user", TranslatingStrategy.LOWER_UNDERSCORE.translate("User"));
      assertEquals("user", TranslatingStrategy.LOWER_UNDERSCORE.translate("_user"));
      assertEquals("user", TranslatingStrategy.LOWER_UNDERSCORE.translate("_User"));
      assertEquals("_user", TranslatingStrategy.LOWER_UNDERSCORE.translate("__user"));
      assertEquals("__user", TranslatingStrategy.LOWER_UNDERSCORE.translate("___user"));
      assertEquals("___user", TranslatingStrategy.LOWER_UNDERSCORE.translate("____user"));
   }

   @Test
   public void testUpperUnderscore() {
     assertEquals("USER_NAME", TranslatingStrategy.UPPER_UNDERSCORE.translate("userName"));
     assertEquals("USER_NAME", TranslatingStrategy.UPPER_UNDERSCORE.translate("UserName"));
     assertEquals("USER_NAME", TranslatingStrategy.UPPER_UNDERSCORE.translate("USER_NAME"));
     assertEquals("USER_NAME", TranslatingStrategy.UPPER_UNDERSCORE.translate("user_name"));
     assertEquals("USER", TranslatingStrategy.UPPER_UNDERSCORE.translate("user"));
     assertEquals("USER", TranslatingStrategy.UPPER_UNDERSCORE.translate("User"));
     assertEquals("USER", TranslatingStrategy.UPPER_UNDERSCORE.translate("_user"));
     assertEquals("USER", TranslatingStrategy.UPPER_UNDERSCORE.translate("_User"));
     assertEquals("_USER", TranslatingStrategy.UPPER_UNDERSCORE.translate("__user"));
   }

   @Test
   public void testLowerCamelCase() {
      assertEquals("userName", TranslatingStrategy.LOWER_CAMEL.translate("user_name"));
      assertEquals("Username", TranslatingStrategy.LOWER_CAMEL.translate("_username"));
      assertEquals("UserName", TranslatingStrategy.LOWER_CAMEL.translate("_user_name"));
      assertEquals("UserName", TranslatingStrategy.LOWER_CAMEL.translate("__user_name"));
      assertEquals("UserName", TranslatingStrategy.LOWER_CAMEL.translate("_user__name"));
      assertEquals("USERNAME", TranslatingStrategy.LOWER_CAMEL.translate("_u_s_e_r_n_a_m_e"));
      assertEquals("uSERNAME", TranslatingStrategy.LOWER_CAMEL.translate("u_s_e_r_n_a_m_e"));
      assertEquals("userName", TranslatingStrategy.LOWER_CAMEL.translate("user__Name"));
   }

   @Test
   public void testUpperCamelCase() {
      assertEquals("UserName", TranslatingStrategy.UPPER_CAMEL.translate("user_name"));
      assertEquals("Username", TranslatingStrategy.UPPER_CAMEL.translate("_username"));
      assertEquals("UserName", TranslatingStrategy.UPPER_CAMEL.translate("_user_name"));
      assertEquals("UserName", TranslatingStrategy.UPPER_CAMEL.translate("__user_name"));
      assertEquals("UserName", TranslatingStrategy.UPPER_CAMEL.translate("_user__name"));
      assertEquals("USERNAME", TranslatingStrategy.UPPER_CAMEL.translate("_u_s_e_r_n_a_m_e"));
      assertEquals("USERNAME", TranslatingStrategy.UPPER_CAMEL.translate("u_s_e_r_n_a_m_e"));
      assertEquals("UserName", TranslatingStrategy.UPPER_CAMEL.translate("user__Name"));
   }
}
