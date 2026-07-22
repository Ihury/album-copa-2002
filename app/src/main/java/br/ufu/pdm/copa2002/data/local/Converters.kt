package br.ufu.pdm.copa2002.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json

/**
 * Conversores de tipo do Room. Persiste `List<String>` (ex.: anos de títulos)
 * como uma string JSON em uma única coluna.
 */
class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>): String =
        Json.encodeToString(ListSerializer(String.serializer()), value)

    @TypeConverter
    fun toStringList(value: String): List<String> =
        if (value.isBlank()) emptyList()
        else Json.decodeFromString(ListSerializer(String.serializer()), value)
}
