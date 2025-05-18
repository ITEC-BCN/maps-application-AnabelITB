package com.example.mapsapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.mapsapp.model.Marcador
import com.example.mapsapp.viewmodels.MyViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MarkerListScreen(modifier: Modifier, navigateToDetail: (Int) -> Unit) {
    var myViewModel: MyViewModel = viewModel()
    val markList by myViewModel.listaMarcadores.observeAsState(emptyList<Marcador>())
    myViewModel.getAllMarcadors()

    LazyColumn(
        modifier
            .fillMaxWidth()
    ) {
        itemsIndexed(markList, key = { _, marcador -> marcador.id }) { _, marcador ->
            val dismissState = rememberSwipeToDismissBoxState(confirmValueChange = {
                if (it == SwipeToDismissBoxValue.EndToStart) {
                    myViewModel.deleteMarcador(marcador.id)
                    true
                } else {
                    false
                }
            })
            if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart &&
                dismissState.targetValue == SwipeToDismissBoxValue.EndToStart
            ) {
                LaunchedEffect(Unit) {
                    myViewModel.deleteMarcador(marcador.id)
                }
            }
            SwipeToDismissBox(state = dismissState, backgroundContent = {
                Box(
                    Modifier
                        .fillMaxSize()
                        .background(Color.Red),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }) {
                MarcadorItem(marcador) { navigateToDetail(marcador.id) }
            }
        }
    }
}

@Composable
fun MarcadorItem(marcador: Marcador, navigateToDetail: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        onClick = { navigateToDetail(marcador.id) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = marcador.image),
                contentDescription = marcador.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = marcador.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}