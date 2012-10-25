package com.digitolio.jdbi;

/**
 *
 */
public class SomeThing {
   private Integer id;
   private String name;
   private String username;

   public SomeThing() {
   }

   public SomeThing(Integer id, String name, String username) {
      this.id = id;
      this.name = name;
      this.username = username;
   }

   public SomeThing(String name, String username) {
      this.name = name;
      this.username = username;
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   @Override
   public boolean equals(Object o) {
      if(this == o) return true;
      if(o == null || getClass() != o.getClass()) return false;

      SomeThing someThing = (SomeThing) o;

      if(id != someThing.id) return false;
      if(name != null ? !name.equals(someThing.name) : someThing.name != null) return false;
      if(username != null ? !username.equals(someThing.username) : someThing.username != null) return false;

      return true;
   }

   @Override
   public int hashCode() {
      int result = id;
      result = 31 * result + (name != null ? name.hashCode() : 0);
      result = 31 * result + (username != null ? username.hashCode() : 0);
      return result;
   }
}
