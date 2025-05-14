package com.example.mapsapp.viewmodels

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.SupabaseApplication
import com.example.mapsapp.model.Marcador
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


class MyViewModel : ViewModel() {
    val database = SupabaseApplication.database


    private val _markerName = MutableLiveData<String>()
    val markerName = _markerName

    private val _markerDescript = MutableLiveData<String>()
    val markerDescript = _markerDescript

    private val _markerImage = MutableLiveData<String>()
    val markerImage = _markerImage

    private val _listaMarcadores = MutableLiveData<List<Marcador>>()
    val listaMarcadores = _listaMarcadores

    private var _selectedMark: Marcador? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertNewMarker(
        name: String,
        descripcion: String,
        image: Bitmap?,
        latitud: Double,
        longitud: Double
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val stream = ByteArrayOutputStream()
            image?.compress(Bitmap.CompressFormat.PNG, 0, stream)
            val imageName = database.uploadImage(stream.toByteArray())
            val newMarker = Marcador(
                name = name, descripcion = descripcion,
                image = imageName,
                latitud = latitud,
                longitud = longitud,
            )
            database.insertMarcador(newMarker)
        }
    }

    fun getAllMarcadors() {
        CoroutineScope(Dispatchers.IO).launch {
            val databaseMark = database.getAllMarcador()
            withContext(Dispatchers.Main) {
                _listaMarcadores.value = databaseMark
            }
        }
    }

    fun getStudent(id: Int){
        if(_selectedMark == null){
            CoroutineScope(Dispatchers.IO).launch {
                val marcador = database.getMarcador(id)
                withContext(Dispatchers.Main) {
                    _selectedMark = marcador
                    _markerName.value = marcador.name
                    _markerDescript.value = marcador.descripcion
                    _markerImage.value = marcador.image
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateMarker(id: Int, name: String, descripcion: String, image: Bitmap?){
        var imageName : String? = null
        var stream: ByteArrayOutputStream? = null
        if(image != null){
            stream = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 0, stream)
            imageName =
                _selectedMark?.image?.removePrefix("https://xlebkybtqrbnyowaavbq.supabase.co/storage/v1/object/public/images/")
        }
        CoroutineScope(Dispatchers.IO).launch {
            database.updateMarcador(id, name, descripcion, imageName, stream?.toByteArray())
        }
    }
    fun editMarkerName(name: String) {
        _markerName.value = name
    }

    fun editMarkerDescript(image: String) {
        _markerDescript.value = image
    }

    fun deleteMarcador(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            database.deleteMarcador(id)
            getAllMarcadors()
        }
    }

}
