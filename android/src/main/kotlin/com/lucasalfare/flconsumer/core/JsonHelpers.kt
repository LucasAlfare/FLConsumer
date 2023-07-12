package com.lucasalfare.flconsumer.core

import com.google.gson.JsonObject

/**
 * Data class to hold common header/info data values about
 * an user.
 */
data class UserInfoModel(
  var avatarUrl: String = "",
  var login: String = "",
  var name: String = "",
  var bio: String = "",
  var publicRepos: Int = 0,
  var followers: Int = 0
)

/**
 * This function for all needed fields and also checks if those fields are not null.
 * If those checkings are valid, then store its values in a holder data class, which
 * is properly returned.
 *
 * When a value is not valid (e.g., null value) then the field is set to a default
 * value:
 * - Strings are set to [""] (empty string);
 * - Integer numbers to [0].
 */
fun getUserInfoModelFor(jsonObject: JsonObject): UserInfoModel {
  val res = UserInfoModel()

  if (jsonObject.has("avatar_url")) {
    jsonObject.get("avatar_url").let {
      res.avatarUrl = if (!it.isJsonNull) it.asString else ""
    }
  }

  if (jsonObject.has("login")) {
    jsonObject.get("login").let {
      res.login = if (!it.isJsonNull) it.asString else ""
    }
  }

  if (jsonObject.has("name")) {
    jsonObject.get("name").let {
      res.name = if (!it.isJsonNull) it.asString else ""
    }
  }

  if (jsonObject.has("bio")) {
    jsonObject.get("bio").let {
      res.bio = if (!it.isJsonNull) it.asString else ""
    }
  }

  if (jsonObject.has("public_repos")) {
    jsonObject.get("public_repos").let {
      res.publicRepos = if (!it.isJsonNull) it.asInt else 0
    }
  }

  if (jsonObject.has("followers")) {
    jsonObject.get("followers").let {
      res.followers = if (!it.isJsonNull) it.asInt else 0
    }
  }

  return res
}