package com.example.contactos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.contactos.ui.theme.ContactosTheme

data class Contacto(val nombre: String, val numero: String, var esFavorito: Boolean = false)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ContactosTheme {
                App()
            }
        }
    }
}

/**
 *
 * Esta aplicación permite a los usuarios crear una lista de contactos con nombre y número de teléfono,
 * además de marcar los contactos como favoritos. La estructura principal se basa en un Scaffold,
 * que contiene una barra superior (TopBar) para el título, una barra inferior (BottomBar) con botones
 * para alternar entre ver todos los contactos o solo los favoritos, y un botón flotante (FloatingActionButton)
 * para alternar la visibilidad de un formulario de creación de contactos.
 *
 * Elementos del Código:
 *
 * - Contacto: Clase de datos que representa un contacto con un nombre, número de teléfono,
 *   y un estado booleano opcional para indicar si es favorito (esFavorito).
 *
 * - MainActivity: Actividad principal de la aplicación que hereda de ComponentActivity y
 *   define la interfaz a través de la función composable `App` dentro de `ContactosTheme`.
 *
 * - App: Función composable principal que define el estado y organiza la estructura de la interfaz.
 *   mostrarFormulario: Estado booleano que controla la visibilidad del formulario de añadir contacto.
 *   textoNombre y textoNumero: MutableState que almacenan el nombre y número introducidos en el formulario.
 *   contactos: Estado mutable que almacena la lista de contactos.
 *   mostrarFavoritos: Booleano que alterna entre ver todos los contactos o solo los favoritos.
 *
 *   `App` utiliza un Scaffold que integra:
 *
 *  @param topBar: Barra superior definida en `CustomTopBar` para mostrar el título "Contactos".
 *  @param bottomBar: Barra inferior `CustomBottomBar` que contiene dos botones para alternar entre
 *  ver todos los contactos o solo los favoritos.
 *  @param floatingActionButton: Botón flotante definido en `CustomFloatingButton` para mostrar u ocultar
 *  el formulario de creación de contacto.
 *  @param CustomContent: Contenido principal que incluye el formulario para añadir contactos y una
 *  lista de contactos con opciones de favoritos.
 *
 * - CustomFloatingButton: Define el FloatingActionButton que permite mostrar/ocultar el formulario de contacto.
 *   onClick: Función lambda que se ejecuta al hacer clic en el botón.
 *
 * - CustomTopBar: Define la barra superior que muestra un título simple "Contactos".
 *
 * - CustomBottomBar: Contiene botones para ver todos los contactos o solo los favoritos.
 *   onClickShow: Función lambda que se ejecuta al hacer clic en el botón "Contactos".
 *   onClickHide: Función lambda que se ejecuta al hacer clic en el botón "Favoritos".
 *
 * - CustomContent: Contenido principal de la pantalla que incluye un formulario opcional para crear un contacto
 *   y una lista de contactos donde el usuario puede ver y gestionar contactos.
 *   mostrarFormulario: Booleano que indica si se muestra el formulario.
 *   textoNombre y textoNumero: Estados para capturar el texto de entrada del usuario para nombre y número.
 *   agregarContacto: Función lambda para agregar un contacto a la lista cuando el formulario está completo.
 *   contactos: Lista de contactos que se renderiza.
 *   actualizarFavorito: Función lambda para cambiar el estado de favorito de un contacto.
 *   innerPadding: PaddingValues para controlar el espacio interior del Scaffold.
 *
 * - AppPreview: Función de previsualización que permite visualizar la interfaz de `App` en Android Studio
 *   sin ejecutar la aplicación.
 *
 */

@Composable
fun App() {
    var mostrarFormulario by remember { mutableStateOf(false) }
    val textoNombre = remember { mutableStateOf("") }
    val textoNumero = remember { mutableStateOf("") }
    var contactos by remember { mutableStateOf(listOf<Contacto>()) }
    var mostrarFavoritos by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CustomTopBar()
        },
        bottomBar = {
            CustomBottomBar(
                onClickHide = { mostrarFavoritos = true },
                onClickShow = { mostrarFavoritos = false }
            )
        },
        floatingActionButton = {
            CustomFloatingButton(onClick = {
                mostrarFormulario = !mostrarFormulario
            })
        },
    ) { innerPadding ->
        CustomContent(
            mostrarFormulario = mostrarFormulario,
            textoNombre = textoNombre,
            textoNumero = textoNumero,
            agregarContacto = {
                if (textoNombre.value.isNotEmpty() && textoNumero.value.isNotEmpty()) {
                    contactos = contactos + Contacto(textoNombre.value, textoNumero.value)
                    textoNombre.value = ""
                    textoNumero.value = ""
                }
            },
            contactos = if (mostrarFavoritos) contactos.filter { it.esFavorito } else contactos,
            actualizarFavorito = { contacto, esFavorito ->
                contactos = contactos.map {
                    if (it == contacto) it.copy(esFavorito = esFavorito) else it
                }
            },
            innerPadding = innerPadding
        )
    }
}

@Composable
fun CustomFloatingButton(onClick: () -> Unit) {
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .wrapContentSize(align = Alignment.BottomEnd)
            .padding(16.dp)
    ) {
        Icon(
            Icons.Default.AddCircle,
            contentDescription = "Agregar Contacto",
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun CustomTopBar() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Text(text = "Contactos")
    }
}

@Composable
fun CustomBottomBar(onClickShow: () -> Unit, onClickHide: () -> Unit) {
    BottomAppBar {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = onClickShow) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Contactos",
                        modifier = Modifier.size(33.dp)
                    )
                }
                Text(text = "Contactos")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = onClickHide) {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = "Favoritos",
                        modifier = Modifier.size(33.dp)
                    )
                }
                Text(text = "Favoritos")
            }
        }
    }
}

@Composable
fun CustomContent(
    mostrarFormulario: Boolean,
    textoNombre: MutableState<String>,
    textoNumero: MutableState<String>,
    agregarContacto: () -> Unit,
    contactos: List<Contacto>,
    actualizarFavorito: (Contacto, Boolean) -> Unit,
    innerPadding: PaddingValues,
) {
    Column(modifier = Modifier.padding(innerPadding)) {
        if (mostrarFormulario) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextField(
                    value = textoNombre.value,
                    onValueChange = { textoNombre.value = it },
                    label = { Text("Nombre") },
                )
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = textoNumero.value,
                    onValueChange = { textoNumero.value = it },
                    label = { Text("Número") },
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = agregarContacto) {
                    Text("Añadir Contacto")
                }
            }
        }

        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(contactos) { contacto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = contacto.nombre)
                            Text(text = contacto.numero)
                        }
                        Switch(
                            checked = contacto.esFavorito,
                            onCheckedChange = { esFavorito ->
                                actualizarFavorito(contacto, esFavorito)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    ContactosTheme {
        App()
    }
}
