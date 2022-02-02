package me.sanchithhegde.wastecollection.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import dagger.hilt.android.AndroidEntryPoint
import me.sanchithhegde.wastecollection.R
import me.sanchithhegde.wastecollection.adapters.MessageAdapter
import me.sanchithhegde.wastecollection.databinding.FragmentMessagesBinding
import me.sanchithhegde.wastecollection.viewmodels.MessageViewModel

@AndroidEntryPoint
class MessagesFragment : Fragment() {
  private val viewModel: MessageViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    val binding = FragmentMessagesBinding.inflate(inflater, container, false)
    context ?: return binding.root

    val adapter = MessageAdapter()
    binding.messagesList.adapter = adapter
    subscribeUi(adapter)

    setHasOptionsMenu(true)
    return binding.root
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
    viewModel.messages.observe(viewLifecycleOwner) { messages -> adapter.submitList(messages) }
  }
}
