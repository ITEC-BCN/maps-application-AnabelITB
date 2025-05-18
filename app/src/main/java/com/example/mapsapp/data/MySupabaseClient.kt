package com.example.mapsapp.data


import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mapsapp.BuildConfig
import com.example.mapsapp.model.Marcador

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MySupabaseClient(val client: SupabaseClient) {

    val storage: Storage = client.storage
    private val supabaseUrl = BuildConfig.SUPABASE_URL

    //SQL operations
    suspend fun getAllMarcador(): List<Marcador> {
        return client.from("Marcador").select().decodeList<Marcador>()
    }

    suspend fun getMarcador(id: Int): Marcador {
        return client.from("Marcador").select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<Marcador>()
    }

    suspend fun insertMarcador(student: Marcador) {
        client.from("Marcador").insert(student)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun updateMarcador(
        id: Int,
        name: String,
        descripcion: String,
        imageName: String?,
        imageFile: ByteArray?
    ) {
        if (imageName != null && imageFile != null) {
            client.storage.from("images").delete(imageName)
            val newImageName = uploadImage(imageFile)
            client.from("Marcador").update({
                set("name", name)
                set("descripcion", descripcion)
                set("image", newImageName)
            }) { filter { eq("id", id) } }
        } else {
            client.from("Marcador").update({
                set("name", name)
                set("descripcion", descripcion)
            }) { filter { eq("id", id) } }
        }
    }

    suspend fun deleteMarcador(id: Int) {
        client.from("Marcador").delete { filter { eq("id", id) } }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun uploadImage(imageFile: ByteArray): String {
        val fechaHoraActual = LocalDateTime.now()
        val formato = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
        val imageName = storage.from("images")
            .upload(path = "image_${fechaHoraActual.format(formato)}.png", data = imageFile)
        return buildImageUrl(imageFileName = imageName.path)
    }

    fun buildImageUrl(imageFileName: String) =
        "${this.supabaseUrl}/storage/v1/object/public/images/${imageFileName}"
}
