package com.example.apprickandmorty.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.apprickandmorty.R
import com.example.apprickandmorty.adapters.CharactersAdapter
import com.example.apprickandmorty.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var charactersAdapter: CharactersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets -> // Usa binding.main
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView() // Configura el RecyclerView
        fetchCharacters() // Llama a la función para obtener los personajes iniciales
    }

    private fun setupRecyclerView() {
        // Inicializa el adaptador, pasando la lambda para el clic en cada elemento
        charactersAdapter = CharactersAdapter { character -> // La lambda recibe el objeto Character clickeado
            val intent = Intent(this, DetailActivity::class.java) // Asumo que DetailActivity es donde muestras los detalles
            intent.putExtra("CHARACTER_ID", character.id) // Pasa el ID del personaje
            startActivity(intent)
        }

        binding.recyclerView.apply { // Accede al RecyclerView a través del binding
            adapter = charactersAdapter // Asigna el adaptador
            layoutManager = GridLayoutManager(this@MainActivity, 2) // Configura el LayoutManager
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activities_main_menu, menu) // Asegúrate de que este sea el nombre correcto de tu archivo de menú

        val menuItem = menu.findItem(R.id.menu_search)
        val searchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchCharacters(query) // Llama a la función de búsqueda con la consulta
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) {
                    fetchCharacters() // Vuelve a cargar todos los personajes si el texto está vacío
                }
                return false
            }
        })

        return true
    }

    // Función para obtener todos los personajes (inicial o cuando el texto de búsqueda está vacío)
    private fun fetchCharacters() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Realiza la llamada a la API para obtener todos los personajes
                RetrofitClient.instance.getCharacters().enqueue(object : Callback<CharacterResponse> {
                    override fun onResponse(call: Call<CharacterResponse>, response: Response<CharacterResponse>) {
                        if (response.isSuccessful) {
                            response.body()?.results?.let { characters ->
                                CoroutineScope(Dispatchers.Main).launch {
                                    charactersAdapter.submitList(characters) // Actualiza el adaptador en el hilo principal
                                }
                            }
                        } else {
                            Toast.makeText(this@MainActivity, "Error al cargar personajes: ${response.code()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Fallo en la conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                        t.printStackTrace()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Función para buscar personajes por nombre (se asume que la API de Rick y Morty soporta el parámetro 'name')
    private fun searchCharacters(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Realiza la llamada a la API para buscar personajes por nombre
                // NOTA: La API de Rick y Morty permite buscar por nombre con el parámetro 'name' en el endpoint /character
                RetrofitClient.instance.searchCharactersByName(query).enqueue(object : Callback<CharacterResponse> {
                    override fun onResponse(call: Call<CharacterResponse>, response: Response<CharacterResponse>) {
                        if (response.isSuccessful) {
                            response.body()?.results?.let { characters ->
                                CoroutineScope(Dispatchers.Main).launch {
                                    charactersAdapter.submitList(characters) // Actualiza el adaptador en el hilo principal con los resultados de la búsqueda
                                }
                            }
                        } else {
                            // La API de Rick y Morty devuelve un 404 si no encuentra resultados.
                            // Puedes manejar esto para mostrar una lista vacía o un mensaje.
                            CoroutineScope(Dispatchers.Main).launch {
                                charactersAdapter.submitList(emptyList()) // Limpia la lista si no hay resultados
                                Toast.makeText(this@MainActivity, "No se encontraron personajes para '$query'", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onFailure(call: Call<CharacterResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Fallo en la conexión al buscar: ${t.message}", Toast.LENGTH_SHORT).show()
                        t.printStackTrace()
                    }
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}