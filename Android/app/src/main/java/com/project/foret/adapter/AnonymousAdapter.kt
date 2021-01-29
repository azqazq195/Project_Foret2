package com.project.foret.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.project.foret.R
import com.project.foret.model.Board


class AnonymousAdapter : RecyclerView.Adapter<AnonymousAdapter.AnonymousBoardViewHolder>() {
    inner class AnonymousBoardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallBack = object : DiffUtil.ItemCallback<Board>() {
        override fun areItemsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Board, newItem: Board): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnonymousAdapter.AnonymousBoardViewHolder {
        return AnonymousBoardViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_anonymous_board,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: AnonymousAdapter.AnonymousBoardViewHolder,
        position: Int
    ) {
        val board = differ.currentList[position]
        holder.itemView.apply {
            holder.itemView.findViewById<TextView>(R.id.tvAnonySubject).text = board.subject
            holder.itemView.findViewById<TextView>(R.id.tvAnonyWriter).text = board.member.nickname
            holder.itemView.findViewById<TextView>(R.id.tvAnonyContent).text = board.content
            holder.itemView.findViewById<TextView>(R.id.tvAnonyRegDate).text =
                board.reg_date.substring(0, board.reg_date.indexOf("T"))
            setOnClickListener {
                onItemClickListener?.let { it(board) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Board) -> Unit)? = null

    fun setOnItemClickListener(listener: (Board) -> Unit) {
        onItemClickListener = listener
    }
}