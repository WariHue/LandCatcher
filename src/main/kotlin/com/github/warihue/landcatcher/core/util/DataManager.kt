package com.github.warihue.landcatcher.core.util

import com.github.warihue.landcatcher.Team
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class EnumMapDeserializer<K : Enum<K>, V>(
    private val keyType: Class<K>,
    private val valueType: Class<V>
) : JsonDeserializer<EnumMap<K, V>> {

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): EnumMap<K, V> {
        val jsonObject = json.asJsonObject
        val map = EnumMap<K, V>(keyType)

        for (entry in jsonObject.entrySet()) {
            val key = java.lang.Enum.valueOf(keyType, entry.key)
            val value = context.deserialize<V>(entry.value, valueType)
            map[key] = value
        }

        return map
    }
}

object DataManager {
    fun convertChunkDataToJson(data: EnumMap<Team, MutableList<Pair<Int, Int>>>): String {
        return Gson().toJson(data)
    }

    fun convertJsonToChunkData(data: String): EnumMap<Team, MutableList<Pair<Int, Int>>> {
        val gson = GsonBuilder()
            .registerTypeAdapter(
                TypeToken.getParameterized(EnumMap::class.java, Team::class.java, String::class.java).type,
                EnumMapDeserializer(Team::class.java, String::class.java)
            )
            .create()
        return gson.fromJson(data, object : TypeToken<EnumMap<Team, MutableList<Pair<Int, Int>>>>() {}.type)
    }
}