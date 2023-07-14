package com.lucasalfare.flconsumer.core

import com.google.gson.JsonObject


/**
 * Enum class to represent a single Json property name. This is used in order to
 * avoid these names be declared hardcoded over the application project.
 */
enum class JsonPropertyName(val propertyName: String) {
  AvatarUrl("avatar_url"),
  Login("login"),
  Name("name"),
  Bio("bio"),
  PublicRepos("public_repos"),
  Followers("followers")
}

/**
 * Data class to hold information about individual Json properties and their values.
 */
data class JsonPropertyValue(val type: String, var value: Any = Any())

/**
 * Data class to hold common header/info data values about
 * an user.
 */
data class UserInfoModel(
  val jsonProperties: Map<JsonPropertyName, JsonPropertyValue> = mapOf(
    Pair(JsonPropertyName.AvatarUrl, JsonPropertyValue("string")),
    Pair(JsonPropertyName.Login, JsonPropertyValue("string")),
    Pair(JsonPropertyName.Name, JsonPropertyValue("string")),
    Pair(JsonPropertyName.Bio, JsonPropertyValue("string")),
    Pair(JsonPropertyName.PublicRepos, JsonPropertyValue("int")),
    Pair(JsonPropertyName.Followers, JsonPropertyValue("int"))
  )
)

/**
 * This function searches for all needed fields and also checks if those fields are not null.
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

  // k: "map key", v: "map value"
  res.jsonProperties.forEach { (k, v) ->
    if (jsonObject.has(k.propertyName)) {
      jsonObject.get(k.propertyName).let {
        v.value = when (v.type) {
          "string" -> if (!it!!.isJsonNull) it.asString else ""
          "int" -> if (!it!!.isJsonNull) it.asInt else 0
          else -> {}
        }
      }
    }
  }

  return res
}