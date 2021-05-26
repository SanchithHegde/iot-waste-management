package me.sanchithhegde.wastecollection

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import dagger.hilt.android.AndroidEntryPoint
import me.sanchithhegde.wastecollection.databinding.FragmentMainBinding


@AndroidEntryPoint
class MainFragment : Fragment() {
    private val viewModel: MessageViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = MessageAdapter()
        binding.messagesList.adapter = adapter
        subscribeUi(adapter)

        setHasOptionsMenu(true)
        return binding.root

        /*val rootView = inflater.inflate(R.layout.fragment_main, container, false)

        val messageAdapter = MessageAdapter()
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = messageAdapter

        return rootView*/
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        val settingsMenuItem = menu.findItem(R.id.settingsFragment)
        settingsMenuItem.isVisible = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = findNavController(this)
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    private fun subscribeUi(adapter: MessageAdapter) {
        viewModel.messages.observe(viewLifecycleOwner) { messages ->
            adapter.submitList(messages)
        }
    }
}