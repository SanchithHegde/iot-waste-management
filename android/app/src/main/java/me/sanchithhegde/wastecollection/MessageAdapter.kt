package me.sanchithhegde.wastecollection

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import me.sanchithhegde.wastecollection.data.Message

class MessageAdapter :
    ListAdapter<Message, MessageAdapter.MessageViewHolder>(MessageDiffCallback) {

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTimeTextView: AppCompatTextView =
            itemView.findViewById(R.id.message_time)
        private val messageTitleTextView: AppCompatTextView =
            itemView.findViewById(R.id.message_title)
        private val messageBodyTextView: AppCompatTextView =
            itemView.findViewById(R.id.message_body)
        private var currentMessage: Message? = null

        fun bind(message: Message) {
            currentMessage = message

            messageTimeTextView.text = message.timestamp
            messageTitleTextView.text = message.title
            messageBodyTextView.text = message.body
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = getItem(position)
        holder.bind(message)
    }
}

object MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }

}