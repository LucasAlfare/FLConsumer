package com.lucasalfare.flconsumer.core

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import io.ktor.client.statement.*


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
  Followers("followers"),
  ReposUrl("repos_url"),

  RepoName("name"),
  RepoDescription("description"),
  RepoStargazersCount("stargazers_count"),
  RepoForksCount("forks_count"),

  Unspecified("[Unspecified]")
}

data class JsonField(
  val jsonPropertyName: JsonPropertyName = JsonPropertyName.Unspecified,
  val type: String = "",
  var value: Any = Any()
) {
  override fun toString() = "[$jsonPropertyName: $value]"
}

private fun userModel() = listOf(
  JsonField(JsonPropertyName.AvatarUrl, "string"),
  JsonField(JsonPropertyName.Login, "string"),
  JsonField(JsonPropertyName.Name, "string"),
  JsonField(JsonPropertyName.Bio, "string"),
  JsonField(JsonPropertyName.PublicRepos, "int"),
  JsonField(JsonPropertyName.Followers, "int"),
  JsonField(JsonPropertyName.ReposUrl, "string")
)

private fun repoModel() = listOf(
  JsonField(JsonPropertyName.RepoName, "string"),
  JsonField(JsonPropertyName.RepoDescription, "string"),
  JsonField(JsonPropertyName.RepoStargazersCount, "int"),
  JsonField(JsonPropertyName.RepoForksCount, "int")
)

fun getBaseModelFor(target: String) = when (target) {
  "user" -> userModel()
  else -> repoModel()
}


/**
 * This function takes a JSON object generated by the Gson library and searches for
 * all needed fields and also checks if those fields are not null. If those checkings
 * are valid, then store its values in a holder data class, which is properly returned.
 *
 * When a value is not valid (e.g., null value) then the field is set to a default
 * value, ruled as following:
 * - Strings are set to [""] (empty string);
 * - Integer numbers to [0].
 */
fun getModelFor(
  jsonObject: JsonObject,
  targetModel: String
): List<JsonField> {
  val model = getBaseModelFor(targetModel)

  model.forEach { field ->
    if (jsonObject.has(field.jsonPropertyName.propertyName)) {
      jsonObject.get(field.jsonPropertyName.propertyName).let {
        field.value = when (field.type) {
          "string" -> if (!it.isJsonNull) it.asString else ""
          "int" -> if (!it.isJsonNull) it.asInt else 0
          else -> {}
        }
      }
    }
  }

  return model
}

fun getModelsFor(
  jsonArray: JsonArray,
  targetItemModel: String
): List<List<JsonField>> {
  val models = mutableListOf<List<JsonField>>()

  jsonArray.forEach {
    models += getModelFor(it.asJsonObject, targetItemModel)
  }

  return models
}

suspend fun responseToText(response: HttpResponse) =
  response.bodyAsText()

fun jsonTextAsJsonObject(jsonText: String) =
  JsonParser.parseString(jsonText).asJsonObject

fun jsonTextAsJsonArray(jsonText: String) =
  JsonParser.parseString(jsonText).asJsonArray